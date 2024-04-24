package com.example.api.service.user;

import com.example.api.model.user.User;
import com.example.api.repository.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private UserRepository repository;

    @Autowired @Lazy
    private PasswordEncoder encoder;

    public void saveUser(User user) {
        repository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.error("User not found: {}", username);
                    return new UsernameNotFoundException("User not found: " + username);
                });
        return new UserDetailsImpl(user);
    }

    @Transactional
    public void updatePassword(User user, String newPassword) {
        logger.info("Updating password for user: {}", user.getUsername());
        user.setPassword(encoder.encode(newPassword));
        user.setLastPasswordChangeDate(new Date(System.currentTimeMillis()));
        repository.save(user);
        logger.info("Password updated");
    }

    public Optional<User> getUserByUsername(String username) {
        return repository.findByUsername(username);
    }

    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    public boolean isUserIsEmpty(Optional<?> optional) {
        logger.info("Validate if user is empty");
        if (optional.isEmpty()) {
            logger.error("User is empty");
            return true;
        }
        logger.error("User is not empty");
        return false;
    }

    public boolean isValidOldPassword(User user, String oldPassword) {
        logger.info("Validate current password");
        if (!encoder.matches(oldPassword, user.getPassword())) {
            logger.error("Old password is not the same");
            return false;
        }
        logger.info("Old password is the same");
        return true;
    }

    public boolean isValidNewPassword(String newPassword, String oldPassword) {
        logger.info("Validate new password");
        if (newPassword.equals(oldPassword)) {
            logger.error("New password is the same as old password");
            return false;
        }
        logger.info("New password is not the same as old password");
        return true;
    }
}
