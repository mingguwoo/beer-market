CREATE TABLE `boms_feature_library`
(
    `id`                  bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `feature_code`        varchar(128)    NOT NULL DEFAULT '' COMMENT 'Feature Code',
    `parent_feature_code` varchar(128)    NOT NULL DEFAULT '' COMMENT 'Parent Feature Code',
    `type`                varchar(32)     NOT NULL DEFAULT '' COMMENT '类型，Group/Feature/Option',
    `display_name`        varchar(128)    NOT NULL DEFAULT '' COMMENT '英文描述',
    `chinese_name`        varchar(128)    NOT NULL DEFAULT '' COMMENT '中文描述',
    `description`         varchar(128)    NOT NULL DEFAULT '' COMMENT '补充描述',
    `selection_type`      varchar(32)     NOT NULL DEFAULT '' COMMENT 'Feature的可选性',
    `may_must`            varchar(32)     NOT NULL DEFAULT '' COMMENT 'Feature的必须性',
    `catalog`             varchar(32)     NOT NULL DEFAULT '' COMMENT 'Feature的分类',
    `maturity`            varchar(32)     NOT NULL DEFAULT '' COMMENT 'Feature的成熟度',
    `version`             varchar(32)     NOT NULL DEFAULT '' COMMENT '版本，颜色件相关',
    `requestor`           varchar(32)     NOT NULL DEFAULT '' COMMENT '创建方',
    `status`              varchar(16)     NOT NULL DEFAULT '' COMMENT '状态，Active/Inactive',
    `create_user`         varchar(32)     NOT NULL DEFAULT '' COMMENT '创建人',
    `update_user`         varchar(32)     NOT NULL DEFAULT '' COMMENT '更新人',
    `create_time`         timestamp       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`         timestamp       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `del_flag`            tinyint(4)      NOT NULL DEFAULT 0 COMMENT '是否逻辑删除，0：否，1：是',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uniq_feature_code_type` (`feature_code`, `type`) USING BTREE,
    INDEX `idx_parent_feature_code` (`parent_feature_code`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC COMMENT ='BOM中台Feature Library配置库';
