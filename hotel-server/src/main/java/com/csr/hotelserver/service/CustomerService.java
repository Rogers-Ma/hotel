package com.csr.hotelserver.service;

import com.csr.hotelserver.dao.CustomerRepository;
import com.csr.hotelserver.entity.Customer;
import com.csr.hotelserver.util.exception.MyException;
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
public class CustomerService implements ServiceTemplate<Customer,Long,CustomerRepository> {
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public CustomerRepository getRepository() {
        return this.customerRepository;
    }

    @Override
    public Specification<Customer> buildJpaSpecification(Map<String, Object> conditions) {
        return (Specification<Customer>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            String realName = conditions.containsKey("realName")?(String)conditions.get("realName"):null;
            String name = conditions.containsKey("name")?(String) conditions.get("name"):null;
            String telephone = conditions.containsKey("telephone")?(String) conditions.get("telephone"):null;
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
    @Transactional
    public void deleteById(Long id) {
        Customer customer = this.customerRepository.getOne(id);
        customer.setDeleted(customer.getDeleted()+1);
        this.update(customer);
    }

    @Override
    public void save(Customer customer) throws MyException {
        Map<String,Object> map = new HashMap<>(1);
        map.put("name",customer.getName());
        map.put("deleted",0);
        if(0 != customerRepository.count(buildJpaSpecification(map))){
            throw new MyException("用户名已存在！");
        }
        customerRepository.save(customer);
    }

    public Customer login(String name, String password){
        return this.customerRepository.findByNameAndPassword(name, password);
    }
}
