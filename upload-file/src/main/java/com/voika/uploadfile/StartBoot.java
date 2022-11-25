package com.voika.uploadfile;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class StartBoot {

    public static void main(String[] args) {
        log.info("==========正在启动项目...==========");
        SpringApplication.run(StartBoot.class, args);
        log.info("==========项目启动成功！==========");
    }

}
