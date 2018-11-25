package pages;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.SendTransaction;
import renderers.TransactionRenderer;

public class SendMoney extends EasyPayServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);

		resp.setContentType("text/html");		
		this.title = "Send Money";
		
		List<SendTransaction> sends = _easyPayService.getSendTransactionsAvailableToCancel(ssn);
		
		printPreHTML();
		
		printSendForm();
		if (req.getParameter("sendmoneyerror") != null) {
			out.println("<p class=\"text-danger\">" + req.getParameter("sendmoneyerror") + "</p>");
		}
		out.println("<h4 style=\"text-align:center\">Recently Sent</h4>");
		new TransactionRenderer(ssn, out).renderSendTransactionsTable(sends, true);
		
		printPostHTML();
	}

	
	private void printSendForm() {
		out.println(
				"<form method=\"get\" action=\"./PerformSendMoney\">"
					+ "<input type=\"hidden\" name=\"ssn\" value=\"" + ssn + "\" />"
					+ "<div class=\"form-group row\">"
						+ "<div class=\"col-sm-6\">"
						+ "  <input type=\"number\" class=\"form-control\" name=\"amount\" placeholder=\"Amount\" />"
						+ "  <input type=\"text\" class=\"form-control\" style=\"margin-top:10px;\" name=\"memo\" placeholder=\"Memo (Optional)\" />"
						+ "</div>"
						+ "<div class=\"col-sm-6\">"
						+ "  <input type=\"text\" class=\"form-control\" name=\"toemail\" placeholder=\"To Email\" />"
						+ "  <div style=\"text-align:center;\">OR</div>"
						+ "  <input type=\"text\" class=\"form-control\" name=\"tophone\" placeholder=\"To Phone\" />"
						+ "</div>"
					+ "</div>"
					+ "<div class=\"form-group row\">"
					+ "  <div class=\"col-sm-12\">"
					+ "    <button type=\"submit\" class=\"btn btn-primary\" style=\"margin-top:10px;\">Send</button>"
					+ "  </div>"
					+ "</div>"
				+ "</form>"
				);
	}
	
}
