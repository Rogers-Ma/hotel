package com.csr.hotelserver.dao;

import com.csr.hotelserver.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRepository extends JpaRepository<Type, Long>, JpaSpecificationExecutor<Type> {
    Type findByNameAndDeleted(String name, Integer deleted);
}
