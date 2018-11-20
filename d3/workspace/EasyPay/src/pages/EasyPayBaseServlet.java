package pages;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dataaccess.EasyPayService;

public abstract class EasyPayBaseServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9007811510279142478L;
	
	protected static final String BASE_PATH = "/webapps8/";
	
	protected EasyPayService _easyPayService;
	
	protected String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";
	protected String title;
	
	protected PrintWriter out;
	
	public EasyPayBaseServlet() {}
	
	public void init() throws ServletException {
		
	}
	
	public void destroy() {
	}

	
	protected void setTitle(String title) {
		this.title = title;
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		out = resp.getWriter();
		_easyPayService = new EasyPayService();
	}
	
	protected void printPreHTML() {
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
	
	protected void printPostHTML() {
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
	
	protected String formatMoney(double money) {
		DecimalFormat df = new DecimalFormat("#.00");
		return "$" + df.format(money);
	}
}
