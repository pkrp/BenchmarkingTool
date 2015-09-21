package uk.ac.rl.test.model;

public class ReportRow {
	private String type;
	private String query;
	private Double time;
	private String title;
	private String user;
	private Long size;

	public ReportRow(QueryType type, String title, String query, Long size, Double time, String user) {
		this.type = type.toString();
		this.query = query;
		this.title = title;
		this.size = size;
		this.user = user;
		this.time = time;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public Long getSize() {
		return size;
	}

	public void setType(QueryType type) {
		this.type = type.toString();
	}

	public String getType() {
		return type;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getQuery() {
		return query;
	}
	
	public String getUser() {
		return user;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public Double getTime() {
		return time;
	}
	
	public void setTime(Double time) {
		this.time = time;
	}
	
}
