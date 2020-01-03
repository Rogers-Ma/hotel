package com.csr.hotelserver.service;

import com.csr.hotelserver.dao.TypeRepository;
import com.csr.hotelserver.entity.Customer;
import com.csr.hotelserver.entity.Type;
import com.csr.hotelserver.util.exception.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class TypeService implements ServiceTemplate<Type, Long, TypeRepository> {

    @Autowired
    TypeRepository typeRepository;

    @Override
    public TypeRepository getRepository() {
        return this.typeRepository;
    }

    @Override
    public Specification<Type> buildJpaSpecification(Map<String, Object> conditions) {
        return (Specification<Type>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            list.add(criteriaBuilder.equal(root.get("deleted").as(Integer.class), 0));
            String name = conditions.containsKey("name") ? (String) conditions.get("name") : null;
            if (name != null && !"".equals(name)) {
                list.add(criteriaBuilder.equal(root.get("name").as(String.class), name));
            }
            criteriaQuery.where(criteriaBuilder.and(list.toArray(new Predicate[0])));
            return criteriaQuery.getRestriction();
        };
    }

    @Override
    public void deleteById(Long id) {
        Type type = this.typeRepository.getOne(id);
        type.setDeleted(type.getDeleted() + 1);
        this.update(type);
    }

    @Override
    public void save(Type type) throws MyException {
        Map<String, Object> map = new HashMap<>(1);
        map.put("name", type.getName());
        map.put("deleted", 0);
        if (0 != this.typeRepository.count(buildJpaSpecification(map))) {
            throw new MyException("类型名称已存在");
        }
        this.typeRepository.save(type);
    }
}
