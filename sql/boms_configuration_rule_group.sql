CREATE TABLE `boms_configuration_rule_group`
(
    `id`                       bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `chinese_name`             varchar(128)    NOT NULL DEFAULT '' COMMENT 'Rule的中文描述',
    `display_name`             varchar(128)    NOT NULL DEFAULT '' COMMENT 'Rule的英文描述',
    `purpose`                  tinyint(4)      NOT NULL DEFAULT 0 COMMENT '创建Rule的目的，1-（Sales —> Eng），2-（Sales —> Sales），3-（Sales <—> Sales），4-（Sales X Sales），5-（Base Vehicle —> Sales）',
    `defined_by`               varchar(64)     NOT NULL DEFAULT '' COMMENT 'Rule适用范围',
    `description`              varchar(1024)   NOT NULL DEFAULT '' COMMENT 'Rule的补充描述',
    `driving_feature`          varchar(128)    NOT NULL DEFAULT '' COMMENT 'Group勾选的Driving Feature Code',
    `constrained_feature_list` varchar(1024)   NOT NULL DEFAULT '' COMMENT 'Group勾选的Constrained Feature Code列表',
    `create_user`              varchar(32)     NOT NULL DEFAULT '' COMMENT '创建人',
    `update_user`              varchar(32)     NOT NULL DEFAULT '' COMMENT '更新人',
    `create_time`              timestamp       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`              timestamp       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `del_flag`                 tinyint(4)      NOT NULL DEFAULT 0 COMMENT '是否逻辑删除，0：否，1：是',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC COMMENT ='配置管理-Rule Group表';
