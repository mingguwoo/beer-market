package com.nio.ngfs.plm.bom.configuration.api.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.nio.ngfs.web.jackson.CommonDateDeserializer;
import com.nio.ngfs.web.jackson.UnixTimestampDateSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigInteger;
import java.util.Date;

/**
 * Jackson配置
 *
 * @author ngfs generated
 */
@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        var mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        var dateModule = buildDateModule();
        mapper.registerModule(dateModule);

        var longModule = buildLongModule();
        mapper.registerModule(longModule);

        return mapper;
    }

    /**
     * 解决js处理Long类型丢失精度的问题
     *
     * @see <a href="http://blog.csdn.net/xufei_0320/article/details/78243527">解决js处理Long类型丢失精度的问题</a>
     */
    private SimpleModule buildLongModule() {
        var longModule = new SimpleModule();
        longModule.addSerializer(BigInteger.class, ToStringSerializer.instance);
        longModule.addSerializer(Long.class, ToStringSerializer.instance);
        longModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        return longModule;
    }

    /**
     * 支持ISO8601格式
     */
    private SimpleModule buildDateModule() {
        var dateModule = new SimpleModule();
        dateModule.addSerializer(Date.class, new UnixTimestampDateSerializer());
        dateModule.addDeserializer(Date.class, new CommonDateDeserializer());
        return dateModule;
    }

}
