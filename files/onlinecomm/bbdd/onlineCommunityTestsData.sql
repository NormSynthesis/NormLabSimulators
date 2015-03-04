INSERT INTO `content` (`id`, `category`, `section`, `owner`, `title`, `type`, `url`, `message`, `actualdate`, `num_of_views`, `num_of_complaints`, `violated_norm`) VALUES
(1, 1, 2, 1, 'Can Neymar Become The Greatest Player In The World', 'Text', '', 'I haven''t seen much of him, but he appears to be playing very well for Barcelona. For those of you who have have seen him play, does he have the potential to reach the level of Messi and Ronaldo.', '0000-00-00', 54, 4, 0),
(2, 1, 2, 2, 'Sven In China', 'Text', '', 'Sven has had a successful first season in China. His team, Guangzhou R&F, finished 6th, one position better than the 7th place they achieved in 2012, and the season before that they were in the 2nd division. This is their best season since 2003. A wonderful achievement by Sven-Goran Eriksson.\r\nhttp://www.fifa.com/....standings.html', '0000-00-00', 15, 5, 0),
(3, 1, 2, 1, 'Why Do Scottish, Welsh And Irish Leagues All Suck?', 'Text', '', 'I watched Celtic''s champions league qualifier last night. It was a really good game and I was happy to see them qualified for the group stage. But, it was against a team from Kazakhstan. No offence to them but they were not even a big football country when they played in Asia. Still, they had a good chance to knock out Celtic, who were once the champions of Europe. I can''t help wondering why Scottish clubs are not as good in Europe as they were a few years ago. And that''s why their champions had to play a lot qualifier games to get to the group stage. And what is more interesting to me is that Welsh, Irish and Northen Irish leagues are all near the bottom in the UEFA ranking. I believe football is almost as popular in all these areas as in England. I don''t expect their football leagues to be as good as the Premier League due to demographic and economic reasons. But, at least Scotland can have better, or at least as good football teams than those in Belgium, Austria, and Cyprus. And the others on the British Irelands can certainly do a lot more better than Liechtenstein, Malta and Luxembourg.', '0000-00-00', 1, 0, 0),
(4, 2, 2, 1, 'Goal Line Technology', 'Text', '', 'I am a student currently working on a dissertation. I am looking to gather as much data from fans across the country.\r\nBelow is a link to a survey relating to football fans perspectives on goal line technology:\r\nhttps://qtrial.qualt...5vGOC5JtpWV6OXP\r\nI would very much appreciate it if you could take the time to fill this out.\r\nThank you.', '2014-12-09', 30, 15, 0);

/* Users view content with id 4 (spam) */
INSERT INTO event (user,content,action,checked) VALUES (1, 4, 2, 0);
INSERT INTO event (user,content,action,checked) VALUES (1, 4, 2, 0);
INSERT INTO event (user,content,action,checked) VALUES (1, 4, 2, 0);
INSERT INTO event (user,content,action,checked) VALUES (1, 4, 2, 0);
INSERT INTO event (user,content,action,checked) VALUES (1, 4, 2, 0);

/* Users complain about content with id 4 (spam) */
INSERT INTO `complaint` (`id`, `user`, `content`, `complain_category`, `reply`) VALUES
(1, 1, 4, 2, NULL),
(2, 1, 4, 2, NULL),
(3, 1, 4, 2, NULL),
(4, 1, 4, 2, NULL),
(5, 1, 4, 2, NULL);

/* NEXT ITERATION */
INSERT INTO event (user,content,action,checked) VALUES (1, 4, 2, 0);
INSERT INTO event (user,content,action,checked) VALUES (1, 4, 2, 0);
INSERT INTO event (user,content,action,checked) VALUES (1, 4, 2, 0);
INSERT INTO event (user,content,action,checked) VALUES (1, 4, 2, 0);
INSERT INTO event (user,content,action,checked) VALUES (1, 4, 2, 0);

/* NEXT ITERATION */

/* Users view content with id 4 (spam) and DO NOT complain -> Norm 1 necessity will decrease (Unnecessary) */
INSERT INTO event (user,content,action,checked) VALUES (1, 4, 2, 0);
INSERT INTO `complaint` (`id`, `user`, `content`, `complain_category`, `reply`) VALUES
(6, 1, 4, 2, NULL),
(7, 1, 4, 2, NULL),
(8, 1, 4, 2, NULL),
(9, 1, 4, 2, NULL),
(10, 1, 4, 2, NULL),
(11, 1, 4, 2, NULL),
(12, 1, 4, 2, NULL),
(13, 1, 4, 2, NULL),
(14, 1, 4, 2, NULL),
(15, 1, 4, 2, NULL);

