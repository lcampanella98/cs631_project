package actions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.electronicaddress.ElectronicAddress;
import pages.EasyPayServlet;

public class RejectRequestTransaction extends EasyPayServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);
		int rtid = Integer.parseInt(req.getParameter("rtid"));
		String eidentifier = req.getParameter("eidentifier");
		
		_easyPayService.deleteRequestTransaction(rtid, new ElectronicAddress(eidentifier));
		
		resp.sendRedirect("./RequestMoney?ssn=" + encParam(ssn));
	}
	
}
