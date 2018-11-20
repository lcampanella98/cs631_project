package pages;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.UserAccount;

public abstract class EasyPayServlet extends EasyPayBaseServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2484043894353865271L;

	
	
	protected String ssn;
	protected UserAccount user;
	
	public EasyPayServlet() {}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);
		ssn = req.getParameter("ssn");
	}
	
	protected void printPreHTML() {
		out.println(docType + 
			"<html>\n" 
				+ "<head>"
					+ "<title>" + title + "</title>"
					+ "<link href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO\" crossorigin=\"anonymous\">"
					+ "<script src=\"https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js\" integrity=\"sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy\" crossorigin=\"anonymous\"></script>"
				+ "</head>\n"
				+ "<body>\n");
		out.println("<nav class=\"navbar navbar-expand-lg navbar-light bg-light\">"
				+ "<a class=\"navbar-brand\" href=\"#\">TIJN EasyPay</a>"
				+ "<div class=\"collapse navbar-collapse\" id=\"navbarSupportedContent\">\r\n" + 
				"    <ul class=\"navbar-nav mr-auto\">\r\n" + 
				"      <li class=\"nav-item\">\r\n" + 
				"        <a class=\"nav-link\" href=\"/MyAccount?ssn=" + encParam(ssn) + "\">My Account</a>\r\n" + 
				"      </li>\r\n" + 
				"      <li class=\"nav-item\">\r\n" + 
				"        <a class=\"nav-link\" href=\"/SendMoney?ssn=" + encParam(ssn) + "\">Send Money</a>\r\n" + 
				"      </li>\r\n" + 
				"      <li class=\"nav-item\">\r\n" + 
				"        <a class=\"nav-link\" href=\"/RequestMoney?ssn=" + encParam(ssn) + "\">Request Money</a>\r\n" + 
				"      </li>\r\n" +  
				"      <li class=\"nav-item\">\r\n" + 
				"        <a class=\"nav-link\" href=\"/Statements?ssn=" + encParam(ssn) + "\">Statements</a>\r\n" + 
				"      </li>\r\n" +  
				"      <li class=\"nav-item\">\r\n" + 
				"        <a class=\"nav-link\" href=\"/SearchTransactions?ssn=" + encParam(ssn) + "\">Search Transactions</a>\r\n" + 
				"      </li>\r\n" +  
				"      <li class=\"nav-item\">\r\n" + 
				"        <a class=\"nav-link disabled\" href=\"/SignOut\">Sign Out</a>\r\n" + 
				"      </li>\r\n" + 
				"    </ul>" +
				"</div>\r\n" + 
				"</nav>"
		);
		out.println("<div class=\"container\">\n" 
					+ "<h1 style=\"text-align:center;\">" + title + "</h1><hr />\n"
		);
	}
	
	protected void printPostHTML() {
		out.println("</div></body></html>");
	}

}
