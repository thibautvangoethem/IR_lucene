SET GLOBAL local_infile = 1;
SHOW VARIABLES LIKE 'local_infile';

LOAD XML LOCAL INFILE '/Users/michiel.teblick/git/IR_lucene/Posts.xml'
INTO TABLE documents ROWS IDENTIFIED BY '<row>';