package bookit.backend.service;

import bookit.backend.model.dto.*;
import bookit.backend.model.entity.user.AdminUser;
import bookit.backend.model.entity.user.BusinessOwnerUser;
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
        if(userRepository.findByEmail(request.getEmail()).isPresent()) {
            return Optional.empty();
        }

        UserRole role = request.getUserRole();
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

    Optional<UserDto> createWorkerAccount(CreateUserRequest request) {
        BusinessOwnerUser user = BusinessOwnerUser.builder()
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

    Optional<UserDto> createClientAccount(CreateUserRequest request) {
        BusinessOwnerUser user = BusinessOwnerUser.builder()
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

    String hashPassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    public Optional<UserDto> login(LoginRequest request) {
        return userService.getUserByEmail(request.getEmail())
                .filter(user -> bCryptPasswordEncoder.matches(request.getPassword(), user.getPassword()));
    }
}
