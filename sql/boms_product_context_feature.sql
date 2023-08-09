CREATE TABLE `boms_product_context_feature` (
      `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
      `model_code` varchar(32) NOT NULL COMMENT '车型编码',
      `feature_code` varchar(32) NOT NULL COMMENT '特征编码',
      `feature_group` varchar(32) NOT NULL COMMENT 'group id',
      `type` varchar(32) NOT NULL COMMENT '类型，Feature、Option',
      `feature_seq` int DEFAULT NULL COMMENT '排序',
      `create_user` varchar(32) NOT NULL DEFAULT '' COMMENT '创建人',
      `update_user` varchar(32) NOT NULL DEFAULT '' COMMENT '更新人',
      `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
      `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
      `del_flag` tinyint NOT NULL DEFAULT '0' COMMENT '是否逻辑删除，0：否，1：是',
      PRIMARY KEY (`id`) USING BTREE,
      UNIQUE KEY `model_feature_idx` (`model_code`,`feature_code`) USING BTREE
) ENGINE=InnoDB  COMMENT='车型的配置项（feature）信息';