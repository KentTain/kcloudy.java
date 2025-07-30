package kc.webapi.controller;

import java.util.List;

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
import kc.dto.config.ConfigEntityDTO;
import kc.framework.base.SeedEntity;
import kc.service.base.ServiceResult;
import kc.service.config.IConfigService;
import kc.service.config.ISeedService;
import kc.web.base.WebApiBaseController;
import springfox.documentation.annotations.ApiIgnore;


@RestController
@lombok.extern.slf4j.Slf4j
@RequestMapping("/Api/Config")
@Api(tags = "配置相关接口", description = "提供用户相关的 Rest API")
public class ConfigApiController extends WebApiBaseController {
	@Autowired
	private IConfigService ConfigService;
	@Autowired
	protected ISeedService SeedService;

	/**
	 * 根据配置名称，获取系统生成的业务单据编码
	 * @param seqName	业务名称
	 * @return ServiceResult<String>
	 */
	@PostMapping("/getSeedCodeByName")
	@ApiOperation("获取生成好的编码")
	public ServiceResult<String> getSeedCodeByName(String seqName) {
		return GetServiceResult(() -> {
			return SeedService.getSeedCodeByName(seqName, 1);
		}, log);
	}

	/**
	 * 根据配置名称，获取系统生成的业务单据编码对象
	 * @param seqName	业务名称
	 * @param step		步长
	 * @return ServiceResult<SeedEntity>
	 */
	@PostMapping("/getSeedEntityByName")
	@ApiOperation("获取生成好的编码对象")
	public ServiceResult<SeedEntity> getSeedEntityByName(String seqName, int step) {
		return GetServiceResult(() -> {
			return SeedService.getSeedEntityByName(seqName, step);
		}, log);
    }
	
	@PostMapping("/add")
	@ApiOperation("新增配置接口")
	public boolean addConfig(@RequestBody ConfigEntityDTO user) {
        return false;
    }
	
    @GetMapping("/GetAllConfigs")
    public ServiceResult<List<ConfigEntityDTO>> GetAllConfigs() {
    	return GetServiceResult(() -> {
			return ConfigService.findAll();
		}, log);
    }
    
    @GetMapping("/GetAllConfigs/{id}")
    public ServiceResult<ConfigEntityDTO> findConfigById(@PathVariable("id") int id) {
    	return GetServiceResult(() -> {
			return ConfigService.GetConfigById(id);
		}, log);
    }
    
    @PutMapping("/update")
    public boolean updateConfig(@RequestBody ConfigEntityDTO user) {
        return true;
    }
    
    @ApiIgnore
    @DeleteMapping("/delete/{id}")
    public boolean deleteConfig(@PathVariable("id") int id) {
        return ConfigService.SoftRemoveConfigAttributeById(id);
    }
}
