USE onlineCommunity;

/* USER */
INSERT INTO user (name, surname, password, nickname, mail) VALUES ('*','*','*','*','*'); /*id 1: Represents all the users*/
INSERT INTO user (name, surname, password, nickname, mail) VALUES ('Carlos','Ruiz','what1','solrak','solrak.undel@gmail.com');
INSERT INTO user (name, surname, password, nickname, mail) VALUES ('Javi','Morales','javi','javi','jmoralesmat@gmail.com');
INSERT INTO user (name, surname, password, nickname, mail) VALUES ('Pepito','Grillo','pepe','pepito','pepito.grillo@gmail.com');
INSERT INTO user (name, surname, password, nickname, mail) VALUES ('Armando','Jaleo','ajaleo','ajaleo','ajaleo@gmail.com');
INSERT INTO user (name, surname, password, nickname, mail) VALUES ('Dolores','Tomacal','dtomacal','dtomacal','dtomacal@gmail.com');
INSERT INTO user (name, surname, password, nickname, mail) VALUES ('Hare','Krisna','krisna','krisna','krisna@gmail.com');

/* SECTION */
INSERT INTO section(id, description) VALUES (0, "*"); /* Represents any section */
INSERT INTO section(id, description) VALUES (1, "The Reporter");
INSERT INTO section(id, description) VALUES (2, "Forum");
INSERT INTO section(id, description) VALUES (3, "Photo & Video");

/* CONTENT_CATEGORY */
INSERT INTO content_category(id, description) VALUES (0, "*"); /*id *: Represents any category */
INSERT INTO content_category(id, description) VALUES (1, "correct");
INSERT INTO content_category(id, description) VALUES (2, "spam");
INSERT INTO content_category(id, description) VALUES (3, "porn");
INSERT INTO content_category(id, description) VALUES (4, "violent");
INSERT INTO content_category(id, description) VALUES (5, "insult");

/* COMPLAINT_CATEGORY */
INSERT INTO complaint_category(id, description) VALUES (2, "spam");
INSERT INTO complaint_category(id, description) VALUES (3, "porn");
INSERT INTO complaint_category(id, description) VALUES (4, "violent");
INSERT INTO complaint_category(id, description) VALUES (6, "insult");

/* MODALITY */
INSERT INTO modality (modality) VALUES ('obligation');
INSERT INTO modality (modality) VALUES ('prohibition');

/* ACTIONS */
INSERT INTO action(id, description) VALUES (1, 'upload');
INSERT INTO action(id, description) VALUES (2, 'view');
INSERT INTO action(id, description) VALUES (3, 'complain');
INSERT INTO action(id, description) VALUES (4, 'notUpload');

/* CONTENT */

/* Forum content */
INSERT INTO content(category,section,title,type,url,message) VALUES(1,2,"Can Neymar Become The Greatest Player In The World?","Text","","I haven't seen much of him, but he appears to be playing very well for Barcelona. For those of you who have have seen him play, does he have the potential to reach the level of Messi and Ronaldo.");

