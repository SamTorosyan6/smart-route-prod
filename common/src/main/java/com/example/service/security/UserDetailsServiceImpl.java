package com.example.service.security;

import com.example.exception.AccountPendingException;
import com.example.exception.AccountRejectedException;
import com.example.exception.BlockedUserException;
import com.example.model.UserStatus;
import com.example.model.entitiy.User;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with Email: " + username));

        if (user.isBlocked()) {
            throw new BlockedUserException("This user is blocked. Contact admin.");
        }

        if(user.getStatus() == UserStatus.PENDING){
            throw new AccountPendingException("Your account is waiting for admin approval.");
        }

        if (user.getStatus() == UserStatus.REJECTED) {
            throw new AccountRejectedException("Your account was rejected by admin");
        }

        return new SpringUser(user);
    }
}
