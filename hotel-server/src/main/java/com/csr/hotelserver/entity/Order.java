package com.csr.hotelserver.entity;

import com.csr.hotelserver.util.isbn.IsbnUtil;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Data
@Entity
@Table(name = "`order`")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "room_id")
    private Long roomId;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "state")
    private Integer state;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "room_id", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Room room;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "customer_id", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Customer customer;

    @NotNull(message = "订单总金额不能为空")
    @Column(name = "price")
    private Double price;

    @Column(name = "deleted")
    private Integer deleted;

    public Order(){}

    public Order(Long roomId, Long customerId, Date startDate, Date endDate, Double prices){
        this.roomId = roomId;
        this.customerId = customerId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = prices;
        this.createDate = new Date((new java.util.Date()).getTime());
        this.deleted = 0;
        this.state = 0;
        this.isbn = IsbnUtil.getOnumber();
    }
}
