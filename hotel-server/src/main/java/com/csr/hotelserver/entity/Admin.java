package com.csr.hotelserver.entity;

import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@Table(name = "admin")
public class Admin {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "用户名不能为空")
    @Column(name = "name")
    private String name;

    @Column(name = "password")
    @NotBlank(message = "密码不能为空")
    private String password;
}
