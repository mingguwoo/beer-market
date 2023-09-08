CREATE TABLE `boms_product_config_option`
(
    `id`              bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `pc_id`           bigint          NOT NULL COMMENT 'PC主键id',
    `option_code`     varchar(128)    NOT NULL COMMENT 'Option Code',
    `feature_code`    varchar(128)    NOT NULL COMMENT 'Feature Code',
    `select_status`   tinyint(4)      NOT NULL DEFAULT 0 COMMENT '勾选状态，0-Unselect，1-Select',
    `select_can_edit` tinyint(4)      NOT NULL DEFAULT 0 COMMENT 'From Base Vehicle下是否可人工编辑，0-No，1-Yes',
    `type`            tinyint(4)      NOT NULL DEFAULT 0 COMMENT '类型，0-正常，1-From Base Vehicle，2-From PC',
    `create_user`     varchar(32)     NOT NULL DEFAULT '' COMMENT '创建人',
    `update_user`     varchar(32)     NOT NULL DEFAULT '' COMMENT '更新人',
    `create_time`     timestamp       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     timestamp       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `del_flag`        tinyint(4)      NOT NULL DEFAULT 0 COMMENT '是否逻辑删除，0：否，1：是',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uniq_pc_id_option_code` (`pc_id`, `option_code`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC COMMENT ='单车PC的Option配置表';
