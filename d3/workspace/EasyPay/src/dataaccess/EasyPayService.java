package dataaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import config.EPConstants;
import models.BankAccount;
import models.MonthYear;
import models.RequestFrom;
import models.RequestTransaction;
import models.SendTransaction;
import models.TransactionStatement;
import models.UserAccount;
import models.electronicaddress.ElectronicAddress;
import models.electronicaddress.EmailAddress;
import models.electronicaddress.Phone;
import tools.Methods;

public class EasyPayService {
	private static final String DEV_CON_STR = "jdbc:mysql://localhost/lrc22", 
								DEV_UNAME = "root", DEV_PASS = "password";
	
	private static final String CON_STR = "jdbc:mysql://sql1.njit.edu/lrc22",
								UNAME = "lrc22", PASS = "I4ZEwI8A3";
	
	private static final boolean IS_DEV = false;
								
	private Connection con;
	
	public EasyPayService() {
		con = getDBConnection();
	}
	
	private static Connection getDBConnection() {
		try {
			String con_str, username, password;
			if (IS_DEV) {
				con_str = DEV_CON_STR;
				username = DEV_UNAME;
				password = DEV_PASS;
			} else {
				con_str = CON_STR;
				username = UNAME;
				password = PASS;
			}
			Connection con = DriverManager.getConnection(con_str, username, password);
			con.setAutoCommit(false);
			return con;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void closeConnection() {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public UserAccount getUserAccountFromSSN(String ssn) {
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM UserAccount WHERE SSN=?");
			ps.setString(1, ssn);
			ResultSet r = ps.executeQuery();
			
			UserAccount u = null;
			if (r.first()) u = getUserAccountFromResultSetRow(r);
			
			r.close();
			con.commit();
			
			return u;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public UserAccount getUserAccountFromName(String name) {
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM UserAccount WHERE Name=?");
			ps.setString(1, name);
			ResultSet r = ps.executeQuery();
			
			UserAccount u = null;
			if (r.first()) u = getUserAccountFromResultSetRow(r);
			
			r.close();
			con.commit();
			
			return u;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public UserAccount getUserAccountFromElectronicAddress(String ea) {
		ElectronicAddress eaObj = new ElectronicAddress();
		eaObj.Identifier = ea;
		return getUserAccountFromElectronicAddress(eaObj);
	}
	
	public UserAccount getUserAccountFromElectronicAddress(ElectronicAddress ea) {
		try {
			PreparedStatement ps = con.prepareStatement(
					  "SELECT u.* FROM UserAccount u"
					+ " INNER JOIN EmailAddress ea ON u.SSN=ea.USSN"
					+ " WHERE ea.Identifier=?"
					+ " UNION ALL"
					+ " SELECT u.* FROM UserAccount u"
					+ " INNER JOIN Phone p ON u.SSN=p.USSN"
					+ " WHERE p.Identifier=?");
			ps.setString(1, ea.Identifier);
			ps.setString(2, ea.Identifier);
			ResultSet r = ps.executeQuery();

			UserAccount u = null;
			if (r.first()) u = getUserAccountFromResultSetRow(r);
			
			r.close();
			con.commit();
			
			return u;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private List<UserAccount> getUserAccountsFromResultSet(ResultSet r) {
		return getUserAccountsFromResultSet(r, null);
	}
	
	private List<UserAccount> getUserAccountsFromResultSet(ResultSet r, String alias) {
		try {
			List<UserAccount> ua = new ArrayList<>();

			while (r.next()) {
				ua.add(getUserAccountFromResultSetRow(r, alias));
			}
			
			return ua;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private UserAccount getUserAccountFromResultSetRow(ResultSet r) {
		return getUserAccountFromResultSetRow(r, null);
	}
	
	private UserAccount getUserAccountFromResultSetRow(ResultSet r, String alias) {
		try {
			String prepend = (alias != null && !alias.isEmpty()) ?  alias + "." : "";
			UserAccount u = new UserAccount();
			u.SSN = r.getString(prepend + "SSN");
			u.Name = r.getString(prepend + "Name");
			u.Balance = r.getInt(prepend + "Balance");
			u.primaryAccount = new BankAccount();
			u.primaryAccount.BankID = r.getInt(prepend + "PBankID");
			u.primaryAccount.BANumber = r.getInt(prepend + "PBANumber");
			u.primaryAccount.Verified = true;
			return u;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<BankAccount> getOtherUserBankAccounts(String ssn) {
		try {
			PreparedStatement ps = con.prepareStatement(
					"SELECT BankID,BANumber,Verified \n"
					+ "FROM Has_Additional,BankAccount "
					+ "WHERE UBankID=BankID AND UBANumber=BANumber AND USSN=?");
			ps.setString(1, ssn);
			ResultSet r = ps.executeQuery();
			List<BankAccount> l = new ArrayList<BankAccount>();
			while (r.next()) {
				BankAccount ba = new BankAccount();
				ba.BankID = r.getInt("BankID");
				ba.BANumber = r.getInt("BANumber");
				ba.Verified = r.getBoolean("Verified");
				l.add(ba);
			}
			r.close();
			con.commit();
			
			return l;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<ElectronicAddress> getUserElectronicAddresses(String ssn) {
		List<EmailAddress> emails = getUserEmailAddresses(ssn);
		List<Phone> phones = getUserPhoneNumbers(ssn);
		List<ElectronicAddress> ea = new ArrayList<ElectronicAddress>();
		ea.addAll(emails);
		ea.addAll(phones);
		return ea;
	}
	
	public List<EmailAddress> getUserEmailAddresses(String ssn) {
		try {
			PreparedStatement ps = con.prepareStatement(
					"SELECT * FROM EmailAddress\n"
					+ "	WHERE USSN=?"
			);
			ps.setString(1, ssn);
			ResultSet r = ps.executeQuery();
			EmailAddress ea;
			List<EmailAddress> l = new ArrayList<EmailAddress>();
			while (r.next()) {
				ea = new EmailAddress();
				ea.Identifier = r.getString("Identifier");
				ea.Verified = r.getBoolean("Verified");
				ea.USSN = r.getString("USSN");
				l.add(ea);
			}
			r.close();
			con.commit();
			
			return l;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Phone> getUserPhoneNumbers(String ssn) {
		try {
			PreparedStatement ps = con.prepareStatement(
					"SELECT * FROM Phone\n"
					+ "	WHERE USSN=?"
			);
			ps.setString(1, ssn);
			ResultSet r = ps.executeQuery();
			Phone p;
			List<Phone> l = new ArrayList<Phone>();
			while (r.next()) {
				p = new Phone();
				p.Identifier = r.getString("Identifier");
				p.Verified = r.getBoolean("Verified");
				p.USSN = r.getString("USSN");
				l.add(p);
			}
			r.close();
			con.commit();
			
			return l;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean electronicAddressExists(ElectronicAddress ea) {
		try {
			PreparedStatement ps = con.prepareStatement(
					"SELECT EXISTS(\n"
							+ "SELECT * FROM ElectronicAddress WHERE Identifier=?\n"
					+ ");");
			ps.setString(1, ea.Identifier);
			ResultSet r = ps.executeQuery();
			boolean exists = r.first() && r.getBoolean(1);
			
			r.close();
			con.commit();
			
			return exists;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public String getUserSSNFromElectronicAddress(ElectronicAddress ea) {
		try {
			PreparedStatement ps = con.prepareStatement(
					"SELECT USSN FROM EmailAddress WHERE Identifier=?\n"
					+ "UNION ALL\n"
					+ "SELECT USSN FROM Phone WHERE Identifier=?\n");
			ps.setString(1, ea.Identifier);
			ps.setString(2, ea.Identifier);
			ResultSet r = ps.executeQuery();
			String ssn = null;
			if (r.first()) ssn = r.getString(1);
			
			r.close();
			con.commit();
			
			return ssn;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void deleteElectronicAddress(String identifier) {
		try {
			PreparedStatement ps = con.prepareStatement("DELETE FROM ElectronicAddress WHERE Identifier=?");
			ps.setString(1, identifier);
			ps.executeUpdate();
			
			ps = con.prepareStatement("DELETE FROM EmailAddress WHERE Identifier=?");
			ps.setString(1, identifier);
			ps.executeUpdate();
			
			ps = con.prepareStatement("DELETE FROM Phone WHERE Identifier=?");
			ps.setString(1, identifier);
			ps.executeUpdate();
			
			con.commit();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addElectronicAddress(ElectronicAddress ea) {
		try {
			String table = null;
			if 		(ea instanceof EmailAddress) table = "EmailAddress";
			else if (ea instanceof Phone)		 table = "Phone";
			
			if (table == null) return;
			
			PreparedStatement ps = con.prepareStatement("INSERT INTO ElectronicAddress (Identifier) VALUES (?)");
			ps.setString(1, ea.Identifier);
			ps.executeUpdate();
			
			ps = con.prepareStatement("INSERT INTO " + table + " (Identifier,Verified,USSN) VALUES (?,?,?)");
			if (table.equals("EmailAddress")) {
				EmailAddress email = (EmailAddress)ea;
				ps.setString(1, email.Identifier);
				ps.setBoolean(2, email.Verified);
				ps.setString(3, email.USSN);
			}
			else if (table.equals("Phone")) {
				Phone p = (Phone)ea;
				ps.setString(1, p.Identifier);
				ps.setBoolean(2, p.Verified);
				ps.setString(3, p.USSN);
			}
			
			ps.executeUpdate();
			con.commit();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addBankAccountToUser(BankAccount acct, String ssn) {
		try {
			if (!bankAccountExists(acct)) {
				addBankAccount(acct);
			}
			
			PreparedStatement ps = con.prepareStatement("INSERT INTO Has_Additional (UBankID,UBANumber,USSN) VALUES (?,?,?)");
			ps.setInt(1, acct.BankID);
			ps.setInt(2, acct.BANumber);
			ps.setString(3, ssn);
			ps.executeUpdate();
			
			con.commit();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addBankAccount(BankAccount acct) {
		try {
			PreparedStatement ps = con.prepareStatement("INSERT INTO BankAccount (BankID,BANumber) VALUES (?,?)");
			ps.setInt(1, acct.BankID);
			ps.setInt(2, acct.BANumber);
			ps.executeUpdate();
			
			con.commit();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteBankAccountFromUser(BankAccount acct, String ssn) {
		try {
			PreparedStatement ps = con.prepareStatement("DELETE FROM Has_Additional WHERE UBankID=? AND UBANumber=? AND USSN=?");
			ps.setInt(1,  acct.BankID);
			ps.setInt(2, acct.BANumber);
			ps.setString(3, ssn);
			ps.executeUpdate();
			
			con.commit();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean bankAccountExists(BankAccount acct) {
		try {
			PreparedStatement ps = con.prepareStatement("SELECT EXISTS (\n"
					+ "SELECT * FROM BankAccount WHERE BankID=? AND BANumber=?\n"
					+ ")");
			ps.setInt(1, acct.BankID);
			ps.setInt(2, acct.BANumber);
			ResultSet r = ps.executeQuery();
			boolean exists = r.first() && r.getBoolean(1);
			
			r.close();
			con.commit();
			
			return exists;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean userHasBankAccount(BankAccount acct, String ssn) {
		return isAdditionalBankAccount(acct, ssn) || isPrimaryBankAccount(acct, ssn);
	}
	
	public boolean isPrimaryBankAccount(BankAccount acct, String ssn) {
		try {
			PreparedStatement ps = con.prepareStatement(
					"SELECT EXISTS (\n"
							+ "SELECT * FROM UserAccount\n"
							+ "	WHERE SSN=? AND PBankID=? AND PBANumber=?\n"
					+ ")");
			ps.setString(1, ssn);
			ps.setInt(2, acct.BankID);
			ps.setInt(3, acct.BANumber);
			ResultSet r = ps.executeQuery();
			boolean isPrimary = r.first() && r.getBoolean(1);
			
			r.close();
			con.commit();
			
			return isPrimary;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean isAdditionalBankAccount(BankAccount acct, String ssn) {
		try {
			PreparedStatement ps = con.prepareStatement(
					"SELECT EXISTS (\n"
							+ "SELECT * FROM Has_Additional\n"
							+ "	WHERE UBankID=? AND UBANumber=? AND USSN=?\n"
					+ ")");
			ps.setInt(1, acct.BankID);
			ps.setInt(2, acct.BANumber);
			ps.setString(3, ssn);
			
			ResultSet r = ps.executeQuery();
			boolean isAdditional = r.first() && r.getBoolean(1);
			
			r.close();
			con.commit();
			
			return isAdditional;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void setPrimaryBankAccount(BankAccount acct, String ssn) {
		try {
			PreparedStatement ps = con.prepareStatement("DELETE FROM Has_Additional WHERE UBankID=? AND UBANumber=? AND USSN=?");
			ps.setInt(1, acct.BankID);
			ps.setInt(2, acct.BANumber);
			ps.setString(3, ssn);
			ps.executeUpdate();
			
			ps = con.prepareStatement(
					"INSERT INTO Has_Additional (UBankID,UBANumber,USSN)\n"
					+ "SELECT PBankID,PBANumber,SSN FROM UserAccount WHERE SSN=?");
			ps.setString(1, ssn);
			ps.executeUpdate();
			
			ps = con.prepareStatement(
					"UPDATE UserAccount\n"
					+ "SET PBankID=?, PBANumber=?\n"
					+ "WHERE SSN=?");
			ps.setInt(1, acct.BankID);
			ps.setInt(2, acct.BANumber);
			ps.setString(3, ssn);
			ps.executeUpdate();
			
			con.commit();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/* Payment methods */
	public List<SendTransaction> getSendTransactionsAvailableToCancel(String fromSSN) {
		try {
			PreparedStatement ps = con.prepareStatement(
					"SELECT * FROM SendTransaction \n"
					+ "WHERE ISSN=? \n"
					+ "AND Cancelled=0 "
					+ "AND TIMESTAMPDIFF(MINUTE,DateInitialized,NOW()) < " + EPConstants.CANCELLATION_WINDOW_MINUTES);
			ps.setString(1, fromSSN);
			ResultSet r = ps.executeQuery();
			List<SendTransaction> l = new ArrayList<SendTransaction>();
			SendTransaction st;
			while (r.next()) {
				st = new SendTransaction();
				st.Amount = r.getInt("Amount");
				st.Cancelled = r.getBoolean("Cancelled");
				st.DateInitialized = r.getTimestamp("DateInitialized");
				st.ISSN = r.getString("ISSN");
				st.Memo = r.getString("Memo");
				st.STID = r.getInt("STID");
				st.ToIdentifier = r.getString("ToIdentifier");
				l.add(st);
			}
			r.close();
			con.commit();
			return l;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// returns error if payment cannot be made
	public String sendPayment(SendTransaction st) {
		// does user have the moolah?
		if (st.Amount > getUserBalance(st.ISSN)) {
			return "Insufficient funds";
		}
		
		// is single payment too big?
		if (st.Amount > EPConstants.SINGLE_SEND_LIMIT_VERIFIED) {
			return "Single send limit of " + Methods.formatMoney(EPConstants.SINGLE_SEND_LIMIT_VERIFIED) + " exceeded";
		}
		
		// did user hit weekly limit?
		if (userHitSendLimit(st)) {
			return "Weekly transfer limit of " + Methods.formatMoney(EPConstants.WEEKLY_TRANSFER_LIMIT_VERIFIED) + " exceeded";
		}
		
		try {
			PreparedStatement ps = con.prepareStatement(
					"INSERT INTO SendTransaction (Amount,DateInitialized,Memo,Cancelled,ISSN,ToIdentifier)\n"
					+ "VALUES (?,?,?,?,?,?)");
			ps.setInt(1, st.Amount);
			ps.setTimestamp(2, new Timestamp(new java.util.Date().getTime()));
			ps.setString(3, st.Memo);
			ps.setBoolean(4, false);
			ps.setString(5, st.ISSN);
			ps.setString(6, st.ToIdentifier);
			ps.executeUpdate();
			
			String toSSN = getUserSSNFromElectronicAddress(new ElectronicAddress(st.ToIdentifier));
			if (toSSN != null) {
				ps = con.prepareStatement(
						"UPDATE UserAccount\n"
						+ "SET Balance=Balance+?\n"
						+ "WHERE SSN=?");
				ps.setInt(1, st.Amount);
				ps.setString(2, toSSN);
				ps.executeUpdate();
				
				ps = con.prepareStatement(
						"UPDATE UserAccount"
						+ " SET Balance=Balance-?"
						+ " WHERE SSN=?"
				);
				ps.setInt(1, st.Amount);
				ps.setString(2, st.ISSN);
				ps.executeUpdate();
			}
			
			con.commit();
			
			return null; // no error
		}
		catch (SQLException e) {
			e.printStackTrace();
			return "An exception occurred";
		}
	}
	
	public int getUserBalance(String ssn) {
		try {
			PreparedStatement ps = con.prepareStatement("SELECT Balance FROM UserAccount WHERE SSN=?");
			ps.setString(1, ssn);
			ResultSet r = ps.executeQuery();
			int balance = r.first() ? r.getInt("Balance") : 0;
			r.close();
			con.commit();
			
			return balance;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public boolean canCancelSendPayment(int STID) {
		try {
			PreparedStatement ps = con.prepareStatement("SELECT EXISTS (\n"
					+ "SELECT * FROM SendTransaction\n"
					+ "WHERE STID=?\n"
					+ "AND TIMESTAMPDIFF(MINUTE,DateInitialized,NOW()) < " + EPConstants.CANCELLATION_WINDOW_MINUTES + "\n"
					+ ")");
			ps.setInt(1, STID);
			
			ResultSet r = ps.executeQuery();
			boolean exists = r.first() && r.getBoolean(1);
			
			r.close();
			con.commit();
			
			return exists;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public SendTransaction getSendTransaction(int STID) {
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM SendTransaction WHERE STID=?");
			ps.setInt(1, STID);
			
			ResultSet r = ps.executeQuery();
			SendTransaction st = null;
			if (r.first()) {
				st = new SendTransaction();
				st.Amount = r.getInt("Amount");
				st.Cancelled = r.getBoolean("Cancelled");
				st.DateInitialized = r.getTimestamp("DateInitialized");
				st.ISSN = r.getString("ISSN");
				st.ToIdentifier = r.getString("ToIdentifier");
			}
			r.close();
			con.commit();
			
			return st;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void cancelSendPayment(int STID) {
		try {
			SendTransaction st = getSendTransaction(STID);
			
			// remove funds from recipient
			PreparedStatement ps = con.prepareStatement("UPDATE UserAccount SET Balance=Balance-("
					+ "SELECT Amount FROM SendTransaction WHERE STID=?)"
					+ " WHERE SSN=?");
			ps.setInt(1, STID);
			ps.setString(2, getUserSSNFromElectronicAddress(new ElectronicAddress(st.ToIdentifier)));
			ps.executeUpdate();
			
			// add funds back to sender
			ps = con.prepareStatement("UPDATE UserAccount SET Balance=Balance+("
					+ "SELECT Amount FROM SendTransaction WHERE STID=?)"
					+ " WHERE SSN=?");
			ps.setInt(1, STID);
			ps.setString(2, st.ISSN);
			ps.executeUpdate();
			
			ps = con.prepareStatement("UPDATE SendTransaction SET Cancelled=1 WHERE STID=?");
			ps.setInt(1, STID);
			ps.executeUpdate();
			
			
			con.commit();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean userHitSendLimit(SendTransaction st) {
		try {
			PreparedStatement ps = con.prepareStatement(
					"SELECT SUM(Amount) \n"
					+ "FROM SendTransaction \n"
					+ "WHERE ISSN=? AND TIMESTAMPDIFF(DAY,DateInitialized,NOW()) < 7 AND Cancelled=0");
			ps.setString(1, st.ISSN);
			ResultSet r = ps.executeQuery();
			r.first();
			int amountSentThisWeek = r.getInt(1);
			r.close();
			con.commit();
			
			if (amountSentThisWeek + st.Amount > EPConstants.WEEKLY_TRANSFER_LIMIT_VERIFIED) {
				return true;
			}
			return false;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/* Request Payment */
	
	// finds the requests for money that have been requested of this user
	public List<RequestTransaction> getRequestTransactionsRequestedOfUser(String ssn) {
		try {
			PreparedStatement ps = con.prepareStatement(
					"SELECT * FROM RequestFrom NATURAL JOIN RequestTransaction INNER JOIN UserAccount ON ISSN=SSN \n"
					+ " WHERE EIdentifier IN ("
					+ "		SELECT Identifier FROM EmailAddress WHERE USSN=?\n"
					+ "		UNION\n"
					+ "		SELECT Identifier FROM Phone WHERE USSN=?"
					+ "	)"
					+ " ORDER BY RTID DESC");
			ps.setString(1, ssn);
			ps.setString(2, ssn);
			ResultSet r = ps.executeQuery();
			List<RequestTransaction> l = new ArrayList<RequestTransaction>();
			RequestTransaction rt;
			RequestFrom rf;
			while (r.next()) {
				rt = new RequestTransaction();
				rt.TotalAmount = r.getInt("TotalAmount");
				rt.DateInitialized = r.getTimestamp("DateInitialized");
				rt.ISSN = r.getString("ISSN");
				rt.Memo = r.getString("Memo");
				rt.RTID = r.getInt("RTID");
				
				rt.IUser = getUserAccountFromResultSetRow(r);
				
				rt.From = new ArrayList<RequestFrom>();
					rf = new RequestFrom();
					rf.EIdentifier = r.getString("EIdentifier");
					rf.User = getUserAccountFromElectronicAddress(rf.EIdentifier);
					rt.From.add(rf);
				
				l.add(rt);
			}
			r.close();
			con.commit();
			
			return l;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public RequestTransaction getRequestTransaction(int RTID, ElectronicAddress ea) {
		try {
			PreparedStatement ps = con.prepareStatement(
					"SELECT * FROM RequestFrom NATURAL JOIN RequestTransaction \n"
							+ " WHERE RTID=?"
							+ " AND EIdentifier=?");
			ps.setInt(1, RTID);
			ps.setString(2, ea.Identifier);
			
			ResultSet r = ps.executeQuery();
			RequestTransaction rt = null;
			if (r.first()) {
				rt = new RequestTransaction();
				rt.TotalAmount = r.getInt("TotalAmount");
				rt.DateInitialized = r.getTimestamp("DateInitialized");
				rt.ISSN = r.getString("ISSN");
				rt.Memo = r.getString("Memo");
				rt.RTID = r.getInt("RTID");
				
				rt.From = new ArrayList<RequestFrom>();
				RequestFrom rf = new RequestFrom();
					rf.EIdentifier = r.getString("EIdentifier");
					rf.RTID = r.getInt("RTID");
					rf.Amount = r.getInt("Amount");
				rt.From.add(rf);
				
			}
			
			r.close();
			con.commit();
			
			return rt;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void deleteRequestTransaction(int RTID, ElectronicAddress ea) {
		try {
			PreparedStatement ps = con.prepareStatement("DELETE FROM RequestFrom WHERE RTID=? AND EIdentifier=?");
			ps.setInt(1, RTID);
			ps.setString(2, ea.Identifier);
			
			ps.executeUpdate();
			con.commit();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String acceptRequestTransaction(int RTID, ElectronicAddress accepter) {
		RequestTransaction rt = getRequestTransaction(RTID, accepter);
		String accepterSSN = getUserSSNFromElectronicAddress(accepter);
		
		SendTransaction st = new SendTransaction();
		st.Amount = rt.From.get(0).Amount;
		st.Cancelled = false;
		st.ISSN = accepterSSN;
		List<ElectronicAddress> initiatorAddresses = getUserElectronicAddresses(rt.ISSN);
		if (initiatorAddresses.isEmpty()) {
			return "This user has no email or phone number";
		}
		st.ToIdentifier = initiatorAddresses.get(0).Identifier; // assume requester has at least one electronic address
		st.Memo = "Accept Request" + (rt.Memo != null && !rt.Memo.isEmpty() ? ": " + rt.Memo : "");
		
		String err = sendPayment(st);
		
		if (err == null) {
			deleteRequestTransaction(RTID, accepter);
		}
		
		return err;
	}
	
	public void newRequestTransaction(RequestTransaction rt) {
		try {
			PreparedStatement ps = con.prepareStatement(
					"INSERT INTO RequestTransaction (TotalAmount,DateInitialized,Memo,ISSN)"
					+ " VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS
			);
			ps.setInt(1, rt.TotalAmount);
			ps.setTimestamp(2, new Timestamp(new Date().getTime()));
			ps.setString(3, rt.Memo);
			ps.setString(4, rt.ISSN);
			
			ps.executeUpdate();
			ResultSet r = ps.getGeneratedKeys();
			if (r.first()) {
				int RTID = r.getInt(1);
				r.close();
				
				ps = con.prepareStatement("INSERT INTO RequestFrom (RTID, EIdentifier, Amount) VALUES (?,?,?)");
				ps.setInt(1, RTID);
				for (RequestFrom recipient : rt.From) {
					ps.setString(2, recipient.EIdentifier);
					ps.setInt(3, recipient.Amount);
					ps.executeUpdate();
				}				
			}
			
			con.commit();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/* Statements */
	public SortedMap<MonthYear,TransactionStatement> getMonthlyStatement(String ssn) {
		try {
			SortedMap<MonthYear, TransactionStatement> stmt = new TreeMap<>();
			
			PreparedStatement ps = con.prepareStatement(
					"  SELECT MONTH(DateInitialized) as Month"
					+ "    , YEAR(DateInitialized) as Year"
					+ "    , SUM(Amount) as AmountSent"
					+ "    , COUNT(*) as NumTransactionsSent"
					+ " FROM SendTransaction"
					+ " WHERE ISSN=?"
					+ " AND Cancelled=0"
					+ " GROUP BY MONTH(DateInitialized), YEAR(DateInitialized)"
			);
			ps.setString(1, ssn);
			ResultSet r = ps.executeQuery();
			
			TransactionStatement ts;
			while (r.next()) {
				ts = new TransactionStatement();
				ts.date = new MonthYear();
					ts.date.month = r.getInt("Month");
					ts.date.year = r.getInt("Year");
				ts.amountSent = r.getInt("AmountSent");
				ts.numTransactionsSent = r.getInt("NumTransactionsSent");
				stmt.put(ts.date, ts);
			}
			r.close();
			
			ps = con.prepareStatement(
					"  SELECT MONTH(DateInitialized) as Month"
					+ "     , YEAR(DateInitialized) as Year"
					+ "     , SUM(Amount) as AmountCancelledSending"
					+ "     , COUNT(*) as NumTransactionsCancelledSending"
					+ "  FROM SendTransaction"
					+ "  WHERE ISSN=? "
					+ "  AND Cancelled=1 "
					+ "  GROUP BY MONTH(DateInitialized), YEAR(DateInitialized) "
			);
			ps.setString(1, ssn);
			r = ps.executeQuery();
			
			MonthYear date;
			while (r.next()) {
				date = new MonthYear(r.getInt("Month"), r.getInt("Year"));
				if (stmt.containsKey(date)) {
					ts = stmt.get(date);
					ts.amountCancelledSending = r.getInt("AmountCancelledSending");
					ts.numTransactionsCancelledSending = r.getInt("NumTransactionsCancelledSending");
				} else {
					ts = new TransactionStatement();
					ts.date = date;
					ts.amountCancelledSending = r.getInt("AmountCancelledSending");
					ts.numTransactionsCancelledSending = r.getInt("NumTransactionsCancelledSending");
					stmt.put(ts.date, ts);
				}
			}
			r.close();
			
			ps = con.prepareStatement(
					"  SELECT MONTH(DateInitialized) as Month"
					+ "     , YEAR(DateInitialized) as Year"
					+ "     , SUM(Amount) as AmountReceived"
					+ "     , COUNT(*) as NumTransactionsReceived"
					+ " FROM SendTransaction"
					+ " WHERE ToIdentifier IN ("
					+ "   SELECT Identifier FROM EmailAddress WHERE USSN=?"
					+ "   UNION"
					+ "   SELECT Identifier FROM Phone WHERE USSN=?"
					+ " )"
					+ " AND Cancelled=0 "
					+ " GROUP BY MONTH(DateInitialized), YEAR(DateInitialized)"
			);
			ps.setString(1, ssn);
			ps.setString(2, ssn);
			r = ps.executeQuery();
			
			while (r.next()) {
				date = new MonthYear(r.getInt("Month"), r.getInt("Year"));
				if (stmt.containsKey(date)) {
					ts = stmt.get(date);
					ts.amountReceived = r.getInt("AmountReceived");
					ts.numTransactionsReceived = r.getInt("NumTransactionsReceived");
				} else {
					ts = new TransactionStatement();
					ts.date = date;
					ts.amountReceived = r.getInt("AmountReceived");
					ts.numTransactionsReceived = r.getInt("NumTransactionsReceived");
					stmt.put(ts.date, ts);
				}
			}
			
			r.close();
			con.commit();
			
			return stmt;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
}
