package com.csr.hotelserver.service;

import com.csr.hotelserver.dao.OrderRepository;
import com.csr.hotelserver.entity.Customer;
import com.csr.hotelserver.entity.Order;
import com.csr.hotelserver.entity.Room;
import com.csr.hotelserver.util.date.DateUtil;
import com.csr.hotelserver.util.exception.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
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
//                System.err.println(e.getMessage());
            }

            Long customerId = conditions.containsKey("customerId") ? (Long) conditions.get("customerId") : null;
            if(customerId != null){
                list.add(criteriaBuilder.equal(root.get("customerId").as(Long.class), customerId));
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
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        Order order = this.orderRepository.getOne(id);
        order.setDeleted(order.getDeleted() + 1);
        this.update(order);
    }

    @Transactional(rollbackFor = Exception.class)
    public void checkIn(Order order) {
        order.setState(order.getState() + 1);
        this.update(order);
    }

    @Transactional(rollbackFor = Exception.class)
    public void checkOut(Order order){
        order.setState(order.getState() + 1);
        Room room = order.getRoom();
        room.setState(0);
        this.update(order);
    }

    @Transactional(rollbackFor = Exception.class)
    public void createOrder(Long typeId, Long customerId, String date0, String date1) throws MyException {
        //查询条件
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("typeId", typeId);
        conditions.put("state", 0);


        List<Room> rooms = this.roomService.findAll(conditions);

        if(rooms == null || rooms.size() == 0){
            throw new MyException("房间已满");
        }

        Room room = rooms.get(0);

        //用户扣钱环节
        Double prices = room.getType().getPrice() * DateUtil.getDays(date0, date1);
        Customer customer = this.customerService.getOne(customerId);
        if(customer.getBalance() < prices){
            throw new MyException("余额不足");
        }

        //修改房间状态为1（已被预定）
        room.setState(1);

        customer.setBalance(customer.getBalance() - prices);
        this.customerService.update(customer);

        //修改房间状态
        this.roomService.update(room);
        Order order = new Order(room.getId(), customerId, DateUtil.strToSqlDate(date0), DateUtil.strToSqlDate(date1), prices);
        this.orderRepository.save(order);
    }

    public void cancel(Order order){
        Customer customer = this.customerService.getOne(order.getCustomerId());
        Room room = this.roomService.getOne(order.getRoomId());
        order.setRoom(null);
        order.setCustomer(null);
        //退钱
        Double price = order.getPrice();
        customer.setBalance(customer.getBalance() + price);
        customerService.update(customer);
        System.out.println(customer);
        //修改房间状态为0（空闲）
        room.setState(0);
        roomService.update(room);
        System.out.println(room);
        //修改订单状态
        order.setState(3);
        System.out.println(order);
        this.orderRepository.save(order);
    }
}
