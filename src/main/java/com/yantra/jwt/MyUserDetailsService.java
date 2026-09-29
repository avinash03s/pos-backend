package com.yantra.jwt;


import com.yantra.entity.User;
import com.yantra.repositroy.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {


    private final UserRepository userRepository;



    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {


        User user = userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new UsernameNotFoundException(
                                "User not found with email: " + email
                        )
                );


        return new CustomUserDetails(

                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getPassword(),
                user.getRole()

        );
    }
}