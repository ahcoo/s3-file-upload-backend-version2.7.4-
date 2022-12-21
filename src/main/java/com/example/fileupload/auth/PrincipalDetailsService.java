package com.example.fileupload.auth;

import com.example.fileupload.user.domain.User;
import com.example.fileupload.user.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("받은 username : " + username);
        User user = userRepository.findByUsername(username);
        if(user == null) {
            return null;
        }
        System.out.println("username : " + user.getUsername());
        System.out.println("password : " + user.getPassword());
        return new PrincipalDetails(user);
    }
}
