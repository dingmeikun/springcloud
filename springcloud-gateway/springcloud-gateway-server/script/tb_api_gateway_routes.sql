/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.101.213
 Source Server Type    : MySQL
 Source Server Version : 50638
 Source Host           : 192.168.101.213:3306
 Source Schema         : test

 Target Server Type    : MySQL
 Target Server Version : 50638
 File Encoding         : 65001

 Date: 21/10/2019 15:02:41
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_api_gateway_routes
-- ----------------------------
DROP TABLE IF EXISTS `tb_api_gateway_routes`;
CREATE TABLE `tb_api_gateway_routes`  (
  `route_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '路由ID',
  `route_uri` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '路由URI',
  `predicates` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路由判定器',
  `filters` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '过滤器',
  `orders` int(11) NOT NULL COMMENT '生效次序',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路由描述',
  `status` int(11) NOT NULL DEFAULT 0 COMMENT '状态:1 启用状态、0 失效状态',
  `createtime` datetime(0) NOT NULL COMMENT '创建时间',
  `updatetime` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`route_id`) USING BTREE,
  UNIQUE INDEX `index_id_uri`(`route_id`, `route_uri`) USING BTREE COMMENT '联合索引id_uri'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '服务网关路由表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of tb_api_gateway_routes
-- ----------------------------
INSERT INTO `tb_api_gateway_routes` VALUES ('region_location_v0', 'http://192.168.101.213:9705/pubNumService/location', '[{\"args\":{\"pattern\":\"/v0/**\"},\"name\":\"Path\"}]', '[{\"args\":{\"fallbackUri\":\"forward:/gateway/fallback\",\"name\":\"fallbackcmd\"},\"name\":\"Hystrix\"}]', 0, '区域归属地服务v0', 1, '2019-09-29 16:04:49', '2019-10-21 03:48:13');
INSERT INTO `tb_api_gateway_routes` VALUES ('region_location_v2', 'http://192.168.101.213:9705/business/location', '[{\"args\":{\"pattern\":\"/v2/**\"},\"name\":\"Path\"}]', '[{\"args\":{\"fallbackUri\":\"forward:/gateway/fallback\",\"name\":\"fallbackcmd\"},\"name\":\"Hystrix\"}]', 0, '区域归属地服务v2', 1, '2019-09-29 16:04:49', '2019-10-21 03:48:16');

SET FOREIGN_KEY_CHECKS = 1;
