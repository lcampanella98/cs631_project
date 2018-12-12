package pages;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.MonthYear;
import models.RequestTransaction;
import models.SendTransaction;
import models.TransactionStatement;
import renderers.TransactionRenderer;
import tools.Methods;

public class SearchTransactions extends EasyPayServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);
		
		setTitle("Search Transactions");
		resp.setContentType("text/html");
		
		String searchQuery = req.getParameter("q");
		String startDate = req.getParameter("startDate");
		String endDate = req.getParameter("endDate");
		
		List<SendTransaction> sends = null;
		List<RequestTransaction> requests = null;
		SortedMap<MonthYear, TransactionStatement> sendStatement = null;
		if (startDate == null || endDate == null) {
			if (searchQuery == null) searchQuery = "";
			sends = _easyPayService.searchSendTransactions(ssn, searchQuery);
			requests = _easyPayService.searchRequestTransactions(ssn, searchQuery);
		} else if (startDate != null && endDate != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				sends = _easyPayService.searchSendTransactionsByDateRange(ssn, sdf.parse(startDate), sdf.parse(endDate));
				requests = _easyPayService.searchRequestTransactionsByDateRange(ssn, sdf.parse(startDate), sdf.parse(endDate));
			} catch (ParseException e) {
				e.printStackTrace();
				sends = new ArrayList<SendTransaction>();
				requests = new ArrayList<RequestTransaction>();
			}			
		}
		sendStatement = _easyPayService.getMonthlyStatement(sends, ssn);
		
		
		printPreHTML();
		printSearchForm();
		out.println("<h3>Send Transaction Results</h3><hr />");
		printSendTransactions(sends);
		if (sendStatement != null && !sendStatement.isEmpty()) {
			out.println("<h3>Monthly Totals</h3><hr />");
			for (TransactionStatement transaction : sendStatement.values()) {
				printStatement(transaction);
			}
		}
		out.println("<h3 style=\"margin-top:15px;\">Request Transaction Results</h3><hr />");
		printRequestTransactions(requests);
		printPostHTML();
	}
	
	void printSearchForm() {
		out.println(
				"<div class=\"row\">"
				+ "<div class=\"col-md-6\">"
				+ "<form method=\"get\" action=\"./SearchTransactions\" style=\"display:inline-block;\">"
					+ "<input type=\"hidden\" name=\"ssn\" value=\"" + ssn + "\" />"
					+ "<div class=\"form-group row\">"
					+ "  <input type=\"text\" class=\"form-control\" name=\"q\" placeholder=\"Search all transactions\" />"
					+ "</div>"
					+ "<div class=\"form-group row\">"
					+ "  <button class=\"btn btn-primary\" type=\"submit\">Search</button>"
					+ "</div>"
				+ "</form>"
				+ "</div>"
				+ "<div class=\"col-md-6\">"
				+ "<form method=\"get\" action=\"./SearchTransactions\" style=\"display:inline-block;\">"
					+ "<input type=\"hidden\" name=\"ssn\" value=\"" + ssn + "\" />"
					+ "<div class=\"form-group row\">"
					+ "  <p>Start Date</p>"
					+ "  <input type=\"date\" class=\"form-control\" name=\"startDate\" placeholder=\"Search all transactions\" />"
					+ "</div>"
					+ "<div class=\"form-group row\">"
					+ "  <p>End Date</p>"
					+ "  <input type=\"date\" class=\"form-control\" name=\"endDate\" placeholder=\"Search all transactions\" />"
					+ "</div>"
					+ "<div class=\"form-group row\">"
					+ "  <button class=\"btn btn-primary\" type=\"submit\">Search</button>"
					+ "</div>"
				+ "</form>"
				+ "</div>"
				+ "</div>"
			);
	}
	
	void printSendTransactions(List<SendTransaction> transactions) {
		if (transactions.isEmpty()) {
			out.println("<h4 style=\"text-align:center\">No send transactions</h4>");
		} else {
			new TransactionRenderer(ssn, out).renderSendTransactionsTable(transactions, false);
		}
	}
	
	void printRequestTransactions(List<RequestTransaction> transactions) {
		if (transactions.isEmpty()) {
			out.println("<h4 style=\"text-align:center\">No request transactions</h4>");
		} else {
			new TransactionRenderer(ssn, out).renderRequestTransactionsTable(transactions, true);
		}
	}
	
	private void printStatement(TransactionStatement ts) {
		String monthName = Methods.getMonth(ts.date.month);
		
		out.println(
			"<h4>" + monthName + " " + ts.date.year + "</h4><hr />"
			+ "<div class=\"row\" style=\"margin-bottom:20px;\">"
			+ "  <div class=\"col-md-6\">"
			+ "    <h4 style=\"text-align:center\">Sent: </h4>"
			+ "    <p>" + ts.numTransactionsSent + Methods.makePlural(" transaction", ts.numTransactionsSent) + "</p>"
			+ "    <p>" + Methods.formatMoney(ts.amountSent) + "</p>"
			+ "    </h4>"
			+ "  </div>"
			+ "  <div class=\"col-md-6\">"
			+ "    <h4 style=\"text-align:center;\">Received: </h4>"
			+ "    <p>" + ts.numTransactionsReceived + Methods.makePlural(" transaction", ts.numTransactionsReceived) + "</p>"
			+ "    <p>" + Methods.formatMoney(ts.amountReceived) + "</p>"
			+ "    </h4>"
			+ "  </div>"
			+ "</div>"
		);
	}
}
