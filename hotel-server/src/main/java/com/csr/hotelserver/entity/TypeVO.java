package com.csr.hotelserver.entity;

import lombok.Data;

import javax.persistence.Entity;
import java.io.Serializable;

@Data
public class TypeVO implements Serializable {
    private Long id;
    private String name;
    private Double price;
    private Integer deleted;
    private Long count;

    public TypeVO(Type type, Long count){
        this.id = type.getId();
        this.name = type.getName();
        this.price = type.getPrice();
        this.deleted = type.getDeleted();
        this.count = count;
    }
}





