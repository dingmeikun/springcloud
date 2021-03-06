/*
 Navicat Premium Data Transfer

 Source Server         : 213MongoDB
 Source Server Type    : MongoDB
 Source Server Version : 30602
 Source Host           : 192.168.101.213:27017
 Source Schema         : locationInfo

 Target Server Type    : MongoDB
 Target Server Version : 30602
 File Encoding         : 65001

 Date: 09/07/2019 09:55:42
*/


// ----------------------------
// Collection structure for baidu_location_info
// ----------------------------
db.getCollection("baidu_location_info").drop();
db.createCollection("baidu_location_info");
db.getCollection("baidu_location_info").createIndex({
    location: "2d"
}, {
    name: "location_2d"
});


// ----------------------------
// Collection structure for gaode_location_info
// ----------------------------
db.getCollection("gaode_location_info").drop();
db.createCollection("gaode_location_info");
db.getCollection("gaode_location_info").createIndex({
    location: "2d"
}, {
    name: "location_2d"
});

