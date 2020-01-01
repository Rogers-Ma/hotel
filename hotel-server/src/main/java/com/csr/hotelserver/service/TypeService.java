package com.csr.hotelserver.service;

import com.csr.hotelserver.dao.TypeRepository;
import com.csr.hotelserver.entity.Customer;
import com.csr.hotelserver.entity.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class TypeService implements ServiceTemplate<Type,Long, TypeRepository> {

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
            String name = conditions.containsKey("name")?(String) conditions.get("name"):null;
            if (name != null && !"".equals(name)) {
                list.add(criteriaBuilder.equal(root.get("name").as(String.class), name));
            }
            criteriaQuery.where(criteriaBuilder.and(list.toArray(new Predicate[0])));
            return criteriaQuery.getRestriction();
        };
    }

    @Override
    public void save(Type type) throws Exception {
        Map<String,Object> map = new HashMap<>(1);
        map.put("name",type.getName());
        if(0 != this.typeRepository.count(buildJpaSpecification(map))){
            throw new Exception("类型名称已存在");
        }
        this.typeRepository.save(type);
    }
}
