package kc.service.webapiservice.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;

import kc.dto.config.ConfigEntityDTO;
import kc.framework.base.SeedEntity;
import kc.framework.enums.ConfigType;
import kc.framework.tenant.ApplicationConstant;
import kc.service.base.ServiceResult;
import kc.service.webapiservice.IConfigApiService;

@Service @lombok.extern.slf4j.Slf4j
public class ConfigApiService extends IdSrvWebApiServiceBase implements IConfigApiService {
    private final static String ServiceName = "kc.service.webapi.impl.ConfigApiService";

    public ConfigApiService() {
    }

    /**
     * 获取系统编码
     *
     * @param name 配置名称
     * @return
     */
    @Override
    public String GetSeedCodeByName(String name) {
        ServiceResult<String> result = null;
        result = WebSendGet(new TypeReference<ServiceResult<String>>() {}, ServiceName + ".GetSeedCodeByName",
                ConfigApiUrl() + "ConfigApi/GetSeedCodeByName?name=" + name, ApplicationConstant.CfgScope, callback -> {
                    return callback;
                }, failCallback -> {
                    log.error(ServiceName + " throw error: " + failCallback.toString());
                }, true);

        if (result != null && result.isSuccess() && result.getResult() != null) {
            return result.getResult();
        }

        return null;
    }

    /**
     * 获取系统编码
     *
     * @param name 配置名称
     * @param step 步长
     * @return
     */
    @Override
    public SeedEntity GetSeedEntityByName(String name, int step) {
        ServiceResult<SeedEntity> result = null;
        result = WebSendGet(new TypeReference<ServiceResult<SeedEntity>>() {}, ServiceName + ".GetSeedEntityByName",
                ConfigApiUrl() + "ConfigApi/GetSeedEntityByName?name=" + name + "&step=" + step,
                ApplicationConstant.CfgScope, callback -> {
                    return callback;
                }, failCallback -> {
                    log.error(ServiceName + " throw error: " + failCallback.toString());
                }, true);

        if (result != null && result.isSuccess() && result.getResult() != null) {
            return result.getResult();
        }

        return null;
    }

    /**
     * 根据配置文件类型获取配置文件
     *
     * @param type 配置文件类型
     * @return
     */
    @Override
    public List<ConfigEntityDTO> GetAllConfigsByType(ConfigType type) {
        int t = type.getIndex();
        ServiceResult<List<ConfigEntityDTO>> result = null;
        result = WebSendGet(new TypeReference<ServiceResult<List<ConfigEntityDTO>>>() {},
                ServiceName + ".GetAllConfigsByType", ConfigApiUrl() + "ConfigApi/GetAllConfigsByType?type=" + t,
                ApplicationConstant.CfgScope, callback -> {
                    return callback;
                }, failCallback -> {
                    log.error(ServiceName + " throw error: " + failCallback.toString());
                }, true);

        if (result != null && result.isSuccess() && result.getResult() != null) {
            return result.getResult();
        }

        return null;
    }

    /**
     * 根据配置文件Id获取配置文件
     *
     * @param configId 配置文件Id
     * @return
     */
    @Override
    public ConfigEntityDTO GetConfigById(int configId) {
        ServiceResult<ConfigEntityDTO> result = null;
        result = WebSendGet(new TypeReference<ServiceResult<ConfigEntityDTO>>() {}, ServiceName + ".GetConfigById",
                ConfigApiUrl() + "ConfigApi/GetConfigById?configId=" + configId, ApplicationConstant.CfgScope,
                callback -> {
                    return callback;
                }, failCallback -> {
                    log.error(ServiceName + " throw error: " + failCallback.toString());
                }, true);

        if (result != null && result.isSuccess() && result.getResult() != null) {
            return result.getResult();
        }

        return null;
    }

}
