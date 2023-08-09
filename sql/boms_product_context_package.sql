CREATE TABLE `boms_product_context_package` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `model_code` varchar(32) NOT NULL COMMENT '车型',
    `model_year` varchar(32) NOT NULL COMMENT '车型年款',
    `row_id` varchar(32) NOT NULL COMMENT '对应option行Id',
    `create_user` varchar(32) NOT NULL DEFAULT '' COMMENT '创建人',
    `update_user` varchar(32) NOT NULL DEFAULT '' COMMENT '更新人',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `del_flag` tinyint NOT NULL DEFAULT '0' COMMENT '是否逻辑删除，0：否，1：是',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_model_row` (`model_code`,`model_year`,`row_id`) USING BTREE
) ENGINE=InnoDB COMMENT 'model modelYear option feature关系';