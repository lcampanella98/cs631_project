import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import dataaccess.EasyPayService;

public abstract class EasyPayServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2484043894353865271L;
	
	private String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";
	protected String title;
	
	protected EasyPayService _easyPayService;
	
	public EasyPayServlet() {
		_easyPayService = new EasyPayService();
	}
	
	public void init() throws ServletException {
		
	}
	
	public void destroy() {
		// do nothing.
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void printPreHTML(PrintWriter out) {
		out.println(docType + 
			"<html>\n" + "<head><title>" + title + "</title></head>\n"
				+ "<body>\n"
				+ "<div style=\"margin-left: 25px;margin-top:20px;>\n" 
					+ "<h1 style=\"text-align:center;\">" + title + "</h1>\n"
		);
	}
	
	public void printPostHTML(PrintWriter out) {
		out.println("</div></body></html>");
	}

}
