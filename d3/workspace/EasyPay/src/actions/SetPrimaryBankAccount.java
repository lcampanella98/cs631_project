package actions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.BankAccount;
import pages.EasyPayServlet;

public class SetPrimaryBankAccount extends EasyPayServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ssn = req.getParameter("ssn");
		BankAccount ba = new BankAccount();
		ba.BankID = Integer.parseInt(req.getParameter("bankid"));
		ba.BANumber = Integer.parseInt(req.getParameter("banumber"));
		
		_easyPayService.setPrimaryBankAccount(ba, ssn);
		
		resp.sendRedirect("/MyAccount?ssn=" + encParam(ssn));
	}
	
}
