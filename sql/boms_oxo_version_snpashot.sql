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