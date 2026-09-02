package com.rookies6.myspringbootlab.runner;

import com.rookies6.myspringbootlab.property.MyPropProperties;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class MyPropRunner implements ApplicationRunner {

    @Autowired
    private MyPropProperties myPropProperties;

    private final Logger logger = LoggerFactory.getLogger(MyPropRunner.class);

    @Override
    public void run(ApplicationArguments args) throws Exception {

        // 1-4)
        logger.debug("1-4)[DEBUG] myprop.username = {}", myPropProperties.getUserName());
        logger.debug("1-4)[DEBUG] myprop.port = {}", myPropProperties.getPort());

        logger.info("1-4)[INFO] myprop.username = {}", myPropProperties.getUserName());
        logger.info("1-4)[INFO] myprop.port = {}", myPropProperties.getPort());

    }

}
