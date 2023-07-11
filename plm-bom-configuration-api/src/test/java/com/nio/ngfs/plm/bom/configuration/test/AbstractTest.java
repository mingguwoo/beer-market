package com.nio.ngfs.plm.bom.configuration.test;

import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author xiaozhou.tu
 * @date 2023/7/11
 */
@SpringJUnitConfig
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BomConfigurationTestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public abstract class AbstractTest {
}
