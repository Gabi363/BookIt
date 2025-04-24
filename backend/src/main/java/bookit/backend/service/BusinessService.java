package bookit.backend.service;

import bookit.backend.model.dto.BusinessDto;
import bookit.backend.model.dto.BusinessFiltersDto;
import bookit.backend.model.entity.Business;
import bookit.backend.model.entity.user.BusinessOwnerUser;
import bookit.backend.model.entity.user.WorkerUser;
import bookit.backend.model.enums.BusinessType;
import bookit.backend.model.request.CreateBusinessRequest;
import bookit.backend.repository.BusinessRepository;
import bookit.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
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
    private final UserService userService;
    private final BusinessAddressService addressService;
    private final ModelMapper modelMapper;

    public List<BusinessDto> getBusinesses() {
        return businessRepository.findAll()
                .stream()
                .map(entity -> modelMapper.map(entity, BusinessDto.class))
                .collect(Collectors.toList());
    }

    public List<BusinessDto> getBusinessesByFilters(BusinessFiltersDto filters) {
        if(filters.getMinimumRating() == null) filters.setMinimumRating(0.0);
        return businessRepository.findAll(filterBy(filters.getBusinessType(), filters.getCity()))
                .stream()
                .filter(b -> b.getAverageRating() >= filters.getMinimumRating())
                .map(entity -> modelMapper.map(entity, BusinessDto.class))
                .collect(Collectors.toList());
    }

    public static Specification<Business> filterBy(BusinessType type, String city) {
        return (root, query, criteriaBuilder) -> {
            var predicate = criteriaBuilder.conjunction();
            if (type != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("type"), type));
            }
            if (city != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("address").get("city"), city));
            }
            return predicate;
        };
    }

    public List<BusinessType> getBusinessTypes() {
        return businessRepository.findAll()
                .stream()
                .map(Business::getType)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> getCities() {
        return businessRepository.findAll()
                .stream()
                .map(b -> b.getAddress().getCity())
                .distinct()
                .collect(Collectors.toList());
    }

    public Optional<BusinessDto> getBusiness(long id) {
        return businessRepository.findById(id).map(entity -> modelMapper.map(entity, BusinessDto.class));
    }

    public Optional<BusinessDto> getBusinessByOwnerId(long ownerId) {
        Optional<Business> optionalBusiness = businessRepository.findFirstByOwner_Id(ownerId);
        return optionalBusiness.map(business -> modelMapper.map(business, BusinessDto.class));
    }

    public Optional<Long> getBusinessIdByOwnerId(long ownerId) {
        return businessRepository.findFirstByOwner_Id(ownerId).map(Business::getId);
    }

    public Optional<Long> getBusinessIdByWorkerId(long workerId) {
        Optional<WorkerUser> worker = userService.getWorkerUserById(workerId).map(w -> modelMapper.map(w, WorkerUser.class));
        return worker.flatMap(workerUser -> businessRepository.findFirstByWorkers(List.of(workerUser)).map(Business::getId));
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
        addressService.updateAddress(request.getAddressRequest(), business.getId());

        return HttpStatus.CREATED;
    }

}

