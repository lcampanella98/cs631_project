USE lrc22;
LOAD DATA LOCAL INFILE '/afs/cad/u/m/b/mb529/mysql/scripts/1_BANK_ACCOUNT.csv'
INTO TABLE BankAccount
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;

LOAD DATA LOCAL INFILE '/afs/cad/u/m/b/mb529/mysql/scripts/2_USER_ACCOUNT.csv'
INTO TABLE UserAccount
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;

LOAD DATA LOCAL INFILE '/afs/cad/u/m/b/mb529/mysql/scripts/3_ELECTRONIC_AD.csv'
INTO TABLE ElectronicAddress
FIELDS TERMINATED BY '\r'
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;

LOAD DATA LOCAL INFILE '/afs/cad/u/m/b/mb529/mysql/scripts/4_EMAIL_AD.csv'
INTO TABLE EmailAddress
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;

LOAD DATA LOCAL INFILE '/afs/cad/u/m/b/mb529/mysql/scripts/PHONE.csv'
INTO TABLE Phone
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;
