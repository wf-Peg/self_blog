package com.pwf.authentication;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pwf.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

//登陆验证失败后的处理
@Slf4j
@Component
public class MyAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Autowired
    ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e
    ) throws IOException, ServletException {
//        response.getWriter().write(2);
        log.info("登陆失败");
        Optional<PrintWriter> writer = Optional.empty();
        response.setContentType("application/json;charset=utf-8");
        try {
            writer = Optional.of(response.getWriter());
            String loginResult = JSON.toJSONString(new ResultVO(false, "用户名或密码错误，请重新输入"));
            writer.ifPresent(printWriter -> printWriter.write(loginResult));
        } finally {
            writer.ifPresent(PrintWriter::close);
        }
//        response.sendRedirect("/errologin");
//        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//        response.setContentType("application/json;charset=utf-8");
//        response.getWriter().write(objectMapper.writeValueAsString(e));
    }
}
