package com.joyfarm.farmstival.member.controllers;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class RequestUpdate {

    private String password;

    private String confirmPassword;

    private String userName;

    private String mobile;
}
