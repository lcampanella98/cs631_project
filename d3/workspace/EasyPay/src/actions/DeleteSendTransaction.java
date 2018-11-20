package actions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pages.EasyPayServlet;

public class DeleteSendTransaction extends EasyPayServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);
		
		int stid = Integer.parseInt(req.getParameter("stid"));
		
		if (_easyPayService.canCancelSendPayment(stid)) {
			_easyPayService.cancelSendPayment(stid);			
		}
		
		resp.sendRedirect(BASE_PATH + "SendMoney?ssn=" + encParam(ssn));
	}
	
}
