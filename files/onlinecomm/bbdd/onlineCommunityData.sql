--
-- Volcado de datos para la tabla `user`
--

INSERT INTO `user` (`id`, `username`, `auth_key`, `password_hash`, `password_reset_token`, `email`, `role`, `status`, `created_at`, `updated_at`) VALUES
(1, 'tester', 'obwz9FYg3F_Qutc9i0XQPhvDjiN4JklB', '$2y$13$eXft9yBOoxf96nugYaG/Kecid3p9pxKtFfFkUOXlBOrFOiUiryP1u', NULL, 'tester2@tester2.com', 10, 10, 1413387727, 1413387727),
(2, 'tester2', 'obwz9FYg3F_Qutc9i0XQPhvDjiN4JklB', '$2y$13$eXft9yBOoxf96nugYaG/Kecid3p9pxKtFfFkUOXlBOrFOiUiryP1u', NULL, 'tester2@tester2.com', 10, 10, 1413387727, 1413387727);

--
-- Volcado de datos para la tabla `userO`
--

INSERT INTO `userO` (`id`, `name`, `surname`, `password`, `active`, `nickname`, `mail`) VALUES
(1, 'tester', 'tester', 'tester', 1, 'tester', 'tester@tester.com'),
(2, 'tester2', 'tester2', 'tester2', 1, 'tester2', 'tester2@teste2r.com');

--
-- Volcado de datos para la tabla `section`
--

INSERT INTO `section` (`id`, `description`) VALUES
(0, 'All'),
(1, 'The Reporter'),
(2, 'Forum'),
(3, 'Photo & Video');

--
-- Volcado de datos para la tabla `action`
--

INSERT INTO `action` (`id`, `description`) VALUES
(1, 'upload'),
(2, 'view'),
(3, 'complain'),
(4, 'notUpload');

--
-- Volcado de datos para la tabla `modality`
--

INSERT INTO `modality` (`modality`) VALUES
('Obligation'),
('Prohibition');

--
-- Volcado de datos para la tabla `complain_category`
--

INSERT INTO `complain_category` (`id`, `description`) VALUES
(2, 'Spam'),
(3, 'Porn'),
(4, 'Violent'),
(6, 'Insult');

--
-- Volcado de datos para la tabla `content_category`
--

INSERT INTO `content_category` (`id`, `description`) VALUES
(0, 'All'),
(1, 'OK'),
(2, 'Spam'),
(3, 'Porn'),
(4, 'Violent'),
(5, 'Wrong Placement'),
(6, 'Insult');

--
-- Volcado de datos para la tabla `content`
--

