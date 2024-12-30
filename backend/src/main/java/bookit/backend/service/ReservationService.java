package bookit.backend.service;

import bookit.backend.model.dto.BusinessDto;
import bookit.backend.model.dto.ReservationDto;
import bookit.backend.model.dto.ServiceDto;
import bookit.backend.model.dto.user.WorkerUserDto;
import bookit.backend.model.entity.Business;
import bookit.backend.model.entity.Reservation;
import bookit.backend.model.entity.user.ClientUser;
import bookit.backend.model.entity.user.WorkerUser;
import bookit.backend.model.request.AddReservationRequest;
import bookit.backend.model.response.ReservationOptionsResponse;
import bookit.backend.model.response.ReservationSlotsResponse;
import bookit.backend.repository.ReservationRepository;
import bookit.backend.repository.ServiceRepository;
import bookit.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    private final WorkingHoursService workingHoursService;
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
        List<LocalTime> workingHours = workingHoursService.getWorkingHoursForDate(localDate, businessDto.getId());

        List<ReservationDto> reservations = reservationRepository.findAllByBusiness_Id(businessDto.getId())
                .stream()
                .filter(r -> r.getDate().toLocalDate().isEqual(localDate))
                .map(r -> modelMapper.map(r, ReservationDto.class))
                .toList();

        Map<WorkerUserDto, List<ReservationDto>> workersReservations = reservations
                .stream()
                .collect(Collectors.groupingBy(ReservationDto::getWorker));

        return new ReservationOptionsResponse(serviceDto, filterPossibleSlots(workersReservations, serviceHours, serviceMinutes, workingHours, businessDto.getWorkers()));
    }

    private List<ReservationSlotsResponse> filterPossibleSlots(Map<WorkerUserDto, List<ReservationDto>> workersReservations,
                                                               int serviceHours, int serviceMinutes,
                                                               List<LocalTime> workingHours,
                                                               List<WorkerUserDto> workers) {
//        List<ReservationSlotsResponse> workersSlots = new ArrayList<>();
//        for(var entry : workersReservations.entrySet()) {
//            workersSlots.add(entry.getKey(), new ArrayList<>());
//        }

        List<ReservationSlotsResponse> slotsResponseList = new ArrayList<>();
        for(var worker : workers) {
            List<LocalTime> slots = new ArrayList<>();
            for(LocalTime slot = workingHours.get(0);
                slot.plusHours(serviceHours).plusMinutes(serviceMinutes).isBefore(workingHours.get(1));
                slot = slot.plusMinutes(15))
            {
                boolean isFree = true;
                for (var reservation : workersReservations.get(worker)) {
                    if (reservation.isOverlapped(slot, slot.plusHours(serviceHours).plusMinutes(serviceMinutes))) {
                        isFree = false;
                        break;
                    }
                }
                if (isFree) slots.add(slot);
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
        if(request.getWorkerId() != null) worker = userRepository.findById(request.getWorkerId())
                .map(user -> modelMapper.map(user, WorkerUser.class));
        else worker = business.getWorkers().stream().findFirst();       // @TODO check if workers are available
        if(worker.isEmpty()) return HttpStatus.NOT_FOUND;

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        Reservation reservation = Reservation.builder()
                .client(client.get())
                .worker(worker.get())
                .service(service.get())
                .date(dateFormatter.parse(request.getDate(), LocalDateTime::from))
                .business(business)
                .build();

        reservationRepository.save(reservation);
        return HttpStatus.CREATED;
    }

    public List<ReservationDto> getReservationsForBusinessOwner(long ownerId) {
        return reservationRepository.findAllByBusiness_Owner_Id(ownerId)
                .stream()
                .map(r -> modelMapper.map(r, ReservationDto.class))
                .toList();
    }

    public List<ReservationDto> getReservationsForWorker(long workerId) {
        return reservationRepository.findAllByWorker_Id(workerId)
                .stream()
                .map(r -> modelMapper.map(r, ReservationDto.class))
                .toList();
    }

    public List<ReservationDto> getReservationsForClient(long clientId) {
        return reservationRepository.findAllByClient_Id(clientId)
                .stream()
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
}