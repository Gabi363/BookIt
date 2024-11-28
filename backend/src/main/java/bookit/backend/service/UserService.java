package bookit.backend.service;

import bookit.backend.model.dto.UserDto;
import bookit.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public List<UserDto> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(entity -> modelMapper.map(entity, UserDto.class))
                .collect(Collectors.toList());
    }

    public Optional<UserDto> getUserByEmail(String email) {
        return userRepository.getUserByEmail(email)
                .map(user -> modelMapper.map(user, UserDto.class));
    }

}
