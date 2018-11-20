package actions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.electronicaddress.EmailAddress;
import pages.EasyPayServlet;

public class AddEmailAddress extends EasyPayServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);
		String identifier = req.getParameter("identifier");
		
		String redir = "/MyAccount?ssn=" + encParam(ssn);
		String errorParam = "addemailerror";
		
		if (identifier == null || identifier.isEmpty() || !identifier.matches(".+@.+")) {
			redir += "&" + errorParam + "=" + encParam("Email address invalid");
		}
		else 
		{	
			EmailAddress email = new EmailAddress();
			email.Identifier = identifier;
			email.Verified = true; // set verified automatically
			email.USSN = ssn;
			
			if (_easyPayService.electronicAddressExists(email)) {
				redir += "&" + errorParam + "=" + encParam("Email already exists");
			}
			else {
				_easyPayService.addElectronicAddress(email);
			}
		}
		
		resp.sendRedirect(redir);
	}
	
}