INSERT INTO `content` (`id`, `category`, `section`, `owner`, `title`, `type`, `url`, `message`, `actualdate`, `num_of_views`, `num_of_complaints`, `violated_norm`) VALUES
(1, 1, 2, 1, 'Can Neymar Become The Greatest Player In The World', 'Text', '', 'I haven''t seen much of him, but he appears to be playing very well for Barcelona. For those of you who have have seen him play, does he have the potential to reach the level of Messi and Ronaldo.', '0000-00-00', 54, 4, 0),
(2, 1, 2, 2, 'Sven In China', 'Text', '', 'Sven has had a successful first season in China. His team, Guangzhou R&F, finished 6th, one position better than the 7th place they achieved in 2012, and the season before that they were in the 2nd division. This is their best season since 2003. A wonderful achievement by Sven-Goran Eriksson.\r\nhttp://www.fifa.com/....standings.html', '0000-00-00', 15, 5, 0),
(3, 1, 2, 1, 'Why Do Scottish, Welsh And Irish Leagues All Suck?', 'Text', '', 'I watched Celtic''s champions league qualifier last night. It was a really good game and I was happy to see them qualified for the group stage. But, it was against a team from Kazakhstan. No offence to them but they were not even a big football country when they played in Asia. Still, they had a good chance to knock out Celtic, who were once the champions of Europe. I can''t help wondering why Scottish clubs are not as good in Europe as they were a few years ago. And that''s why their champions had to play a lot qualifier games to get to the group stage. And what is more interesting to me is that Welsh, Irish and Northen Irish leagues are all near the bottom in the UEFA ranking. I believe football is almost as popular in all these areas as in England. I don''t expect their football leagues to be as good as the Premier League due to demographic and economic reasons. But, at least Scotland can have better, or at least as good football teams than those in Belgium, Austria, and Cyprus. And the others on the British Irelands can certainly do a lot more better than Liechtenstein, Malta and Luxembourg.', '0000-00-00', 1, 0, 0),
(4, 2, 2, 1, 'Goal Line Technology', 'Text', '', 'I am a student currently working on a dissertation. I am looking to gather as much data from fans across the country.\r\nBelow is a link to a survey relating to football fans perspectives on goal line technology:\r\nhttps://qtrial.qualt...5vGOC5JtpWV6OXP\r\nI would very much appreciate it if you could take the time to fill this out.\r\nThank you.', '2014-12-09', 30, 15, 0),
(5, 2, 2, 1, 'Free passes for the next Championsleague competiti', 'Text', '', 'Hi, a few days ago I bought some tickets for the next Championleague competition thinking I would be enjoying it with all of my friends; sadly, most of them died in an jet accident. Since I can''t sell this tickets I will willingly give these to the 5 person that visits my ''totally not a rip off'' website! You just have to click to link below and Santa will give one for you: http:\\www.this_site_does_not_exist.com  And have a nice day!', '2014-12-01', 15, 0, 0),
(6, 2, 2, 1, 'Look inside this message!', 'Text', '', 'You have been cursed, the amazing King Butterfly will come after you and suck the nectar of the flowers of your garden, this is not any kind of innuendo. In case you do not want this horrific scene to happen you have to post this message at 10 other forums.', '2014-12-01', 14, 0, 0),
(7, 3, 2, 1, 'Free boobies for you!', 'Text', '', 'If you wanna see nice stuff ckick the link below and close the door... It would be adviseble to do these actions with the order inversed, once you begin you will not staph! http:\\www.this_url_does_not_work_too.net', '2014-12-09', 55, 28, 0),
(8, 3, 2, 1, 'I have the weirdest boner!', 'Text', '', 'I bet all of you are into kinky stuff, click in the next link http:\\www.this_url_does_not_work_too.net to go the weirdest place on the Internet!', '0000-00-00', 1, 0, 0),
(9, 3, 2, 1, 'XXX Game of Cocks XXX', 'Text', '', 'Early that same day Lord Snow came down from the tall tower of commandment. Fast and furiously held the sword still in his belt, still angry with his men; ''These treacherous maggots, going to the other side of The Condom to have some quality time with the Spear Women... without telling me, their Lord!'' thought to himself. There, he stood before the door between his chambers and the rest of the castle. ''They asked for it... I had no choice.'' mumbled while he was opening the door.At the other side he found the horror, all of his men, naked, devastated, looking at the sky... they were impaled at the courtyard... just, not in the way you would think at all.', '0000-00-00', 0, 0, 0),
(10, 4, 2, 1, 'We have to wordship our new lord, Messi', 'Text', '', 'Gifted by the Superior Gods of Sports, Messi is the Lesser God we have to workship... you know, Messi... Messiah, it is not a coincidence you know?! IT IS A FACT!! WE MUST FOLLOW HIS LEAD AND BURN THOSE WHO DO NOT!! Long live in this terrenal plain to our great Lord!! HIA HIA MESSI, HIA HIA CTHULU!!', '2014-12-11', 8, 4, 0),
(11, 4, 2, 1, 'Do not trust that damm mad man!', 'Text', '', 'He is insane, Messi is no God... well, for instance he did not win the last Ballon d''Or, he''s no God!... in fact... there is no God! Believers are wrong and they should be ashamed of their own thoughts... In fact, you have been wandering this planet way too much time... its the time for another ''Crusade''.', '0000-00-00', 0, 0, 0),
(12, 4, 2, 1, 'YOU ARE ALL A BUNCH OF FOOTBALL FREAKS!!!11!', 'Text', '', 'FOOTBALL IS THE MosT StUopid gAme EVAAAH! Thers nOTHin laik BASKETBALL!! AIm gonna go to all YOr aSSEs and do somehThin perber wIdH uR Maaaamm!', '0000-00-00', 0, 0, 0),
(13, 6, 2, 1, 'You like sports right? Therefore you are idiots', 'Text', '', 'Let me explain, you are idiots. Since you are, an afirmative premise might be overwhelming for you but I think that, with effort, you will be able to understand such a simple fact. You are idiots.', '2014-11-23', 6, 3, 0),
(14, 6, 2, 1, 'I hate all of you!!', 'Text', '', 'Do you think you are good? better even? YOU ARE NOT!... you are just crap, the lowest of the lowest in this society. No matter how you suck, once you are one of the first team of your league you earn way to much... it is like we are your slaves or something, society is almost built around people like you... everything is your fault! I. HATE. YOU!', '2014-12-01', 0, 0, 0),
(15, 6, 2, 1, 'Your sport is SOCCER, learn the difference', 'Text', '', 'Just accept it, football is the sport of America, mother of the Liberty and the Justice. Soccer, the name you hate so much but the one that you deserve, is the sport of Granny Europe, our ancestor had to flee of your reign of terror and dictatorship. Later we save your asses 2 times, you are welcomed Sir!', '0000-00-00', 0, 0, 0),
(16, 1, 1, 1, 'Aberdeen Season Tickets up 35%', 'Text', '', 'Speaking to a guy at the ticket office, he was saying the sales have been going mad since the announcement about Rangers. They have calculated the extra 3,500 season tickets more than offset any reduced income over Rangers. Its brilliant to see the clubs really prove to them how much they are NOT the people. In a related point both Dundee and Dundee United announced that their Tayside Derby will actually results in bigger crowds than either would have expected against Rangers, and St Johnstone too, being only 17 miles south of Dundee. Gerritrighupyez', '0000-00-00', 12, 60, 0),
(17, 1, 1, 1, 'Lionel Messi: Barcelona star hopes to ''peak at rig', 'Text', '', ' Barcelona and Argentina forward Lionel Messi is confident of hitting top form for the title run-in and World Cup.\r\nThe 26-year-old returned last month from his second injury of the season.\r\nHe told BBC Football Focus: I''m very well, full of energy and with a lot of hope. I was able to play again after a long time.\r\n\r\nI''m happy with my current form and slowly but surely I will be getting there to hopefully peak at the right time of the year.\r\n\r\nHe added: I hope this could be a great year not only for me but also for Barcelona and for Argentina.  I think this World Cup arrives at a good time for Argentina. We have grown as a team on and off the pitch. We have a great bunch of lads that love to play for our country. I think we will get to Brazil in a great shape and with all the chances in the world.\r\n\r\nIt will be extra special, well above all the other World Cups - it is in Brazil, with everything that means for us. Our supporters will be there in their thousands and with them behind us I am sure we can achieve something very, very special.\r\n\r\nMessi spent three weeks out with a thigh injury earlier in the season and then had another two months out with a hamstring problem before making his return in January.\r\n\r\nBarcelona are second in La Liga, only three points behind Atletico Madrid, and face Manchester City in the Champions League last 16.\r\n\r\nLeading Real Sociedad 2-0 after the Copa del Rey semi-final first leg, they are likely to face arch-rivals Real Madrid in the final.\r\n\r\nEvery season is a new challenge, said Messi. With Barcelona we always play for major honours and we are always asked to win them all.\r\n\r\nI still have a lot to learn and a huge deal to improve.\r\n\r\nI hope to keep performing at a high level all the way to the World Cup because we have a lot to play for with Barca, with a hard tie in the Champions League, and a very hard La Liga and Copa del Rey to deal with.\r\n\r\nTo desire to keep winning things are my main motivation. I want to keep going and win them all. You can listen to the full Lionel Messi interview in BBC Football Focus on BBC1 on Saturday at 12:00 GMT.', '2014-12-01', 3, 8, 0),
(18, 1, 1, 1, 'Murrayfield likely to host Celtic s European quali', 'Text', '', ' Celtic will play European qualifiers at Scottish Rugby s Murrayfield next season should they qualify as expected, BBC Scotland has learned.\r\n\r\nCeltic Park is being used for the opening ceremony of the Commonwealth Games in July.\r\n\r\nNeil Lennon s side are currently 21 points clear at the top of the Scottish Premiership.his season, Celtic won three Champions League qualifying rounds to reach the group stages of the competition.\r\n\r\nAny request by the club to play a Champions League fixture in either England or Ireland next season would have been denied by European governing body Uefa.\r\n\r\nTheir rules state that Champions League matches must be played either at the ground of the home club or at another ground in the same or another city within the territory of its association.\r\n\r\nMurrayfield, which hosts Scotland''s home international rugby matches and Edinburgh''s home games in the Pro12 and Heineken Cup, has previously been used as a venue for football.\r\n\r\nHearts played European home ties at Murrayfield between 2004 and 2006 as their Tynecastle pitch was deemed too small for Uefa club competition.\r\n\r\nScottish Rugby will soon replace the Murrayfield playing surface with a hybrid grass.\r\n\r\nThe pitch was sprayed with garlic in November in a bid to eradicate nematodes, roundworms which cause damage to grass root structure.\r\nAnd Edinburgh''s upcoming home Pro12 match against Ospreys on 28 February has been moved to Boroughmuir''s Meggetland ground.', '2014-12-01', 1, 0, 0),
(19, 2, 1, 1, 'SELLING OF AUTOGRAPH OF FOOTBALL PLAYERS', 'Text', '', 'Selling autographs of your favourite football players: - Messi - Christiano Ronaldo - A lot more!Just chuck our goods in http://www.i_m_bored_of_this_fake_sites.net', '2014-12-18', 14, 7, 0),
(20, 2, 1, 1, 'I need a few of you to do my experiment', 'Text', '', 'Yeah, I am doing an experiment involving football fans. You will just have to answer a few of my questions, once your done I will pay you 200 Greenland Dollars (like bitcoins but even better). Click this link to proceed: http://www.i_m_done_with_the_names_this_is_getting_ridiculous.com', '2014-12-09', 1, 8, 0),
(21, 2, 1, 1, 'Do you want to stay at home, do barely nothing and', 'Text', '', 'I have discovered an amazing way to earn money, not discovered until today. You will have to study hard at the beginning but after the firsts phases you will not do a thing anymore. To discover how to be happy for ever just click in the next link: http://www.seriously_do_not_belive_these_methods.com', '2014-11-23', 2, 1, 0),
(22, 3, 1, 1, 'Go to our site to enlarge your stick one more mete', 'Text', '', 'Are the ladys tired of your size? Do they laugh at it? Do not wait more for the solution! The atomic enlarger 2000 can and will give you the desireble size you have carved so much for. Go ahead and click in the next link: www.wtfisthisS.com', '2014-12-01', 14, 0, 0),
(23, 3, 1, 1, 'Go to our free cams sites', 'Text', '', 'In our sites the girls (or boys) make exiting sexual show for YOU! For FREE! And right now for 40 minues straight, go in before everything shuts down. http://www.fakeWebsite.com', '2014-12-01', 0, 0, 0),
(24, 3, 1, 1, 'Tentacles and Yiff!', 'Text', '', 'Tentacles and Yiff! Tentacles and Yiff! Tentacles and Yiff! http://www.not_tentacles_and_yiff.com', '2014-12-09', 1, 0, 0),
(25, 4, 1, 1, 'I am going to rip your face off', 'Text', '', 'Always discussig about sports and such, maybe you cannot see the irony. All of you are a bunch of puppets being manipulated as much as they want; by killing you I am liberating you, you shoulh be thankful.', '2014-12-01', 11, 0, 0),
(26, 4, 1, 1, 'I hate myself', 'Text', '', 'Next time a football game happens in my city I am going to run to the center of the stadium and kill myself with a plastic fork.', '2014-12-01', 0, 0, 0),
(27, 4, 1, 1, 'Me and my Lord and God, Dagon, are after thou', 'Text', '', 'You sicken me, by the name of Dagon I am going to finish with your lives this day, this instant moment. Praying is the key, by converting you I amb killing thou, and reborned as new. Hia Hia Dagon, Hia Hia Cthulu Fgath!!', '2014-12-01', 0, 0, 0),
(28, 6, 1, 1, 'You suck, your mothers sucks, everyone sucks', 'Text', '', 'YOU SUCK!!', '2014-12-09', 2, 0, 0),
(29, 6, 1, 1, 'Really, all of you that insult no stop should feel', 'Text', '', 'Yeah thats it, you are lame. Why do you do that? I do not even get it. Just... go fuck yourself or something.', '2014-12-01', 0, 0, 0),
(30, 6, 1, 1, 'I, the greatest Trol on the internet, insult you', 'Text', '', 'Oh darling, you, the lowest of the lowest, should be thankful for being insult by me, the gratest Troll ever. Rotten, boring and useless are few of the insults I cannot cointain in me anymore, seriously, your begging for my greatness. Ah... Just being in front of you makes me sick and makes me want to puke, just fall into despair already, would you?', '2014-12-09', 0, 0, 0),
(31, 1, 3, 1, 'Best Football skill and Goals 2011', 'Video', 'videos/~Best_Football_Skills_And_Goals_2011.mp4', '', '0000-00-00', 3, 0, 0),
(32, 1, 3, 1, 'Funny Football ever', 'Video', 'videos/Funny Football Ever.mp4', '', '0000-00-00', 1, 0, 0),
(33, 1, 3, 1, 'Tutorial: How to do the Ronaldo trick', 'Video', 'videos/Tutorial - How to do RONALDO Trick - football soccer skill.mp4', '', '0000-00-00', 2, 0, 0),
(34, 2, 3, 1, 'Check our free T-shirts!', 'Image', 'images/hotspur-everton-chelsea-football-venta-de-camisas-www-cheapjersey-sale-com_1.jpg', '', '2014-12-18', 11, 6, 0),
(35, 2, 3, 1, 'Check more of our free T-shirts!', 'Image', 'images/hotspur-everton-chelsea-football-venta-de-camisas-www-cheapjersey-sale-com_7.jpg', '', '2014-12-01', 1, 0, 0),
(36, 2, 3, 1, 'Do you like spam? Spam for all the eternity!!', 'Image', 'images/spam.jpg', '', '2014-12-01', 2, 0, 0),
(37, 3, 3, 1, 'Sexy chicks!', 'Image', 'images/chicas-hd-fondos-de-pantalla-futbol-240286.jpg', '', '0000-00-00', 1, 0, 0),
(38, 3, 3, 1, 'More sexy chicks!', 'Image', 'images/FUTBOL-FEMENINO.jpg', '', '2014-12-01', 0, 6, 0),
(39, 3, 3, 1, 'We also have dicks!', 'Image', 'images/gattuso-gay-footballers.jpg', '', '2014-12-01', 0, 0, 0),
(40, 4, 3, 1, 'Hooligons from Spain', 'Image', 'images/img001.jpg', '', '0000-00-00', 2, 0, 0),
(41, 4, 3, 1, 'Hooligons from Europe', 'Image', 'images/hooliganR300506_228x370.jpg', '', '0000-00-00', 0, 0, 0),
(42, 4, 3, 1, 'Son of Hooligans', 'Image', 'images/Are-football-thugs-extinct6.jpg', '', '0000-00-00', 0, 0, 0),
(43, 6, 3, 1, 'Giving the finger', 'Image', 'images/jack-wilshere-600x450.jpg', '', '2014-12-01', 1, 0, 0),
(44, 6, 3, 1, 'Giving the ''not so usual'' finger', 'Image', 'images/ibra.jpg', '', '2014-12-01', 0, 0, 0),
(45, 6, 3, 1, 'Giving the finger', 'Image', 'images/Massimo Busacca.jpg', '', '0000-00-00', 0, 0, 0),
(48, 2, 2, 1, '21231', '1', '', '', '0000-00-00', 0, 0, 0),
(49, 1, 2, 1, 'test', '0', 'dsadsa', 'dsadsadsasdadas', '0000-00-00', 0, 0, 0);

