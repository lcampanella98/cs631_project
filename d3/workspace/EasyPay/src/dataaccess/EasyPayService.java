package dataaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import models.BankAccount;
import models.UserAccount;
import models.electronicaddress.ElectronicAddress;
import models.electronicaddress.EmailAddress;
import models.electronicaddress.Phone;

import java.util.ArrayList;
import java.util.List;

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
			return con;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public UserAccount getUserAccountFromSSN(String ssn) {
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM UserAccount WHERE SSN=?");
			ps.setString(1, ssn);
			List<UserAccount> accList = getUserAccountsFromResultSet(ps.executeQuery());
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
			List<UserAccount> accList = getUserAccountsFromResultSet(ps.executeQuery());
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
				ua.add(u);
			}
			return ua;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<BankAccount> getOtherUserBankAccounts(String ssn) {
		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM Has_Additional WHERE USSN=?");
			ps.setString(1, ssn);
			ResultSet r = ps.executeQuery();
			List<BankAccount> l = new ArrayList<BankAccount>();
			while (r.next()) {
				BankAccount ba = new BankAccount();
				ba.BankID = r.getInt("UBankID");
				ba.BANumber = r.getInt("UBANumber");
				l.add(ba);
			}
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
					"SELECT * FROM EmailAddress"
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
					"SELECT * FROM Phone"
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
			return l;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
