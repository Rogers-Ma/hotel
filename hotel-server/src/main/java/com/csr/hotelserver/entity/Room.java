package com.csr.hotelserver.entity;

import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "房间号不能为空")
    @Column(name = "number")
    private String number;

    @Column(name = "type_id")
    @NotNull(message = "房间类型不能为空")
    private Long typeId;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "type_id", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Type type;

    @Column(name = "state")
    private Integer state;
}
