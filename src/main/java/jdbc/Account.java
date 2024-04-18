package jdbc;

public class Account {

    private long idAccount;
	private String name;
	private String username;
	private String password;
	private String role;
    private String accessRight;

	public Account() {
		super();
	}

	public Account(long idAccount, String name, String username, String password, String role, String accessRight) {
        super();
        this.idAccount = idAccount;
        this.name = name;
        this.username = username;
        this.password = password;
        this.role = role;
        this.accessRight = accessRight;
    }

    public long getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(long idAccount) {
        this.idAccount = idAccount;
    }

    public String getRole() {
		return role;
	}

	public void setRole(String role) {
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


	public String getAccessRight() {
        return accessRight;
    }

    public void setAccessRight(String accessRight) {
        this.accessRight = accessRight;
    }

    @Override
    public String toString() {
        return "Account [idAccount=" + idAccount + ", name=" + name + ", username=" + username + ", password="
                + password + ", role=" + role + ", accessRight=" + accessRight + "]";
    }

}
