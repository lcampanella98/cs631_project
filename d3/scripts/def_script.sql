use easypay;
CREATE TABLE BankAccount (
	BankID INT NOT NULL,
    BANumber INT NOT NULL,
    Verified BIT NOT NULL DEFAULT 0,
    PRIMARY KEY (BankID, BANumber)
);

CREATE TABLE UserAccount (
	SSN INT PRIMARY KEY NOT NULL,
    Name VARCHAR(100) NOT NULL,
    Balance INT NOT NULL DEFAULT 0,
    PBankID INT NOT NULL,
    PBANumber INT NOT NULL,
    FOREIGN KEY fk_user_primary_bank_account(PBankID, PBANumber)
		REFERENCES BankAccount(BankID, BANumber)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE Has_Additional (
	UBankID INT NOT NULL,
    UBANumber INT NOT NULL,
    USSN INT NOT NULL,
    PRIMARY KEY (UBankID, UBANumber, USSN),
    FOREIGN KEY fk_add_bank_account(UBankID, UBANumber)
		REFERENCES BankAccount(BankID, BANumber)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
	FOREIGN KEY fk_add_user_ssn(USSN)
		REFERENCES UserAccount(SSN)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE ElectronicAddress (
	Identifier VARCHAR(100) PRIMARY KEY NOT NULL
);

CREATE TABLE EmailAddress (
	Identifier VARCHAR(100) PRIMARY KEY NOT NULL,
    Verified BIT NOT NULL DEFAULT 0,
    USSN INT NOT NULL,
    FOREIGN KEY fk_email_identifier(Identifier)
		REFERENCES ElectronicAddress(Identifier)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
	FOREIGN KEY fk_email_user_ssn(USSN)
		REFERENCES UserAccount(SSN)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE Phone (
	Identifier VARCHAR(100) PRIMARY KEY NOT NULL,
    Verified BIT NOT NULL DEFAULT 0,
    USSN INT NOT NULL,
    FOREIGN KEY fk_phone_identifier(Identifier)
		REFERENCES ElectronicAddress(Identifier)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
	FOREIGN KEY fk_phone_user_ssn(USSN)
		REFERENCES UserAccount(SSN)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE SendTransaction (
	STID INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
	Amount INT NOT NULL,
	DateInitialized DATETIME NOT NULL,
	Memo VARCHAR(500),
	Cancelled BIT NOT NULL DEFAULT 0,
	ISSN INT NOT NULL,
	ToIdentifier VARCHAR(100) NOT NULL,
	FOREIGN KEY fk_send_initiator_ssn(ISSN)
		REFERENCES UserAccount(SSN)
		ON UPDATE CASCADE
		ON DELETE CASCADE,
	FOREIGN KEY fk_send_to_identifier(ToIdentifier)
		REFERENCES ElectronicAddress(Identifier)
		ON UPDATE CASCADE
		ON DELETE CASCADE
);


CREATE TABLE RequestTransaction (
	RTID INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    Amount INT NOT NULL,
    DateInitialized DATETIME NOT NULL,
    Memo VARCHAR(500),
    ISSN INT NOT NULL,
    FOREIGN KEY fk_request_initiator_ssn(ISSN)
		REFERENCES UserAccount(SSN)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE RequestFrom (
	RTID INT NOT NULL,
    EIdentifier VARCHAR(100) NOT NULL,
    PRIMARY KEY (RTID, EIdentifier),
    FOREIGN KEY fk_request_id(RTID)
		REFERENCES RequestTransaction(RTID)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
	FOREIGN KEY fk_request_to_identifier(EIdentifier)
		REFERENCES ElectronicAddress(Identifier)
		ON UPDATE CASCADE
        ON DELETE CASCADE
);

