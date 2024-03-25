package com.sh.beer.market.infrastructure.common.generator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author
 * @date 2023/10/20
 */
@Component
@RequiredArgsConstructor
public class RuleNumberGenerator {

    /*private static final String RULE_NUMBER_PREFIX = "RZ";

    private final BomsConfigurationRuleDao bomsConfigurationRuleDao;
    private final StringRedisTemplate redisTemplate;

    *//**
     * 批量申请Rule Number
     *//*
    public List<String> applyRuleNumber(int size) {
        if (size <= 0) {
            return Lists.newArrayList();
        }
        String redisKey = RedisKeyConstant.CONFIGURATION_RULE_RULE_NUMBER_INCR_LOCK;
        String sequence = redisTemplate.opsForValue().get(redisKey);
        if (StringUtils.isBlank(sequence)) {
            int sequenceNumber = getSequenceFromDB();
            redisTemplate.opsForValue().setIfAbsent(redisKey, String.valueOf(sequenceNumber), 10, TimeUnit.MINUTES);
        } else {
            redisTemplate.expire(redisKey, 10, TimeUnit.MINUTES);
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
        if (!maxRuleNumber.startsWith(RULE_NUMBER_PREFIX)) {
            throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_MAX_RULE_NUMBER_FORMAT_ERROR);
        }
        String number = maxRuleNumber.substring(2);
        for (int i = 0; i < number.length(); i++) {
            if (number.charAt(i) != '0') {
                try {
                    return Integer.parseInt(number.substring(i));
                } catch (Exception e) {
                    throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_MAX_RULE_NUMBER_FORMAT_ERROR);
                }
            }
        }
        return 0;
    }*/

}
