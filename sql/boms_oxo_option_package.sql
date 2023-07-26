CREATE TABLE `boms_oxo_option_package` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `feature_option_id` bigint NOT NULL COMMENT 'oxo行id',
  `base_vehicle_id` varchar(11) NOT NULL COMMENT 'oxo列id',
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