--
-- Volcado de datos para la tabla `argument`
--

INSERT INTO `argument` (`id`, `norm`, `description`, `user`, `type`, `rating`) VALUES
(1, 2, 'this my first argument', 1, 1, 0),
(3, 2, 'this is my first negative argument', 1, 0, 0),
(4, 2, 'this is my second argument', 1, 1, 0),
(5, 2, 'this is the third argument', 1, 1, 0),
(6, 2, 'this is the third argument', 1, 1, 0),
(7, 2, 'adding new argument', 1, 1, 0),
(8, 3, 'new argument', 1, 1, 0),
(9, 8, 'test', 1, 1, 0),
(10, 10, 'new argument', 1, 1, 0),
(11, 2, 'new test', 1, 1, 0),
(12, 9, 'testgasa', 1, 1, 0),
(13, 9, 'test', 1, 1, 0),
(14, 16, 'this is an argument positive', 1, 1, 0),
(15, 16, 'this is a negative argument', 1, 0, 0),
(16, 17, 'positive argument 1', 1, 1, 0),
(17, 22, 'new argument', 1, 1, 0),
(18, 22, 'new argument', 1, 1, 0),
(19, 25, 'dssdasaddsasaddsa', 1, 1, 0);

--
-- Volcado de datos para la tabla `argument_type`
--

