package pages;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SignIn extends EasyPayBaseServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);
		
		resp.setContentType("text/html");
		this.title = "Sign In";
		
		String ssn = req.getParameter("ssn");
		boolean invalidUser = false;
		if (ssn != null) {
			// try to sign in
			if (_easyPayService.getUserAccountFromSSN(ssn) != null) {
				// we found the user
				resp.sendRedirect("./MyAccount?ssn=" + encParam(ssn));
				return;
			} else {
				invalidUser = true;
			}
		}
		
		printPreHTML();
		out.println("<div style=\"text-align:center;\">"
						+ "<form method=\"get\" action=\"./SignIn\" style=\"display:inline-block;\">"
							+ "<input type=\"text\" class=\"form-control\" name=\"ssn\" placeholder=\"Enter SSN\" />"
							+ "<button style=\"margin-top:15px;\" class=\"btn btn-primary\" type=\"submit\">Sign In</button>"
				);
		if (invalidUser) {
			out.println("<p class=\"text-danger\">SSN not in system</p>");
		}
		out.println("</form></div>");
		printPostHTML();
	}
	
}
