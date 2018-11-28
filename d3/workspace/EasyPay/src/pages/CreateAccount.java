package pages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.BankAccount;
import models.UserAccount;
import models.electronicaddress.ElectronicAddress;
import models.electronicaddress.EmailAddress;
import models.electronicaddress.Phone;
import tools.Methods;

public class CreateAccount extends EasyPayBaseServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);
		
		setTitle("Create Account");
		
		String redir = "./CreateAccount";
		
		String ssn = req.getParameter("ssn");
		String error = null;
		
		if (ssn != null) {
			String pBankID = req.getParameter("bankid");
			String pBANumber = req.getParameter("banumber");
			String name = req.getParameter("name");
			
			EmailAddress email = new EmailAddress();
			Phone phone = new Phone();
			email.USSN = ssn;
			phone.USSN = ssn;
			email.Verified = true;
			phone.Verified = true;
			email.Identifier = req.getParameter("email");
			phone.Identifier = req.getParameter("phone");
			
			UserAccount u = new UserAccount();
			BankAccount ba = new BankAccount();
			
			
			if (pBankID == null || pBANumber == null || email.Identifier == null || phone.Identifier == null || name == null) {
				error = "Invalid request";
			}
			if (error == null) {
				if (_easyPayService.getUserAccountFromSSN(ssn) != null) {
					error = "SSN already exists in database";
				}
			}
			if (error == null) {
				if (name.trim().isEmpty()) {
					error = "Please enter your name";
				}
			}
			if (error == null) {
				try {
					ba.BankID = Integer.parseInt(pBankID);
					ba.BANumber = Integer.parseInt(pBANumber);
				} catch (NumberFormatException e) {
					error = "Bank id and account must be valid integers";
				}
			}
			boolean hasValidEmail = false, hasValidPhone = false;
			if (error == null) {
				if (Methods.IsValidEmail(email.Identifier)) {
					if (_easyPayService.electronicAddressExists(email)) {
						error = "Email already exists";
					} else {
						hasValidEmail = true;
					}
				}
				if (Methods.IsValidPhone(phone.Identifier)) {
					if (_easyPayService.electronicAddressExists(phone)) {
						error = "Phone number already exists";
					} else {
						hasValidPhone = true;
					}
				}
			}
			if (error == null && !(hasValidEmail || hasValidPhone)) {
				error = "Must provide a VALID email or phone";
			}
			if (error == null) {
				// Create Account!
				u.SSN = ssn;
				u.Name = name;
				u.primaryAccount = ba;
				u.Balance = 0;
				List<ElectronicAddress> addAddresses = new ArrayList<>();
				if (hasValidEmail) addAddresses.add(email);
				if (hasValidPhone) addAddresses.add(phone);
				_easyPayService.createUserAccount(u, addAddresses);
				
				resp.sendRedirect("./MyAccount?ssn=" + encParam(ssn));
				return;
			}
		}
		
		printPreHTML();
		printForm();
		if (error != null) {
			out.println("<p class=\"text-danger\">" + error + "</p>");
		}
		printPostHTML();
	}
	
	public void printForm() {
		out.println("<form method=\"get\" action=\"./CreateAccount\">");
		// ssn
		out.println("<div class=\"form-group row\">"
				+ "<div class=\"col-sm-6\">"
				+ " <p>SSN</p>"
				+ " <input type=\"text\" class=\"form-control\" name=\"ssn\" placeholder=\"Enter SSN\" />"
				+ "</div>"
				+ "<div class=\"col-sm-6\">"
				+ " <p>Name</p>"
				+ " <input type=\"text\" class=\"form-control\" name=\"name\" placeholder=\"Enter Your Name\" />"
				+ "</div>"
				+ "</div>");
		
		out.println("<div class=\"form-group row\">");
		
		out.println(
				 	"<div class=\"col-sm-6\"><p>Primary Bank Account</p>"
				    + "<div class=\"form-group row\">"
					+ "  <input type=\"number\" class=\"form-control\" name=\"bankid\" placeholder=\"Bank ID\" />"
					+ "</div>"
					+ "<div class=\"form-group row\">"
					+ "  <input type=\"number\" class=\"form-control\" name=\"banumber\" placeholder=\"Account No.\" />"
					+ "</div>"
					+ "</div>"
		);
		
		out.println(
					"<div class=\"col-sm-6\"><p>Electronic Addresses</p>"
						+ "<div style=\"margin-top:10px;margin-bottom:10px;\">"
						+ "  <input type=\"text\" class=\"form-control\" name=\"email\" placeholder=\"Email Address\" />"
						+ "  <div style=\"text-align:center;\">OR</div>"
						+ "  <input type=\"text\" class=\"form-control\" name=\"phone\" placeholder=\"Phone\" />"
						+ "</div>"
						+ "</div>"
			);
		
		out.println("</div>");
		
		out.println(
					"<div class=\"form-group row\">"
					+ "  <button class=\"btn btn-primary\" type=\"submit\">Add</button>"
					+ "</div>"
				+ "</form>"
			);
		out.println(
			"<div class=\"row\" style=\"margin-top:15px;\">"
			+ "<a href=\"./SignIn\"><button type=\"button\" class=\"btn btn-default\">Back</button></a>"
			+ "</div>"
				);
		List<UserAccount> allUsers = _easyPayService.getAllUsers();
		out.println("<div class=\"row\">All SSNs: ");
		for (UserAccount u : allUsers) {
			out.println(u.SSN + ",");
		}
		out.println("</div>");
	}
	
}
