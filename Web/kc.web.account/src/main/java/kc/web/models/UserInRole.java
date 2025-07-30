package kc.web.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserInRole implements Serializable{

	private static final long serialVersionUID = -4349042198518597830L;

	public String roleId;

    public List<String> addList = new ArrayList<String>();

    public List<String> delList = new ArrayList<String>();

}
