package com.joyfarm.farmstival.member.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.joyfarm.farmstival.member.constants.Authority;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Builder
@IdClass(AuthoritiesId.class)
@NoArgsConstructor
@AllArgsConstructor
public class Authorities {
    @Id
    @JsonIgnore //json변환시 문제 발생 배제 - 순환참조 문제 해결
    @ManyToOne(fetch= FetchType.LAZY)
    private Member member;

    @Id
    @Column(length=20)
    @Enumerated(EnumType.STRING)
    private Authority authority;
}