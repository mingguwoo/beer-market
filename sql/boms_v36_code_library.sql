CREATE TABLE `boms_v36_code_library`
(
    `id`                 bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `code`               varchar(32)     NOT NULL COMMENT 'Code',
    `parent_id`          bigint          NOT NULL COMMENT 'Parent Code Id',
    `type`               varchar(8)      NOT NULL COMMENT '类型，Digit/Option',
    `display_name`       varchar(128)    NOT NULL DEFAULT '' COMMENT '英文描述',
    `chinese_name`       varchar(128)    NOT NULL DEFAULT '' COMMENT '中文描述',
    `sales_feature_list` varchar(256)    NOT NULL DEFAULT '' COMMENT 'Sales Feature列表，逗号分隔',
    `remark`             varchar(1024)   NOT NULL DEFAULT '' COMMENT '备注说明',
    `create_user`        varchar(32)     NOT NULL DEFAULT '' COMMENT '创建人',
    `update_user`        varchar(32)     NOT NULL DEFAULT '' COMMENT '更新人',
    `create_time`        timestamp       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`        timestamp       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `del_flag`           tinyint(4)      NOT NULL DEFAULT 0 COMMENT '是否逻辑删除，0：否，1：是',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uniq_code_parent_id_chinese_name` (`code`, `parent_id`, `chinese_name`) USING BTREE,
    INDEX `idx_parent_id` (`parent_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC COMMENT ='BOM中台V36 Code Library配置库';
