package com.example.AgroVilla.dto;

import com.example.AgroVilla.constants.SellerStatus;
import lombok.Getter;
import lombok.Setter;
import com.example.AgroVilla.constants.Role;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserResponse {
    private Long id;
    private String name;
    private Role role;
    private String phoneNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private SellerStatus sellerStatus;
}