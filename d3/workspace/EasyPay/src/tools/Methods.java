package tools;

import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public final class Methods {
	
	public static String formatMoney(double money) {
		DecimalFormat df = new DecimalFormat("#.00");
		df.setMinimumIntegerDigits(1);
		return "$" + df.format(money);
	}
	
	public static String friendlyDate(Timestamp ts) {
		return ts.toLocalDateTime().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM));
	}
	
	public static String getMonth(int month) {
	    return new DateFormatSymbols().getMonths()[month-1];
	}
	
	public static String makePlural(String s, int i) {
		if (i == 1) return s;
		return s + "s";
	}
	
	public static boolean IsValidEmail(String email) {
		return email != null && email.matches(".+@.+");		
	}
	
	public static boolean IsValidPhone(String phone) {
		return phone != null && phone.matches("\\d[\\d\\s-]*");
	}

}
