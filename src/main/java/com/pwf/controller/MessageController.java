package com.pwf.controller;

import com.pwf.domain.Message;
import com.pwf.domain.PageBean;
import com.pwf.service.MessageService;
import com.pwf.util.ConstraintViolationExceptionHandler;
import com.pwf.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PWF on 2019/2/21.
 */
@CrossOrigin
@Controller
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @PostMapping("/sendMessage")
    @ResponseBody
    public ResultVO save(Message message){
        try {
            messageService.save(message);
            return new ResultVO(true,"发送邮件成功,如需回复请耐心等待");
        }  catch (ConstraintViolationException e) {
            return new ResultVO(false, ConstraintViolationExceptionHandler.getMessage(e));
        }
    }

    @GetMapping("/list")
    public String messageList(Model model, PageBean pageBean){
        Page<Message> list = messageService.findAll(pageBean);
        model.addAttribute("messageList",list.getContent());
        model.addAttribute("totalPage", list.getTotalPages());
        model.addAttribute("currentPage", pageBean.getPage());
        return "background/message-tables";
    }

    @DeleteMapping("/delete")
    @ResponseBody // 响应给客户端的是数据
    public ResultVO delete(Integer id) {
        messageService.deleteById(id);
        return new ResultVO(true,"删除message信息成功");
    }

}
