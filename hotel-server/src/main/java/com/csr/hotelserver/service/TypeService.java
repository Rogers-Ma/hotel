package com.csr.hotelserver.service;

import com.csr.hotelserver.dao.TypeRepository;
import com.csr.hotelserver.entity.Type;
import com.csr.hotelserver.entity.TypeVO;
import com.csr.hotelserver.util.exception.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import javax.persistence.RollbackException;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TypeService implements ServiceTemplate<Type, Long, TypeRepository> {

    @Autowired
    private TypeRepository typeRepository;

    @Autowired
    private RoomService roomService;

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

            Long typeId = conditions.containsKey("typeId") ?(Long) conditions.get("typeId"):null;
            if(typeId != null)
            {
                list.add(criteriaBuilder.equal(root.get("typeId").as(Long.class), typeId));
            }
            criteriaQuery.where(criteriaBuilder.and(list.toArray(new Predicate[0])));
            return criteriaQuery.getRestriction();
        };
    }

    @Override
    @Transactional(rollbackOn = RollbackException.class)
    public void deleteById(Long id) {
        Type type = this.typeRepository.getOne(id);
        type.setDeleted(type.getDeleted() + 1);
        this.update(type);
    }

    private Long count(Map conditions){
        return this.typeRepository.count(this.buildJpaSpecification(conditions));
    }

    public List getReserveMessage(){
        List<Type> types = this.findAll(new HashMap<>());
        List<TypeVO> typeVOS = new ArrayList<>(types.size());
        for (Type type: types){
            Map<String,Object> map = new HashMap<>();
            map.put("typeId",type.getId());
            map.put("state",0);
            TypeVO typeVO = new TypeVO(type,this.roomService.count(map));
            typeVOS.add(typeVO);
        }
        return typeVOS;
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
