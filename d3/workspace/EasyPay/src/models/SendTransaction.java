package models;

import java.sql.Timestamp;

public class SendTransaction {
	public int STID;
	public int Amount;
	public Timestamp DateInitialized;
	public String Memo;
	public boolean Cancelled;
	public String ISSN;
	public String ToIdentifier;
	public boolean IsToNewUser;
	public String ToNewUserIdentifier;
	public UserAccount FromUser;
	public UserAccount ToUser;
}
