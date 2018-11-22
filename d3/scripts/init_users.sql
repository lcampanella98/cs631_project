START TRANSACTION;

INSERT INTO BankAccount (BankID,BANumber,Verified)
VALUES (2,4444,1);

INSERT INTO BankAccount (BankID,BANumber,Verified)
VALUES (2,4445,1);

INSERT INTO BankAccount (BankID,BANumber,Verified)
VALUES (3,4444,1);

INSERT INTO BankAccount (BankID,BANumber,Verified)
VALUES (5, 66,1);

INSERT INTO UserAccount (SSN,Name,Balance,PBankID,PBANumber)
VALUES ('987654321', 'Darth Vader', 0, 5, 66);


INSERT INTO UserAccount (SSN,Name,Balance,PBankID,PBANumber)
VALUES ('123456789', 'Guy Higgs', 0, 2, 4444);
INSERT INTO ElectronicAddress (Identifier)
VALUES ('111-222-3333');
INSERT INTO ElectronicAddress (Identifier)
VALUES ('guyhiggs@fake.email');
INSERT INTO Phone (Identifier,Verified,USSN)
VALUES ('111-222-3333', 1, '123456789');
INSERT INTO EmailAddress (Identifier,Verified,USSN)
VALUES ('guyhiggs@fake.email',1,'123456789');
INSERT INTO Has_Additional(UBankID,UBANumber,USSN)
VALUES (3, 4444, '123456789');

COMMIT;

