package com.joyfarm.farmstival.member.controllers;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.UUID;

@Data
public class RequestJoin {

    private String gid = UUID.randomUUID().toString();

    @NotBlank @Email
    private String email;

    @NotBlank @Size(min=8)
    private String password;

    @NotBlank
    private String confirmPassword;

    @NotBlank
    private String userName;

    @NotBlank
    private String mobile;

    @AssertTrue
    private boolean agree;
}
