CREATE TABLE `boms_product_context_row_info` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `option_code` varchar(32) NOT NULL DEFAULT '' COMMENT 'option_code',
    `feature_code` varchar(32) NOT NULL DEFAULT '' COMMENT 'feature_code',
    `feature_group` varchar(32)  NOT NULL COMMENT 'group_id',
    `create_user` varchar(32) NOT NULL DEFAULT '' COMMENT '创建人',
    `update_user` varchar(32) NOT NULL DEFAULT '' COMMENT '更新人',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `del_flag` tinyint NOT NULL DEFAULT '0' COMMENT '是否逻辑删除，0：否，1：是',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_model_feature_option` (`option_code`,`feature_code`,`feature_group`) USING BTREE
) ENGINE=InnoDB COMMENT 'option feature group 行信息';