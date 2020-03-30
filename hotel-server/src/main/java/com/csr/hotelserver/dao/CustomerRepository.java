package com.csr.hotelserver.dao;

import com.csr.hotelserver.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
    Customer findByNameAndPassword(String name, String password);
    Customer findByNameAndDeleted(String name, Integer deleted);
}
