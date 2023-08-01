package com.nio.ngfs.plm.bom.configuration.infrastructure.generator;

import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.infrastructure.config.RedisClient;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsBasicVehicleDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsBasicVehicleEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.text.Format;

/**
 * @author bill.wang
 * @date 2023/7/20
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BaseVehicleIdGenerator {

    private final RedisClient redisClient;
    private final BomsBasicVehicleDao bomsBasicVehicleDao;

    public String createBaseVehicleId(String redisKey) {
        Long sequence = null;
        Format format = new DecimalFormat("000000");
        try {
            Object sequenceObject = redisClient.get(redisKey);
            if (ObjectUtils.isNotEmpty(sequenceObject)) {
                sequence = redisClient.incrBy(redisKey);
            }
        } catch (Exception e) {
            //redis查不到，去查db后再存入redis
            log.error("Redis读取key: {} sequence失败！", redisKey);
        }
        if (ObjectUtils.isNotEmpty(sequence)) {
            //生成流水号
            String number = format.format(sequence);
            return ConfigConstants.BASE_VEHICLE_ID_PREFIX + number;
        }
        sequence = getSequenceFromDb();
        try {
            redisClient.set(redisKey, String.valueOf(sequence));
        } catch (Exception error) {
            log.error( "Redis写入key: {} sequence失败！sequence: {} ", redisKey, sequence);
        }
        //生成流水号
        String number = format.format(sequence);
        return ConfigConstants.BASE_VEHICLE_ID_PREFIX + number;
    }

    private long getSequenceFromDb(){
        Long sequence = 1L;
        BomsBasicVehicleEntity bomsBasicVehicleEntity = bomsBasicVehicleDao.getLastestBaseVehicle();
        if (ObjectUtils.isEmpty(bomsBasicVehicleEntity)) {
            log.info( "base vehicle表为空, 设置base vehicle id 为1");
            return sequence;
        }
        return bomsBasicVehicleEntity.getId() + 1;
    }
}
