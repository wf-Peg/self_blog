package com.pwf.dao;

import com.pwf.domain.Banner;
import com.pwf.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by PWF on 2019/1/17.
 */
public interface BannerRepository extends JpaRepository<Banner,Integer> {
    Page<Banner> findByNameLike(String name, Pageable pageable);
}
