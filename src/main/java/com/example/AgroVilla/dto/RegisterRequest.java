package com.example.AgroVilla.dto;


import com.example.AgroVilla.annotation.ValidRole;
import com.example.AgroVilla.constants.Role;
import com.example.AgroVilla.constants.SellerStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 5, message = "Password must be at least 5 characters")
        String password,

        @ValidRole(message = "Role must be valid")
        Role role,

        String name,
        String phoneNumber,
        SellerStatus sellerStatus
) {
}