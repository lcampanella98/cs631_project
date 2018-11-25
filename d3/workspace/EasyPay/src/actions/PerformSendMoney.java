package actions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.SendTransaction;
import models.electronicaddress.EmailAddress;
import models.electronicaddress.Phone;
import pages.EasyPayServlet;
import tools.Methods;

public class PerformSendMoney extends EasyPayServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);
		
		String redir = "./SendMoney?ssn=" + encParam(ssn);
		String errorParam = "sendmoneyerror";
		
		SendTransaction st = new SendTransaction();
		st.ISSN = ssn;
		try {
			st.Amount = Integer.parseInt(req.getParameter("amount"));
		} catch (NumberFormatException e) {
			redir += "&" + errorParam + "=" + encParam("Invalid amount");
			resp.sendRedirect(redir);
			return;
		}
		
		EmailAddress email = new EmailAddress();
		email.Identifier = req.getParameter("toemail");
		Phone phone = new Phone();
		phone.Identifier = req.getParameter("tophone");
		if (Methods.IsValidEmail(email.Identifier)) {
			st.ToIdentifier = email.Identifier;
		}
		else if (Methods.IsValidPhone(phone.Identifier)) {
			st.ToIdentifier = phone.Identifier;
		} else {
			redir += "&" + errorParam + "=" + encParam("Must specify a VALID email or phone");
			resp.sendRedirect(redir);
			return;
		}
		
		st.Memo = req.getParameter("memo");
		// no need to set st.DateInitialized. default is current timestamp in service method
		
		String err = _easyPayService.sendPayment(st);
		if (err != null) {
			redir += "&" + errorParam + "=" + encParam(err);
		}
		
		resp.sendRedirect(redir);
		
	}
	
	
}
