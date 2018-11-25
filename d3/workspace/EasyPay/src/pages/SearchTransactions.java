package pages;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.RequestTransaction;
import models.SendTransaction;
import renderers.TransactionRenderer;

public class SearchTransactions extends EasyPayServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);
		
		setTitle("Search Transactions");
		resp.setContentType("text/html");
		
		String searchQuery = req.getParameter("q");
		if (searchQuery == null) {
			searchQuery = "";
		}
		List<SendTransaction> sends = _easyPayService.searchSendTransactions(ssn, searchQuery);
		List<RequestTransaction> requests = _easyPayService.searchRequestTransactions(ssn, searchQuery);
		
		printPreHTML();
		printSearchForm();
		out.println("<h3>Send Transaction Results</h3><hr />");
		printSendTransactions(sends);
		out.println("<h3 style=\"margin-top:15px;\">Request Transaction Results</h3><hr />");
		printRequestTransactions(requests);
		printPostHTML();
	}
	
	void printSearchForm() {
		out.println(
				"<form method=\"get\" action=\"./SearchTransactions\" style=\"display:inline-block;\">"
					+ "<input type=\"hidden\" name=\"ssn\" value=\"" + ssn + "\" />"
					+ "<div class=\"form-group row\">"
					+ "  <input type=\"text\" class=\"form-control\" name=\"q\" placeholder=\"Search all transactions\" />"
					+ "</div>"
					+ "<div class=\"form-group row\">"
					+ "  <button class=\"btn btn-primary\" type=\"submit\">Search</button>"
					+ "</div>"
				+ "</form>"
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
	
}
