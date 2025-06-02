package com.example.AgroVilla.service;

import com.example.AgroVilla.constants.Role;
import com.example.AgroVilla.constants.SellerStatus;
import com.example.AgroVilla.model.CustomUserDetails;
import com.example.AgroVilla.model.User;
import com.example.AgroVilla.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getUsersByRole(Role role) {
        return userRepository.findByRole(role);
    }

    @Transactional
    public User createUser(User user) {
        if (userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
            throw new RuntimeException("Email is already in use");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // Update fields
        user.setName(userDetails.getName());
        user.setPhoneNumber(userDetails.getPhoneNumber());

        // Only update email if it has changed and is not already in use
        if (!user.getPhoneNumber().equals(userDetails.getPhoneNumber())) {
            if (userRepository.existsByPhoneNumber(userDetails.getPhoneNumber())) {
                throw new RuntimeException("Email is already in use");
            }
            user.setPhoneNumber(userDetails.getPhoneNumber());
        }

        // Update password if provided
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }

        // Update role if provided
        if (userDetails.getRole() != null) {
            user.setRole(userDetails.getRole());
        }

        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }

        userRepository.deleteById(id);
    }

    public User getCurrentUser(Authentication authentication) {
        if (authentication == null) {
            return null;
        }

        if (authentication.getPrincipal() instanceof CustomUserDetails) {
            return ((CustomUserDetails) authentication.getPrincipal()).user();
        }

        return null;
    }

    @Transactional
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);
    }

    public UserDetails loadUserByUsername(String phoneNumber) {
        Optional<User> byPhoneNumber = userRepository.findByPhoneNumber(phoneNumber);
        return byPhoneNumber.map(CustomUserDetails::new).orElse(null);
    }

    public List<User> getUsersByRoleAndStatus(Role role, SellerStatus sellerStatus) {
        return userRepository.findByRoleAndSellerStatus(role, sellerStatus);
    }
}