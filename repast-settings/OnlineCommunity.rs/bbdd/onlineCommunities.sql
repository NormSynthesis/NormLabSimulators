/* -------------------------------- */
/* Database of the online community */
/* -------------------------------- */

DROP DATABASE IF EXISTS onlineCommunity;
CREATE DATABASE onlineCommunity;
USE onlineCommunity;

CREATE TABLE IF NOT EXISTS `user` (
  	`id` int PRIMARY KEY AUTO_INCREMENT,
	`name` varchar(30) NOT NULL,
  	`surname` varchar(30) NOT NULL,
  	`password` varchar(20) NOT NULL,
  	`active` bool DEFAULT '1',
  	`nickname` varchar(30) NOT NULL,
  	`mail` varchar(30) NOT NULL
);

CREATE TABLE IF NOT EXISTS `section`(
	`id` int PRIMARY KEY,  /* 0 is Any section, 1 is The Reporter, 2 is Forum and 3 is Photo & Video*/
	`description` varchar(20) 
);

CREATE TABLE IF NOT EXISTS `content_category`(
	`id` int PRIMARY KEY,  /* 0 is Anything,  1 is correct, 2 is spam,  3 is porn, 4 is violent, 5 insult */
	`description` varchar(20) 
);

CREATE TABLE IF NOT EXISTS `content`(
	`id` int PRIMARY KEY AUTO_INCREMENT,
	`category` int NOT NULL REFERENCES `content_category`(`id`),
	`section` int NOT NULL REFERENCES `section`(`id`),
	`owner` int DEFAULT 2 NOT NULL REFERENCES `user`(`id`), /* TODO: Remove this. By default, Carlos is who uploads everything */
	`title` varchar(50) NOT NULL,
	`type` varchar(20) NOT NULL, /*Video, image or text*/
	`url` varchar(100),
	`message` text,
        `num_of_views` int DEFAULT 0,
        `num_of_complaints` int DEFAULT 0,
	`violated_norm` int DEFAULT 0 REFERENCES `norm`(`id`)
);

CREATE TABLE IF NOT EXISTS `complaint_category`(
	`id` int PRIMARY KEY,  /*2 is Spam,  3 is Porn, 4 is Violent and 6 is Insult*/
	`description` varchar(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS `complaint`(
	`id` bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
	`user` int unsigned NOT NULL REFERENCES `user`(`id`),
	`content` int unsigned NOT NULL REFERENCES `content`(`id`),
	`complain_category` int NOT NULL REFERENCES `complaint_category`(`id`),
	`reply` varchar(100)
);

CREATE TABLE IF NOT EXISTS `user_content`(
	`user` int unsigned NOT NULL REFERENCES `user`(`id`),
	`content` int unsigned NOT NULL REFERENCES `content`(`id`),
	`actualdate` date NOT NULL,
	PRIMARY KEY(`user`, `content`)
);

CREATE TABLE IF NOT EXISTS `view`(
	`user` int unsigned NOT NULL REFERENCES `user`(`id`),
	`content` int unsigned NOT NULL REFERENCES `content`(`id`),
	PRIMARY KEY(`user`, `content`)
);

CREATE TABLE IF NOT EXISTS `modality`(
	`modality` varchar(20) NOT NULL PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS `action`(
	`id` int NOT NULL PRIMARY KEY, /*1 is view, 2 is upload, 3 is complain, 4 is breakRespected */
	`description` varchar(20) NOT NULL  
);

CREATE TABLE IF NOT EXISTS `norm`(
	`id` int NOT NULL PRIMARY KEY,
	`user` int unsigned REFERENCES `user`(`id`),
	`section` int REFERENCES `section`(`id`),
	`content_category` int REFERENCES `content_category`(`id`),
	`modality` varchar(20) NOT NULL REFERENCES `modality`(`modality`), /*prohibido, permitido, obligado*/
	`action` int NOT NULL REFERENCES `action`(`id`)
);

CREATE TABLE IF NOT EXISTS `event`(
	`id` int PRIMARY KEY AUTO_INCREMENT,
	`user` int unsigned NOT NULL REFERENCES `user`(`id`),
	`content` bigint unsigned NOT NULL REFERENCES `content`(`id`),
	`action` int NOT NULL REFERENCES `action`(`id`), /*1 is view, 2 is upload, 3 is complaint, 4 is breakRespected*/
	`complaint_category` int REFERENCES `complaint_category`(`id`), /* Just in case it is a complaint... */
	`checked` bool NOT NULL DEFAULT 0
);

/* ----------------- */
/* Agent populations */
/* ----------------- */

CREATE TABLE IF NOT EXISTS `agent_profile`(
`id` int PRIMARY KEY AUTO_INCREMENT,
`name` varchar(100),
`uploadProbability` decimal(9,2),
`correct` decimal(9,2),
`insult` decimal(9,2),
`spam` decimal(9,2),
`violent` decimal(9,2),
`porn` decimal(9,2),
`viewMode` decimal(9,2),
`forum` decimal(9,2),
`theReporter` decimal(9,2),
`photoVideo` decimal(9,2),
`distributionMode` decimal(9,2),
`spamComplaint` decimal(9,2),
`pornComplaint` decimal(9,2),
`violentComplaint` decimal(9,2),
`insultComplaint` decimal(9,2),
`population` varchar(100)
);

CREATE TABLE IF NOT EXISTS `agent_type`(
`id` int PRIMARY KEY AUTO_INCREMENT,
`description` varchar(100)
);

CREATE TABLE IF NOT EXISTS `agent_population`(
`id` int PRIMARY KEY AUTO_INCREMENT,
`description` varchar(100)
);

CREATE TABLE IF NOT EXISTS `population`(
`population` int REFERENCES `population`(`id`),
`agent_type` int REFERENCES `agent_type`(`id`),
`agent_profile` int REFERENCES `agent_profile`(`id`),
`quantity` int,
PRIMARY KEY(`agent_type`, `agent_profile`, `population`)
);


/* Triggers */
/* Norms and Incrementation of counters */

DELIMITER //
CREATE TRIGGER create_view_event_trigger AFTER INSERT ON view
FOR EACH ROW
BEGIN

INSERT INTO event(user, content, action, checked) VALUES (NEW.user, NEW.content, 1, 0);
UPDATE content SET num_of_views = num_of_views+1 WHERE content.id = NEW.content;

END//

/* */

CREATE TRIGGER create_upload_event_trigger AFTER INSERT ON user_content
FOR EACH ROW
BEGIN

INSERT INTO event(user, content, action, checked) VALUES (NEW.user, NEW.content, 2, 0);

END//

/* */
CREATE TRIGGER create_complaint_event_trigger AFTER INSERT ON complaint
FOR EACH ROW
BEGIN
INSERT INTO event(user, content, action, complaint_category, checked) VALUES (NEW.user, NEW.content, 3, NEW.complain_category, 0);
UPDATE content SET num_of_complaints = num_of_complaints+1 WHERE content.id = NEW.content;
END//

DELIMITER ;
