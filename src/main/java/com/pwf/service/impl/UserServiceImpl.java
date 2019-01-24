package com.pwf.service.impl;

import com.pwf.dao.UserRepository;
import com.pwf.domain.PageBean;
import com.pwf.domain.User;
import com.pwf.service.UserService;
import com.pwf.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Created by PWF on 2018/11/6.
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository repository;

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public User save(User user) {
        return repository.save(user);
    }

    @Override
    public void removeUsersInBatch(List<User> users) {
        repository.deleteInBatch(users);
    }

    @Override
    public User update(User user) {
        User updateUser = repository.getOne(user.getUserId());
        //把需要修改的数据拷贝复制到更新数据上
        if (user.getUserName() == null) {//进入找回密码更新用户的业务
            updateUser.setPassWord(user.getPassWord());
            BeanUtils.copyProperties(updateUser, user);
            return repository.saveAndFlush(user);
        }
        //普通更新用户的业务,把user的值拷贝到updateUser上
        BeanUtils.copyProperties(user, updateUser);
        return repository.saveAndFlush(updateUser);
    }

    @Override
    public User getUserById(Integer id) {
        return repository.getOne(id);
    }

    @Override
    public List<User> listUsers() {
        return repository.findAll();
    }

    @Override
    public Page<User> listUsersByNameLike(String searchText, Pageable pageable) {
        // 模糊查询
        searchText = "%" + searchText + "%";
//        Page<User> users = repository.findByNameLike(name, pageable);
        Page<User> users = repository.findByUserNameLike(searchText, pageable);
        return users;
    }

    @Override
    public Page<User> listAllOrderByCreateDate(PageBean pageBean) {
        return repository.findAll(PageRequest.of(pageBean.getPage(), pageBean.getSize(), Sort.by(Sort.Direction.DESC, "createDate")));
    }

    @Override
    public Page<User> listUsersPage(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public void deleteIn(Integer... ids) {
        for (Integer id : ids) {
            try {
                delete(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public boolean isExist(String username, Integer id) {
        return id == null || id == 0
                ? repository.findByUserName(username).isPresent()
                : repository.findByUserNameAndUserId(username, id).isPresent();
    }

    @Override
    public ResultVO existByUsernameAndEmail(String userName, String email) {
        Integer userId = 0;
        boolean isExist = repository.findByUserNameAndEmail(userName, email).isPresent();
        if (isExist) {
            Optional<User> byUserNameAndEmail = repository.findByUserNameAndEmail(userName, email);
            userId = byUserNameAndEmail.get().getUserId();
            return new ResultVO(isExist, userId + "");
        } else {
            log.info("查询的账号或邮箱不存在");
            return new ResultVO(isExist, "查询的账号或邮箱不存在");
        }

    }

    @Override
    public Optional<User> login(String username, String pwd) {
        Optional<User> uesrOptional = repository.findByUserNameAndPassWord(username, pwd);
        uesrOptional.ifPresent(user -> {
//            user.setLastLoginDate(new Date());
            repository.save(user);
        });
        return uesrOptional;
    }

//    @Override
//    public Page<User> findByLastLoginDate() {
//        return null;
//    }
}
