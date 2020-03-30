package com.csr.hotelserver.dao;

import com.csr.hotelserver.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long>, JpaSpecificationExecutor<Room> {
    Room findByNumberAndDeleted(String number, Integer deleted);
}
