package actions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.RequestFrom;
import models.RequestTransaction;
import pages.EasyPayServlet;

public class PerformRequestMoney extends EasyPayServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);
		
		String redir = "./RequestMoney?ssn=" + encParam(ssn);
		String errorParam = "requestmoneyerror";
		
		RequestTransaction rt = new RequestTransaction();
		rt.ISSN = ssn;
		rt.TotalAmount = 0;
		rt.Memo = req.getParameter("memo");
		
		int i = 1;
		List<RequestFrom> recipients = new ArrayList<RequestFrom>();
		String identifierStr = null;
		String amountStr = null;
		while (true) {
			identifierStr = req.getParameter("toemail" + i);
			if (identifierStr == null || identifierStr.trim().isEmpty()) {
				identifierStr = req.getParameter("tophone" + i);
			}
			
			if (identifierStr == null) break;
			else if (identifierStr.trim().isEmpty()) {
				redir += "&" + errorParam + "=" + encParam("Please specify email or phone for recipient number " + i);
				resp.sendRedirect(redir);
				return;
			}
			
			amountStr = req.getParameter("amount" + i);
			if (amountStr == null) break;
			else if (amountStr.trim().isEmpty()) {
				redir += "&" + errorParam + "=" + encParam("Please specify an amount for recipient number " + i);
				resp.sendRedirect(redir);
				return;
			}
			
			int amount = Integer.parseInt(amountStr);
			RequestFrom rf = new RequestFrom();
			rf.Amount = amount;
			rf.EIdentifier = identifierStr;
			recipients.add(rf);
			rt.TotalAmount += rf.Amount;
			
			i++;
		}
		
		rt.From = recipients;
		_easyPayService.newRequestTransaction(rt);
		
		resp.sendRedirect(redir);
	}
	
}
