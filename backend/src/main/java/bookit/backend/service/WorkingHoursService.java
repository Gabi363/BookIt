package bookit.backend.service;

import bookit.backend.model.dto.BusinessDto;
import bookit.backend.model.dto.user.UserDto;
import bookit.backend.model.entity.Business;
import bookit.backend.model.entity.BusinessWorkingHours;
import bookit.backend.model.entity.user.BusinessOwnerUser;
import bookit.backend.model.enums.WeekDay;
import bookit.backend.model.request.AddWorkingHoursRequest;
import bookit.backend.repository.WorkingHoursRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@Transactional
@RequiredArgsConstructor
public class WorkingHoursService {

    private final WorkingHoursRepository workingHoursRepository;
    private final BusinessService businessService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public HttpStatus addWorkingHours(long businessId, long ownerId, AddWorkingHoursRequest request) {
        Optional<BusinessDto> businessOptional = businessService.getBusiness(businessId);
        if(businessOptional.isEmpty()) return HttpStatus.NOT_FOUND;
        Business business = modelMapper.map(businessOptional.get(), Business.class);

        Optional<UserDto> userOptional = userService.getUserById(ownerId);
        if(userOptional.isEmpty()) return HttpStatus.NOT_FOUND;
        if(modelMapper.map(userOptional.get(), BusinessOwnerUser.class).getBusiness().getId() != businessId) return HttpStatus.FORBIDDEN;

        List<BusinessWorkingHours> existingWorkingHours = workingHoursRepository.findAllByBusiness_Id(businessId);
        if(existingWorkingHours.size() >= 7 || request.getWorkingHoursList().size() > 7) return HttpStatus.CONFLICT;

        List<BusinessWorkingHours> workingHoursList = new ArrayList<>();
        for(var dayRequest : request.getWorkingHoursList()) {
            BusinessWorkingHours workingHours = BusinessWorkingHours.builder()
                    .weekDay(modelMapper.map(dayRequest.getWeekDay(), WeekDay.class))
                    .isOpen(dayRequest.getIsOpen())
                    .startTime(dayRequest.getStartTime())
                    .endTime(dayRequest.getEndTime())
                    .business(business)
                    .build();
            workingHoursList.add(workingHours);
        }
        workingHoursRepository.saveAll(workingHoursList);
        return HttpStatus.CREATED;
    }

    public HttpStatus updateWorkingHours(long businessId, long ownerId, AddWorkingHoursRequest request) {
        Optional<BusinessDto> businessOptional = businessService.getBusinessByOwnerId(ownerId);
        if(businessOptional.isEmpty()) return HttpStatus.NOT_FOUND;
        if(businessOptional.get().getId() != businessId) return HttpStatus.FORBIDDEN;

        List<BusinessWorkingHours> existingWorkingHours = workingHoursRepository.findAllByBusiness_Id(businessId);
        if(request.getWorkingHoursList().size() > 7) return HttpStatus.CONFLICT;

        List<BusinessWorkingHours> workingHoursList = new ArrayList<>();
        for(var dayRequest : request.getWorkingHoursList()) {
            BusinessWorkingHours day = existingWorkingHours.stream().filter(d -> d.getWeekDay() == dayRequest.getWeekDay()).findFirst().orElse(null);
            if(day == null) continue;
            day.setStartTime(dayRequest.getStartTime());
            day.setEndTime(dayRequest.getEndTime());
            day.setIsOpen(dayRequest.getIsOpen());
            workingHoursList.add(day);
        }
        workingHoursRepository.saveAll(workingHoursList);
        return HttpStatus.OK;
    }


}