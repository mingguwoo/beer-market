CREATE TABLE `boms_basic_vehicle` (
   `id` bigint NOT NULL AUTO_INCREMENT,
   `base_vehicle_id` varchar(11) NOT NULL COMMENT '流水号',
   `model_code` varchar(50) NOT NULL DEFAULT '' COMMENT '车型',
   `model_year` varchar(50) NOT NULL DEFAULT '' COMMENT '年款',
   `region` varchar(50) NOT NULL DEFAULT '' COMMENT '区域',
   `drive_hand` varchar(50) NOT NULL DEFAULT '' COMMENT '驾驶位',
   `sales_version` varchar(50) NOT NULL DEFAULT '' COMMENT '销售版本',
   `status` varchar(50) NOT NULL COMMENT '状态 Active/Inactive',
   `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
   `create_user` varchar(32) NOT NULL DEFAULT '' COMMENT '创建人',
   `update_user` varchar(32) NOT NULL DEFAULT '' COMMENT '修改人',
   `maturity` varchar(16) NOT NULL DEFAULT 'U' COMMENT '定义Model/Model Year的成熟度(U代表Under Study,P代表Production)',
   `del_flag` smallint NOT NULL DEFAULT '0' COMMENT '1删除 0未删除',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB  COMMENT='BaseVehicle整车基础配置表';