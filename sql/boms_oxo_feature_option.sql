CREATE TABLE `boms_oxo_feature_option` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `model_code` varchar(32) NOT NULL DEFAULT '' COMMENT '车型',
  `feature_code` varchar(32) NOT NULL COMMENT '配置code',
  `rule_check` varchar(32) NOT NULL DEFAULT '固定值Y/N 定义Rule发布时软校验',
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