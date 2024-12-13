package bookit.backend.service;

import bookit.backend.model.dto.ReservationDto;
import bookit.backend.model.entity.Business;
import bookit.backend.model.entity.Reservation;
import bookit.backend.model.entity.user.ClientUser;
import bookit.backend.model.entity.user.WorkerUser;
import bookit.backend.model.request.AddReservationRequest;
import bookit.backend.repository.BusinessRepository;
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
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@Transactional
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;
    private final ModelMapper modelMapper;


    public List<ReservationDto> getReservationsForGivenDay(LocalDateTime date, Long businessId) {
        return reservationRepository.findAllByBusiness_IdAndDate(businessId, date)
                .stream()
                .map(reservation -> modelMapper.map(reservation, ReservationDto.class))
                .collect(Collectors.toList());
    }

//    public ReservationOptionsResponse getReservationOptions(Long serviceId, LocalDate date) {
//        ServiceDto serviceDto = servicesService.getServiceById(serviceId).orElse(null);
//        if (serviceDto == null) return null;
//        BusinessDto businessDto = businessService.getBusiness(serviceDto.getBusinessId()).orElse(null);
//        if (businessDto == null) return null;
//        List<ReservationDto> reservationDtoList = getReservationsForGivenDay(date, businessDto.getId());
//
//    }

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
        else worker = business.getWorkers().stream().findFirst();
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
}
