package com.pwf.dao;

import com.pwf.domain.Banner;
import com.pwf.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer>,JpaSpecificationExecutor<User> {
    /**
     * 根据帐号查询用户
     *
     * @param username
     * @return
     */
    Optional<User> findByUserName(String username);

    /**
     * 根据用户名分页查询用户列表
     * @param name
     * @param pageable
     * @return
     */
//    Page<User> findByNameLike(String name, Pageable pageable);
    Page<User> findByUserNameLike(String name, Pageable pageable);

//    Page<User> findUsersByEmailAndUserNameLike(String searchText,Pageable pageable);


    /**
     * 根据帐号和id查询
     *
     * @param username
     * @param id
     * @return
     */
    Optional<User> findByUserNameAndUserId(String username, Integer id);

    /**
     * 根据帐号和密码查询
     *
     * @param username
     * @param password
     * @return
     */
    Optional<User> findByUserNameAndPassWord(String username, String password);

    Optional<User> findByUserNameAndEmail(String username, String email);

    /**
     * 多id批量查询
     *
     * @param ids
     * @return
     */
    List<User> findByUserIdIsIn(Integer... ids);


}
