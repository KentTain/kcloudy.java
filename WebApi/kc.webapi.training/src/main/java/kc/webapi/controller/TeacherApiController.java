package kc.webapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kc.dto.training.TeacherDTO;
import kc.service.base.ServiceResult;
import kc.service.training.ITeacherService;
import kc.web.base.WebApiBaseController;

@RestController
@lombok.extern.slf4j.Slf4j
@RequestMapping("/Api/Config")
@Api(tags = "配置相关接口", description = "提供用户相关的 Rest API")
public class TeacherApiController extends WebApiBaseController {
	@Autowired
	private ITeacherService _teacherService;

	@PostMapping("/add")
	@ApiOperation("新增配置接口")
	public boolean addConfig(@RequestBody TeacherDTO user) {
        return false;
    }
	
    @GetMapping("/GetAllConfigs")
    public ServiceResult<List<TeacherDTO>> GetAllConfigs() {
    	return GetServiceResult(() -> {
			return _teacherService.findAll();
		}, log);
    }
    
    @GetMapping("/GetAllConfigs/{id}")
    public ServiceResult<TeacherDTO> findConfigById(@PathVariable("id") int id) {
    	return GetServiceResult(() -> {
			return _teacherService.GetTeacherById(id);
		}, log);
    }
    
    @PutMapping("/update")
    public boolean updateConfig(@RequestBody TeacherDTO user) {
        return true;
    }
    
}
