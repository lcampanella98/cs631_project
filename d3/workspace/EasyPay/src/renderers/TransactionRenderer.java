package renderers;

import java.io.PrintWriter;
import java.util.List;

import models.RequestFrom;
import models.RequestTransaction;
import models.SendTransaction;
import tools.Methods;

public class TransactionRenderer extends EasyPayRenderer {

	public TransactionRenderer(String ssn, PrintWriter out) {
		super(ssn, out);
	}
	
	public void renderSendTransactionsTable(List<SendTransaction> transactions, boolean cancellable) {
		out.println(
				"<table class=\"table table-striped table-bordered\">"
				+ "<thead>"
					+ "<th>From</th>"
					+ "<th>To</th>"
					+ "<th>Amount</th>"
					+ "<th>Time</th>"
					+ "<th>Memo</th>"
					+ (cancellable ? "<th>Cancel</th>" : "")
				+ "</thead>");
		out.println("<tbody>");
		for (SendTransaction st : transactions) renderSendTransactionRow(st, cancellable);
		out.println("</tbody>");
		out.println("</table>");
	}
	
	private void renderSendTransactionRow(SendTransaction st, boolean cancellable) {
		out.println("<tr>"
						+ "<td>" + st.FromUser.Name + "</td>"
						+ "<td>" + (st.ToIdentifier == null ? st.ToNewUserIdentifier : st.ToIdentifier) + (st.ToUser == null ? " (New User)" : " (" + st.ToUser.Name + ")") + "</td>"
						+ "<td>" + Methods.formatMoney(st.Amount) + "</td>"
						+ "<td>" + Methods.friendlyDate(st.DateInitialized) + "</td>"
						+ "<td>" + st.Memo + "</td>"
			);
		
		if (cancellable) {
			out.println("<td>");
			renderSendMoneyDeleteButton(st);
			out.println("</td>");
		}
		
		out.println("</tr>");
	}
	
	private void renderSendMoneyDeleteButton(SendTransaction st) {
		out.println("<form method=\"get\" action=\"./DeleteSendTransaction\" style=\"display:inline-block;\">"
							+ "<input type=\"hidden\" name=\"ssn\" value=\"" + ssn + "\" />"
							+ "<input type=\"hidden\" name=\"stid\" value=\"" + st.STID + "\" />"
							+ "<button class=\"btn btn-sm btn-danger\" type=\"submit\">Delete</button>"
						+ "</form>");
	}
	
	
	public void renderRequestTransactionsTable(List<RequestTransaction> requests, boolean actionable) {
		out.println(
				"<table class=\"table table-striped table-bordered\">"
				+ "<thead>"
					+ "<th>From</th>"
					+ "<th>Amount</th>"
					+ "<th>Time</th>"
					+ "<th>Memo</th>"
					+ (actionable ? "<th></th>" : "")
				+ "</thead>");
		out.println("<tbody>");
		for (RequestTransaction rt : requests) renderRequestTransaction(rt, actionable);
		out.println("</tbody>");
	}
	
	private void renderRequestTransaction(RequestTransaction rt, boolean actionable) {
		RequestFrom rf = rt.From.get(0);
		out.println("<tr>"
				+ "<td>" + rt.IUser.Name + "</td>"
				+ "<td>" + Methods.formatMoney(rf.Amount) + "</td>"
				+ "<td>" + Methods.friendlyDate(rt.DateInitialized) + "</td>"
				+ "<td>" + ((rt.Memo != null) ? rt.Memo : "") + "</td>"
			);
		if (actionable) {
			out.println("<td>");
			renderRequestAcceptButton(rt);
			renderRequestRejectButton(rt);
			out.println("</td>");		
		}
		
		out.println("</tr>");
	}
	
	private void renderRequestRejectButton(RequestTransaction rt) {
		out.println("<form method=\"get\" action=\"./RejectRequestTransaction\" style=\"display:inline-block;\">"
							+ "<input type=\"hidden\" name=\"ssn\" value=\"" + ssn + "\" />"
							+ "<input type=\"hidden\" name=\"rtid\" value=\"" + rt.RTID + "\" />"
							+ "<input type=\"hidden\" name=\"eidentifier\" value=\"" + rt.From.get(0).EIdentifier + "\" />"
							+ "<button class=\"btn btn-sm btn-danger\" type=\"submit\">Reject</button>"
						+ "</form>");
	}
	
	private void renderRequestAcceptButton(RequestTransaction rt) {
		out.println("<form method=\"get\" action=\"./AcceptRequestTransaction\" style=\"display:inline-block;\">"
				+ "<input type=\"hidden\" name=\"ssn\" value=\"" + ssn + "\" />"
				+ "<input type=\"hidden\" name=\"rtid\" value=\"" + rt.RTID + "\" />"
				+ "<input type=\"hidden\" name=\"eidentifier\" value=\"" + rt.From.get(0).EIdentifier + "\" />"
				+ "<button class=\"btn btn-sm btn-primary\" type=\"submit\">Accept</button>"
			+ "</form>");
	}
	
}
