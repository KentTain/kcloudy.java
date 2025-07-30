package kc.database;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import kc.framework.base.EntityBase;

@Entity(name = "test_user")
public class TestObj extends EntityBase implements Serializable {
	
	private static final long serialVersionUID = 6752882874280690568L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="user_id", nullable = false, unique = true)
    private long userId;
	@Column(name="user_name", nullable = false, unique = true)
    private String userName;
	@Column(name="user_birthday", nullable = false)
    private Date userBirthday;
	@Column(name="user_salary", nullable = true)
    private Double userSalary;

    public TestObj() {
		super();
	}
	public TestObj(String userName,Date userBirthday,Double userSalary) {
		super();
		this.userName = userName;
		this.userBirthday = userBirthday;
		this.userSalary = userSalary;
	}
	
    public long getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public Date getUserBirthday() {
        return userBirthday;
    }

    public void setUserBirthday(Date userBirthday) {
        this.userBirthday = userBirthday;
    }

    public Double getUserSalary() {
        return userSalary;
    }

    public void setUserSalary(Double userSalary) {
        this.userSalary = userSalary;
    }
    
    @Override
    public String toString() {
        return "User [id=" + userId + ", userName=" + userName + ", userBirthday="
                + userBirthday + ", userSalary=" + userSalary + "]";
    }
}