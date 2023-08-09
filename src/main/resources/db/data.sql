insert into user_tb(username, password, email)values('ssar','$2a$10$KLgOuIRnybDAPMGe9cHpg.xyuLyEbp1thFb5c4AgWkoqLixY1pBRu' , 'ssar@nate.com');
insert into user_tb(username, password, email) values('cos','$2a$10$KLgOuIRnybDAPMGe9cHpg.xyuLyEbp1thFb5c4AgWkoqLixY1pBRu', 'cos@nate.com');

insert into board_tb(title, content, user_id, created_at) values('제목1', '내용1', 1, now());
insert into board_tb(title, content, user_id, created_at) values('제목2', '내용2', 1, now());
insert into board_tb(title, content, user_id, created_at) values('제목3', '내용3', 1, now());
insert into board_tb(title, content, user_id, created_at) values('제목4', '내용4', 2, now());
insert into board_tb(title, content, user_id, created_at) values('제목5', '내용5', 2, now());

insert into reply_tb(board_id, comment, user_id) values(1, '반갑습니당!',1);
insert into reply_tb(board_id, comment, user_id) values(1, '안녕!',1);