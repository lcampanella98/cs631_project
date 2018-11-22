package pages;

import java.io.IOException;
import java.util.SortedMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.MonthYear;
import models.TransactionStatement;
import tools.Methods;

public class Statements extends EasyPayServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);
		
		setTitle("Statements");
		resp.setContentType("text/html");
		
		SortedMap<MonthYear, TransactionStatement> stmt = _easyPayService.getMonthlyStatement(ssn);
		
		printPreHTML();
		if (stmt.isEmpty()) {
			out.println("<h3>No transaction history</h3>");
		} else {
			for (TransactionStatement transaction : stmt.values()) {
				printStatement(transaction);
			}
		}
		printPostHTML();
	}
	
	private void printStatement(TransactionStatement ts) {
		String monthName = Methods.getMonth(ts.date.month);
		
		out.println(
			"<h3>" + monthName + " " + ts.date.year + "</h3><hr />"
			+ "<div class=\"row\" style=\"margin-bottom:20px;\">"
			+ "  <div class=\"col-md-4\">"
			+ "    <h4 style=\"text-align:center\">Sent: </h4>"
			+ "    <p>" + ts.numTransactionsSent + Methods.makePlural(" transaction", ts.numTransactionsSent) + "</p>"
			+ "    <p>" + Methods.formatMoney(ts.amountSent) + "</p>"
			+ "    </h4>"
			+ "  </div>"
			+ "  <div class=\"col-md-4\">"
			+ "    <h4 style=\"text-align:center;\">Received: </h4>"
			+ "    <p>" + ts.numTransactionsReceived + Methods.makePlural(" transaction", ts.numTransactionsReceived) + "</p>"
			+ "    <p>" + Methods.formatMoney(ts.amountReceived) + "</p>"
			+ "    </h4>"
			+ "  </div>"
			+ "  <div class=\"col-md-4\">"
			+ "    <h4 style=\"text-align:center;\">Cancelled: </h4>"
			+ "    <p>" + ts.numTransactionsCancelledSending + Methods.makePlural(" transaction", ts.numTransactionsCancelledSending) + "</p>"
			+ "    <p>" + Methods.formatMoney(ts.amountCancelledSending) + "</p>"
			+ "    </h4>"
			+ "  </div>"
			+ "</div>"
		);
	}
}
