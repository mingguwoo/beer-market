package com.sh.beer.market.test;


import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author
 * @date 2023/7/11
 */
//@SpringJUnitConfig
@AutoConfigureMockMvc
//@RunWith(SpringRunner.class)
@SpringBootTest(classes = BeerMarketTestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public abstract class AbstractTest {
}
