package bookit.backend.service;

import bookit.backend.model.dto.UserDto;
import bookit.backend.model.entity.user.User;
import bookit.backend.model.enums.UserRole;
import bookit.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public List<UserDto> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(entity -> modelMapper.map(entity, UserDto.class))
                .collect(Collectors.toList());
    }

    public Optional<UserDto> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(user -> modelMapper.map(user, UserDto.class));
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);

        return (UserDetails) user.orElseThrow();
//        return user.get().orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public Collection<? extends GrantedAuthority> mapRoleToAuthority(UserRole role) {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }
}
