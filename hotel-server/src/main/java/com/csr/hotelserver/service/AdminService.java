package com.csr.hotelserver.service;

import com.csr.hotelserver.dao.AdminRepository;
import com.csr.hotelserver.entity.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService{

    @Autowired
    private AdminRepository adminRepository;

    public Admin login(String name, String password){
        return this.adminRepository.findByNameAndPassword(name, password);
    }
}
