package com.csr.hotelserver.controller;

import com.alibaba.fastjson.JSONObject;
import com.csr.hotelserver.entity.Order;
import com.csr.hotelserver.service.OrderService;
import com.csr.hotelserver.util.result.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "order-manage", method = RequestMethod.GET)
    public Object findByPage(@RequestParam Integer pageNo, @RequestParam Integer pageSize, @RequestParam(required = false, name = "condition") String condition){
        Map conditionMap = new HashMap<>();
        if (condition != null && !"".equals(condition)) {
            conditionMap.putAll(JSONObject.parseObject(condition, Map.class));
        }
        return ResultUtil.ok(this.orderService.findByPage(pageNo - 1, pageSize, conditionMap));
    }

    @RequestMapping(value = "order-manage", method = RequestMethod.PATCH)
    public Object update(Order order){
        this.orderService.update(order);
        return ResultUtil.ok("修改成功");
    }

    @RequestMapping(value = "order-manage", method = RequestMethod.DELETE)
    public Object delete(Long id){
        this.orderService.deleteById(id);
        return ResultUtil.ok("删除成功");
    }

    @RequestMapping(value = "order-manage", method = RequestMethod.POST)
    public Object save(Order order, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            FieldError fieldError = bindingResult.getFieldError();
            return ResultUtil.error(fieldError.getDefaultMessage());
        }
        try {
            this.orderService.save(order);
        } catch (Exception e) {
            return ResultUtil.error(e.getMessage());
        }
        return ResultUtil.ok("添加成功");
    }
}