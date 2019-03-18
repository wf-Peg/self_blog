package com.pwf.service;

import com.pwf.domain.PageBean;
import com.pwf.domain.User;
import com.pwf.vo.ResultVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Created by PWF on 2018/11/6.
 */
public interface UserService {

    @ApiOperation(value = "判断用户名是否存在")
    boolean isExist(String username, Integer id);

    @ApiOperation("根据用户名和邮箱验证用户")
    ResultVO existByUsernameAndEmail(String userName, String email);

    @ApiOperation(value = "删除用户")
    void delete(Integer id);

    @ApiOperation(value = "保存用户")
    User save(User user) throws Exception;

    @ApiOperation(value = "删除列表里面的用户")
    void removeUsersInBatch(List<User> users);

    @ApiOperation(value = "更新用户")
    User update(User user) throws Exception;

    @ApiOperation(value = "根据id获取用户")
    User getUserById(Integer id);

    @ApiOperation(value = "根据用户名获取用户")
    Optional<User> getUserByUsername(String username);

    @ApiOperation(value = "获取用户列表")
    List<User> listUsers();

    @ApiOperation(value = "根据用户创建时间获取用户列表")
    Page<User> listAllOrderByCreateDate(PageBean pageBean);

    @ApiOperation(value = "根据用户名进行分页模糊查询")
    Page<User> listUsersByNameLike(String searchText, Pageable pageable);

    @ApiOperation(value = "分页获取用户列表")
    Page<User> listUsersPage(PageBean pageBean);

    @ApiOperation(value = "获取用户列表")
    void deleteIn(Integer... ids);

    @ApiOperation(value = "获取用户数量")
    long count();

    @ApiOperation(value = "效验用户名和密码")
    Optional<User> login(String userName, String passWord);

    @ApiOperation(value = "根据用户名查询并添加博客数量")
    void addBlogCount(String username);

    @ApiOperation(value = "查询博客数量最多的用户")
    User findMostBlogsUser();

    void decreaseUserBlogCount(String author);


//    Page<User> findByLastLoginDate();
}
