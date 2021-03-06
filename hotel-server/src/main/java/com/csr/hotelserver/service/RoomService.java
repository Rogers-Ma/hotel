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
import java.util.List;
import java.util.Map;

@Service
public class RoomService implements ServiceTemplate<Room, Long, RoomRepository> {
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

            Integer state = conditions.containsKey("state") ? (Integer) conditions.get("state") : null;
            if (state != null) {
                list.add(criteriaBuilder.equal(root.get("state").as(Integer.class), state));
            }
            String number = conditions.containsKey("number") ? (String) conditions.get("number") : null;
            if (number != null && !"".equals(number)) {
                list.add(criteriaBuilder.equal(root.get("number").as(String.class), number));
            }
            try {
                Long type = conditions.containsKey("typeId") ? (Long) conditions.get("typeId") : null;
                if (type != null && !type.equals(0)) {
                    list.add(criteriaBuilder.equal(root.get("typeId").as(Long.class), type));
                }
            } catch (Exception e) {
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
        room.setDeleted(room.getDeleted() + 1);
        this.update(room);
    }

    @Override
    public void save(Room room) {
        if (this.roomRepository.findByNumberAndDeleted(room.getNumber(), 0) != null) {
            throw new MyException("房间编号已存在");
        }
        this.roomRepository.save(room);
    }

    @Override
    public void update(Room room) {
        Room oldRoom = this.roomRepository.findByNumberAndDeleted(room.getNumber(), 0);
        if (oldRoom != null && !oldRoom.getId().equals(room.getId())) {
            throw new MyException("房间编号已存在");
        }
        this.roomRepository.save(room);
    }

    public Long count(Map conditions) {
        return this.roomRepository.count(this.buildJpaSpecification(conditions));
    }
}
