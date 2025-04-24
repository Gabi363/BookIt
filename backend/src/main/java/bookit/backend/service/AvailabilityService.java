package bookit.backend.service;

import bookit.backend.model.dto.AvailabilityDto;
import bookit.backend.model.dto.user.WorkerUserDto;
import bookit.backend.model.entity.Availability;
import bookit.backend.model.entity.Business;
import bookit.backend.model.entity.user.WorkerUser;
import bookit.backend.model.request.AddAvailabilityRequest;
import bookit.backend.model.response.AvailabilityResponse;
import bookit.backend.model.response.AvailabilitySlotResponse;
import bookit.backend.repository.AvailabilityRepository;
import bookit.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@Transactional
@RequiredArgsConstructor
public class AvailabilityService {

    private final BusinessService businessService;
    private final UserRepository userRepository;
    private final AvailabilityRepository availabilityRepository;
    private final ModelMapper modelMapper;

    public HttpStatus addAvailability(AddAvailabilityRequest request, LoggedUserInfo user) {
        var business = businessService.getBusinessByOwnerId(user.getId());
        if(business.isEmpty()) return HttpStatus.NOT_FOUND;

        Optional<WorkerUser> worker;
        worker = userRepository.findById(request.getWorkerId())
                .map(w -> modelMapper.map(w, WorkerUser.class));
        if(worker.isEmpty()) return HttpStatus.NOT_FOUND;

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date = dateFormatter.parse(request.getDate(), LocalDate::from);

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime startHour = timeFormatter.parse(request.getStartHour(), LocalTime::from);
        LocalTime endHour = timeFormatter.parse(request.getEndHour(), LocalTime::from);

        Availability availability = Availability.builder()
                .business(modelMapper.map(business.get(), Business.class))
                .worker(worker.get())
                .date(date)
                .startHour(startHour)
                .endHour(endHour)
                .build();

        availabilityRepository.save(availability);
        return HttpStatus.CREATED;
    }

    public List<AvailabilityResponse> getAllAvailabilities(String startDate, String endDate, LoggedUserInfo user) {
        return mapAvailabilities(getAvailabilities(startDate, endDate, user));
    }

    public List<AvailabilityResponse> getAvailabilitiesForWorker(Long workerId, String startDate, String endDate, LoggedUserInfo user) {
        return mapAvailabilities(getAvailabilities(startDate, endDate, user)
                .stream()
                .filter(availability -> availability.getWorker().getId().equals(workerId))
                .toList());
    }

    public List<AvailabilityDto> getAvailabilities(String startDate, String endDate, LoggedUserInfo user) {
        Optional<Long> businessId;
        if(!user.isNotBusinessOwner()) {
            businessId = businessService.getBusinessIdByOwnerId(user.getId());
        } else {
            businessId = businessService.getBusinessIdByWorkerId(user.getId());
        }
        if(businessId.isEmpty()) return List.of();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate start = dateFormatter.parse(startDate, LocalDate::from);
        LocalDate end = dateFormatter.parse(endDate, LocalDate::from);

        return availabilityRepository.findAllByBusiness_Id(businessId.get())
                .stream()
                .filter(a -> !a.getDate().isBefore(start))
                .filter(a -> !a.getDate().isAfter(end))
                .map(a -> modelMapper.map(a, AvailabilityDto.class))
                .toList();
    }

    public List<AvailabilityResponse> mapAvailabilities(List<AvailabilityDto> availabilities) {
        Map<WorkerUserDto, List<AvailabilityDto>> workersAvailabilities = availabilities
                .stream()
                .collect(Collectors.groupingBy(AvailabilityDto::getWorker));

        List<AvailabilityResponse> workersList = new ArrayList<>();
        for(var entry : workersAvailabilities.entrySet()) {
            List<AvailabilitySlotResponse> slotList = new ArrayList<>();
            for(var availability : entry.getValue()) {
                slotList.add(new AvailabilitySlotResponse(availability.getId(), availability.getDate(), availability.getStartHour(), availability.getEndHour()));
            }
            workersList.add(new AvailabilityResponse(entry.getKey(), slotList));
        }
        return workersList;
    }

    public HttpStatus deleteAvailability(Long id) {
        Optional<Availability> availabilityOptional = availabilityRepository.findById(id);
        if(availabilityOptional.isEmpty()) return HttpStatus.NOT_FOUND;
        Availability availability = availabilityOptional.get();

        availabilityRepository.delete(availability);
        return HttpStatus.OK;
    }
}
