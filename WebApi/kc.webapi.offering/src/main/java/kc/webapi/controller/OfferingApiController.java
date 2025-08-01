package kc.webapi.controller;

import java.util.Arrays;
import java.util.List;

import kc.dto.PaginatedBaseDTO;
import kc.dto.offering.OfferingDTO;
import kc.enums.offering.OfferingType;
import kc.enums.offering.OfferingVersion;
import kc.service.offering.IOfferingService;
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
@Api(tags = "配置相关接口", description = "提供用户相关的 Rest API")
public class OfferingApiController extends WebApiBaseController {
	@Autowired
	private IOfferingService OfferingService;

    @GetMapping("/findPaginatedOfferingsByType")
    public ServiceResult<PaginatedBaseDTO<OfferingDTO>> findPaginatedOfferingsByType(
    		int pageIndex, int pageSize, OfferingType offeringType) {
    	return GetServiceResult(() -> {
			//List<OfferingVersion> versions = Arrays.asList(OfferingVersion.Free, OfferingVersion.TryOut, OfferingVersion.Normal, OfferingVersion.Enterprise, OfferingVersion.Group);
			return OfferingService.findPaginatedOfferingsByType(pageIndex, pageSize, offeringType);
		}, log);
    }

    @GetMapping("/getOfferingById/{id}")
    public ServiceResult<OfferingDTO> GetOfferingById(@PathVariable("id") int id) {
    	return GetServiceResult(() -> {
			return OfferingService.GetOfferingById(id);
		}, log);
    }

}
