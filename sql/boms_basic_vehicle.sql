CREATE TABLE `boms_basic_vehicle` (
   `id` bigint NOT NULL AUTO_INCREMENT,
   `base_vehicle_id` varchar(11) NOT NULL COMMENT '流水号',
   `model` varchar(50) NOT NULL DEFAULT '' COMMENT '车型',
   `model_year` varchar(50) NOT NULL DEFAULT '' COMMENT '年款',
   `region` varchar(50) NOT NULL DEFAULT '' COMMENT '区域',
   `region_english_name` varchar(50) NOT NULL DEFAULT '' COMMENT '区域英文名',
   `region_chinese_name` varchar(50) NOT NULL DEFAULT '' COMMENT '区域中文名',
   `drive_hand` varchar(50) NOT NULL DEFAULT '' COMMENT '驾驶位',
   `drive_hand_english_name` varchar(50) NOT NULL DEFAULT '' COMMENT '驾驶位英文名',
   `drive_hand_chinese_name` varchar(50) NOT NULL DEFAULT '' COMMENT '驾驶位中文名',
   `sales_version` varchar(50) NOT NULL DEFAULT '' COMMENT '销售版本',
   `sales_version_chinese_name` varchar(50) NOT NULL DEFAULT '' COMMENT '销售版本中文名',
   `sales_version_english_name` varchar(50) NOT NULL DEFAULT '' COMMENT '销售版本英文名',
   `status` varchar(50) NOT NULL COMMENT '状态 Active/Inactive',
   `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
   `create_user` varchar(32) NOT NULL DEFAULT '' COMMENT '创建人',
   `update_user` varchar(32) NOT NULL DEFAULT '' COMMENT '修改人',
   `maturity` varchar(16) NOT NULL DEFAULT 'U' COMMENT '定义Model/Model Year的成熟度(U代表Under Study,P代表Production)',
   `del_flag` smallint NOT NULL DEFAULT '0' COMMENT '1删除 0未删除',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB  COMMENT='BaseVehicle整车基础配置表';