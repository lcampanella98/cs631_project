package pages;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SignOut extends EasyPayBaseServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);
		
		resp.setContentType("text/html");
		this.title = "Sign Out";
		
		printPreHTML();
		out.println("<h3>Thanks for using the TIJN EasyPay Service!</h3>");
		out.println("<a href=\"./SignIn\"><button class=\"btn btn-primary\">Sign In</button></a>");
		printPostHTML();
	}
	
}