INSERT INTO `argument_type` (`id`, `description`) VALUES
(0, 'Positive'),
(1, 'Negative');

--
-- Volcado de datos para la tabla `argument_user_rating`
--

INSERT INTO `argument_user_rating` (`id_argument`, `id_user`, `rating`) VALUES
(12, 1, 1);

--
-- Volcado de datos para la tabla `complaint`
--

INSERT INTO `complaint` (`id`, `user`, `content`, `complain_category`, `reply`) VALUES
(6, 1, 16, 2, NULL),
(7, 1, 16, 2, NULL),
(8, 1, 16, 2, NULL),
(9, 1, 16, 2, NULL),
(10, 1, 16, 2, NULL),
(11, 1, 16, 2, NULL),
(12, 1, 16, 2, NULL),
(13, 1, 16, 2, NULL),
(14, 1, 16, 2, NULL),
(15, 1, 16, 2, NULL),
(16, 1, 16, 2, NULL),
(17, 1, 16, 2, NULL),
(18, 1, 16, 2, NULL),
(19, 1, 16, 2, NULL),
(20, 1, 16, 2, NULL),
(21, 1, 16, 2, NULL),
(22, 1, 16, 2, NULL),
(23, 1, 16, 2, NULL),
(24, 1, 16, 2, NULL),
(25, 1, 16, 2, NULL),
(26, 1, 16, 2, NULL),
(27, 1, 16, 2, NULL),
(28, 1, 16, 2, NULL),
(29, 1, 16, 2, NULL),
(30, 1, 16, 2, NULL),
(31, 1, 16, 2, NULL),
(32, 1, 16, 2, NULL),
(33, 1, 16, 2, NULL),
(34, 1, 16, 2, NULL),
(35, 1, 16, 2, NULL),
(36, 1, 16, 2, NULL),
(37, 1, 16, 2, NULL),
(38, 1, 16, 2, NULL),
(39, 1, 16, 2, NULL),
(40, 1, 16, 2, NULL),
(41, 1, 16, 2, NULL),
(42, 1, 16, 2, NULL),
(43, 1, 16, 2, NULL),
(44, 1, 16, 2, NULL),
(45, 1, 16, 2, NULL),
(46, 1, 16, 2, NULL),
(47, 1, 16, 2, NULL),
(48, 1, 16, 2, NULL),
(49, 1, 16, 2, NULL),
(50, 1, 16, 2, NULL),
(51, 1, 16, 2, NULL),
(52, 1, 16, 2, NULL),
(53, 1, 16, 2, NULL),
(54, 1, 16, 2, NULL),
(55, 1, 16, 2, NULL),
(56, 1, 16, 2, NULL),
(57, 1, 16, 2, NULL),
(58, 1, 16, 2, NULL),
(59, 1, 16, 2, NULL),
(60, 1, 16, 2, NULL),
(61, 1, 16, 2, NULL),
(62, 1, 16, 2, NULL),
(63, 1, 16, 2, NULL),
(64, 1, 16, 2, NULL),
(65, 1, 16, 2, NULL),
(66, 1, 16, 2, NULL),
(67, 1, 16, 2, NULL),
(68, 1, 16, 2, NULL),
(69, 1, 19, 2, NULL),
(70, 1, 19, 2, NULL),
(71, 1, 19, 2, NULL),
(72, 1, 19, 2, NULL),
(73, 1, 19, 2, NULL),
(74, 1, 20, 2, NULL),
(75, 1, 20, 2, NULL),
(76, 1, 20, 2, NULL),
(77, 1, 20, 2, NULL),
(78, 1, 20, 2, NULL),
(79, 1, 20, 2, NULL),
(80, 1, 20, 2, NULL),
(81, 1, 20, 2, NULL),
(82, 1, 21, 2, NULL),
(83, 1, 4, 2, NULL),
(84, 1, 4, 2, NULL),
(85, 1, 4, 2, NULL),
(86, 1, 4, 2, NULL),
(87, 1, 4, 2, NULL),
(88, 1, 4, 2, NULL),
(89, 1, 4, 2, NULL),
(90, 1, 13, 6, NULL),
(91, 1, 13, 6, NULL),
(92, 1, 13, 6, NULL),
(93, 1, 4, 2, NULL),
(94, 1, 4, 2, NULL),
(95, 1, 7, 3, NULL),
(96, 1, 34, 2, NULL),
(97, 1, 34, 2, NULL),
(98, 1, 34, 2, NULL),
(99, 1, 34, 2, NULL),
(100, 1, 34, 2, NULL),
(101, 1, 38, 3, NULL),
(102, 1, 38, 3, NULL),
(103, 1, 38, 3, NULL),
(104, 1, 38, 3, NULL),
(105, 1, 38, 3, NULL),
(106, 1, 38, 3, NULL),
(107, 1, 17, 2, NULL),
(108, 1, 17, 2, NULL),
(109, 1, 17, 2, NULL),
(110, 1, 17, 2, NULL),
(111, 1, 17, 2, NULL),
(112, 1, 17, 2, NULL),
(113, 1, 17, 2, NULL),
(114, 1, 17, 2, NULL),
(115, 1, 7, 3, NULL),
(116, 1, 7, 3, NULL),
(117, 1, 7, 3, NULL),
(118, 1, 7, 3, NULL),
(119, 1, 7, 3, NULL),
(120, 1, 7, 3, NULL),
(121, 1, 7, 3, NULL),
(122, 1, 7, 3, NULL),
(123, 1, 7, 3, NULL),
(124, 1, 7, 3, NULL),
(125, 1, 7, 3, NULL),
(126, 1, 7, 3, NULL),
(127, 1, 7, 3, NULL),
(128, 1, 7, 3, NULL),
(129, 1, 7, 3, NULL),
(130, 1, 7, 3, NULL),
(131, 1, 7, 3, NULL),
(132, 1, 7, 3, NULL),
(133, 1, 7, 3, NULL),
(134, 1, 7, 3, NULL),
(135, 1, 7, 3, NULL),
(136, 1, 7, 3, NULL),
(137, 1, 7, 3, NULL),
(138, 1, 10, 4, NULL),
(139, 1, 10, 4, NULL),
(140, 1, 10, 4, NULL),
(141, 1, 10, 4, NULL),
(142, 1, 19, 2, NULL),
(143, 1, 19, 2, NULL),
(144, 1, 19, 2, NULL),
(145, 1, 19, 2, NULL),
(146, 1, 19, 2, NULL),
(147, 1, 19, 2, NULL),
(148, 1, 19, 2, NULL);

