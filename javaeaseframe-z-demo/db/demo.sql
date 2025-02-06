DROP database if exists `javaeaseframe-demo`;
CREATE database `javaeaseframe-demo` default charset=utf8;
use `javaeaseframe-demo`;

DROP TABLE IF EXISTS `javaeaseframe_demo_cluster`;
CREATE TABLE `javaeaseframe_demo_cluster`(
	`id` BIGINT(20) NOT NULL  COMMENT '主键ID',
	`name` VARCHAR(100) default NULL  COMMENT '集群名称',
	`code` VARCHAR(100) default NULL  COMMENT '集群编码',
	`type` VARCHAR(100) default NULL  COMMENT '集群类型，如：kafka,rocketmq',
	`version` VARCHAR(50) default NULL  COMMENT '版本号',
	`client_version` VARCHAR(2000) default NULL  COMMENT '客户端版本号，多个使用逗号分隔',
	`zk_url` VARCHAR(2000) default NULL  COMMENT 'zk地址',
	`broker_list` VARCHAR(2000) default NULL  COMMENT '主机列表',
	`create_time` DATETIME default NULL  COMMENT '创建时间',
	PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='集群管理';

DROP TABLE IF EXISTS `javaeaseframe_demo_cluster_node`;
CREATE TABLE `javaeaseframe_demo_cluster_node`(
	`id` BIGINT(20) NOT NULL  COMMENT '主键ID',
	`cluster_id` BIGINT(20) default NULL  COMMENT '',
	`name` VARCHAR(100) default NULL  COMMENT '集群名称',
	`code` VARCHAR(100) default NULL  COMMENT '集群编码',
	`type` VARCHAR(100) default NULL  COMMENT '集群类型，如：kafka,rocketmq',
	`version` VARCHAR(50) default NULL  COMMENT '版本号',
	`client_version` VARCHAR(2000) default NULL  COMMENT '客户端版本号，多个使用逗号分隔',
	`zk_url` VARCHAR(2000) default NULL  COMMENT 'zk地址',
	`broker_list` VARCHAR(2000) default NULL  COMMENT '主机列表',
	`create_time` DATETIME default NULL  COMMENT '创建时间',
	PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='集群管理';

DROP TABLE IF EXISTS `javaeaseframe_demo_task`;
CREATE TABLE `javaeaseframe_demo_task`(
	`id` BIGINT(20) NOT NULL  COMMENT '主键ID',
	`name` VARCHAR(200) default NULL  COMMENT '名称',
	`code` VARCHAR(200) default NULL  COMMENT '编码',
	`from_cluster_id` BIGINT(20) default NULL  COMMENT '源集群ID',
	`from_topic` VARCHAR(200) default NULL  COMMENT '源主题',
	`from_config` TEXT default NULL  COMMENT '源集群消费配置',
	`to_cluster_id` BIGINT(20) default NULL  COMMENT '目标集群ID',
	`to_topic` VARCHAR(200) default NULL  COMMENT '目标主题',
	`to_config` TEXT default NULL  COMMENT '目标集群生产配置',
	`filter_config` TEXT default NULL  COMMENT '过滤配置',
	`convert_config` TEXT default NULL  COMMENT '转换配置',
	`partition_match_strategy` VARCHAR(50) default NULL  COMMENT '分区匹配策略，AVG_BY_CIRCLE:平均分配，CONFIG：配置，PARTITION_ROOM：分区匹配，HASH：hash值，RANDOM：随机',
	`partition_match_config` TEXT default NULL  COMMENT '分区匹配配图，如：1:1,2:2,3:3,4:4',
	`status` TINYINT(4) default NULL  COMMENT '状态，0：停用，1：启用',
	`create_time` DATETIME default NULL  COMMENT '创建时间',
	PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务管理';

DROP TABLE IF EXISTS `javaeaseframe_demo_weather`;
CREATE TABLE `javaeaseframe_demo_weather`(
	`id` BIGINT(20) NOT NULL  auto_increment  COMMENT '序号，主键，自增长',
	`areaId` BIGINT(20) default NULL  COMMENT '区域ID',
	`city` VARCHAR(30) default NULL  COMMENT '城市名称',
	`cityCode` VARCHAR(30) default NULL  COMMENT '城市编号，用于同步天气信息',
	`date` INT(11) default NULL  COMMENT '日期，格式：yyyyMMdd',
	`reportTime` VARCHAR(30) default NULL  COMMENT '天气发布时间，格式：yyyy-MM-dd HH:mm:ss',
	`sunriseTime` VARCHAR(30) default NULL  COMMENT '日出时间，格式：hh:mm',
	`sunsetTime` VARCHAR(30) default NULL  COMMENT '日落时间，格式：hh:mm',
	`weatherType` TINYINT(4) default NULL  COMMENT '天气类型(白天)，1：晴，2：多云，3：阴，4：小雨，5：中雨，6：大雨，7：暴雨，8：雨夹雪，9：小雪，10：中雪，11：大雪，12：暴风雪，13：多云转雨',
	`weatherTypeForNight` TINYINT(4) default NULL  COMMENT '天气类型(夜间)，1：晴，2：多云，3：阴，4：小雨，5：中雨，6：大雨，7：暴雨，8：雨夹雪，9：小雪，10：中雪，11：大雪，12：暴风雪，13：多云转雨',
	`weatherTypeForReal` TINYINT(4) default NULL  COMMENT '天气类型(实时)，1：晴，2：多云，3：阴，4：小雨，5：中雨，6：大雨，7：暴雨，8：雨夹雪，9：小雪，10：中雪，11：大雪，12：暴风雪，13：多云转雨',
	`weather` VARCHAR(30) default NULL  COMMENT '天气(白天)，晴、多云、雨',
	`weatherForNight` VARCHAR(30) default NULL  COMMENT '天气(夜间)，晴、多云、雨',
	`weatherForReal` VARCHAR(30) default NULL  COMMENT '天气(实时)，晴、多云、雨',
	`airTemperature` DECIMAL(20,2) default NULL  COMMENT '空气温度(白天)，单位：摄氏度',
	`airTemperatureForNight` DECIMAL(20,2) default NULL  COMMENT '空气温度(夜间)，单位：摄氏度',
	`airTemperatureForReal` DECIMAL(20,2) default NULL  COMMENT '空气温度(实时)，单位：摄氏度',
	`airHumidity` DECIMAL(20,2) default NULL  COMMENT '空气湿度(白天)，单位：百分比',
	`airHumidityForReal` DECIMAL(20,2) default NULL  COMMENT '空气湿度(实时)，单位：百分比',
	`airQuality` VARCHAR(30) default NULL  COMMENT '空气质量，优、良、差',
	`airQI` DECIMAL(20,2) default NULL  COMMENT '空气指数',
	`airMaxTemperature` DECIMAL(20,2) default NULL  COMMENT '最高温(白天)，白天温度，单位：摄氏度',
	`airMinTemperature` DECIMAL(20,2) default NULL  COMMENT '最低温(夜间)，夜间温度，单位：摄氏度',
	`airPM25Value` DECIMAL(20,2) default NULL  COMMENT 'PM2.5，单位：ug/m3',
	`airPM10Value` DECIMAL(20,2) default NULL  COMMENT 'PM10，单位：ug/m3',
	`windDirection` VARCHAR(30) default NULL  COMMENT '风向(白天)，东风、西风',
	`windDirectionForNight` VARCHAR(30) default NULL  COMMENT '风向(夜间)，东风、西风',
	`windDirectionForReal` VARCHAR(30) default NULL  COMMENT '风向(实时)，东风、西风',
	`windPower` VARCHAR(30) default NULL  COMMENT '风力(白天)',
	`windPowerForNight` VARCHAR(30) default NULL  COMMENT '风力(夜间)',
	`windPowerForReal` VARCHAR(30) default NULL  COMMENT '风力(实时)',
	`barometric` DECIMAL(20,2) default NULL  COMMENT '大气压力，单位：hPa',
	`ultravioletRays` DECIMAL(20,2) default NULL  COMMENT '紫外线，单位：W/㎡',
	`ultraVioletIndex` DECIMAL(20,2) default NULL  COMMENT '紫外线指数，单位：W/㎡',
	`remark` VARCHAR(2000) default NULL  COMMENT '天气描述',
	`ext` VARCHAR(2000) default NULL  COMMENT '天气扩展',
	`lastSyncTime` DATETIME default NULL  COMMENT '最后同步时间',
	PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='城市天气信息';

DROP TABLE IF EXISTS `javaeaseframe_demo_dept`;
CREATE TABLE `javaeaseframe_demo_dept`(
	`id` bigint(20) NOT NULL  auto_increment  COMMENT '主键ID，主键，自增长',
	`name` varchar(100) default NULL  COMMENT '部门名称',
	`childSize` int(11) default NULL  COMMENT '子部门数量',
	`level` int(11) default NULL  COMMENT '部门层级',
	`remark` text default NULL  COMMENT '部门描述',
	`type` tinyint(4) default '1' COMMENT '部门类型，0：区域，1：部门，默认1',
	`orderId` int(11) default NULL  COMMENT '排序编号',
	`maxChildId` int(11) default NULL  COMMENT '子部门的最大ID',
	`status` tinyint(4) default '1' COMMENT '部门状态，0：停用，1：正常，2：删除，默认1',
	`createTime` datetime default NULL  COMMENT '创建时间',
	`createUserId` bigint(20) default NULL  COMMENT '创建用户ID',
	`createUserName` varchar(100) default NULL  COMMENT '创建用户',
	PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='部门管理';

DROP TABLE IF EXISTS `javaeaseframe_demo_user`;
CREATE TABLE `javaeaseframe_demo_user`(
	`id` bigint(20) NOT NULL  auto_increment  COMMENT '主键ID，主键，自增长',
	`loginName` varchar(100) NOT NULL  COMMENT '登录名',
	`loginPwd` varchar(200) NOT NULL  COMMENT '登录密码',
	`loginLimitAddress` varchar(2000) default NULL  COMMENT '登录限制地址，多个IP地址用逗号分隔，可以使用IP段匹配，如：172.17.*非空：则只能该值内的IP可以登录',
	`name` varchar(100) NOT NULL  COMMENT '用户名',
	`headImg` varchar(200) default NULL  COMMENT '头像，图片路径地址',
	`mobile` varchar(20) default NULL  COMMENT '联系电话',
	`email` varchar(100) default NULL  COMMENT '联系邮箱',
	`userType` tinyint(4) default '0' COMMENT '用户类型，0：系统用户，1：普通用户，默认0',
	`status` tinyint(4) default '1' COMMENT '用户状态，0：停用，1：正常，2：冻结，3：销户，默认1',
	`createTime` datetime default NULL  COMMENT '创建时间',
	`createUserId` bigint(20) default NULL  COMMENT '创建用户ID',
	`createUserName` varchar(100) default NULL  COMMENT '创建用户',
	PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户管理';
