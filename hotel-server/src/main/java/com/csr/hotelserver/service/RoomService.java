package com.csr.hotelserver.service;

import com.csr.hotelserver.dao.RoomRepository;
import com.csr.hotelserver.entity.Room;
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
public class RoomService implements ServiceTemplate<Room, Long, RoomRepository>{
    @Autowired
    private RoomRepository roomRepository;

    @Override
    public RoomRepository getRepository() {
        return this.roomRepository;
    }

    @Override
    public Specification<Room> buildJpaSpecification(Map<String, Object> conditions) {
        return (Specification<Room>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            list.add(criteriaBuilder.equal(root.get("deleted").as(Integer.class), 0));
            String number = conditions.containsKey("number")?(String) conditions.get("number"):null;
            if (number != null && !"".equals(number)) {
                list.add(criteriaBuilder.equal(root.get("number").as(String.class), number));
            }
            try {
                Long type = conditions.containsKey("typeId") ? (Long)conditions.get("typeId") : null;
                if (type != null && !type.equals(0)) {
                    list.add(criteriaBuilder.equal(root.get("typeId").as(Long.class), type));
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            criteriaQuery.where(criteriaBuilder.and(list.toArray(new Predicate[0])));
            return criteriaQuery.getRestriction();
        };
    }

    @Transactional(rollbackOn = RollbackException.class)
    @Override
    public void deleteById(Long id) {
        Room room = this.roomRepository.getOne(id);
        room.setDeleted(room.getDeleted()+1);
        this.update(room);
    }

    public Long count(Map conditions){
        return this.roomRepository.count(this.buildJpaSpecification(conditions));
    }

    @Override
    public void save(Room room) throws MyException {
        Map<String, Object> map = new HashMap<>();
        map.put("number",room.getNumber());
        map.put("deleted",0);
        if(this.roomRepository.count(buildJpaSpecification(map)) > 0){
            throw new MyException("房间号已存在");
        }
        this.roomRepository.save(room);
    }
}
