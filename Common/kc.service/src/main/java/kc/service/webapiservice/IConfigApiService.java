package kc.service.webapiservice;

import kc.dto.config.ConfigEntityDTO;
import kc.framework.base.SeedEntity;
import kc.framework.enums.ConfigType;

import java.util.List;

public interface IConfigApiService {
    /**
     * 获取系统编码
     *
     * @param name 配置名称
     * @return
     */
    String GetSeedCodeByName(String name);

    /**
     * 获取系统编码
     *
     * @param name 配置名称
     * @param step 步长
     * @return
     */
    SeedEntity GetSeedEntityByName(String name, int step);

    /**
     * 根据配置文件类型获取配置文件
     *
     * @param type 配置文件类型
     * @return
     */
    List<ConfigEntityDTO> GetAllConfigsByType(ConfigType type);

    /**
     * 根据配置文件Id获取配置文件
     *
     * @param configId 配置文件Id
     * @return
     */
    ConfigEntityDTO GetConfigById(int configId);
}
