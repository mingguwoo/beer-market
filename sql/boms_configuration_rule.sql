CREATE TABLE `boms_configuration_rule`
(
    `id`           bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `rule_number`  varchar(16)     NOT NULL DEFAULT '' COMMENT 'Rule Number',
    `rule_version` varchar(8)      NOT NULL DEFAULT '' COMMENT 'Rule的版本',
    `group_id`     bigint          NOT NULL COMMENT 'Group Id',
    `purpose`      tinyint(4)      NOT NULL DEFAULT 0 COMMENT '创建Rule的目的，1-（Sales —> Eng），2-（Sales —> Sales），3-（Sales <—> Sales），4-（Sales X Sales），5-（Base Vehicle —> Sales）',
    `rule_type`    varchar(16)     NOT NULL DEFAULT '' COMMENT 'Rule的类型，Inclusive、Exclusive、Default、Available',
    `change_type`  varchar(8)      NOT NULL DEFAULT '' COMMENT 'Rule的变更类型，Add、Modify、Remove',
    `status`       varchar(8)      NOT NULL DEFAULT '' COMMENT 'Rule的状态，In Work、Released',
    `eff_in`       datetime        NOT NULL DEFAULT '9999-12-30 00:00:00' COMMENT 'Rule的制造生效时间',
    `eff_out`      datetime        NOT NULL DEFAULT '9999-12-31 00:00:00' COMMENT 'Rule的制造失效时间',
    `release_date` datetime        NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT 'Rule的发布时间',
    `create_user`  varchar(32)     NOT NULL DEFAULT '' COMMENT '创建人',
    `update_user`  varchar(32)     NOT NULL DEFAULT '' COMMENT '更新人',
    `create_time`  timestamp       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  timestamp       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `del_flag`     tinyint(4)      NOT NULL DEFAULT 0 COMMENT '是否逻辑删除，0：否，1：是',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uniq_rule_number_rule_version` (`rule_number`, `rule_version`) USING BTREE,
    INDEX `idx_group_id` (`group_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC COMMENT ='配置管理-Rule表';
