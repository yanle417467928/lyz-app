/* 创建管理页面菜单信息表 */
CREATE TABLE IF NOT EXISTS `app_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `login_name` varchar(255) NOT NULL COMMENT '登录名',
  `name` varchar(255) DEFAULT NULL COMMENT '真实姓名',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `salt` varchar(255) NOT NULL COMMENT '盐',
  `user_type` int(11) NOT NULL COMMENT '用户身份类型',
  PRIMARY KEY (`id`),
  UNIQUE KEY `login_name_index` (`login_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
