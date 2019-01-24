package com.pwf.validate.code;

import com.alibaba.fastjson.JSON;
import com.pwf.authentication.MyAuthenticationFailureHandler;
import com.pwf.controller.ValidateCodeController;
import com.pwf.validate.code.image.ImageCode;
import com.pwf.vo.ResultVO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@Slf4j
public class ValidateCodeFilter extends OncePerRequestFilter {

    private SessionStrategy sessionStrategy= new HttpSessionSessionStrategy();
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (StringUtils.equals("/login",request.getRequestURI())&&StringUtils.equalsIgnoreCase(
                request.getMethod(),"post")){
            try {
                validate(new ServletWebRequest(request));
            }catch (ValidateCodeException e){
                Optional<PrintWriter> writer = Optional.empty();
                response.setContentType("application/json;charset=utf-8");
                try {
                    writer = Optional.of(response.getWriter());
                    String loginResult = JSON.toJSONString(new ResultVO(false, e.getMessage()));
                    writer.ifPresent(printWriter -> printWriter.write(loginResult));
                } finally {
                    writer.ifPresent(PrintWriter::close);
                }
//                myAuthenticationFailureHandler.onAuthenticationFailure(request,response,e);
//                request.setAttribute("msg","验证码错误");
//                request.getRequestDispatcher("/login?error=true").forward(request,response);
//                response.sendRedirect("login?error=true");
                return;
            }
        }
            filterChain.doFilter(request,response);

    }

    private void validate(ServletWebRequest request) throws ServletRequestBindingException {
        ImageCode codeInSession= (ImageCode) sessionStrategy.getAttribute(request, ValidateCodeController.SESSION_KEY);
        String codeInRequest= ServletRequestUtils.getRequiredStringParameter(request.getRequest(),"imageCode");


        if (StringUtils.isBlank(codeInRequest)) {
            log.info("验证码的值不能为空");
            throw new ValidateCodeException("验证码的值不能为空");
        }

        if (codeInSession == null) {
            log.info("验证码不存在");
            throw new ValidateCodeException("验证码不存在");
        }

        if (codeInSession.isExpried()) {
            sessionStrategy.removeAttribute(request, ValidateCodeController.SESSION_KEY);
            log.info("验证码已过期");
            throw new ValidateCodeException("验证码已过期");
        }

        if (!StringUtils.equals(codeInSession.getCode(), codeInRequest)) {
            log.info("验证码不匹配");
            throw new ValidateCodeException("验证码不匹配");
        }

        sessionStrategy.removeAttribute(request, ValidateCodeController.SESSION_KEY);
    }
}
