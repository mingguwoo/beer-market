CREATE TABLE `boms_model_year_config`
(
    `id`          bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `model`       varchar(32)     NOT NULL DEFAULT '' COMMENT 'Model',
    `model_year`  varchar(32)     NOT NULL DEFAULT '' COMMENT 'Model Year',
    `oxo_release` varchar(8)      NOT NULL DEFAULT '' COMMENT 'OXO是否Release，取值Yes、No',
    `create_user` varchar(32)     NOT NULL DEFAULT '' COMMENT '创建人',
    `update_user` varchar(32)     NOT NULL DEFAULT '' COMMENT '更新人',
    `create_time` timestamp       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `del_flag`    tinyint(4)      NOT NULL DEFAULT 0 COMMENT '是否逻辑删除，0：否，1：是',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uniq_model_model_year` (`model`, `model_year`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC COMMENT ='配置管理Model Year配置表';
