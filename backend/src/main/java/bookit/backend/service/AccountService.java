package bookit.backend.service;

import bookit.backend.model.dto.*;
import bookit.backend.model.entity.user.*;
import bookit.backend.model.enums.UserRole;
import bookit.backend.model.request.CreateUserRequest;
import bookit.backend.model.request.LoginRequest;
import bookit.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserService userService;


    public Optional<UserDto> createUser(CreateUserRequest request) {
        UserRole role = request.getUserRole();
        if(userRepository.findByEmail(request.getEmail()).isPresent() || role == null) {
            return Optional.empty();
        }
        switch (role) {
            case ADMIN -> {
                return createAdminAccount(request);
            }
            case BUSINESS_OWNER -> {
                return createBusinessOwnerAccount(request);
            }
            case WORKER -> {
                return createWorkerAccount(request);
            }
            default -> {
                return createClientAccount(request);
            }
        }
    }

    public Optional<UserDto> updateUser(CreateUserRequest request, long id, UserRole oldRole) {
        if(userRepository.findById(id).isEmpty() || oldRole == null) {
            return Optional.empty();
        }
        if(request.getUserRole() == null) request.setUserRole(oldRole);
        switch (oldRole) {
            case ADMIN -> {
                return updateAdminAccount(request, id);
            }
            case BUSINESS_OWNER -> {
                return updateBusinessOwnerAccount(request, id);
            }
            case WORKER -> {
                return updateWorkerAccount(request, id);
            }
            default -> {
                return updateClientAccount(request, id);
            }
        }
    }

    Optional<UserDto> createAdminAccount(CreateUserRequest request) {
        AdminUser user = AdminUser.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(hashPassword(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .userRole(request.getUserRole())
                .isActive(true)
                .build();
        userRepository.save(user);
        return Optional.ofNullable(modelMapper.map(user, AdminUserDto.class));
    }

    Optional<UserDto> updateAdminAccount(CreateUserRequest request, long id) {
        Optional<AdminUser> userOptional;
        if((userOptional = userRepository.findById(id).map(u -> modelMapper.map(u, AdminUser.class))).isEmpty()) {
            return Optional.empty();
        }
        AdminUser user = userOptional.get();

        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(hashPassword(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
        user.setUserRole(request.getUserRole());

        userRepository.save(user);
        return Optional.ofNullable(modelMapper.map(user, AdminUserDto.class));
    }

    Optional<UserDto> createBusinessOwnerAccount(CreateUserRequest request) {
        BusinessOwnerUser user = BusinessOwnerUser.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(hashPassword(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .userRole(request.getUserRole())
                .isActive(true)
                .nip(request.getNip())
                .build();
        userRepository.save(user);
        return Optional.ofNullable(modelMapper.map(user, BusinessOwnerUserDto.class));
    }

    Optional<UserDto> updateBusinessOwnerAccount(CreateUserRequest request, long id) {
        Optional<BusinessOwnerUser> userOptional;
        if((userOptional = userRepository.findById(id).map(u -> modelMapper.map(u, BusinessOwnerUser.class))).isEmpty()) {
            return Optional.empty();
        }
        BusinessOwnerUser user = userOptional.get();

        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(hashPassword(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
        user.setUserRole(request.getUserRole());
        user.setNip(request.getNip());

        userRepository.save(user);
        return Optional.ofNullable(modelMapper.map(user, BusinessOwnerUserDto.class));
    }

    Optional<UserDto> createWorkerAccount(CreateUserRequest request) {
        WorkerUser user = WorkerUser.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(hashPassword(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .userRole(request.getUserRole())
                .isActive(true)
                .build();
        userRepository.save(user);
        return Optional.ofNullable(modelMapper.map(user, WorkerUserDto.class));
    }

    Optional<UserDto> updateWorkerAccount(CreateUserRequest request, long id) {
        Optional<WorkerUser> userOptional;
        if((userOptional = userRepository.findById(id).map(u -> modelMapper.map(u, WorkerUser.class))).isEmpty()) {
            return Optional.empty();
        }
        WorkerUser user = userOptional.get();

        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(hashPassword(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
        user.setUserRole(request.getUserRole());

        userRepository.save(user);
        return Optional.ofNullable(modelMapper.map(user, WorkerUserDto.class));
    }

    Optional<UserDto> createClientAccount(CreateUserRequest request) {
        ClientUser user = ClientUser.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(hashPassword(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .userRole(request.getUserRole())
                .isActive(true)
                .build();
        userRepository.save(user);
        return Optional.ofNullable(modelMapper.map(user, ClientUserDto.class));
    }

    Optional<UserDto> updateClientAccount(CreateUserRequest request, long id) {
        Optional<ClientUser> userOptional;
        if((userOptional = userRepository.findById(id).map(u -> modelMapper.map(u, ClientUser.class))).isEmpty()) {
            return Optional.empty();
        }
        ClientUser user = userOptional.get();
//        User user = userRepository.findById(id).get();

        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(hashPassword(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
        user.setUserRole(request.getUserRole());

        userRepository.save(user);
        return Optional.ofNullable(modelMapper.map(user, ClientUserDto.class));
    }

    String hashPassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    public Optional<UserDto> login(LoginRequest request) {
        return userService.getUserByEmail(request.getEmail())
                .filter(user -> bCryptPasswordEncoder.matches(request.getPassword(), user.getPassword()));
    }
}
