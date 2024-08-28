package com.joyfarm.farmstival.member.controllers;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.joyfarm.farmstival.member.entities.Authorities;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
public class RequestUpdate {

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedAt;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deletedAt;

    private List<Authorities> authorities;

    private Long seq;

    private String gid;

    private String email;

    private String password;

    private String confirmPassword;

    private String userName;

    private String mobile;
}