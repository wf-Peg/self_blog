package com.pwf.controller;

import com.pwf.vo.ResultVO;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.Valid;

/**
 * 统一异常处理
 *
 * @author PWF
 * @since 2019/2/1
 */
@RestControllerAdvice
public class BaseExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResultVO erro(Exception e) {
        e.printStackTrace();
        return new ResultVO(false, "服务器异常",e.getMessage());
    }
}
