package renderers;

import java.io.PrintWriter;

public abstract class EasyPayRenderer {
	
	protected PrintWriter out;
	protected String ssn;
	
	public EasyPayRenderer(String ssn, PrintWriter out) {
		this.out = out;
		this.ssn = ssn;
	}
	
}
