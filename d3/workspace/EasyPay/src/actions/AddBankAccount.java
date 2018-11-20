package actions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.BankAccount;
import pages.EasyPayServlet;

public class AddBankAccount extends EasyPayServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);
		
		String redir = "/MyAccount?ssn=" + encParam(ssn);
		String errorParam = "addbankaccounterror";
		
		BankAccount ba = new BankAccount();
		try {
			ba.BankID = Integer.parseInt(req.getParameter("bankid"));
			ba.BANumber = Integer.parseInt(req.getParameter("banumber"));
		} catch (NumberFormatException e) {
			redir += "&" + errorParam + "=" + encParam("Invalid ID and/or account number");
			resp.sendRedirect(redir);
			return;
		}
		
		if (_easyPayService.userHasBankAccount(ba, ssn)) {
			redir += "&" + errorParam + "=" + encParam("Account already linked");
		} else {
			_easyPayService.addBankAccountToUser(ba, ssn);			
		}
		
		resp.sendRedirect(redir);
	}
	
}
