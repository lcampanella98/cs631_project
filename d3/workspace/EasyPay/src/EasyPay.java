
// Import required java libraries
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

// Extend HttpServlet class
public class EasyPay extends HttpServlet {

	private static final String CON_STR = "jdbc:mysql://localhost:3306/world";
	private String message;

	public void init() throws ServletException {
		// Do required initialization
		message = "Hello World";
	}

	public Connection getDBConnection() {
		try {
			//Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(CON_STR, "root", "password");
			return con;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Set response content type
		response.setContentType("text/html");

		// Actual logic goes here.
		PrintWriter out = response.getWriter();
		String title = "Using GET Method to Read Form Data";
		String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";
		
		Connection con = getDBConnection();
		String cityMsg = null;
		if (con != null) {
			try {
				Statement stmt = con.createStatement();
				ResultSet r = stmt.executeQuery("SELECT * FROM city WHERE id=1");
				if (r.first()) {
					cityMsg = r.getString(r.findColumn("name"));
				} else {
					cityMsg = "No row returned";
				}
			} catch (SQLException e) {
				e.printStackTrace();
				cityMsg = "unable to find result";
			}
		} else {
			cityMsg = "unable to connect";
		}

		out.println(docType + "<html>\n" + "<head><title>" + title + "</title></head>\n"
				+ "<body bgcolor = \"#f0f0f0\">\n" + "<h1 align = \"center\">" + title + "</h1>\n" + "<ul>\n"
				+ "  <li><b>First Name</b>: " + request.getParameter("first_name") + "\n" + "  <li><b>Last Name</b>: "
				+ request.getParameter("last_name") + "\n" + "</ul>\n" + "<p>" + cityMsg + "</p>" + "</body>" + "</html>");
	}

	public void destroy() {
		// do nothing.
	}
}