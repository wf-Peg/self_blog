package com.pwf.dao;

import com.pwf.domain.User;
import com.pwf.service.impl.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

/**
 * Created by PWF on 2018/11/6.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserRepositoryTest {
    @Autowired
    private UserRepository dao;
    @Autowired
    private UserServiceImpl service;

    @Test
    public void update() throws Exception {
//        Optional<User> user = dao.findByUserName("1");
        User user = service.getUserById(1);
        System.out.println(user);
    }

    @Test
    public void findByUserNameAndUserId() throws Exception {
    }

    @Test
    public void findByUserNameAndPassWord() throws Exception {
    }

    @Test
    public void findByUserIdIsIn() throws Exception {
    }

}