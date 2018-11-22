package pages;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.SendTransaction;
import tools.Methods;

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
		
		out.println(
				"<table class=\"table table-striped table-bordered\">"
				+ "<thead>"
					+ "<th>To</th>"
					+ "<th>Amount</th>"
					+ "<th>Time</th>"
					+ "<th>Cancel</th>"
				+ "</thead>");
		
		out.println("<tbody>");
		for (SendTransaction st : sends) printCancellableSendTransaction(st);
		out.println("</tbody>");
		out.println("</table>");
		
		printPostHTML();
	}

	
	private void printCancellableSendTransaction(SendTransaction st) {
		out.println("<tr>"
						+ "<td>" + st.ToIdentifier + "</td>"
						+ "<td>" + Methods.formatMoney(st.Amount) + "</td>"
						+ "<td>" + Methods.friendlyDate(st.DateInitialized) + "</td>"
			);
		out.println("<td>");
		printDeleteButton(st);
		out.println("</td>");
		
		out.println("</tr>");
	}
	
	private void printDeleteButton(SendTransaction st) {
		out.println("<form method=\"get\" action=\"./DeleteSendTransaction\" style=\"display:inline-block;\">"
							+ "<input type=\"hidden\" name=\"ssn\" value=\"" + ssn + "\" />"
							+ "<input type=\"hidden\" name=\"stid\" value=\"" + st.STID + "\" />"
							+ "<button class=\"btn btn-sm btn-danger\" type=\"submit\">Delete</button>"
						+ "</form>");
	}
	
	private void printSendForm() {
		out.println("<div class=\"row\">"
				+ "<div class=\"col-sm-6\">"
				+ "<form method=\"get\" action=\"./PerformSendMoney\">"
					+ "<input type=\"hidden\" name=\"ssn\" value=\"" + ssn + "\" />"
					+ "<input type=\"number\" class=\"form-control\" name=\"amount\" placeholder=\"Amount\" />"
					+ "<div style=\"margin-top:10px;margin-bottom:10px;\">"
						+ "<input type=\"text\" class=\"form-control\" name=\"toemail\" placeholder=\"To Email\" />"
						+ "<div style=\"text-align:center;\">OR</div>"
						+ "<input type=\"text\" class=\"form-control\" name=\"tophone\" placeholder=\"To Phone\" />"
					+ "</div>"
					+ "<input type=\"text\" class=\"form-control\" name=\"memo\" placeholder=\"Memo (Optional)\" />"
					+ "<button type=\"submit\" class=\"btn btn-primary\" style=\"margin-top:10px;\">Send</button>"
					+ "</form>"
					+ "</div></div>"
				);
	}
	
}
