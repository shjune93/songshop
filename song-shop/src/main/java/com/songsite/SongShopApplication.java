package com.songsite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.songsite.property.FileUploadProperties;



@EnableConfigurationProperties({
    FileUploadProperties.class
})

@SpringBootApplication
public class SongShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(SongShopApplication.class, args);
	}

}
