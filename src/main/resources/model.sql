--
-- Table structure for table `permission`
--

DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission` (
  `id` int NOT NULL AUTO_INCREMENT,
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '权限路径，权限组通常为空',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限名称',
  `type` tinyint NOT NULL DEFAULT '0' COMMENT '权限类型，0具体权限，1权限组，默认0',
  `level` tinyint NOT NULL DEFAULT '0' COMMENT '权限层级，0顶层，正数代表具体层级，默认0',
  `parent_id` int DEFAULT NULL COMMENT '父级id，null没有父级，即处于顶层',
  `anonymous` tinyint NOT NULL DEFAULT '0' COMMENT '是否支持匿名访问，0否，1是，默认0',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_url` (`url`),
  KEY `parent_id` (`parent_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `permission`
--

LOCK TABLES `permission` WRITE;
INSERT INTO `permission` VALUES (1,'/logout','退出登录',0,0,NULL,0,'2023-08-10 11:01:43','2023-08-10 11:01:43',0),(2,NULL,'用户模块',1,0,NULL,0,'2023-08-10 11:01:43','2023-08-10 11:01:43',0),(3,'/user/register','用户注册',0,1,2,1,'2023-08-10 11:01:43','2023-08-10 11:01:43',0),(4,'/test/1','测试1',0,0,NULL,0,'2023-08-10 11:01:43','2023-08-10 11:01:43',0);
UNLOCK TABLES;

--
-- Table structure for table `relation_role_permission`
--

DROP TABLE IF EXISTS `relation_role_permission`;
CREATE TABLE `relation_role_permission` (
  `role_id` int NOT NULL,
  `permission_id` int NOT NULL,
  PRIMARY KEY (`role_id`,`permission_id`),
  KEY `role_permission_ibfk_1` (`role_id`) USING BTREE,
  KEY `role_permission_ibfk_2` (`permission_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `relation_role_permission`
--

LOCK TABLES `relation_role_permission` WRITE;
INSERT INTO `relation_role_permission` VALUES (1,1),(1,2),(1,3),(1,4),(2,1),(3,1);
UNLOCK TABLES;

--
-- Table structure for table `relation_user_role`
--

DROP TABLE IF EXISTS `relation_user_role`;
CREATE TABLE `relation_user_role` (
  `user_id` int NOT NULL,
  `role_id` int NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `role_id` (`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `relation_user_role`
--

LOCK TABLES `relation_user_role` WRITE;
INSERT INTO `relation_user_role` VALUES (1,1),(2,2),(3,3);
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色代码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '角色描述',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
INSERT INTO `role` VALUES (1,'admin','管理员','拥有系统后台权限','2023-08-10 11:01:43','2023-08-10 11:01:43',0),(2,'test','测试','用于系统测试','2023-08-10 11:01:43','2023-08-10 11:01:43',0),(3,'user','用户','仅支持客户端权限','2023-08-10 11:01:43','2023-08-10 11:01:43',0);
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '昵称',
  `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '头像链接',
  `phone` char(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '手机号',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
INSERT INTO `user` VALUES (1,'13800000000','202cb962ac59075b964b07152d234b70','admin(123)','1','13800000000','2023-08-10 11:01:43','2023-08-10 11:01:43',0),(2,'15800000000','202cb962ac59075b964b07152d234b70','test(123)','1','15800000000','2023-08-10 11:01:43','2023-08-10 11:01:43',0),(3,'18800000000','202cb962ac59075b964b07152d234b70','user(123)','1','18800000000','2023-08-10 11:01:43','2023-08-10 11:01:43',0);
UNLOCK TABLES;