package com.example.AgroVilla.dto;

import com.example.AgroVilla.constants.Role;

public record UserUpdateRequest(
        String name,
        Role role,
        String password,
        String phoneNumber
) {
}