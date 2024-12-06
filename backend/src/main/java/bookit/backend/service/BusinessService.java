package bookit.backend.service;

import bookit.backend.model.dto.BusinessDto;
import bookit.backend.model.dto.user.BusinessOwnerUserDto;
import bookit.backend.model.entity.Business;
import bookit.backend.model.entity.user.BusinessOwnerUser;
import bookit.backend.model.enums.BusinessType;
import bookit.backend.model.request.CreateBusinessRequest;
import bookit.backend.model.request.CreateServiceRequest;
import bookit.backend.repository.BusinessRepository;
import bookit.backend.repository.ServiceRepository;
import bookit.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@Transactional
@RequiredArgsConstructor
public class BusinessService {

    private final BusinessRepository businessRepository;
    private final UserRepository userRepository;
    private final BusinessAddressService addressService;
    private final UserService userService;
    private final ServiceRepository serviceRepository;
    private final ModelMapper modelMapper;

    public List<BusinessDto> getBusinesses() {
        return businessRepository.findAll()
                .stream()
                .map(entity -> modelMapper.map(entity, BusinessDto.class))
                .collect(Collectors.toList());
    }

    public boolean businessExists(Long id) {
        return businessRepository.existsById(id);
    }

    public Optional<BusinessDto> getBusiness(long id) {
        return businessRepository.findById(id).map(entity -> modelMapper.map(entity, BusinessDto.class));
    }

    public Optional<BusinessDto> getBusinessByOwnerId(long ownerId) {
        Optional<BusinessOwnerUserDto> owner = userService.getBusinessOwnerUserById(ownerId);
        return owner.flatMap(businessOwnerUserDto -> businessRepository.findById(businessOwnerUserDto.getBusinessId())
                .map(business -> modelMapper.map(business, BusinessDto.class)));

    }

    public Optional<BusinessDto> createBusiness(CreateBusinessRequest request, long ownerId) {
        Optional<BusinessOwnerUser> ownerOptional;
        if((ownerOptional = userRepository.findById(ownerId).map(user -> modelMapper.map(user, BusinessOwnerUser.class))).isEmpty()) {
            return Optional.empty();
        }
        BusinessOwnerUser owner = ownerOptional.get();

        Business business = Business.builder()
                .name(request.getName())
                .type(modelMapper.map(request.getType().toString(), BusinessType.class))
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .build();

        owner.setBusiness(business);
        businessRepository.save(business);
        userRepository.save(owner);

        var address = addressService.createAddress(request.getAddressRequest(), business);
        if(address.isEmpty()) return Optional.empty();

        return Optional.of(modelMapper.map(business, BusinessDto.class));
    }

    public HttpStatus updateBusinessInfo(CreateBusinessRequest request, long ownerId) {
        Optional<Business> businessOptional;
        if((businessOptional = this.getBusinessByOwnerId(ownerId)
                .map(b -> modelMapper.map(b, Business.class)))
                .isEmpty()) {
            return HttpStatus.NOT_FOUND;
        }
        Business business = businessOptional.get();
        business.setName(request.getName());
        business.setType(request.getType());
        business.setPhoneNumber(request.getPhoneNumber());
        business.setEmail(request.getEmail());

        businessRepository.save(business);
        addressService.updateAddress(request.getAddressRequest(), business);

        return HttpStatus.CREATED;
    }

    public HttpStatus addService(long businessId, CreateServiceRequest request) {
        Optional<Business> businessOptional;
        if((businessOptional = businessRepository.findById(businessId)).isEmpty()){
            return HttpStatus.NOT_FOUND;
        }
        Business business = businessOptional.get();

        bookit.backend.model.entity.Service service = bookit.backend.model.entity.Service.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .category(request.getCategory())
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
