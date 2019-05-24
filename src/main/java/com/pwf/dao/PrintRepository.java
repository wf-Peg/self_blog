package com.pwf.dao;

import com.pwf.domain.Banner;
import com.pwf.domain.Print;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by PWF on 2019/1/17.
 */
public interface PrintRepository extends JpaRepository<Print,Integer>,JpaSpecificationExecutor<Print> {
    Page<Print> findByFilenameLike(String filename, Pageable pageable);
}
