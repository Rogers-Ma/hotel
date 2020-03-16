package com.csr.hotelserver.controller;

import com.alibaba.fastjson.JSONObject;
import com.csr.hotelserver.entity.Type;
import com.csr.hotelserver.service.TypeService;
import com.csr.hotelserver.util.result.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TypeController {

    @Autowired
    private TypeService typeService;

    @RequestMapping(value = "type-manage", method = RequestMethod.GET, params = "!pageNo")
    public Object findAll(){
        Map<String, Object> map = new HashMap<>();
        return ResultUtil.ok(this.typeService.findAll(map));
    }

    @RequestMapping(value = "type-manage", method = RequestMethod.GET)
    public Object findAllByPage(@RequestParam Integer pageNo, @RequestParam Integer pageSize, @RequestParam(required = false, name = "condition") String condition) {
        Map<String, Object> conditionMap = new HashMap<>();
        if (condition != null && !"".equals(condition)) {
            conditionMap.putAll(JSONObject.parseObject(condition, Map.class));
        }
        return ResultUtil.ok(this.typeService.findByPage(pageNo - 1, pageSize, conditionMap));
    }

    @RequestMapping(value = "type-manage", method = RequestMethod.PATCH)
    public Object updateCustomer(@RequestBody Type type) throws Exception {
        this.typeService.update(type);
        return ResultUtil.ok("修改成功");
    }

    @RequestMapping(value = "type-manage", method = RequestMethod.POST)
    public Object addCustomer(@Valid @RequestBody Type type, BindingResult bindingResult){
        type.setDeleted(0);
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            return ResultUtil.error(fieldError.getDefaultMessage());
        }
        try {
            this.typeService.save(type);
        } catch (Exception e) {
            return ResultUtil.error(e.getMessage());
        }
        return ResultUtil.ok("添加成功");
    }

    @RequestMapping(value = "type-manage", method = RequestMethod.DELETE)
    public Object deleteCustomer(@RequestParam(name = "id") String id) {
        this.typeService.deleteById(Long.valueOf(id));
        return ResultUtil.ok("删除成功");
    }

    @RequestMapping(value = "type", method = RequestMethod.GET)
    public Object getMessage(){
        return ResultUtil.ok(this.typeService.getReserveMessage());
    }

    @RequestMapping(value = "type", method = RequestMethod.PATCH)
    public Object reserve(){
        return ResultUtil.ok();
    }
}
