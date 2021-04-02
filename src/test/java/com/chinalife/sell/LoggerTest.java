package com.chinalife.sell;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@Slf4j
public class LoggerTest {

    @Test
    public void test1()
    {
        String name="wjc";
        String password="123";
        log.info("name:{},password:{}",name,password);
    }


}
