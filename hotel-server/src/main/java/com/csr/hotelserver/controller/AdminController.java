package com.csr.hotelserver.controller;

import com.csr.hotelserver.entity.Admin;
import com.csr.hotelserver.service.AdminService;
import com.csr.hotelserver.util.result.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AdminController {

    @Autowired
    private AdminService adminService;

    @RequestMapping(value = "admin-login", method = RequestMethod.POST)
    public Object Login(@RequestBody Admin a){
        Admin admin = this.adminService.login(a.getName(),a.getPassword());
        if(admin == null) {
            return ResultUtil.error("账号或密码错误");
        }else {
            return ResultUtil.ok(admin);
        }
    }

    @RequestMapping(value = "admin-logout", method = RequestMethod.GET)
    public Object Logout(){
        return ResultUtil.ok();
    }
}
