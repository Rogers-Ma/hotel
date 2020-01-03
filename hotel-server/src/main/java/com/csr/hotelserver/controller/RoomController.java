package com.csr.hotelserver.controller;

import com.alibaba.fastjson.JSONObject;
import com.csr.hotelserver.entity.Room;
import com.csr.hotelserver.service.RoomService;
import com.csr.hotelserver.util.result.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
public class RoomController {
    @Autowired
    private RoomService roomService;

    @RequestMapping(value = "room-manage", method = RequestMethod.GET, params = "!pageNo")
    public Object findAll(@RequestParam(required = false, name = "condition") String condition){
        return ResultUtil.ok();
    }

    @RequestMapping(value = "room-manage", method = RequestMethod.GET)
    public Object findByPage(@RequestParam Integer pageNo, @RequestParam Integer pageSize, @RequestParam(required = false, name = "condition") String condition){
        Map<String, Object> conditionMap = new HashMap<>();
        if (condition != null && !"".equals(condition)) {
            conditionMap.putAll(JSONObject.parseObject(condition, Map.class));
        }
        return ResultUtil.ok(this.roomService.findByPage(pageNo-1, pageSize, conditionMap));
    }

    @RequestMapping(value = "room-manage", method = RequestMethod.PATCH)
    public Object updateRoom(@RequestBody Room room){
        this.roomService.update(room);
        return ResultUtil.ok("修改成功");
    }

    @RequestMapping(value = "room-manage", method = RequestMethod.DELETE)
    public Object delete(Long id){
        this.roomService.deleteById(id);
        return ResultUtil.ok("删除成功");
    }

    @RequestMapping(value = "room-manage", method = RequestMethod.POST)
    public Object save(@RequestBody @Valid Room room, BindingResult bindingResult){
        room.setDeleted(0);
        if(bindingResult.hasErrors()){
            FieldError fieldError = bindingResult.getFieldError();
            return ResultUtil.error(fieldError.getDefaultMessage());
        }
        try {
            this.roomService.save(room);
        } catch (Exception e) {
            return ResultUtil.error(e.getMessage());
        }
        return ResultUtil.ok("添加成功");
    }
}