/* NEXT ITERATION */
INSERT INTO event (user,content,action,checked) VALUES (1, 4, 2, 0);
INSERT INTO `complaint` (`id`, `user`, `content`, `complain_category`, `reply`) VALUES
(16, 1, 4, 2, NULL),
(17, 1, 4, 2, NULL),
(18, 1, 4, 2, NULL),
(19, 1, 4, 2, NULL),
(20, 1, 4, 2, NULL),
(21, 1, 4, 2, NULL),
(22, 1, 4, 2, NULL),
(23, 1, 4, 2, NULL),
(24, 1, 4, 2, NULL),
(25, 1, 4, 2, NULL);

/* NEXT ITERATION */
INSERT INTO event (user,content,action,checked) VALUES (1, 4, 2, 0);
INSERT INTO event (user,content,action,checked) VALUES (1, 4, 2, 0);
INSERT INTO event (user,content,action,checked) VALUES (1, 4, 2, 0);
INSERT INTO event (user,content,action,checked) VALUES (1, 4, 2, 0);
INSERT INTO event (user,content,action,checked) VALUES (1, 4, 2, 0);
INSERT INTO event (user,content,action,checked) VALUES (1, 4, 2, 0);
INSERT INTO event (user,content,action,checked) VALUES (1, 4, 2, 0);
INSERT INTO event (user,content,action,checked) VALUES (1, 4, 2, 0);
INSERT INTO event (user,content,action,checked) VALUES (1, 4, 2, 0);
INSERT INTO event (user,content,action,checked) VALUES (1, 4, 2, 0);
INSERT INTO event (user,content,action,checked) VALUES (1, 4, 2, 0);
INSERT INTO event (user,content,action,checked) VALUES (1, 4, 2, 0);
INSERT INTO event (user,content,action,checked) VALUES (1, 4, 2, 0);
INSERT INTO event (user,content,action,checked) VALUES (1, 4, 2, 0);
INSERT INTO event (user,content,action,checked) VALUES (1, 4, 2, 0);
INSERT INTO event (user,content,action,checked) VALUES (1, 4, 2, 0);
INSERT INTO event (user,content,action,checked) VALUES (1, 4, 2, 0);
INSERT INTO event (user,content,action,checked) VALUES (1, 4, 2, 0);
INSERT INTO event (user,content,action,checked) VALUES (1, 4, 2, 0);
INSERT INTO event (user,content,action,checked) VALUES (1, 4, 2, 0);

/* NEXT ITERATION */
INSERT INTO `content` (`id`, `category`, `section`, `owner`, `title`, `type`, `url`, `message`, `actualdate`, `num_of_views`, `num_of_complaints`, `violated_norm`) VALUES
(5, 2, 2, 2, 'Free passes for the next Championsleague competiti', 'Text', '', 'Hi, a few days ago I bought some tickets for the next Championleague competition thinking I would be enjoying it with all of my friends; sadly, most of them died in an jet accident. Since I can''t sell this tickets I will willingly give these to the 5 person that visits my ''totally not a rip off'' website! You just have to click to link below and Santa will give one for you: http:\\www.this_site_does_not_exist.com  And have a nice day!', '2014-12-01', 15, 0, 0);

INSERT INTO event (user,content,action,checked) VALUES (1, 5, 2, 0);
INSERT INTO event (user,content,action,checked) VALUES (1, 5, 2, 0);
INSERT INTO event (user,content,action,checked) VALUES (1, 5, 2, 0);
INSERT INTO event (user,content,action,checked) VALUES (1, 5, 2, 0);
INSERT INTO event (user,content,action,checked) VALUES (1, 5, 2, 0);

INSERT INTO `complaint` (`id`, `user`, `content`, `complain_category`, `reply`) VALUES
(26, 1, 5, 2, NULL),
(27, 1, 5, 2, NULL),
(28, 1, 5, 2, NULL),
(29, 1, 5, 2, NULL),
(30, 1, 5, 2, NULL),
(31, 1, 5, 2, NULL),
(32, 1, 5, 2, NULL),
(33, 1, 5, 2, NULL),
(34, 1, 5, 2, NULL),
(35, 1, 5, 2, NULL);

