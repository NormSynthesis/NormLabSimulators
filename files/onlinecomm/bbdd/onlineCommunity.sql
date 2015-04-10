SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Base de datos: `onlineCommunity`
--

--
-- Estructura de tabla para la tabla `user`
--

CREATE TABLE IF NOT EXISTS `user` (
`id` int(11) NOT NULL,
  `username` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `auth_key` varchar(32) COLLATE utf8_unicode_ci NOT NULL,
  `password_hash` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `password_reset_token` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `email` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `role` smallint(6) NOT NULL DEFAULT '10',
  `status` smallint(6) NOT NULL DEFAULT '10',
  `created_at` int(11) NOT NULL,
  `updated_at` int(11) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=2 ;

--
-- Estructura de tabla para la tabla `userO`
--

CREATE TABLE IF NOT EXISTS `userO` (
`id` int(11) NOT NULL,
  `name` varchar(30) NOT NULL,
  `surname` varchar(30) NOT NULL,
  `password` varchar(20) NOT NULL,
  `active` tinyint(1) DEFAULT '1',
  `nickname` varchar(30) NOT NULL,
  `mail` varchar(30) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Estructura de tabla para la tabla `section`
--

CREATE TABLE IF NOT EXISTS `section` (
  `id` int(11) NOT NULL,
  `description` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Estructura de tabla para la tabla `action`
--

CREATE TABLE IF NOT EXISTS `action` (
  `id` int(11) NOT NULL,
  `description` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Estructura de tabla para la tabla `content`
--

CREATE TABLE IF NOT EXISTS `content` (
`id` int(11) NOT NULL,
  `category` int(11) NOT NULL,
  `section` int(11) NOT NULL,
  `owner` int(11) DEFAULT NULL,
  `title` varchar(50) NOT NULL,
  `type` varchar(20) NOT NULL,
  `url` varchar(100) DEFAULT NULL,
  `message` text,
  `actualdate` date NOT NULL,
  `num_of_views` int(11) DEFAULT '0',
  `num_of_complaints` int(11) DEFAULT '0',
  `violated_norm` int(11) DEFAULT '0'
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=50 ;

--
-- Estructura de tabla para la tabla `content_category`
--

CREATE TABLE IF NOT EXISTS `content_category` (
  `id` int(11) NOT NULL,
  `description` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Estructura de tabla para la tabla `view`
--

CREATE TABLE IF NOT EXISTS `view` (
  `user` int(10) unsigned NOT NULL,
  `content` int(10) unsigned NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Estructura de tabla para la tabla `complain_category`
--

CREATE TABLE IF NOT EXISTS `complain_category` (
  `id` int(11) NOT NULL,
  `description` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


--
-- Estructura de tabla para la tabla `complaint`
--

CREATE TABLE IF NOT EXISTS `complaint` (
  `id` bigint(20) NOT NULL,
  `user` int(10) unsigned NOT NULL,
  `content` int(10) unsigned NOT NULL,
  `complain_category` int(11) NOT NULL,
  `reply` varchar(100) DEFAULT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=149 ;

--
-- Estructura de tabla para la tabla `conflict`
--

CREATE TABLE IF NOT EXISTS `conflict` (
  `id` int(11) NOT NULL,
  `complaint` int(10) unsigned DEFAULT NULL,
  `norm` int(10) unsigned DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Estructura de tabla para la tabla `modality`
--

CREATE TABLE IF NOT EXISTS `modality` (
  `modality` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Estructura de tabla para la tabla `norm`
--

CREATE TABLE IF NOT EXISTS `norm` (
`id` int(11) NOT NULL,
  `section` int(11) DEFAULT NULL,
  `content_category` int(11) DEFAULT NULL,
  `modality` varchar(20) NOT NULL,
  `action` int(11) NOT NULL,
  `rating` int(11) DEFAULT '0',
  `name` varchar(20) NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=26 ;

--
-- Estructura de tabla para la tabla `event`
--

CREATE TABLE IF NOT EXISTS `event` (
`id` int(11) NOT NULL,
  `user` int(10) unsigned NOT NULL,
  `content` bigint(20) unsigned NOT NULL,
  `action` int(11) NOT NULL,
  `infringedNorm` int(11) DEFAULT NULL,
  `fulfilledNorm` int(11) DEFAULT NULL,
  `complain_category` int(11) DEFAULT NULL,
  `checked` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=195 ;

--
-- Estructura de tabla para la tabla `population`
--

CREATE TABLE IF NOT EXISTS `population` (
  `population` int(11) NOT NULL DEFAULT '0',
  `agent_type` int(11) NOT NULL DEFAULT '0',
  `agent_profile` int(11) NOT NULL DEFAULT '0',
  `quantity` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Estructura de tabla para la tabla `agent_population`
--

CREATE TABLE IF NOT EXISTS `agent_population` (
`id` int(11) NOT NULL,
  `description` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Estructura de tabla para la tabla `agent_profile`
--

CREATE TABLE IF NOT EXISTS `agent_profile`(
`id` int,
`name` varchar(100),
`correct` decimal(9,2),
`spam` decimal(9,2),
`porn` decimal(9,2),
`violent` decimal(9,2),
`insult` decimal(9,2),
`uploadProbability` decimal(9,2),
`forum` decimal(9,2),
`theReporter` decimal(9,2),
`photoVideo` decimal(9,2),
`viewMode` decimal(9,2),
`distributionMode` decimal(9,2),
`spamComplaint` decimal(9,2),
`pornComplaint` decimal(9,2),
`violentComplaint` decimal(9,2),
`insultComplaint` decimal(9,2),
`population` varchar(100)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Estructura de tabla para la tabla `agent_type`
--

CREATE TABLE IF NOT EXISTS `agent_type` (
`id` int(11) NOT NULL,
  `description` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;


--
-- Estructura de tabla para la tabla `argument`
--

CREATE TABLE IF NOT EXISTS `argument` (
`id` int(11) NOT NULL,
  `norm` int(11) NOT NULL,
  `description` varchar(100) NOT NULL,
  `user` int(10) unsigned DEFAULT NULL,
  `type` tinyint(1) DEFAULT NULL,
  `rating` int(11) DEFAULT '0'
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=20 ;

--
-- Estructura de tabla para la tabla `argument_type`
--

CREATE TABLE IF NOT EXISTS `argument_type` (
  `id` int(11) NOT NULL,
  `description` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Estructura de tabla para la tabla `argument_user_rating`
--

CREATE TABLE IF NOT EXISTS `argument_user_rating` (
  `id_argument` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `rating` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `action`
--
ALTER TABLE `action`
 ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `agent_population`
--
ALTER TABLE `agent_population`
 ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `agent_profile`
--
ALTER TABLE `agent_profile`
 ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `agent_type`
--
ALTER TABLE `agent_type`
 ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `argument`
--
ALTER TABLE `argument`
 ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `argument_type`
--
ALTER TABLE `argument_type`
 ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `complaint`
--
ALTER TABLE `complaint`
 ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `complain_category`
--
ALTER TABLE `complain_category`
 ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `conflict`
--
ALTER TABLE `conflict`
 ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `content`
--
ALTER TABLE `content`
 ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `content_category`
--
ALTER TABLE `content_category`
 ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `event`
--
ALTER TABLE `event`
 ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `modality`
--
ALTER TABLE `modality`
 ADD PRIMARY KEY (`modality`);

--
-- Indices de la tabla `norm`
--
ALTER TABLE `norm`
 ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `population`
--
ALTER TABLE `population`
 ADD PRIMARY KEY (`agent_type`,`agent_profile`,`population`);

--
-- Indices de la tabla `section`
--
ALTER TABLE `section`
 ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `user`
--
ALTER TABLE `user`
 ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `userO`
--
ALTER TABLE `userO`
 ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `view`
--
ALTER TABLE `view`
 ADD PRIMARY KEY (`user`,`content`);

--
-- AUTO_INCREMENT de la tabla `agent_population`
--
ALTER TABLE `agent_population`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT de la tabla `agent_profile`
--
ALTER TABLE `agent_profile`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT de la tabla `agent_type`
--
ALTER TABLE `agent_type`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT de la tabla `argument`
--
ALTER TABLE `argument`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=20;
--
-- AUTO_INCREMENT de la tabla `complaint`
--
ALTER TABLE `complaint`
MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=149;
--
-- AUTO_INCREMENT de la tabla `content`
--
ALTER TABLE `content`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=50;
--
-- AUTO_INCREMENT de la tabla `event`
--
ALTER TABLE `event`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=195;
--
-- AUTO_INCREMENT de la tabla `norm`
--
ALTER TABLE `norm`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=26;
--
-- AUTO_INCREMENT de la tabla `user`
--
ALTER TABLE `user`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT de la tabla `userO`
--
ALTER TABLE `userO`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;


/*Triggers*/
/*Norms and Incrementation of counters*/

DELIMITER $$
CREATE TRIGGER create_upload_event_trigger
	AFTER INSERT ON content
	FOR EACH ROW
	BEGIN
		/*Creation fo the event upload */
		INSERT INTO event(user, content, action, checked) VALUES (NEW.owner, NEW.id, 1, 0);
	END$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER create_view_event_trigger
	AFTER INSERT ON view
	FOR EACH ROW
	BEGIN
		/*Creation fo the event view */
		INSERT INTO event(user, content, action, checked) VALUES (NEW.user, NEW.content, 2, 0);
		/* Increment of the counter of the content views*/
		UPDATE content SET num_of_views = num_of_views+1 WHERE content.id = NEW.content;
	END$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER create_complaint_event_trigger
	AFTER INSERT ON complaint
	FOR EACH ROW
	BEGIN
		/*Creation fo the event complaint */
		INSERT INTO event(user, content, action, complain_category, checked) VALUES (NEW.user, NEW.content, 3, NEW.complain_category, 0);
		/*Increment of the counter of the content complaints*/
		UPDATE content SET num_of_complaints = num_of_complaints+1 WHERE content.id = NEW.content;
	END$$
DELIMITER ;
