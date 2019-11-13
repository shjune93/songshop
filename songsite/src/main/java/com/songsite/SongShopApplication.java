package com.songsite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.songsite.property.FileUploadProperties;



@EnableConfigurationProperties({
    FileUploadProperties.class
})

@SpringBootApplication
@EnableJpaAuditing//도메인 클래스 중복제거에 사용되는 코드
public class SongShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(SongShopApplication.class, args);
	}

}
