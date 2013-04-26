package tdl.client;

import java.io.Serializable;
import java.util.Date;

/**
 * Provides objects that encapsulate information about a single task.
 */
public class Task implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String key;
	private String name;
	private Date date;
	private String email;
	
	public Task(String key, String name, Date date, String email) {
		this.key = key;
		this.name = name;
		this.date = date;
		this.email = email;
	}
	
	public Task () {
	}
	
	public String getKey() {
		return key;
	}
	public String getName() {
		return name;
	}
	public String getDate () {
		return (date == null) ? "" : date.toString();
	}
	public String getEmail() {
		return email;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setDate (Date date) {
		this.date = date;
	}
	public void setEmail (String email) {
		this.email = email;
	}
	

}
