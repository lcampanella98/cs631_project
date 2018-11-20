import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.UserAccount;
public class MyAccount extends EasyPayServlet{
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		setTitle("Using GET Method to Read Form Data");
		PrintWriter out = response.getWriter();
		
		String ssn = request.getParameter("ssn");
		
		UserAccount u = null;
		if (ssn != null && !ssn.isEmpty()) {
			u = _easyPayService.getUserAccountFromSSN(request.getParameter("ssn"));
		}
		

		printPreHTML(out);
		out.println("<p>you passed in ssn: " + ssn + "</p>");
		if (u != null) {
			out.println("<p>SSN: " + u.SSN + "</p>");
			out.println("<p>Name: " + u.Name + "</p>");
			out.println("<p>Balance: $" + u.Balance + "</p>");
			out.println("<p>Primary Bank Account: </p>");
			out.println("<ul style=\"list-style-type:none\">");
			out.println("<li>Bank: " + u.primaryAccount.BankID + "</li>");
			out.println("<li>Account No: " + u.primaryAccount.BANumber + "</li>");
		} else {
			out.println("<p>User with ssn " + ssn + " not found");
		}
		printPostHTML(out);
		
	}

}
