package com.csr.hotelserver.dao;

import com.csr.hotelserver.entity.Admin;
import com.csr.hotelserver.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long>, JpaSpecificationExecutor<Admin> {
    Admin findByNameAndPassword(String name, String password);
}
