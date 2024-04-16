package jdbc;

public class Account {
	private String name;
	private String username;
	private String password;
	private String role;
	
	public Account() {
		super();
	}

	public String getRole() {
		return role;
	}


	public void setRole(String role) {
		this.role = role;
	}


	public Account(String name, String username, String password,String role) {
		super();
		this.name = name;
		this.username = username;
		this.password = password;
		this.role = role;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	@Override
	public String toString() {
		return "Account [name=" + name + ", username=" + username + ", password=" + password + ", role=" + role + "]";
	}


	
	

}
