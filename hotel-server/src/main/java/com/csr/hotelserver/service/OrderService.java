package com.csr.hotelserver.service;

import com.csr.hotelserver.dao.OrderRepository;
import com.csr.hotelserver.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OrderService implements ServiceTemplate<Order, Long, OrderRepository> {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public OrderRepository getRepository() {
        return this.orderRepository;
    }

    @Override
    public Specification<Order> buildJpaSpecification(Map<String, Object> conditions) {
        return null;
    }

    @Override
    public void save(Order order) throws Exception {

    }
}
