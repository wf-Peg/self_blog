package com.pwf.authentication;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pwf.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

//登陆验证成功后的处理
@Slf4j
@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("登陆成功");
//        response.setContentType("application/json;charset=utf-8");
//        response.getWriter().write(objectMapper.writeValueAsString(authentication));
//        response.sendRedirect("/");
        Optional<PrintWriter> writer = Optional.empty();
        response.setContentType("application/json;charset=utf-8");
        try {
            writer = Optional.of(response.getWriter());
            String loginResult = JSON.toJSONString(new ResultVO(true, "登陆成功"));
            writer.ifPresent(printWriter -> printWriter.write(loginResult));
        } finally {
            writer.ifPresent(PrintWriter::close);
        }
    }
}
