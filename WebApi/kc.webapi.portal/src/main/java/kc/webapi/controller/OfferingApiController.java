package kc.webapi.controller;

import java.util.List;

import kc.dto.portal.OfferingCategoryDTO;
import kc.service.portal.IOfferingService;
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
import kc.framework.base.SeedEntity;
import kc.service.base.ServiceResult;
import kc.web.base.WebApiBaseController;
import springfox.documentation.annotations.ApiIgnore;


@RestController
@lombok.extern.slf4j.Slf4j
@RequestMapping("/Api/Offering")
@Api(tags = "门户相关接口", description = "提供门户产品相关的 Rest API")
public class OfferingApiController extends WebApiBaseController {
    @Autowired
    private IOfferingService OfferingService;


    @GetMapping("/LoadCategoryList")
    public ServiceResult<List<OfferingCategoryDTO>> LoadCategoryList(String name) {
    	return GetServiceResult(() -> {
			return OfferingService.GetRootOfferingCategoriesByName(name);
		}, log);
    }
    
    @GetMapping("/findCategoryById/{id}")
    public ServiceResult<OfferingCategoryDTO> findCategoryById(@PathVariable("id") int id) {
    	return GetServiceResult(() -> {
			return OfferingService.GetCategoryById(id);
		}, log);
    }
    
    @PutMapping("/update")
    public boolean updateConfig(@RequestBody OfferingCategoryDTO user) {
        return true;
    }
    
    @ApiIgnore
    @DeleteMapping("/delete/{id}")
    public boolean deleteCategory(@PathVariable("id") int id) {
        return OfferingService.RemoveCategory(id);
    }
}
