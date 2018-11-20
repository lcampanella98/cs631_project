package dataaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import config.EPConstants;
import models.BankAccount;
import models.SendTransaction;
import models.UserAccount;
import models.electronicaddress.ElectronicAddress;
import models.electronicaddress.EmailAddress;
import models.electronicaddress.Phone;

public class EasyPayService {
	private static final String CON_STR = "jdbc:mysql://sql1.njit.edu/lrc22";
	
	private Connection con;
	
	public EasyPayService() {
		con = getDBConnection();
	}
	
	private static Connection getDBConnection() {
		try {
			//Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(CON_STR, "lrc22", "I4ZEwI8A3");
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
			
			List<UserAccount> accList = getUserAccountsFromResultSet(r);
			
			r.close();
			con.commit();
			
			if (accList.isEmpty()) return null;
			return accList.get(0);
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
			
			List<UserAccount> accList = getUserAccountsFromResultSet(r);
			
			r.close();
			con.commit();
			
			if (accList.isEmpty()) return null;
			return accList.get(0);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<UserAccount> getUserAccountsFromResultSet(ResultSet r) {
		try {
			List<UserAccount> ua = new ArrayList<>();
			UserAccount u;
			while (r.next()) {
				u = new UserAccount();
				u.SSN = r.getString("SSN");
				u.Name = r.getString("Name");
				u.Balance = r.getInt("Balance");
				u.primaryAccount = new BankAccount();
				u.primaryAccount.BankID = r.getInt("PBankID");
				u.primaryAccount.BANumber = r.getInt("PBANumber");
				u.primaryAccount.Verified = true;
				ua.add(u);
			}
			r.close();
			con.commit();
			
			return ua;
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
	
	public void sendPayment(SendTransaction st) {
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
			
			ElectronicAddress ea = new ElectronicAddress();
			ea.Identifier = st.ToIdentifier;
			String toSSN = getUserSSNFromElectronicAddress(ea); 
			if (toSSN != null) {
				ps = con.prepareStatement(
						"UPDATE UserAccount\n"
						+ "SET Balance=Balance+?\n"
						+ "WHERE SSN=?");
				ps.setInt(1, st.Amount);
				ps.setString(2, toSSN);
				ps.executeUpdate();
			}
			
			con.commit();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean canCancelSendPayment(int STID) {
		try {
			PreparedStatement ps = con.prepareStatement("SELECT EXISTS (\n"
					+ "SELECT * FROM SendTransaction\n"
					+ "WHERE STID=?\n"
					+ "AND TIMESTAMPDIFF(MINUTE,DateInitialized,NOW()) < " + EPConstants.CANCELLATION_WINDOW_MINUTES + "\n"
					+ ")");
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
	
	public void cancelSendPayment(int STID) {
		try {
			PreparedStatement ps = con.prepareStatement("UPDATE SendTransaction SET Cancelled=1 WHERE STID=?");
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
}
