CREATE TABLE `boms_product_config_model_option`
(
    `id`           bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `model`        varchar(64)     NOT NULL DEFAULT '' COMMENT '车型',
    `option_code`  varchar(64)     NOT NULL DEFAULT '' COMMENT 'Option Code',
    `feature_code` varchar(64)     NOT NULL DEFAULT '' COMMENT 'Feature Code',
    `create_user`  varchar(32)     NOT NULL DEFAULT '' COMMENT '创建人',
    `update_user`  varchar(32)     NOT NULL DEFAULT '' COMMENT '更新人',
    `create_time`  timestamp       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  timestamp       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `del_flag`     tinyint(4)      NOT NULL DEFAULT 0 COMMENT '是否逻辑删除，0：否，1：是',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uniq_model_option_code` (`model`, `option_code`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC COMMENT ='单车PC车型的Option表';
