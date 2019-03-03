//package com.pwf.service.impl;
//
//import com.pwf.domain.Banner;
//import com.pwf.service.BannerService;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.List;
//
//import static org.junit.Assert.*;
//
///**
// * Created by PWF on 2019/1/17.
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class BannerServiceImplTest {
//    @Autowired
//    BannerService service;
//    @Test
//    public void findAll() throws Exception {
//        Page<Banner> all = service.findAll(PageRequest.of(0,10));
//        for (Banner banner :
//                all) {
//            System.out.println(banner.toString());
//        }
//    }
//
//    @Test
//    public void findOne() throws Exception {
//        Banner one = service.findOne(1);
//        System.out.println(one);
//    }
//
//    @Test
//    public void save() throws Exception {
//        Banner banner=new Banner("2","2","2");
//        service.save(banner);
//    }
//
//    @Test
//    public void update() throws Exception {
////        Banner updateBanner = service.findOne(2);
//        //后赋前
//        Banner newBanner=new Banner("3","3","3");
//        newBanner.setId(2);
//        service.update(newBanner);
//    }
//
//    @Test
//    public void delete() throws Exception {
//        service.delete(2);
//    }
//
//}