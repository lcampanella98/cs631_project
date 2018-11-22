package actions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.electronicaddress.ElectronicAddress;
import pages.EasyPayServlet;

public class AcceptRequestTransaction extends EasyPayServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);
		
		String redir = "./RequestMoney?ssn=" + encParam(ssn);
		String errorParam = "acceptrequesterror";
		
		int rtid = Integer.parseInt(req.getParameter("rtid"));
		String eidentifier = req.getParameter("eidentifier");
		
		
		String err = _easyPayService.acceptRequestTransaction(rtid, new ElectronicAddress(eidentifier));
		if (err != null) {
			redir += "&" + errorParam + "=" + encParam(err);
		}
		
		resp.sendRedirect(redir);
	}
	
}
