package com.csr.hotelserver.controller;

import com.alibaba.fastjson.JSONObject;
import com.csr.hotelserver.entity.Customer;
import com.csr.hotelserver.service.CustomerService;
import com.csr.hotelserver.util.exception.MyException;
import com.csr.hotelserver.util.result.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @RequestMapping(value = "customer-manage", method = RequestMethod.GET)
    public Object findAllByPage(@RequestParam Integer pageNo,@RequestParam Integer pageSize, @RequestParam(required = false, name = "condition") String condition){
        Map<String,Object> conditionMap = new HashMap<>();
        if(condition != null && !condition.equals("")) {
            conditionMap.putAll(JSONObject.parseObject(condition,Map.class));
        }
        return ResultUtil.ok(this.customerService.findByPage(pageNo-1,pageSize,conditionMap));
    }

    @RequestMapping(value = "customer-manage", method = RequestMethod.PATCH)
    public Object updateCustomer(@RequestBody Customer customer){
        System.out.println(customer);
        this.customerService.update(customer);
        return ResultUtil.ok("修成成功");
    }

    @RequestMapping(value = "customer-manage", method = RequestMethod.POST)
    public Object addCustomer(@Valid @RequestBody Customer  customer, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            return ResultUtil.error(fieldError.getDefaultMessage());
        }
        customer.setBalance(0.000);
        customer.setId(null);
        customer.setDeleted(0);
        customer.setPassword("123456");
        try {
            this.customerService.save(customer);
        } catch (MyException e) {
            return ResultUtil.error("用户名已存在！");
        }
        return ResultUtil.ok("添加成功");
    }

    @RequestMapping(value = "customer-manage", method = RequestMethod.DELETE)
    public Object deleteCustomer(@RequestParam(name = "id") String id){
        this.customerService.deleteById(Long.valueOf(id));
        return ResultUtil.ok("删除成功");
    }

    @RequestMapping(value = "customer-login", method = RequestMethod.POST)
    public Object Login(@RequestBody Customer customer){
        Customer admin = this.customerService.login(customer.getName(),customer.getPassword());
        if(admin == null) {
            return ResultUtil.error("账号或密码错误");
        }else {
            return ResultUtil.ok(admin);
        }
    }
}
