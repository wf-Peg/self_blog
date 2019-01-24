package com.pwf.validate.code.image;

import lombok.Data;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;


@Data
public class ImageCode {

	private BufferedImage image;
	private String code;
	private LocalDateTime expireTime;
	
	public ImageCode(BufferedImage image, String code, int expireIn){
//		super(code, expireIn);
		this.image = image;
		this.code=code;
		this.expireTime=LocalDateTime.now().plusSeconds(expireIn);
	}

	public ImageCode(BufferedImage image, String code, LocalDateTime expireTime){
//		super(code, expireTime);
		this.image = image;
		this.code=code;
		this.expireTime=expireTime;
	}
	public boolean isExpried(){
		return LocalDateTime.now().isAfter(expireTime);
	}
}
