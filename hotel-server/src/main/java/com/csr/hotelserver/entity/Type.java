package com.csr.hotelserver.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "type")
public class Type {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "类型名称不能为空")
    @Column(name = "name")
    private String name;

    @NotNull(message = "价格不能为空")
    @Column(name = "price")
    private Double price;

    @Column(name = "deposit")
    @NotNull(message = "押金不能为空")
    private Double deposit;
}
