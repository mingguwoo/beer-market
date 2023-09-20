CREATE TABLE `boms_v36_code_library_change_log`
(
    `id`               bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `code_id`       bigint unsigned NOT NULL DEFAULT 0 COMMENT 'Code Id',
    `change_attribute` varchar(32)     NOT NULL DEFAULT '' COMMENT '变更属性',
    `old_value`        varchar(128)    NOT NULL DEFAULT '' COMMENT '变更属性的旧值',
    `new_value`        varchar(128)    NOT NULL DEFAULT '' COMMENT '变更属性的新值',
    `type`             varchar(32)     NOT NULL DEFAULT '' COMMENT '变更类型，Auto/Hand',
    `create_user`      varchar(32)     NOT NULL DEFAULT '' COMMENT '创建人',
    `update_user`      varchar(32)     NOT NULL DEFAULT '' COMMENT '更新人',
    `create_time`      timestamp       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      timestamp       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `del_flag`         tinyint(4)      NOT NULL DEFAULT 0 COMMENT '是否逻辑删除，0：否，1：是',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_code_id` (`code_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC COMMENT ='BOM中台V36 Code Library属性变更记录表';