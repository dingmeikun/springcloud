/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.101.15(dingmk)
 Source Server Type    : MySQL
 Source Server Version : 50615
 Source Host           : localhost:3306
 Source Schema         : spring_config

 Target Server Type    : MySQL
 Target Server Version : 50615
 File Encoding         : 65001

 Date: 05/09/2019 16:13:48
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for properties
-- ----------------------------
DROP TABLE IF EXISTS `properties`;
CREATE TABLE `properties`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `key` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `value` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `application` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `profile` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `lable` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of properties
-- ----------------------------
INSERT INTO `properties` VALUES (1, 'com.dingmk.spring.config', 'I am the mysql configuration file from dev environment.', 'config-info', 'dev', 'master');
INSERT INTO `properties` VALUES (2, 'com.dingmk.spring.config', 'I am the mysql configuration file from test environment.', 'config-info', 'test', 'master');
INSERT INTO `properties` VALUES (3, 'com.dingmk.spring.config', 'I am the mysql configuration file from prod environment.', 'config-info', 'prod', 'master');

SET FOREIGN_KEY_CHECKS = 1;
