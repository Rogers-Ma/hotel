package com.csr.hotelserver.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Data
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "用户姓名不能为空！")
    @Column(name = "real_name")
    private String realName;

    @NotBlank(message = "用户名不能为空！")
    @Column(name = "name")
    private String name;


    @Column(name = "password")
    private String password;

    @NotBlank(message = "手机号不能为空！")
    @Column(name = "telephone")
    private String telephone;

    @Column(name = "balance")
    private Double balance;
}
