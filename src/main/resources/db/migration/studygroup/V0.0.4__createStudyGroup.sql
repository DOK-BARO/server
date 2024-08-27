CREATE TABLE `study_group`
(
	`id`                bigint      NOT NULL AUTO_INCREMENT,
	`name`              varchar(50) NOT NULL,
	`profile_image_url` varchar(100)         DEFAULT NULL,
	`created_at`        datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`updated_at`        datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	`deleted`           tinyint(1)  NOT NULL DEFAULT '0',
	PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci

