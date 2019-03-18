package com.pwf;

import com.pwf.util.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class SelfBlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(SelfBlogApplication.class, args);
	}
	@Bean
	public IdWorker idWorker(){
		return new IdWorker();
	}
}
