package com.pwf.dao;

import com.pwf.domain.Banner;
import com.pwf.domain.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by PWF on 2019/1/17.
 */
public interface MessageRepository extends JpaRepository<Message,Integer> {
}
