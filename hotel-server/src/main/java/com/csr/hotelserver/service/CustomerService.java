package com.csr.hotelserver.service;

import com.csr.hotelserver.dao.CustomerRepository;
import com.csr.hotelserver.entity.Customer;
import com.csr.hotelserver.util.exception.MyException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerService implements ServiceTemplate<Customer, Long, CustomerRepository> {
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public CustomerRepository getRepository() {
        return this.customerRepository;
    }

    public Customer findByNameAndDeleted(String name, Integer deleted){
        return this.customerRepository.findByNameAndDeleted(name, 0);
    }

    @Override
    public Specification<Customer> buildJpaSpecification(Map<String, Object> conditions) {
        return (Specification<Customer>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            String realName = conditions.containsKey("realName") ? (String) conditions.get("realName") : null;
            String name = conditions.containsKey("name") ? (String) conditions.get("name") : null;
            String telephone = conditions.containsKey("telephone") ? (String) conditions.get("telephone") : null;
            if (realName != null && !"".equals(realName)) {
                list.add(criteriaBuilder.like(root.get("realName").as(String.class), realName));
            }
            if (name != null && !"".equals(name)) {
                list.add(criteriaBuilder.equal(root.get("name").as(String.class), name));
            }
            if (telephone != null && !"".equals(telephone)) {
                list.add(criteriaBuilder.like(root.get("telephone").as(String.class), telephone));
            }
            list.add(criteriaBuilder.equal(root.get("deleted").as(Integer.class), 0));
            criteriaQuery.where(criteriaBuilder.and(list.toArray(new Predicate[0])));
            return criteriaQuery.getRestriction();
        };
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteById(Long id) {
        Customer customer = this.customerRepository.getOne(id);
        customer.setDeleted(customer.getDeleted() + 1);
        this.customerRepository.save(customer);
    }

    @Override
    public void save(Customer customer) {
        if (this.customerRepository.findByNameAndDeleted(customer.getName(), 0) != null){
            throw new MyException("用户名已存在");
        }
        this.customerRepository.save(customer);
    }

    @Override
    public void update(Customer customer) {
        Customer oldCustomer = this.customerRepository.findByNameAndDeleted(customer.getName(), 0);
        System.out.println(oldCustomer);
        if(oldCustomer != null && !oldCustomer.getId().equals(customer.getId())){
            throw new MyException("用户名已存在");
        }
        this.customerRepository.save(customer);
    }

    public Customer login(String name, String password) {
        return this.customerRepository.findByNameAndPassword(name, password);
    }
}