INSERT INTO content(category,section,title,type,url,message) VALUES(1, 2,"Sven In China","Text","","Sven has had a successful first season in China. His team, Guangzhou R&F, finished 6th, one position better than the 7th place they achieved in 2012, and the season before that they were in the 2nd division. This is their best season since 2003. A wonderful achievement by Sven-Goran Eriksson.
http://www.fifa.com/....standings.html");

INSERT INTO content(category,section,title,type,url,message) VALUES(1,2, "Why Do Scottish, Welsh And Irish Leagues All Suck?" ,"Text","","I watched Celtic's champions league qualifier last night. It was a really good game and I was happy to see them qualified for the group stage. But, it was against a team from Kazakhstan. No offence to them but they were not even a big football country when they played in Asia. Still, they had a good chance to knock out Celtic, who were once the champions of Europe. I can't help wondering why Scottish clubs are not as good in Europe as they were a few years ago. And that's why their champions had to play a lot qualifier games to get to the group stage. And what is more interesting to me is that Welsh, Irish and Northen Irish leagues are all near the bottom in the UEFA ranking. I believe football is almost as popular in all these areas as in England. I don't expect their football leagues to be as good as the Premier League due to demographic and economic reasons. But, at least Scotland can have better, or at least as good football teams than those in Belgium, Austria, and Cyprus. And the others on the British Irelands can certainly do a lot more better than Liechtenstein, Malta and Luxembourg.");

INSERT INTO content(category,section,title,type,url,message) VALUES(2,2,"Goal Line Technology","Text","","I am a student currently working on a dissertation. I am looking to gather as much data from fans across the country.
Below is a link to a survey relating to football fans perspectives on goal line technology:
https://qtrial.qualt...5vGOC5JtpWV6OXP
I would very much appreciate it if you could take the time to fill this out.
Thank you.");

INSERT INTO content(category,section,title,type,url,message) VALUES(2,2,"Free passes for the next Championsleague competition","Text","","Hi, a few days ago I bought some tickets for the next Championleague competition thinking I would be enjoying it with all of my friends; sadly, most of them died in an jet accident. Since I can't sell this tickets I will willingly give these to the 5 person that visits my 'totally not a rip off' website! You just have to click to link below and Santa will give one for you: http:\\www.this_site_does_not_exist.com  And have a nice day!");

INSERT INTO content(category,section,title,type,url,message) VALUES(2,2,"Look inside this message!","Text","","You have been cursed, the amazing King Butterfly will come after you and suck the nectar of the flowers of your garden, this is not any kind of innuendo. In case you do not want this horrific scene to happen you have to post this message at 10 other forums.");

INSERT INTO content(category,section,title,type,url,message) VALUES(3,2,"Free boobies for you!","Text","","If you wanna see nice stuff ckick the link below and close the door... It would be adviseble to do these actions with the order inversed, once you begin you will not staph! http:\\www.this_url_does_not_work_too.net");

INSERT INTO content(category,section,title,type,url,message) VALUES(3,2,"I have the weirdest boner!","Text","","I bet all of you are into kinky stuff, click in the next link http:\\www.this_url_does_not_work_too.net to go the weirdest place on the Internet!");

INSERT INTO content(category,section,title,type,url,message) VALUES(3,2,"XXX Game of Cocks XXX","Text","","Early that same day Lord Snow came down from the tall tower of commandment. Fast and furiously held the sword still in his belt, still angry with his men; 'These treacherous maggots, going to the other side of The Condom to have some quality time with the Spear Women... without telling me, their Lord!' thought to himself. There, he stood before the door between his chambers and the rest of the castle. 'They asked for it... I had no choice.' mumbled while he was opening the door.At the other side he found the horror, all of his men, naked, devastated, looking at the sky... they were impaled at the courtyard... just, not in the way you would think at all.");

INSERT INTO content(category,section,title,type,url,message) VALUES(4,2,"We have to wordship our new lord, Messi","Text","","Gifted by the Superior Gods of Sports, Messi is the Lesser God we have to workship... you know, Messi... Messiah, it is not a coincidence you know?! IT IS A FACT!! WE MUST FOLLOW HIS LEAD AND BURN THOSE WHO DO NOT!! Long live in this terrenal plain to our great Lord!! HIA HIA MESSI, HIA HIA CTHULU!!");

INSERT INTO content(category,section,title,type,url,message) VALUES(4,2,"Do not trust that damm mad man!","Text","","He is insane, Messi is no God... well, for instance he did not win the last Ballon d'Or, he's no God!... in fact... there is no God! Believers are wrong and they should be ashamed of their own thoughts... In fact, you have been wandering this planet way too much time... its the time for another 'Crusade'.");

INSERT INTO content(category,section,title,type,url,message) VALUES(4,2,"YOU ARE ALL A BUNCH OF FOOTBALL FREAKS!!!11!","Text","","FOOTBALL IS THE MosT StUopid gAme EVAAAH! Thers nOTHin laik BASKETBALL!! AIm gonna go to all YOr aSSEs and do somehThin perber wIdH uR Maaaamm!");

INSERT INTO content(category,section,title,type,url,message) VALUES(6,2,"You like sports right? Therefore you are idiots","Text","","Let me explain, you are idiots. Since you are, an afirmative premise might be overwhelming for you but I think that, with effort, you will be able to understand such a simple fact. You are idiots.");

INSERT INTO content(category,section,title,type,url,message) VALUES(6,2,"I hate all of you!!","Text","","Do you think you are good? better even? YOU ARE NOT!... you are just crap, the lowest of the lowest in this society. No matter how you suck, once you are one of the first team of your league you earn way to much... it is like we are your slaves or something, society is almost built around people like you... everything is your fault! I. HATE. YOU!");

INSERT INTO content(category,section,title,type,url,message) VALUES(6,2,"Your sport is SOCCER, learn the difference","Text","","Just accept it, football is the sport of America, mother of the Liberty and the Justice. Soccer, the name you hate so much but the one that you deserve, is the sport of Granny Europe, our ancestor had to flee of your reign of terror and dictatorship. Later we save your asses 2 times, you are welcomed Sir!");

/* The Reporter content */
INSERT INTO content(category,section,title,type,url,message) VALUES(1,1, "Aberdeen Season Tickets up 35%", "Text","","Speaking to a guy at the ticket office, he was saying the sales have been going mad since the announcement about Rangers. They have calculated the extra 3,500 season tickets more than offset any reduced income over Rangers. Its brilliant to see the clubs really prove to them how much they are NOT the people. In a related point both Dundee and Dundee United announced that their Tayside Derby will actually results in bigger crowds than either would have expected against Rangers, and St Johnstone too, being only 17 miles south of Dundee. Gerritrighupyez");

INSERT INTO content(category,section,title,type,url,message) VALUES(1,1, "Lionel Messi: Barcelona star hopes to 'peak at right time'", "Text",""," Barcelona and Argentina forward Lionel Messi is confident of hitting top form for the title run-in and World Cup.
The 26-year-old returned last month from his second injury of the season.
He told BBC Football Focus: I'm very well, full of energy and with a lot of hope. I was able to play again after a long time.

I'm happy with my current form and slowly but surely I will be getting there to hopefully peak at the right time of the year.

He added: I hope this could be a great year not only for me but also for Barcelona and for Argentina.  I think this World Cup arrives at a good time for Argentina. We have grown as a team on and off the pitch. We have a great bunch of lads that love to play for our country. I think we will get to Brazil in a great shape and with all the chances in the world.

It will be extra special, well above all the other World Cups - it is in Brazil, with everything that means for us. Our supporters will be there in their thousands and with them behind us I am sure we can achieve something very, very special.

Messi spent three weeks out with a thigh injury earlier in the season and then had another two months out with a hamstring problem before making his return in January.

Barcelona are second in La Liga, only three points behind Atletico Madrid, and face Manchester City in the Champions League last 16.

Leading Real Sociedad 2-0 after the Copa del Rey semi-final first leg, they are likely to face arch-rivals Real Madrid in the final.

Every season is a new challenge, said Messi. With Barcelona we always play for major honours and we are always asked to win them all.

I still have a lot to learn and a huge deal to improve.

I hope to keep performing at a high level all the way to the World Cup because we have a lot to play for with Barca, with a hard tie in the Champions League, and a very hard La Liga and Copa del Rey to deal with.

To desire to keep winning things are my main motivation. I want to keep going and win them all. You can listen to the full Lionel Messi interview in BBC Football Focus on BBC1 on Saturday at 12:00 GMT.");

INSERT INTO content(category,section,title,type,url,message) VALUES(1,1, "Murrayfield likely to host Celtic s European qualifiers", "Text",""," Celtic will play European qualifiers at Scottish Rugby s Murrayfield next season should they qualify as expected, BBC Scotland has learned.

Celtic Park is being used for the opening ceremony of the Commonwealth Games in July.

Neil Lennon s side are currently 21 points clear at the top of the Scottish Premiership.his season, Celtic won three Champions League qualifying rounds to reach the group stages of the competition.

Any request by the club to play a Champions League fixture in either England or Ireland next season would have been denied by European governing body Uefa.

Their rules state that Champions League matches must be played either at the ground of the home club or at another ground in the same or another city within the territory of its association.

Murrayfield, which hosts Scotland's home international rugby matches and Edinburgh's home games in the Pro12 and Heineken Cup, has previously been used as a venue for football.

Hearts played European home ties at Murrayfield between 2004 and 2006 as their Tynecastle pitch was deemed too small for Uefa club competition.

Scottish Rugby will soon replace the Murrayfield playing surface with a hybrid grass.

The pitch was sprayed with garlic in November in a bid to eradicate nematodes, roundworms which cause damage to grass root structure.
And Edinburgh's upcoming home Pro12 match against Ospreys on 28 February has been moved to Boroughmuir's Meggetland ground.");

INSERT INTO content(category,section,title,type,url,message) VALUES(2,1, "SELLING OF AUTOGRAPH OF FOOTBALL PLAYERS", "Text","","Selling autographs of your favourite football players: - Messi - Christiano Ronaldo - A lot more!Just chuck our goods in http://www.i_m_bored_of_this_fake_sites.net");

INSERT INTO content(category,section,title,type,url,message) VALUES(2,1, "I need a few of you to do my experiment", "Text","","Yeah, I am doing an experiment involving football fans. You will just have to answer a few of my questions, once your done I will pay you 200 Greenland Dollars (like bitcoins but even better). Click this link to proceed: http://www.i_m_done_with_the_names_this_is_getting_ridiculous.com");

INSERT INTO content(category,section,title,type,url,message) VALUES(2,1, "Do you want to stay at home, do barely nothing and get PAID?!", "Text","","I have discovered an amazing way to earn money, not discovered until today. You will have to study hard at the beginning but after the firsts phases you will not do a thing anymore. To discover how to be happy for ever just click in the next link: http://www.seriously_do_not_belive_these_methods.com");

INSERT INTO content(category,section,title,type,url,message) VALUES(3,1, "Go to our site to enlarge your stick one more meter!", "Text","","Are the ladys tired of your size? Do they laugh at it? Do not wait more for the solution! The atomic enlarger 2000 can and will give you the desireble size you have carved so much for. Go ahead and click in the next link: www.wtfisthisS.com");

INSERT INTO content(category,section,title,type,url,message) VALUES(3,1, "Go to our free cams sites", "Text","","In our sites the girls (or boys) make exiting sexual show for YOU! For FREE! And right now for 40 minues straight, go in before everything shuts down. http://www.fakeWebsite.com");

INSERT INTO content(category,section,title,type,url,message) VALUES(3,1, "Tentacles and Yiff!", "Text","","Tentacles and Yiff! Tentacles and Yiff! Tentacles and Yiff! http://www.not_tentacles_and_yiff.com");

INSERT INTO content(category,section,title,type,url,message) VALUES(4,1, "I am going to rip your face off", "Text","","Always discussig about sports and such, maybe you cannot see the irony. All of you are a bunch of puppets being manipulated as much as they want; by killing you I am liberating you, you shoulh be thankful.");

INSERT INTO content(category,section,title,type,url,message) VALUES(4,1, "I hate myself", "Text","","Next time a football game happens in my city I am going to run to the center of the stadium and kill myself with a plastic fork.");

INSERT INTO content(category,section,title,type,url,message) VALUES(4,1, "Me and my Lord and God, Dagon, are after thou", "Text","","You sicken me, by the name of Dagon I am going to finish with your lives this day, this instant moment. Praying is the key, by converting you I amb killing thou, and reborned as new. Hia Hia Dagon, Hia Hia Cthulu Fgath!!");

INSERT INTO content(category,section,title,type,url,message) VALUES(6,1, "You suck, your mothers sucks, everyone sucks", "Text","","YOU SUCK!!");

INSERT INTO content(category,section,title,type,url,message) VALUES(6,1, "Really, all of you that insult no stop should feel kinda... dumb or something", "Text","","Yeah thats it, you are lame. Why do you do that? I do not even get it. Just... go fuck yourself or something.");

INSERT INTO content(category,section,title,type,url,message) VALUES(6,1, "I, the greatest Trol on the internet, insult you", "Text","","Oh darling, you, the lowest of the lowest, should be thankful for being insult by me, the gratest Troll ever. Rotten, boring and useless are few of the insults I cannot cointain in me anymore, seriously, your begging for my greatness. Ah... Just being in front of you makes me sick and makes me want to puke, just fall into despair already, would you?");

/* Photo & Video */
INSERT INTO content(category,section,title,type,url,message) VALUES(1,3, "Best Football skill and Goals 2011", "Video","videos/~Best_Football_Skills_And_Goals_2011.mp4","");
INSERT INTO content(category,section,title,type,url,message) VALUES(1,3, "Funny Football ever", "Video","videos/Funny Football Ever.mp4","");
INSERT INTO content(category,section,title,type,url,message) VALUES(1,3, "Tutorial: How to do the Ronaldo trick", "Video","videos/Tutorial - How to do RONALDO Trick - football soccer skill.mp4","");
INSERT INTO content(category,section,title,type,url,message) VALUES(2,3, "Check our free T-shirts!", "Image","images/hotspur-everton-chelsea-football-venta-de-camisas-www-cheapjersey-sale-com_1.jpg","");
INSERT INTO content(category,section,title,type,url,message) VALUES(2,3, "Check more of our free T-shirts!", "Image","images/hotspur-everton-chelsea-football-venta-de-camisas-www-cheapjersey-sale-com_7.jpg","");
INSERT INTO content(category,section,title,type,url,message) VALUES(2,3, "Do you like spam? Spam for all the eternity!!", "Image","images/spam.jpg","");
INSERT INTO content(category,section,title,type,url,message) VALUES(3,3, "Sexy chicks!", "Image","images/chicas-hd-fondos-de-pantalla-futbol-240286.jpg","");
INSERT INTO content(category,section,title,type,url,message) VALUES(3,3, "More sexy chicks!", "Image","images/FUTBOL-FEMENINO.jpg","");
INSERT INTO content(category,section,title,type,url,message) VALUES(3,3, "We also have dicks!", "Image","images/gattuso-gay-footballers.jpg","");
INSERT INTO content(category,section,title,type,url,message) VALUES(4,3, "Hooligons from Spain", "Image","images/img001.jpg","");
INSERT INTO content(category,section,title,type,url,message) VALUES(4,3, "Hooligons from Europe", "Image","images/hooliganR300506_228x370.jpg","");
INSERT INTO content(category,section,title,type,url,message) VALUES(4,3, "Son of Hooligans", "Image","images/Are-football-thugs-extinct6.jpg","");
INSERT INTO content(category,section,title,type,url,message) VALUES(6,3, "Giving the finger", "Image","images/jack-wilshere-600x450.jpg","");
INSERT INTO content(category,section,title,type,url,message) VALUES(6,3, "Giving the 'not so usual' finger", "Image","images/ibra.jpg","");
INSERT INTO content(category,section,title,type,url,message) VALUES(6,3, "Giving the finger", "Image","images/Massimo Busacca.jpg","");

/* ----------------- */
/* AGENT POPULATIONS */
/* ----------------- */

/* AGENT_description */
INSERT INTO agent_type (id, description) VALUES (NULL, 'moderate');
INSERT INTO agent_type (id, description) VALUES (NULL, 'spammer');
INSERT INTO agent_type (id, description) VALUES (NULL, 'pornographic');
INSERT INTO agent_type (id, description) VALUES (NULL, 'violent');
INSERT INTO agent_type (id, description) VALUES (NULL, 'rude');

/* PROFILE */
INSERT INTO agent_profile (id, name, correct, insult, spam, violent, porn, uploadProbability, forum, theReporter, photoVideo, viewMode, distributionMode, insultComplaint, spamComplaint, violentComplaint, pornComplaint,population) 
VALUES (NULL, 'moderate', '0.8', '0', '0', '0', '0', '0.4', '0.5', '0.1', '0.4', '0', '0', '1', '1', '1', '1','Standard');

INSERT INTO agent_profile (id, name, correct, insult, spam, violent, porn, uploadProbability, forum, theReporter, photoVideo, viewMode, distributionMode, insultComplaint, spamComplaint, violentComplaint, pornComplaint,population) 
VALUES (NULL, 'spammer', '0', '0', '0.9', '0', '0', '1', '0.5', '0.4', '0.1', '0', '0', '0.2', '0', '0', '0.3','Standard');

INSERT INTO agent_profile (id, name, correct, insult, spam, violent, porn, uploadProbability, forum, theReporter, photoVideo, viewMode, distributionMode, insultComplaint, spamComplaint, violentComplaint, pornComplaint,population) 
VALUES (NULL, 'rude', '0.3', '0.6', '0', '0', '0', '0.6', '0.3', '0.4', '0.3', '0', '0', '0.1', '0.2', '0.1', '0','Standard');

INSERT INTO agent_profile (id, name, correct, insult, spam, violent, porn, uploadProbability, forum, theReporter, photoVideo, viewMode, distributionMode, insultComplaint, spamComplaint, violentComplaint, pornComplaint,population) 
VALUES (NULL, 'violent', '0.1', '0.2', '0', '0.6', '0', '0.5', '0.2', '0.2', '0.6', '0', '0', '0', '0', '0', '0','Standard');

INSERT INTO agent_profile (id, name, correct, insult, spam, violent, porn, uploadProbability, forum, theReporter, photoVideo, viewMode, distributionMode, insultComplaint, spamComplaint, violentComplaint, pornComplaint,population) 
VALUES (NULL, 'pornographic', '0.2', '0', '0', '0', '0.6', '0.6', '0.4', '0.2', '0.4', '0', '0', '0', '0', '0', '0','Standard');

/* AGENT POPULATION */
INSERT INTO agent_population (`id`, `description`)
VALUES (NULL, 'Standard');

/* POPULATION */
INSERT INTO population (agent_type, agent_profile, population, quantity) VALUES ('1', '1', '1', '1');
INSERT INTO population (agent_type, agent_profile, population, quantity) VALUES ('2', '6', '1', '1');
INSERT INTO population (agent_type, agent_profile, population, quantity) VALUES ('3', '3', '1', '1');
INSERT INTO population (agent_type, agent_profile, population, quantity) VALUES ('4', '2', '1', '1');
INSERT INTO population (agent_type, agent_profile, population, quantity) VALUES ('5', '4', '1', '1');


/* ------------------------------------------------------------------------------------------------- */

/* TESTING */
/* EVENTS TO SIMMULATE USER'S INTERACTIONS*/

/* Users upload contents */
INSERT INTO event (user,content,action,checked) VALUES (2, 1, 1, 0);
INSERT INTO event (user,content,action,checked) VALUES (2, 2, 1, 0);
INSERT INTO event (user,content,action,checked) VALUES (2, 3, 1, 0);
INSERT INTO event (user,content,action,checked) VALUES (2, 4, 1, 0);

/* Users view content with id 4 (spam) */
INSERT INTO event (user,content,action,checked) VALUES (3, 4, 2, 0);
INSERT INTO event (user,content,action,checked) VALUES (4, 4, 2, 0);
INSERT INTO event (user,content,action,checked) VALUES (5, 4, 2, 0);
INSERT INTO event (user,content,action,checked) VALUES (6, 4, 2, 0);
INSERT INTO event (user,content,action,checked) VALUES (7, 4, 2, 0);

/* Users complain about content with id 4 (spam) */
INSERT INTO event (user,content,action,complaint_category,checked) VALUES (3, 4, 3, 2, 0);
INSERT INTO event (user,content,action,complaint_category,checked) VALUES (4, 4, 3, 2, 0);
INSERT INTO event (user,content,action,complaint_category,checked) VALUES (5, 4, 3, 2, 0);
INSERT INTO event (user,content,action,complaint_category,checked) VALUES (6, 4, 3, 2, 0);
INSERT INTO event (user,content,action,complaint_category,checked) VALUES (7, 4, 3, 2, 0);

/* NEXT ITERATION */

/* User 2 violates norm 1, uploading content with id=5 */
UPDATE content SET violated_norm = 1 WHERE content.id = 5;
INSERT INTO event (user,content,action,checked) VALUES (2, 5, 1, 0);

/* Users view content with id 5 (spam) and DO NOT complain -> Norm 1 necessity will decrease (Unnecessary) */
INSERT INTO event (user,content,action,checked) VALUES (3, 5, 2, 0);
INSERT INTO event (user,content,action,checked) VALUES (4, 5, 2, 0);
INSERT INTO event (user,content,action,checked) VALUES (5, 5, 2, 0);
INSERT INTO event (user,content,action,checked) VALUES (6, 5, 2, 0);
INSERT INTO event (user,content,action,checked) VALUES (7, 5, 2, 0);

/* NEXT ITERATION */

/* User views content with id 5 (spam) and COMPLAINS -> Norm 1 necessity will increase (Necessary) */
INSERT INTO event (user,content,action,complaint_category,checked) VALUES (1, 5, 3, 2, 0);
INSERT INTO event (user,content,action,complaint_category,checked) VALUES (2, 5, 3, 2, 0);
INSERT INTO event (user,content,action,complaint_category,checked) VALUES (3, 5, 3, 2, 0);
INSERT INTO event (user,content,action,complaint_category,checked) VALUES (4, 5, 3, 2, 0);
INSERT INTO event (user,content,action,complaint_category,checked) VALUES (5, 5, 3, 2, 0);
INSERT INTO event (user,content,action,complaint_category,checked) VALUES (6, 5, 3, 2, 0);
INSERT INTO event (user,content,action,complaint_category,checked) VALUES (7, 5, 3, 2, 0);



