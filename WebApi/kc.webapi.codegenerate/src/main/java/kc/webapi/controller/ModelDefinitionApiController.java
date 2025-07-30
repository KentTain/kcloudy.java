package kc.webapi.controller;

import java.util.List;

import kc.dto.codegenerate.ModelDefinitionDTO;
import kc.dto.codegenerate.ModelDefFieldDTO;
import kc.dto.codegenerate.ModelDefinitionDTO;
import kc.framework.tenant.RoleConstants;
import kc.service.codegenerate.IModelDefinitionService;
import kc.service.codegenerate.IModelDefinitionService;
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
@RequestMapping("/Api/ModelDefinition")
@Api(tags = "数据模型相关接口", description = "提供数据模型相关的 Rest API")
public class ModelDefinitionApiController extends WebApiBaseController {
	@Autowired
	private IModelDefinitionService modelDefinitionService;
	
	@PostMapping("/add")
	@ApiOperation("新增数据模型接口")
	public boolean addModelDefinition(@RequestBody ModelDefinitionDTO user) {
        return false;
    }
	
    @GetMapping("/GetAllModelDefinitions")
    public ServiceResult<List<ModelDefFieldDTO>> GetAllModelDefinitions(int defId) {
    	return GetServiceResult(() -> {
			return modelDefinitionService.findAllModelDefFieldsByDefId(defId);
		}, log);
    }
    
    @GetMapping("/GetAllModelDefinitions/{id}")
    public ServiceResult<ModelDefinitionDTO> findModelDefinitionById(@PathVariable("id") int id) {
    	return GetServiceResult(() -> {
			return modelDefinitionService.getModelDefinitionById(id);
		}, log);
    }
    
    @PutMapping("/update")
    public boolean updateModelDefinition(@RequestBody ModelDefinitionDTO user) {
        return true;
    }
    
    @ApiIgnore
    @DeleteMapping("/delete/{id}")
    public boolean deleteModelDefinition(@PathVariable("id") int id) {
        return modelDefinitionService.removeModelDefinitionById(id, RoleConstants.AdminUserId, RoleConstants.AdminUserName);
    }
}
