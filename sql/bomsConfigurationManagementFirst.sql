CREATE TABLE `boms_base_vehicle` (
     `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
     `base_vehicle_id` varchar(11) NOT NULL COMMENT '流水号',
     `model_code` varchar(50) NOT NULL DEFAULT '' COMMENT '车型',
     `model_year` varchar(50) NOT NULL DEFAULT '' COMMENT '年款',
     `region_option_code` varchar(50) NOT NULL DEFAULT '' COMMENT '区域',
     `drive_hand` varchar(50) NOT NULL DEFAULT '' COMMENT '驾驶位',
     `sales_version` varchar(50) NOT NULL DEFAULT '' COMMENT '销售版本',
     `status` varchar(50) NOT NULL COMMENT '状态 Active/Inactive',
     `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
     `create_user` varchar(32) NOT NULL DEFAULT '' COMMENT '创建人',
     `update_user` varchar(32) NOT NULL DEFAULT '' COMMENT '修改人',
     `maturity` varchar(16) NOT NULL DEFAULT 'U' COMMENT '定义Model/Model Year的成熟度(U代表Under Study,P代表Production)',
     `del_flag` tinyint NOT NULL DEFAULT '0' COMMENT '1删除 0未删除',
                                     PRIMARY KEY (`id`)
) ENGINE=InnoDB  COMMENT='BaseVehicle整车基础配置表';

CREATE TABLE `boms_feature_change_log`
(
    `id`               bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `feature_id`       bigint unsigned NOT NULL DEFAULT 0 COMMENT 'Feature Id',
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
    INDEX `idx_feature_id_update_time` (`feature_id`, `update_time`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC COMMENT ='BOM中台Feature属性变更记录表';

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

CREATE TABLE `boms_model_year_config`
(
    `id`          bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `model`       varchar(64)     NOT NULL DEFAULT '' COMMENT 'Model',
    `model_year`  varchar(32)     NOT NULL DEFAULT '' COMMENT 'Model Year',
    `oxo_release` varchar(4)      NOT NULL DEFAULT '' COMMENT 'OXO是否Release，取值Yes、No',
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

CREATE TABLE `boms_oxo_feature_option` (
   `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
   `model_code` varchar(32) NOT NULL DEFAULT '' COMMENT '车型',
   `feature_code` varchar(32) NOT NULL COMMENT '配置code',
   `rule_check` varchar(32) NOT NULL DEFAULT '固定值Y/N 定义Rule发布时软校验' COMMENT '规则校验',
   `type` varchar(32) NOT NULL COMMENT '类型，Feature、Option',
   `comment` varchar(512) DEFAULT NULL COMMENT '评论',
   `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序id',
   `soft_delete` smallint NOT NULL DEFAULT '0' COMMENT '软删除 1删除 0未删除',
   `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
   `create_user` varchar(32) NOT NULL DEFAULT '' COMMENT '创建者',
   `update_user` varchar(32) NOT NULL DEFAULT '' COMMENT '更新者',
   `del_flag` smallint NOT NULL DEFAULT '0' COMMENT '是否删除 1删除 0未删除',
   PRIMARY KEY (`id`),
   KEY `idx_model_code` (`model_code`) USING BTREE,
   KEY `idx_feature_code` (`feature_code`) USING BTREE
) ENGINE=InnoDB COMMENT='oxo Feature/Option行信息表';

CREATE TABLE `boms_oxo_option_package` (
   `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
   `feature_option_id` bigint NOT NULL COMMENT 'oxo行id',
   `base_vehicle_id` bigint NOT NULL COMMENT 'oxo列id',
   `package_code` varchar(32) NOT NULL DEFAULT '' COMMENT 'Default ●  Unavailable - Available ○',
   `description` varchar(255) DEFAULT NULL COMMENT '描述',
   `brand` varchar(32) NOT NULL COMMENT '品牌',
   `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
   `create_user` varchar(32)  NOT NULL DEFAULT '' COMMENT '创建者',
   `update_user` varchar(32)  NOT NULL DEFAULT '' COMMENT '更新者',
   `del_flag` smallint NOT NULL DEFAULT '0' COMMENT '是否删除 1删除 0未删除',
   PRIMARY KEY (`id`),
   UNIQUE KEY `uniq_key` (`feature_option_id`,`base_vehicle_id`) USING BTREE,
   KEY `idx_base_vehicle_id` (`base_vehicle_id`) USING BTREE
) ENGINE=InnoDB COMMENT='oxo 打点信息表';

CREATE TABLE `boms_oxo_version_snapshot` (
     `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
     `model_code` varchar(32) NOT NULL DEFAULT '' COMMENT '车型',
     `version` varchar(32) NOT NULL COMMENT '版本号',
     `oxo_snapshot` longtext COMMENT 'oxo快照信息',
     `type` varchar(32)  NOT NULL  COMMENT 'Formal/Informal',
     `brand` varchar(32) NOT NULL COMMENT '品牌',
     `title` varchar(1024) DEFAULT NULL COMMENT '发版标题',
     `change_content` varchar(1024)  DEFAULT NULL COMMENT '发布内容',
     `email_group` varchar(255) DEFAULT NULL COMMENT '邮件组',
     `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
     `create_user` varchar(32) NOT NULL DEFAULT '' COMMENT '创建者',
     `update_user` varchar(32) NOT NULL DEFAULT '' COMMENT '更新者',
     `del_flag` smallint NOT NULL DEFAULT '0' COMMENT '是否删除 1删除 0未删除',
     PRIMARY KEY (`id`) USING BTREE,
     UNIQUE KEY `uniq_model_version` (`model_code`,`version`,`type`) USING BTREE
) ENGINE=InnoDB COMMENT='oxo快照表' ;

