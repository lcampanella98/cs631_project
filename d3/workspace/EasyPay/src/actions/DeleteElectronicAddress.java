package actions;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pages.EasyPayServlet;

public class DeleteElectronicAddress extends EasyPayServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ssn = req.getParameter("ssn");
		String identifier = req.getParameter("identifier");
		
		_easyPayService.deleteElectronicAddress(identifier);
		resp.sendRedirect("/MyAccount?ssn=" + URLEncoder.encode(ssn, "UTF-8"));
	}
}
