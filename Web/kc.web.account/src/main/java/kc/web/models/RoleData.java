package kc.web.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import kc.dto.account.MenuNodeDTO;
import kc.dto.account.PermissionDTO;
import kc.dto.account.UserDTO;

public class RoleData implements Serializable{

	private static final long serialVersionUID = -3450688746087536625L;

	public UUID RoleId;

    public List<PermissionDTO> Permissions = new ArrayList<PermissionDTO>();

    public List<MenuNodeDTO> MenuNodes = new ArrayList<MenuNodeDTO>();

    public List<UserDTO> Users = new ArrayList<UserDTO>();

    public List<Dictionarys> RoleList = new ArrayList<Dictionarys>();

    public class Dictionarys
    {
        public String Key;
        
        public String Value;

        public boolean checked;
        
        public List<String> Itms = new ArrayList<String>();

        public UUID AppId;
    }
}
