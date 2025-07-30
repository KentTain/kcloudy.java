package kc.webapi.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/Api/Config")
@Api(tags = "用户相关接口", description = "提供用户相关的 Rest API")
public class AccountApiController {

    @ApiIgnore
    @DeleteMapping("/delete/{id}")
    public boolean deleteConfig(@PathVariable("id") int id) {
        return true;
    }
}
