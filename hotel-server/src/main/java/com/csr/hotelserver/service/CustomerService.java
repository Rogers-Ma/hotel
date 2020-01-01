package com.csr.hotelserver.service;

import com.csr.hotelserver.dao.CustomerRepository;
import com.csr.hotelserver.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import javax.persistence.criteria.Predicate;
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
            Predicate p1 = null, p2 = null, p3 = null;
            if (realName != null && !"".equals(realName)) {
                p1 = criteriaBuilder.equal(root.get("realName").as(String.class), realName);
                list.add(p1);
            }
            if (name != null && !"".equals(name)) {
                p2 = criteriaBuilder.equal(root.get("name").as(String.class), name);
                list.add(p2);
            }
            if (telephone != null && !"".equals(telephone)) {
                p3 = criteriaBuilder.equal(root.get("telephone").as(String.class), telephone);
                list.add(p3);
            }

            criteriaQuery.where(criteriaBuilder.and(list.toArray(new Predicate[0])));
            return criteriaQuery.getRestriction();
        };
    }

    @Override
    public void save(Customer customer) throws Exception {
        Map<String,Object> map = new HashMap<>(1);
        map.put("name",customer.getName());
        if(0 != customerRepository.count(buildJpaSpecification(map))){
            throw new Exception("用户名已存在！");
        }
        customerRepository.save(customer);
    }
}
