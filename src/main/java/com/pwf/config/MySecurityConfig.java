package com.pwf.config;

import com.pwf.authentication.MyAuthenticationFailureHandler;
import com.pwf.authentication.MyAuthenticationSuccessHandler;
import com.pwf.service.MyUserDetailService;
import com.pwf.validate.code.ValidateCodeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;
import java.io.PrintWriter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //方法安全设置
public class MySecurityConfig extends WebSecurityConfigurerAdapter{
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        //remember-me token
        JdbcTokenRepositoryImpl tokenRepository=new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(myDataSource);
        //启动时自动建token-登陆表
//        tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
    }


    @Autowired
    private DataSource myDataSource;
    @Autowired
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
    @Autowired
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;
    @Autowired
    private MyUserDetailService myUserDetailService;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ValidateCodeFilter codeFilter =new ValidateCodeFilter();

        http.authorizeRequests()
//                .antMatchers("/").permitAll()
//                .antMatchers("/userlogin").permitAll()
//                .antMatchers("/index").permitAll()
//                .antMatchers("/static/**").permitAll()
//                .antMatchers("/code/image").permitAll()
//                .antMatchers("/*").permitAll()
                .antMatchers("/background/**").hasAnyRole("admin","user")
//                任何请求都需要身份认证
//                .anyRequest()
//                .authenticated()
                .and()
                //登出操作
                .logout().logoutUrl("/admin/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    PrintWriter writer = response.getWriter();
                    writer.write("true");
                    writer.close();
                }).permitAll()
        .and()
//        开启配置登陆，如下使用get访问userlogin登录页请求，login页面发送post到login进行表单验证(必须同名才能交由security处理)
//        或者使用loginProcessingUrl处理请求
                .formLogin().loginPage("/userlogin")
                .loginProcessingUrl("/login")
                .successHandler(myAuthenticationSuccessHandler)
                .failureHandler(myAuthenticationFailureHandler)
//                .failureHandler(myAuthenticationFailureHandler).failureUrl("/login?error=true")
        .and()
                .rememberMe().tokenRepository(persistentTokenRepository())
                         .tokenValiditySeconds(60*60*24)
                         .userDetailsService(myUserDetailService)
        .and()
                .addFilterBefore(codeFilter, UsernamePasswordAuthenticationFilter.class)
        .csrf().disable();
//        设置自定义参数名
//        http.formLogin().usernameParameter("name").passwordParameter("pwd").loginPage("/userlogin");
//        退出访问主页
        http.logout().logoutSuccessUrl("/");
//        记住我cookie保存十四天,默认参数为remember-me
//        http.rememberMe().rememberMeParameter("remember-me");
    }


//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        super.configure(auth);
//        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder()).
//                withUser("user").password(new BCryptPasswordEncoder().encode("user")).roles("VIP1").
//                and().withUser("2").password(new BCryptPasswordEncoder().encode("2")).roles("VIP2","VIP1").
//                and().withUser("3").password(new BCryptPasswordEncoder().encode("3")).roles("VIP2","VIP3","VIP1");
//    }
}