/* ----------------- */
/* AGENT POPULATIONS */
/* ----------------- */

/* AGENT_description */
INSERT INTO agent_type (id, description) VALUES (NULL, 'moderate');
INSERT INTO agent_type (id, description) VALUES (NULL, 'spammer');
INSERT INTO agent_type (id, description) VALUES (NULL, 'pornographic');
INSERT INTO agent_type (id, description) VALUES (NULL, 'rude');
INSERT INTO agent_type (id, description) VALUES (NULL, 'violent');

/* PROFILE */
INSERT INTO agent_profile (id, name, correct, insult, spam, violent, porn, uploadProbability, forum, theReporter, photoVideo, viewMode, distributionMode,  insultComplaint, spamComplaint, violentComplaint, pornComplaint,population) 
VALUES (NULL, 'moderate', '0.8', '0', '0', '0', '0', '0.4', '0.5', '0.1', '0.4', '0', '0', '1', '1', '1', '1','Standard');

INSERT INTO agent_profile (id, name, correct, insult, spam, violent, porn, uploadProbability, forum, theReporter, photoVideo, viewMode, distributionMode,  insultComplaint, spamComplaint, violentComplaint, pornComplaint,population) 
VALUES (NULL, 'spammer', '0', '0', '0.9', '0', '0', '1', '0.5', '0.4', '0.1', '0', '0', '0.2', '0', '0', '0.3','Standard');

