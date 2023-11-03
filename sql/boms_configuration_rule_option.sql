CREATE TABLE `boms_configuration_rule_option`
(
    `id`                       bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `rule_id`                  bigint          NOT NULL COMMENT 'Rule Id',
    `driving_option_code`      varchar(128)    NOT NULL DEFAULT '' COMMENT 'Rule条件的Option Code',
    `driving_feature_code`     varchar(128)    NOT NULL DEFAULT '' COMMENT 'Rule条件的Feature Code',
    `constrained_option_code`  varchar(128)    NOT NULL DEFAULT '' COMMENT 'Rule结果的Option Code',
    `constrained_feature_code` varchar(128)    NOT NULL DEFAULT '' COMMENT 'Rule结果的Feature Code',
    `matrix_value`             tinyint(4)      NOT NULL DEFAULT 0 COMMENT '矩阵打点，1-Inclusive，2-Exclusive，3-Unavailable',
    `create_user`              varchar(32)     NOT NULL DEFAULT '' COMMENT '创建人',
    `update_user`              varchar(32)     NOT NULL DEFAULT '' COMMENT '更新人',
    `create_time`              timestamp       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`              timestamp       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `del_flag`                 tinyint(4)      NOT NULL DEFAULT 0 COMMENT '是否逻辑删除，0：否，1：是',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_rule_id` (`rule_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC COMMENT ='配置管理-Rule Option表';
