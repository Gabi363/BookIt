package bookit.backend.service;

import bookit.backend.model.dto.AvailabilityDto;
import bookit.backend.model.dto.BusinessDto;
import bookit.backend.model.dto.ReservationDto;
import bookit.backend.model.dto.ServiceDto;
import bookit.backend.model.dto.user.WorkerUserDto;
import bookit.backend.model.entity.Business;
import bookit.backend.model.entity.BusinessAddress;
import bookit.backend.model.entity.Reservation;
import bookit.backend.model.entity.user.ClientUser;
import bookit.backend.model.entity.user.WorkerUser;
import bookit.backend.model.request.AddReservationRequest;
import bookit.backend.model.response.ReservationOptionsResponse;
import bookit.backend.model.response.ReservationSlotsResponse;
import bookit.backend.repository.BusinessAddressRepository;
import bookit.backend.repository.ReservationRepository;
import bookit.backend.repository.ServiceRepository;
import bookit.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.*;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
@Transactional
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;
    private final ServicesService servicesService;
    private final BusinessService businessService;
    private final BusinessAddressRepository businessAddressRepository;
    private final AvailabilityService availabilityService;
    private final ModelMapper modelMapper;


    public ReservationOptionsResponse getReservationOptions(Long serviceId, String date) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate = LocalDate.parse(date, dateFormatter);
        ServiceDto serviceDto = servicesService.getServiceById(serviceId).orElse(null);
        if (serviceDto == null) return null;
        int serviceHours = serviceDto.getDuration().intValue();
        int serviceMinutes = (int) ((serviceDto.getDuration() - serviceHours) * 60);

        BusinessDto businessDto = businessService.getBusiness(serviceDto.getBusinessId()).orElse(null);
        if (businessDto == null) return null;
        Map<WorkerUserDto, List<AvailabilityDto>> workersAvailabilities = availabilityService.getAvailabilitiesForDay(localDate, businessDto.getId());
        List<ReservationDto> reservations = reservationRepository.findAllByBusiness_Id(businessDto.getId())
                .stream()
                .filter(r -> r.getDate().toLocalDate().isEqual(localDate))
                .map(r -> modelMapper.map(r, ReservationDto.class))
                .toList();

        Map<WorkerUserDto, List<ReservationDto>> workersReservations = reservations
                .stream()
                .collect(Collectors.groupingBy(ReservationDto::getWorker));
        if(workersReservations.isEmpty()) {
            for(var worker : businessDto.getWorkers()) workersReservations.put(worker, List.of());
        }

        return new ReservationOptionsResponse(serviceDto, filterPossibleSlots(workersReservations, serviceHours, serviceMinutes, workersAvailabilities));
    }

    private List<ReservationSlotsResponse> filterPossibleSlots(Map<WorkerUserDto, List<ReservationDto>> workersReservations,
                                                               int serviceHours, int serviceMinutes,
                                                               Map<WorkerUserDto, List<AvailabilityDto>> workersAvailabilities) {
        List<ReservationSlotsResponse> slotsResponseList = new ArrayList<>();
        for(var entry : workersAvailabilities.entrySet()) {
            WorkerUserDto worker = entry.getKey();
            List<LocalTime> slots = new ArrayList<>();

            for(var availability : entry.getValue()) {
                for(LocalTime slot = availability.getStartHour();
                    slot.plusHours(serviceHours).plusMinutes(serviceMinutes).isBefore(availability.getEndHour());
                    slot = slot.plusMinutes(15))
                {
                    boolean isFree = true;
                    if(workersReservations.containsKey(worker)) {
                        for (var reservation : workersReservations.get(worker)) {
                            if (reservation.isOverlapped(slot, slot.plusHours(serviceHours).plusMinutes(serviceMinutes))) {
                                isFree = false;
                                break;
                            }
                        }
                    }
                    if(isFree){
                        slots.add(slot);
                    }
                }
            }
            ReservationSlotsResponse slotsResponse = new ReservationSlotsResponse(worker, slots);
            slotsResponseList.add(slotsResponse);
        }
        return slotsResponseList;
    }

    public HttpStatus addReservation(AddReservationRequest request, long serviceId, long clientId) {
        Optional<bookit.backend.model.entity.Service> service = serviceRepository.findById(serviceId);
        if(service.isEmpty()) return HttpStatus.NOT_FOUND;

        Optional<ClientUser> client = userRepository.findById(clientId)
                .map(user -> modelMapper.map(user, ClientUser.class));
        if(client.isEmpty()) return HttpStatus.NOT_FOUND;

        Business business = service.get().getBusiness();
        Optional<WorkerUser> worker;
        worker = userRepository.findById(request.getWorkerId())
                .map(user -> modelMapper.map(user, WorkerUser.class));
        if(worker.isEmpty()) return HttpStatus.NOT_FOUND;

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        LocalDateTime date = dateFormatter.parse(request.getDate(), LocalDateTime::from);

        if(reservationCannotBeBooked(date, request.getWorkerId(), business.getId(), service.get().getDuration())) return HttpStatus.CONFLICT;
        Reservation reservation = Reservation.builder()
                .client(client.get())
                .worker(worker.get())
                .service(service.get())
                .date(date)
                .business(business)
                .build();

        reservationRepository.save(reservation);
        return HttpStatus.CREATED;
    }

    private boolean reservationCannotBeBooked(LocalDateTime date, long workerId, long businessId, Double duration) {
        var availabilities = availabilityService.getAvailabilitiesForDay(date.toLocalDate(), businessId);
        boolean reservationCannotBeBooked = true;
        for(var entry : availabilities.entrySet()) {
            for(var availability : entry.getValue()) {
                if(!availability.getStartHour().isAfter(date.toLocalTime()) && !availability.getEndHour().isBefore(date.toLocalTime())) {
                    reservationCannotBeBooked = false;
                    break;
                }
            }
            if(!reservationCannotBeBooked) break;
        }
        if(reservationCannotBeBooked) return true;

        List<ReservationDto> reservations = reservationRepository.findAllByBusiness_Id(businessId)
                .stream()
                .filter(r -> r.getDate().toLocalDate().isEqual(date.toLocalDate())
                        && Objects.equals(r.getWorker().getId(), workerId))
                .map(r -> modelMapper.map(r, ReservationDto.class))
                .toList();
        int serviceHours = duration.intValue();
        int serviceMinutes = (int) ((duration - serviceHours) * 60);
        for(var r : reservations) {
            if(r.isOverlapped(date.toLocalTime(), date.toLocalTime().plusHours(serviceHours).plusMinutes(serviceMinutes))) {
                return true;
            }
        }
        return false;
    }

    public List<ReservationDto> getReservationsForBusinessOwner(long ownerId) {
        return reservationRepository.findAllByBusiness_Owner_Id(ownerId)
                .stream()
                .filter(r -> r.getDate().isAfter(LocalDateTime.now()))
                .map(r -> modelMapper.map(r, ReservationDto.class))
                .toList();
    }

    public List<ReservationDto> getReservationsForWorker(long workerId) {
        return reservationRepository.findAllByWorker_Id(workerId)
                .stream()
                .filter(r -> r.getDate().isAfter(LocalDateTime.now()))
                .map(r -> modelMapper.map(r, ReservationDto.class))
                .toList();
    }

    public List<ReservationDto> getReservationsForClient(long clientId) {
        return reservationRepository.findAllByClient_Id(clientId)
                .stream()
                .filter(r -> r.getDate().isAfter(LocalDateTime.now()))
                .map(r -> modelMapper.map(r, ReservationDto.class))
                .toList();
    }

    public List<ReservationDto> filterReservationsForDay(String date, List<ReservationDto> reservations) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, dateFormatter);

        return reservations.stream()
                .filter(r -> r.getDate().toLocalDate().isEqual(localDate))
                .collect(Collectors.toList());
    }

    public HttpStatus deleteReservation(long reservationId, LoggedUserInfo user) {
        var reservation = reservationRepository.findById(reservationId);

        if(reservation.isEmpty()) return HttpStatus.NOT_FOUND;
        if(!user.isNotBusinessOwner()) {
            var businessId = businessService.getBusinessIdByOwnerId(user.getId());
            if(businessId.isEmpty() || !businessId.get().equals(reservation.get().getBusiness().getId())) return HttpStatus.FORBIDDEN;
        }
        else if(!Objects.equals(reservation.get().getClient().getId(), user.getId())) return HttpStatus.FORBIDDEN;

        reservationRepository.delete(reservation.get());
        return HttpStatus.OK;
    }

    public net.fortuna.ical4j.model.Calendar exportCalendar(long userId, long reservationId) {
        ReservationDto reservation = reservationRepository.findById(reservationId)
                .map(r -> modelMapper.map(r, ReservationDto.class))
                .filter(r -> r.getClient().getId() == userId || r.getWorker().getId() == userId)
                .orElse(null);
        if(reservation == null) return null;

        BusinessAddress address = businessAddressRepository.findById(reservation.getBusinessId()).orElse(null);
        String businessName = businessService.getBusiness(reservation.getBusinessId()).get().getName();

        LocalDateTime startDateTime = reservation.getDate();
        LocalDateTime endDateTime = reservation.getEndDate();

        Date startDate = Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant());

        VEvent event = new VEvent(new net.fortuna.ical4j.model.DateTime(startDate),
                new net.fortuna.ical4j.model.DateTime(endDate),
                "Wizyta w " + businessName);

        event.getProperties().add(new Description(reservation.getService().getDescription()));
        if(address == null) event.getProperties().add(new Location("-"));
        else event.getProperties().add(new Location(address.toString()));

        Uid uid = new Uid(UUID.randomUUID().toString());
        event.getProperties().add(uid);

        net.fortuna.ical4j.model.Calendar calendar = new net.fortuna.ical4j.model.Calendar();
        calendar.getProperties().add(new ProdId("-//BookIt//Reservation 1.0//EN"));
        calendar.getProperties().add(Version.VERSION_2_0);
        calendar.getProperties().add(CalScale.GREGORIAN);
        calendar.getComponents().add(event);

        return calendar;
    }
}