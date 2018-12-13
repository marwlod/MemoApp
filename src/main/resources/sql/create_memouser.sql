# after running change username from root to memouser,
# password from your root mySQL password to memouser
create user if not exists 'memouser'@'localhost' identified by 'memouser';
grant all privileges on *.* to 'memouser'@'localhost';