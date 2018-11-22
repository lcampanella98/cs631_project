package pages;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.BankAccount;
import models.electronicaddress.ElectronicAddress;
import models.electronicaddress.EmailAddress;
import models.electronicaddress.Phone;
import tools.Methods;
public class MyAccount extends EasyPayServlet{
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);
		
		resp.setContentType("text/html");		
		this.title = "My Account";
		
		List<EmailAddress> emails = null;
		List<Phone> phones = null;
		List<BankAccount> otherAccounts = null;
		if (ssn != null && !ssn.isEmpty()) {
			this.user = _easyPayService.getUserAccountFromSSN(ssn);
			if (user != null) {
				emails = _easyPayService.getUserEmailAddresses(user.SSN);
				phones = _easyPayService.getUserPhoneNumbers(user.SSN);
				otherAccounts = _easyPayService.getOtherUserBankAccounts(user.SSN);
			}
		}
		

		printPreHTML();
		if (user == null) {
			out.println("<h4>User with ssn " + ssn + " not found</h4>");
		}
		else
		{
			out.println("<div class=\"row\">");
			out.println("<h4 class=\"col-sm-4\">SSN: " + user.SSN + "</h4>");
			out.println("<h4 class=\"col-sm-4\">Name: " + user.Name + "</h4>");
			out.println("<h4 class=\"col-sm-4\">Balance: " + Methods.formatMoney(user.Balance) + "</h4>");
			out.println("</div>");
			
			out.println("<hr />");
			out.println("<div class=\"row\" style=\"margin-top:20px;\">");
			
			/* Bank account */
			out.println("<div class=\"col-sm-4\">");
			out.println("<h4>Bank accounts:</h4>");
			out.println("<div style=\"margin-left: 25px;\">");
			out.println("<p>Primary Bank Account: </p>");
			printBankAccount(user.primaryAccount, true);
			
			if (otherAccounts == null || otherAccounts.isEmpty()) {
				out.println("<p>No other bank accounts</p>");
			} else 
			{
				out.println("<p>Other bank accounts</p>");
				for (BankAccount ba : otherAccounts) {
					printBankAccount(ba);
				}
			}
			printAddBankAccountForm();
			if (req.getParameter("addbankaccounterror") != null) {
				out.println("<p class=\"text-danger\">" + req.getParameter("addbankaccounterror") + "</p>");
			}
			
			out.println("</div>");
			out.println("</div>");
			
			/* emails */
			out.println("<div class=\"col-sm-4\">");
			if (emails == null || emails.isEmpty()) {
				out.println("<p>No email addresses</p>");
			}
			else 
			{
				out.println("<h4>Email addresses:</h4>"
							+ "<ul style=\"list-style-type:none\">");
				for (EmailAddress email : emails) {
					printEmail(email);
				}
				out.println("</ul>");
			}
			printAddEmailForm();
			if (req.getParameter("addemailerror") != null) {
				out.println("<p class=\"text-danger\">" + req.getParameter("addemailerror") + "</p>");
			}
			out.println("</div>");
			
			/* phones */
			out.println("<div class=\"col-sm-4\">");
			if (phones == null || phones.isEmpty()) {
				out.println("<p>No phone numbers</p>");
			} else 
			{
				out.println("<h4>Phone numbers:</h4>"
						+ "<ul style=\"list-style-type:none\">");
				for (Phone phone : phones) {
					printPhone(phone);
				}
				out.println("</ul>");
			}
			printAddPhoneForm();
			if (req.getParameter("addphoneerror") != null) {
				out.println("<p class=\"text-danger\">" + req.getParameter("addphoneerror") + "</p>");
			}
			out.println("</div>");
			
			out.println("</div>");
			
		}
		printPostHTML();
		
	}
	
	void printAddEmailForm() {
		out.println(
			"<form method=\"get\" action=\"./AddEmailAddress\" style=\"display:inline-block;\">"
				+ "<input type=\"hidden\" name=\"ssn\" value=\"" + ssn + "\" />"
				+ "<div class=\"form-group row\">"
				+ "  <input type=\"text\" class=\"form-control\" name=\"identifier\" placeholder=\"Enter an email\" />"
				+ "</div>"
				+ "<div class=\"form-group row\">"
				+ "  <button class=\"btn btn-primary\" type=\"submit\">Add</button>"
				+ "</div>"
			+ "</form>"
		);
	}
	
	void printAddPhoneForm() {
		out.println(
				"<form method=\"get\" action=\"./AddPhoneNumber\" style=\"display:inline-block;\">"
					+ "<input type=\"hidden\" name=\"ssn\" value=\"" + ssn + "\" />"
					+ "<div class=\"form-group row\">"
					+ "  <input type=\"text\" class=\"form-control\" name=\"identifier\" placeholder=\"Enter a phone number\" />"
					+ "</div>"
					+ "<div class=\"form-group row\">"
					+ "  <button class=\"btn btn-primary\" type=\"submit\">Add</button>"
					+ "</div>"
				+ "</form>"
			);
	}
	
	void printAddBankAccountForm() {
		out.println(
				"<form method=\"get\" action=\"./AddBankAccount\" style=\"display:inline-block;\">"
					+ "<input type=\"hidden\" name=\"ssn\" value=\"" + ssn + "\" />"
					+ "<div class=\"form-group row\">"
					+ "  <input type=\"number\" class=\"form-control\" name=\"bankid\" placeholder=\"Bank ID\" />"
					+ "</div>"
					+ "<div class=\"form-group row\">"
					+ "  <input type=\"number\" class=\"form-control\" name=\"banumber\" placeholder=\"Account No.\" />"
					+ "</div>"
					+ "<div class=\"form-group row\">"
					+ "  <button class=\"btn btn-primary\" type=\"submit\">Add</button>"
					+ "</div>"
				+ "</form>"
		);
	}

	void printBankAccount(BankAccount acct) {
		printBankAccount(acct, false);
	}
	
	void printBankAccount(BankAccount acct, boolean isPrimary) {
		out.println("<ul style=\"list-style-type:none\">");
		out.println("<li>Bank ID: " + acct.BankID + "</li>");
		out.println("<li><span>Account No: " + acct.BANumber + "</span>");
		if (!isPrimary) {
			printDeleteBankAccountForm(acct);
			printSetPrimaryBankAccountForm(acct);
		}
		out.println("</li>");
		out.println("</ul>");
	}
	
	void printEmail(EmailAddress email) {
		out.println("<li><span>" + email.Identifier + "</span>");
		printDeleteElectronicAddressForm(email);
		out.println("</li>");
	}
	
	void printPhone(Phone p) {
		out.println("<li><span>" + p.Identifier + "</span>");
		printDeleteElectronicAddressForm(p);
		out.println("</li>");
	}
	
	void printDeleteBankAccountForm(BankAccount ba) {
		out.println("<form method=\"get\" action=\"./DeleteBankAccount\" style=\"display:inline-block;\">"
				+ "<input type=\"hidden\" name=\"ssn\" value=\"" + ssn + "\" />"
				+ "<input type=\"hidden\" name=\"bankid\" value=\"" + ba.BankID + "\" />"
				+ "<input type=\"hidden\" name=\"banumber\" value=\"" + ba.BANumber + "\" />"
				+ "<button class=\"btn btn-sm btn-danger\" type=\"submit\">Delete</button>"
			+ "</form>");
	}
	
	void printSetPrimaryBankAccountForm(BankAccount ba) {
		out.println("<form method=\"get\" action=\"./SetPrimaryBankAccount\" style=\"display:inline-block;\">"
				+ "<input type=\"hidden\" name=\"ssn\" value=\"" + ssn + "\" />"
				+ "<input type=\"hidden\" name=\"bankid\" value=\"" + ba.BankID + "\" />"
				+ "<input type=\"hidden\" name=\"banumber\" value=\"" + ba.BANumber + "\" />"
				+ "<button class=\"btn btn-sm btn-default\" type=\"submit\">Set Primary</button>"
			+ "</form>");
	}

	void printDeleteElectronicAddressForm(ElectronicAddress ea) {
		out.println("<form method=\"get\" action=\"./DeleteElectronicAddress\" style=\"display:inline-block;\">"
							+ "<input type=\"hidden\" name=\"ssn\" value=\"" + ssn + "\" />"
							+ "<input type=\"hidden\" name=\"identifier\" value=\"" + ea.Identifier + "\" />"
							+ "<button class=\"btn btn-sm btn-danger\" type=\"submit\">Delete</button>"
						+ "</form>");
	}

}
