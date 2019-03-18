package com.pwf.service;

import com.aliyun.oss.OSSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Component
public class FileUploadService {

    @Value("${aliyun.endpoint}")
    private String endpoint;

    @Value("${aliyun.ak}")
    private String accessKeyId;

    @Value("${aliyun.sk}")
    private String accessKeySecret;

    @Value("${aliyun.url}")
    private String url;


    public String upload(MultipartFile uploadFile) throws IOException {
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        // 上传文件流。
        InputStream inputStream = uploadFile.getInputStream();
        String fileName = uploadFile.getOriginalFilename();
        String hz = fileName.substring(fileName.lastIndexOf("."));
        String uuid = UUID.randomUUID().toString();
        String newFileName = uuid + hz;
        // 上传文件到云服务器
        ossClient.putObject("blog-banner", newFileName, inputStream);
        // 关闭OSSClient。
        ossClient.shutdown();
        return url + newFileName;
    }
}
