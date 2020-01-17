package com.csr.hotelserver.service;

import com.csr.hotelserver.dao.OrderRepository;
import com.csr.hotelserver.entity.Customer;
import com.csr.hotelserver.entity.Order;
import com.csr.hotelserver.entity.Room;
import com.csr.hotelserver.util.exception.MyException;
import javafx.beans.value.ObservableObjectValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.RollbackException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService implements ServiceTemplate<Order, Long, OrderRepository> {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private RoomService roomService;

    @Override
    public OrderRepository getRepository() {
        return this.orderRepository;
    }

    @Override
    public Specification<Order> buildJpaSpecification(Map<String, Object> conditions) {
        return (Specification<Order>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            list.add(criteriaBuilder.equal(root.get("deleted").as(Integer.class), 0));
            try {
                Integer state = conditions.containsKey("state") ? (Integer) conditions.get("state") : null;
                if (state != null && state >= 0) {
                    list.add(criteriaBuilder.equal(root.get("state").as(Integer.class), state));
                }
            } catch (Exception e) {
            }
            String realName = conditions.containsKey("realName") ? (String) conditions.get("realName") : null;
            if (realName != null && !"".equals(realName)) {
                List<Customer> customers = null;
                Map<String, Object> map = new HashMap<>();
                map.put("realName", realName);
                customers = this.customerService.findAll(map);
                if (customers != null && customers.size() != 0) {
                    CriteriaBuilder.In<Long> in = criteriaBuilder.in(root.get("customerId"));
                    for (Customer customer : customers) {
                        in.value(customer.getId());
                    }
                    list.add(in);
                } else {
                    list.add(criteriaBuilder.equal(root.get("customerId"), 0L));
                }
            }
            criteriaQuery.where(criteriaBuilder.and(list.toArray(new Predicate[0])));
            return criteriaQuery.getRestriction();
        };
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Order order = this.orderRepository.getOne(id);
        order.setDeleted(order.getDeleted() + 1);
        this.update(order);
    }

    public void checkIn(Order order) {
        order.setState(order.getState() + 1);
        this.update(order);
    }

    public void checkOut(Order order){
        order.setState(order.getState() + 1);
        Room room = order.getRoom();
        room.setState(0);
        this.update(order);
    }
}
