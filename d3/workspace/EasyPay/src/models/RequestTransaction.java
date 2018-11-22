package models;

import java.sql.Timestamp;
import java.util.List;

public class RequestTransaction {
	public int RTID;
	public int TotalAmount;
	public Timestamp DateInitialized;
	public String Memo;
	public String ISSN;
	public UserAccount IUser;
	public List<RequestFrom> From;
}
