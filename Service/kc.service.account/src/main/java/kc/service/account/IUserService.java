package kc.service.account;

import java.util.Date;
import java.util.List;

import kc.dto.PaginatedBaseDTO;
import kc.dto.account.UserDTO;
import kc.dto.account.UserSimpleDTO;
import kc.dto.account.UserTracingLogDTO;
import kc.service.base.echarts.Option;

public interface IUserService {
	List<UserDTO> GetAllUsersWithRolesAndOrgs();
	List<UserSimpleDTO> GetUserUsersByRoleIds(List<String> roleIds);

	PaginatedBaseDTO<UserDTO> findPaginatedUsersByFilter(int pageIndex, int pageSize, String email, String phone, String name, Integer status, Integer position, Integer orgId, boolean isClient);
	
	UserDTO findById(String userId);

	UserSimpleDTO findSimpleUserWithOrgAndRoleNameByUserId(String userId);

	boolean SaveUser(UserDTO model);
	
	boolean RemoveUserById(Integer id);

	boolean UpdateClientById(Integer id);

	boolean UpdateRoleInUser(String userId, List<String> addList, String operatorId, String operatorName);

	String ChangePassword(String userId, String OldPassword, String NewPassword);

	String ChangeMailPhone(String userId, String Email, String Phone);

    boolean ExistUserName(String userName);

	boolean ExistUserEmail(String email);

	boolean ExistUserPhone(String phone);

	PaginatedBaseDTO<UserTracingLogDTO> findPaginatedUserLogsByName(int pageIndex, int pageSize, String name);

	Option GetUserLoginReportData(Date startDate, Date endDate);
}
