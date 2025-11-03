/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : PostgreSQL
 Source Server Version : 140017
 Source Host           : localhost:5432
 Source Catalog        : mdb
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 140017
 File Encoding         : 65001

 Date: 03/11/2025 14:53:48
*/


-- ----------------------------
-- Table structure for t_system_application
-- ----------------------------
DROP TABLE IF EXISTS "public"."t_system_application";
CREATE TABLE "public"."t_system_application" (
  "id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "system_name" varchar(255) COLLATE "pg_catalog"."default",
  "bucket_name" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamptz(6)
)
;

-- ----------------------------
-- Records of t_system_application
-- ----------------------------
INSERT INTO "public"."t_system_application" VALUES ('11111', 'm-file', 'm-file', '2025-08-29 16:01:49+08');

-- ----------------------------
-- Primary Key structure for table t_system_application
-- ----------------------------
ALTER TABLE "public"."t_system_application" ADD CONSTRAINT "t_system_application_pkey" PRIMARY KEY ("id");
