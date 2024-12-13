package bookit.backend.service;

import bookit.backend.model.dto.BusinessDto;
import bookit.backend.model.dto.ServiceDto;
import bookit.backend.model.entity.Business;
import bookit.backend.model.request.CreateServiceRequest;
import bookit.backend.repository.ServiceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
@Transactional
@RequiredArgsConstructor
public class ServicesService {

    private final BusinessService businessService;
    private final ModelMapper modelMapper;
    private final ServiceRepository serviceRepository;

    public Optional<ServiceDto> getServiceById(Long id) {
        return serviceRepository.findById(id).map(service -> modelMapper.map(service, ServiceDto.class));
    }

    public HttpStatus addService(long businessId, CreateServiceRequest request) {
        Optional<BusinessDto> businessOptional;
        if((businessOptional = businessService.getBusiness(businessId)).isEmpty()){
            return HttpStatus.NOT_FOUND;
        }
        Business business = modelMapper.map(businessOptional.get(), Business.class);

        bookit.backend.model.entity.Service service = bookit.backend.model.entity.Service.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .category(request.getCategory())
                .duration(request.getDuration())
                .business(business)
                .build();

        serviceRepository.save(service);
        return HttpStatus.CREATED;
    }

    public HttpStatus updateService(long businessId, CreateServiceRequest request, long serviceId) {
        Optional<bookit.backend.model.entity.Service> serviceOptional = serviceRepository.findById(serviceId);
        if(serviceOptional.isEmpty()) return HttpStatus.NOT_FOUND;

        bookit.backend.model.entity.Service service = serviceOptional.get();
        if(service.getBusiness().getId() != businessId) return HttpStatus.FORBIDDEN;

        service.setName(request.getName());
        service.setDescription(request.getDescription());
        service.setPrice(request.getPrice());
        service.setCategory(request.getCategory());
        service.setDuration(request.getDuration());

        serviceRepository.save(service);
        return HttpStatus.OK;
    }

    public HttpStatus deleteService(long businessId, long serviceId) {
        Optional<bookit.backend.model.entity.Service> serviceOptional = serviceRepository.findById(serviceId);
        if(serviceOptional.isEmpty()) return HttpStatus.NOT_FOUND;
        bookit.backend.model.entity.Service service = serviceOptional.get();
        if(service.getBusiness().getId() != businessId) return HttpStatus.FORBIDDEN;

        serviceRepository.delete(service);
        return HttpStatus.OK;
    }
}
