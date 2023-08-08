CREATE TABLE `boms_product_config`
(
    `id`                       bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `pc_id`                    varchar(64)     NOT NULL DEFAULT '' COMMENT '单车PC ID',
    `model`                    varchar(32)     NOT NULL DEFAULT '' COMMENT 'Model',
    `model_year`               varchar(32)     NOT NULL DEFAULT '' COMMENT 'Model Year',
    `name`                     varchar(128)    NOT NULL DEFAULT '' COMMENT '单车PC Name',
    `marketing_name`           varchar(128)    NOT NULL DEFAULT '' COMMENT '单车PC Marketing Name',
    `description`              varchar(128)    NOT NULL DEFAULT '' COMMENT '单车PC描述',
    `based_on_base_vehicle_id` varchar(32)     NOT NULL DEFAULT '' COMMENT 'Based On Base Vehicle ID',
    `based_on_pc_id`           varchar(64)     NOT NULL DEFAULT '' COMMENT 'Based On PC ID',
    `brand`                    varchar(8)      NOT NULL DEFAULT '' COMMENT 'Model的品牌',
    `skip_check`               varchar(8)      NOT NULL DEFAULT '' COMMENT 'Skip Check开关，取值Open、Close',
    `create_user`              varchar(32)     NOT NULL DEFAULT '' COMMENT '创建人',
    `update_user`              varchar(32)     NOT NULL DEFAULT '' COMMENT '更新人',
    `create_time`              timestamp       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`              timestamp       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `del_flag`                 tinyint(4)      NOT NULL DEFAULT 0 COMMENT '是否逻辑删除，0：否，1：是',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uniq_pc_id` (`pc_id`) USING BTREE,
    INDEX `idx_model_model_year` (`model`, `model_year`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC COMMENT ='单车PC表';
