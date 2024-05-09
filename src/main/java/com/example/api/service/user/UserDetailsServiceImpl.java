package com.example.api.service.user;

import com.example.api.config.SecurityConfig;
import com.example.api.model.user.Role;
import com.example.api.model.user.User;
import com.example.api.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Autowired @Lazy
    private SecurityConfig securityConfig;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username)
                .map(UserDetailsImpl::new)
                .orElseThrow(() -> usernameNotFoundException(username));
    }

    public User getUserEntityByUsername(String username) {
        log.info("Fetching user {}", username);
        return repository.findByUsername(username)
                .orElseThrow(() -> usernameNotFoundException(username));
    }

    private String getEncodePassword(String password) {
        return securityConfig.passwordEncoder().encode(password);
    }

    private Date getLastPasswordChangeDate() {
        return new Date();
    }

    private Role getRole(Role role) {
        return role == null ? Role.USER : role;
    }

    @Transactional
    public void saveUser(User user) {
        log.info("Saving user {}", user.getUsername());
        repository.save(user);
    }

    @Transactional
    public void createUser(String username, String password, Role role) {
        log.info("Creating user {}", username);
        User user = new User();
        user.setUsername(username);
        user.setPassword(getEncodePassword(password));
        user.setLastPasswordChangeDate(getLastPasswordChangeDate());
        user.setRole(getRole(role));
        saveUser(user);
        log.info("User {} created", username);
    }

    @Transactional
    public void updatePassword(User user, String newPassword) {
        String username = user.getUsername();
        log.info("Updating password {}", username);
        user.setPassword(getEncodePassword(newPassword));
        user.setLastPasswordChangeDate(getLastPasswordChangeDate());
        saveUser(user);
        log.info("User password {} updated", username);
    }

    @Transactional
    public boolean existsUserByUsername(String username) {
        log.info("Checking if user {} exists", username);
        if (repository.findByUsername(username).isPresent()) {
            log.info("User {} already exists", username);
            return true;
        }
        log.info("User {} does not exist", username);
        return false;
    }

    private UsernameNotFoundException usernameNotFoundException(String username){
        String errorMsg = "User " + username + " not found";
        log.error(errorMsg);
        return new UsernameNotFoundException(errorMsg);
    }

    public boolean isValidOldPassword(User user, String oldPassword) {
        log.info("Validate current password");
        if (!securityConfig.passwordEncoder().matches(oldPassword, user.getPassword())) {
            log.error("Old password is not the same");
            return false;
        }
        log.info("Old password is the same");
        return true;
    }

    public boolean isValidNewPassword(String newPassword, String oldPassword) {
        log.info("Validate new password");
        if (newPassword.equals(oldPassword)) {
            log.error("New password is the same as old password");
            return false;
        }
        log.info("New password is not the same as old password");
        return true;
    }
}
