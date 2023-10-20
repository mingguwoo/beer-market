package com.nio.ngfs.plm.bom.configuration.infrastructure.common.generator;

import com.google.common.collect.Lists;
import com.nio.bom.share.exception.BusinessException;
import com.nio.ngfs.plm.bom.configuration.common.constants.RedisKeyConstant;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsConfigurationRuleDao;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author xiaozhou.tu
 * @date 2023/10/20
 */
@Component
@RequiredArgsConstructor
public class RuleNumberGenerator {

    private static final String RULE_NUMBER_PREFIX = "RZ";

    private final BomsConfigurationRuleDao bomsConfigurationRuleDao;
    private final StringRedisTemplate redisTemplate;

    /**
     * 批量申请Rule Number
     */
    public List<String> applyRuleNumber(int size) {
        String redisKey = RedisKeyConstant.CONFIGURATION_RULE_RULE_NUMBER_INCR_LOCK;
        String sequence = redisTemplate.opsForValue().get(redisKey);
        if (StringUtils.isBlank(sequence)) {
            int sequenceNumber = getSequenceFromDB();
            redisTemplate.opsForValue().setIfAbsent(redisKey, String.valueOf(sequenceNumber), 10, TimeUnit.SECONDS);
        } else {
            redisTemplate.expire(redisKey, 10, TimeUnit.SECONDS);
        }
        Long incrResult = redisTemplate.opsForValue().increment(redisKey, size);
        if (incrResult == null) {
            throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_RULE_NUMBER_GENERATE_ERROR);
        }
        List<String> ruleNumberList = Lists.newArrayList();
        for (int i = 0; i < size; i++) {
            ruleNumberList.add(String.format("%s%08d", RULE_NUMBER_PREFIX, incrResult - i));
        }
        Collections.reverse(ruleNumberList);
        return ruleNumberList;
    }

    private int getSequenceFromDB() {
        String maxRuleNumber = bomsConfigurationRuleDao.getMaxRuleNumber();
        if (StringUtils.isBlank(maxRuleNumber)) {
            return 0;
        }
        for (int i = 0; i < maxRuleNumber.length(); i++) {
            if (maxRuleNumber.charAt(i) != '0') {
                return Integer.parseInt(maxRuleNumber.substring(i));
            }
        }
        return 0;
    }

}
