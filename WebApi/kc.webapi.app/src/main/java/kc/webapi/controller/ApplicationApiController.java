package kc.webapi.controller;

import java.util.List;
import java.util.UUID;

import kc.dto.app.ApplicationDTO;
import kc.framework.tenant.RoleConstants;
import kc.service.app.IApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kc.service.base.ServiceResult;
import kc.web.base.WebApiBaseController;
import springfox.documentation.annotations.ApiIgnore;


@RestController
@lombok.extern.slf4j.Slf4j
@RequestMapping("/Api/Application")
@Api(tags = "应用相关接口", description = "提供用户相关的 Rest API")
public class ApplicationApiController extends WebApiBaseController {
	@Autowired
	private IApplicationService ApplicationService;
	
	@PostMapping("/add")
	@ApiOperation("新增应用接口")
	public boolean addApplication(@RequestBody ApplicationDTO user) {
        return false;
    }
	
    @GetMapping("/GetAllApplications")
    public ServiceResult<List<ApplicationDTO>> GetAllApplications() {
    	return GetServiceResult(() -> {
			return ApplicationService.findAll();
		}, log);
    }
    
    @GetMapping("/GetAllApplications/{id}")
    public ServiceResult<ApplicationDTO> findApplicationById(@PathVariable("id") String id) {
    	return GetServiceResult(() -> {
			return ApplicationService.GetApplicationById(id);
		}, log);
    }
    
    @PutMapping("/update")
    public boolean updateApplication(@RequestBody ApplicationDTO user) {
        return true;
    }
    
    @ApiIgnore
    @DeleteMapping("/delete/{id}")
    public boolean deleteApplication(@PathVariable("id") String id) {
        return ApplicationService.SoftRemoveApplicationById(id, RoleConstants.AdminUserId, RoleConstants.AdminUserName);
    }
}
