package com.example.AgroVilla.repository;

import com.example.AgroVilla.constants.Role;
import com.example.AgroVilla.constants.SellerStatus;
import com.example.AgroVilla.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByPhoneNumber(String email);

    boolean existsByPhoneNumber(String email);

    List<User> findByRole(Role role);

    List<User> findByRoleAndSellerStatus(Role role, SellerStatus sellerStatus);
}