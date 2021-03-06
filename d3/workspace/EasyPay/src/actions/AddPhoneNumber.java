package actions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.electronicaddress.Phone;
import pages.EasyPayServlet;
import tools.Methods;

public class AddPhoneNumber extends EasyPayServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);
		
		String identifier = req.getParameter("identifier");
		
		String redir = "./MyAccount?ssn=" + encParam(ssn);
		String errorParam = "addphoneerror";
		
		if (!Methods.IsValidPhone(identifier)) {
			redir += "&" + errorParam + "=" + encParam("Phone number invalid");
		}
		else
		{
			
			Phone phone = new Phone();
			phone.Identifier = identifier;
			phone.Verified = true; // set verified automatically
			phone.USSN = ssn;
			
			
			if (_easyPayService.electronicAddressExists(phone)) {
				redir += "&" + errorParam + "=" + encParam("Phone number already exists");
			}
			else {
				_easyPayService.addElectronicAddress(phone);
			}
		}
		
		resp.sendRedirect(redir);
	}
	
}
