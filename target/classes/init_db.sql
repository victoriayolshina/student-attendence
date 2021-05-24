INSERT INTO `auto_user` (`autoUserId`, `email`, `first_name`, `last_name`, `password`, `role`, `username`) VALUES (1, 'admin@gmail.com', 'Ivan', 'Ivanov', 'pass', 'ROLE_ADMIN', 'admin');
INSERT INTO `auto_user` (`autoUserId`, `email`, `first_name`, `last_name`, `password`, `role`, `username`, `group_id`) VALUES (2, 'user@gmail.com', 'Ivan', 'Petrov', 'pass', 'ROLE_USER', 'user2361', 1);
INSERT INTO `auto_user` (`autoUserId`, `email`, `first_name`, `last_name`, `password`, `role`, `username`, `group_id`) VALUES (3, 'user@gmail.com', 'Ivan', 'Ivanov', 'pass', 'ROLE_USER', 'user2362', 2);

-- --------------------------------------------------------

INSERT INTO `groups` (`id`, `name`, `code`, `year`) VALUES (1, 'informatics', 2361, 2017);
INSERT INTO `groups` (`id`, `name`, `code`, `year`) VALUES (2, 'math', 2362, 2017);

INSERT INTO `student` (`id`, `group_id`, `fullname`) VALUES (1, 1, 'Zhizhin Gordey');
INSERT INTO `student` (`id`, `group_id`, `fullname`) VALUES (2, 1, 'Ivanov Ivan');

INSERT INTO `subject` (`id`, `name`) VALUES (1, 'math');
INSERT INTO `subject` (`id`, `name`) VALUES (2, 'programming');
INSERT INTO `subject` (`id`, `name`) VALUES (3, 'english');

INSERT INTO `schedule` (`id`, `day_of_week`, `lesson_number`, `subject_id`, `group_id`) VALUES (1, 1, 1, 1, 1);
INSERT INTO `schedule` (`id`, `day_of_week`, `lesson_number`, `subject_id`, `group_id`) VALUES (2, 1, 2, 2, 1);
INSERT INTO `schedule` (`id`, `day_of_week`, `lesson_number`, `subject_id`, `group_id`) VALUES (3, 1, 3, 3, 1);
INSERT INTO `schedule` (`id`, `day_of_week`, `lesson_number`, `subject_id`, `group_id`) VALUES (4, 3, 3, 3, 1);
INSERT INTO `schedule` (`id`, `day_of_week`, `lesson_number`, `subject_id`, `group_id`) VALUES (5, 3, 2, 1, 1);

/*INSERT INTO `lesson` (`id`, `subject_id`, `datetime`, `group_id`) VALUES (1, 1, '2020-05-26 08:30:00', 1);
INSERT INTO `lesson` (`id`, `subject_id`, `datetime`, `group_id`) VALUES (2, 2, '2020-05-25 10:10:00', 1);*/