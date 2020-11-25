/*
 * MvcConfig
 * ------------------------------------------------------------------
 * This file is used in place of dispatcher servlet file.
 * ------------------------------------------------------------------
 */
package com.example.demo;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class MvcConfig implements WebMvcConfigurer{

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		Path userUploadDir = Paths.get("./user-logos");
		
		String userUploadPath = userUploadDir.toFile().getAbsolutePath();
		
		registry.addResourceHandler("/user-logos/**").addResourceLocations("file:/"+ userUploadPath + "/");
	}
	
}
