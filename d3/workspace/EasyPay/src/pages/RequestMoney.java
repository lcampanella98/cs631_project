package pages;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.RequestTransaction;
import renderers.TransactionRenderer;

public class RequestMoney extends EasyPayServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);

		resp.setContentType("text/html");		
		this.title = "Request Money";
		
		List<RequestTransaction> requests = _easyPayService.getRequestTransactionsRequestedOfUser(ssn);
		
		
		
		printPreHTML();
		
		printRequestForm();
		
		if (req.getParameter("requestmoneyerror") != null) {
			out.println("<p class=\"text-danger\">" + req.getParameter("requestmoneyerror") + "</p>");
		}
		
		if (req.getParameter("acceptrequesterror") != null) {
			out.println("<p class=\"text-danger\">" + req.getParameter("acceptrequesterror") + "</p>");
		}
		
		new TransactionRenderer(ssn, out).renderRequestTransactionsTable(requests, true);
		
		printPostHTML();
	}
	

	private void printRequestForm() {
		String formGroup = "'"
				+ "<div class=\"form-group row\" id=\"form-group-recipient-' + id + '\">"
				+ " <input type=\"number\" class=\"col-sm-6 form-control\" name=\"amount' + id + '\" placeholder=\"Amount ' + id + '\" />"
				+ " <div class=\"col-sm-6\">"
				+ "  <input type=\"text\" class=\"form-control\" name=\"toemail' + id + '\" placeholder=\"To Email ' + id + '\" />"
				+ "  <div style=\"text-align:center;\">OR</div>"
				+ "  <input type=\"text\" class=\"form-control\" name=\"tophone' + id + '\" placeholder=\"To Phone ' + id + '\" />"
				+ " </div>"
				+ "</div>"
			+ "'";
		out.println("<script>");
		out.println(
			  "function addRecipient(id) {"
			+ "  $('#form-memo').before(" + formGroup + ");"
			+ "}"
		);
		out.println(
			  "function removeRecipient(id) {"
			+ "  $('#form-group-recipient-' + id).remove();"
			+ "}"
		);
		out.println(
			"$(function () {"
				+ "var nextRecipient = 1;"
				+ "addRecipient(nextRecipient++);"
				+ "$('.btn-add-from').on('click', function () {"
				+ "  addRecipient(nextRecipient++);"
				+ "});"
				+ "$('.btn-remove-from').on('click', function () {"
				+ "  if (nextRecipient > 2) {"
				+ "    removeRecipient(--nextRecipient);"
				+ "  }"
				+ "});"
			+ "});");
		out.println("</script>");
		
		out.println(
			//"<div class=\"row\">"
			//+ "<div class=\"col-sm-12\">"
			"  <form method=\"get\" action=\"./PerformRequestMoney\">"
			+ "    <input type=\"hidden\" name=\"ssn\" value=\"" + ssn + "\" />"
			+ "    <input type=\"text\" style=\"margin-bottom:10px;\" class=\"form-control\" name=\"memo\" id=\"form-memo\" placeholder=\"Memo (Optional)\" />"
			+ "    <div class=\"row\">"
			+ "      <div class=\"col-sm-6\">"
			+ "        <button type=\"button\" class=\"btn btn-secondary btn-add-from\">Add Recipient</button>"
			+ "        <button type=\"button\" class=\"btn btn-warning btn-remove-from\">Remove Recipient</button>"
			+ "      </div>"
			+ "    </div>"
			+ "    <button type=\"submit\" class=\"btn btn-primary\" style=\"margin-top:10px;\">Send</button>"
			+ "  </form>"
			//+ "</div>"
  		  //+ "</div>"
		);
	}
	
	
	
}
