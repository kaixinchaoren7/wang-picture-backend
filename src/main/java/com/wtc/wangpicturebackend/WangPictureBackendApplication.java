package com.wtc.wangpicturebackend;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.wtc.wangpicturebackend.mapper")
public class WangPictureBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(WangPictureBackendApplication.class, args);
    }

}
