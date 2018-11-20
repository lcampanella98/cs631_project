package pages;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import dataaccess.EasyPayService;
import models.UserAccount;

public abstract class EasyPayServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2484043894353865271L;
	
	private String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";
	protected String title;
	
	protected EasyPayService _easyPayService;
	
	protected String ssn;
	protected UserAccount user;
	protected PrintWriter out;
	
	public EasyPayServlet() {}
	
	public void init() throws ServletException {
		_easyPayService = new EasyPayService();
	}
	
	public void destroy() {
		_easyPayService.closeConnection();
	}
	
	protected void setTitle(String title) {
		this.title = title;
	}
	
	protected void printPreHTML(PrintWriter out) {
		out.println(docType + 
			"<html>\n" 
				+ "<head>"
					+ "<title>" + title + "</title>"
					+ "<link href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO\" crossorigin=\"anonymous\">"
					+ "<script src=\"https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js\" integrity=\"sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy\" crossorigin=\"anonymous\"></script>"
				+ "</head>\n"
				+ "<body>\n"
				+ "<div class=\"container\">\n" 
					+ "<h1 style=\"text-align:center;\">" + title + "</h1><hr />\n"
		);
	}
	
	protected void printPostHTML(PrintWriter out) {
		out.println("</div></body></html>");
	}
	
	protected String encParam(String param) {
		try {
			return URLEncoder.encode(param, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

}
