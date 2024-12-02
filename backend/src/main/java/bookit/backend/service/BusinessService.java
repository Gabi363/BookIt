package bookit.backend.service;

import bookit.backend.model.dto.BusinessDto;
import bookit.backend.model.dto.user.BusinessOwnerUserDto;
import bookit.backend.model.entity.Business;
import bookit.backend.model.entity.user.BusinessOwnerUser;
import bookit.backend.model.enums.BusinessType;
import bookit.backend.model.request.CreateBusinessRequest;
import bookit.backend.repository.BusinessRepository;
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
    private final AccountService accountService;
    private final ModelMapper modelMapper;

    public List<BusinessDto> getBusinesses() {
        return businessRepository.findAll()
                .stream()
                .map(entity -> modelMapper.map(entity, BusinessDto.class))
                .collect(Collectors.toList());
    }

    public Optional<BusinessDto> getBusinessByOwnerId(long ownerId) {
        Optional<BusinessOwnerUserDto> owner = userService.getBusinessOwnerUserById(ownerId);
        if(owner.isEmpty()) return Optional.empty();

        return businessRepository.findById(owner.get().getBusinessId())
                .map(business -> modelMapper.map(business, BusinessDto.class));
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

//    public HttpStatus addWorkerToBusiness(long businessId, CreateUserRequest request) {
//        Optional<Business> businessOptional;
//        if((businessOptional = businessRepository.findById(businessId)).isEmpty()){
//            return HttpStatus.NOT_FOUND;
//        }
//        Business business = businessOptional.get();
////        var workerOptional = userRepository.findByEmail(request.getEmail());
////        if(workerOptional.isEmpty()) return HttpStatus.NOT_FOUND;
//
////        User user = modelMapper.map(workerOptional.get(), User.class);
////        if(user.getUserRole() != UserRole.WORKER) {
////            log.info(user.getUserRole());
////            return HttpStatus.FORBIDDEN;
////        }
//
//        Optional<UserDto> worker = accountService.createWorkerAccount(request, business);
////        WorkerUser worker = WorkerUser.builder()
////                .email(request.getEmail())
////                .firstName(request.getFirstName())
////                .lastName(request.getLastName())
////                .password(request.getPassword())
////                .phoneNumber(request.getPhoneNumber())
////                .userRole(UserRole.WORKER)
////                .isActive(true)
////                .business(business)
////                .build();
////        log.info("start deleting");
////        userService.deleteUser(user.getId());
////        log.info("deleted");
////        log.info("start saving");
////        worker.setBusiness(business);
////        userRepository.save(worker);
////        log.info("saved worker");
//
////        var workersList = business.getWorkers();
////        workersList.add(worker);
////        business.setWorkers(workersList);
////        businessRepository.save(business);
////        log.info("saved business");
//
//        return HttpStatus.OK;
//    }
}
