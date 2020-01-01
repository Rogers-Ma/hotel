package com.csr.hotelserver.service;

import com.csr.hotelserver.dao.RoomRepository;
import com.csr.hotelserver.entity.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import javax.persistence.criteria.Predicate;
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
            String number = conditions.containsKey("number")?(String) conditions.get("number"):null;
            if (number != null && !"".equals(number)) {
                list.add(criteriaBuilder.equal(root.get("number").as(String.class), number));
            }
            try {
                Integer type = conditions.containsKey("typeId") ? (Integer)conditions.get("typeId") : null;
                if (number != null && !type.equals(0)) {
                    list.add(criteriaBuilder.equal(root.get("typeId").as(Long.class), Long.valueOf(type)));
                }
            }catch (Exception e){}

            criteriaQuery.where(criteriaBuilder.and(list.toArray(new Predicate[0])));
            return criteriaQuery.getRestriction();
        };
    }

    @Override
    public void save(Room room) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("number",room.getNumber());
        if(this.roomRepository.count(buildJpaSpecification(map)) > 0){
            throw new Exception("房间号已存在");
        }
        this.roomRepository.save(room);
    }
}
