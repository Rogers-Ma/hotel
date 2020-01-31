/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80018
 Source Host           : localhost:3306
 Source Schema         : hotel

 Target Server Type    : MySQL
 Target Server Version : 80018
 File Encoding         : 65001

 Date: 31/01/2020 15:45:31
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `id` int(10) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (110, 'admin', '123');

-- ----------------------------
-- Table structure for customer
-- ----------------------------
DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `real_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `telephone` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `balance` double(10, 3) NOT NULL DEFAULT 0.000,
  `deleted` int(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 127511 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of customer
-- ----------------------------
INSERT INTO `customer` VALUES (127458, '李四', 'lisi', '123', '12306987456', 183.000, 0);
INSERT INTO `customer` VALUES (127493, '张三', 'zhangsan', '123', '15848546875', 132.000, 0);
INSERT INTO `customer` VALUES (127510, '王五', 'wangwu', '123', '1584845697', 600.000, 0);

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `isbn` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `state` int(1) NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `create_date` date NOT NULL,
  `price` decimal(10, 2) NOT NULL,
  `customer_id` int(10) NOT NULL,
  `room_id` int(10) NOT NULL,
  `deleted` int(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order
-- ----------------------------
INSERT INTO `order` VALUES (19, '5804516938579', 2, '2020-01-31', '2020-02-01', '2020-01-31', 169.00, 127493, 1011, 0);
INSERT INTO `order` VALUES (20, '5804518285636', 2, '2020-01-31', '2020-02-01', '2020-01-31', 209.00, 127493, 1012, 0);
INSERT INTO `order` VALUES (21, '5804559653745', 3, '2020-01-31', '2020-02-01', '2020-01-31', 169.00, 127458, 1011, 0);
INSERT INTO `order` VALUES (22, '5804565000827', 2, '2020-01-31', '2020-02-01', '2020-01-31', 169.00, 127458, 1011, 0);
INSERT INTO `order` VALUES (23, '5804565428702', 2, '2020-01-31', '2020-02-01', '2020-01-31', 180.00, 127458, 1013, 0);

-- ----------------------------
-- Table structure for room
-- ----------------------------
DROP TABLE IF EXISTS `room`;
CREATE TABLE `room`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `number` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `type_id` int(10) NOT NULL,
  `state` int(255) NOT NULL DEFAULT 0,
  `deleted` int(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1015 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of room
-- ----------------------------
INSERT INTO `room` VALUES (1011, 'A1001', 1004, 0, 0);
INSERT INTO `room` VALUES (1012, 'B1001', 1005, 0, 0);
INSERT INTO `room` VALUES (1013, 'C1002', 1011, 0, 0);
INSERT INTO `room` VALUES (1014, 'D3002', 1012, 0, 0);

-- ----------------------------
-- Table structure for type
-- ----------------------------
DROP TABLE IF EXISTS `type`;
CREATE TABLE `type`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `price` decimal(10, 2) NOT NULL,
  `deleted` int(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1013 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of type
-- ----------------------------
INSERT INTO `type` VALUES (1004, '大床房', 169.00, 0);
INSERT INTO `type` VALUES (1005, '豪华房', 209.00, 0);
INSERT INTO `type` VALUES (1011, '三人房', 180.00, 0);
INSERT INTO `type` VALUES (1012, '标准间', 150.00, 0);

SET FOREIGN_KEY_CHECKS = 1;