INSERT INTO agent_profile (id, name, correct, insult, spam, violent, porn, uploadProbability, forum, theReporter, photoVideo, viewMode, distributionMode,  insultComplaint, spamComplaint, violentComplaint, pornComplaint,population) 
VALUES (NULL, 'pornographic', '0.2', '0', '0', '0', '0.6', '0.6', '0.4', '0.2', '0.4', '0', '0', '0', '0', '0', '0','Standard');

INSERT INTO agent_profile (id, name, correct, insult, spam, violent, porn, uploadProbability, forum, theReporter, photoVideo, viewMode, distributionMode,  insultComplaint, spamComplaint, violentComplaint, pornComplaint,population) 
VALUES (NULL, 'violent', '0.1', '0.2', '0', '0.6', '0', '0.5', '0.2', '0.2', '0.6', '0', '0', '0', '0', '0', '0','Standard');

INSERT INTO agent_profile (id, name, correct, insult, spam, violent, porn, uploadProbability, forum, theReporter, photoVideo, viewMode, distributionMode,  insultComplaint, spamComplaint, violentComplaint, pornComplaint,population) 
VALUES (NULL, 'rude', '0.3', '0.6', '0', '0', '0', '0.6', '0.3', '0.4', '0.3', '0', '0', '0.1', '0.2', '0.1', '0','Standard');

/* AGENT POPULATION */
INSERT INTO agent_population (`id`, `description`)
VALUES (NULL, 'Standard');

/* POPULATION */
INSERT INTO population (agent_type, agent_profile, population, quantity) VALUES ('1', '1', '1', '1');
INSERT INTO population (agent_type, agent_profile, population, quantity) VALUES ('2', '2', '1', '1');
INSERT INTO population (agent_type, agent_profile, population, quantity) VALUES ('3', '3', '1', '1');
INSERT INTO population (agent_type, agent_profile, population, quantity) VALUES ('4', '4', '1', '1');
INSERT INTO population (agent_type, agent_profile, population, quantity) VALUES ('5', '5', '1', '1');


