package models;

public class MonthYear implements Comparable<MonthYear> {
	public int month;
	public int year;
	
	public MonthYear() {
		
	}
	
	public MonthYear(int month, int year) {
		this.month = month;
		this.year = year;
	}
	
	@Override
	public int compareTo(MonthYear o) {
		if (year > o.year) return -1;
		if (year < o.year) return 1;
		if (month > o.month) return -1;
		if (month < o.month) return 1;
		return 0;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof MonthYear)) return false;
		MonthYear o = (MonthYear)obj;
		return year == o.year && month == o.month;
	}
	
	@Override
	public int hashCode() {
		int result = year;
        result = 31 * year + month;
        return result;
	}
	
}