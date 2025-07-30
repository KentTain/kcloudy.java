package kc.service.app;

import java.util.List;
import java.util.UUID;

import kc.dataaccess.app.IApplicationLogRepository;
import kc.dto.app.AppSettingDTO;
import kc.dto.app.AppSettingPropertyDTO;
import kc.enums.app.AppLogType;
import kc.framework.util.SerializeHelper;
import kc.mapping.app.AppSettingMapping;
import kc.model.app.AppSetting;
import kc.model.app.AppSettingProperty;
import kc.model.app.ApplicationLog;
import kc.service.base.ServiceBase;
import kc.service.webapiservice.thridparty.IGlobalConfigApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import kc.dataaccess.app.IAppSettingPropertyRepository;
import kc.dataaccess.app.IAppSettingRepository;
import kc.dto.PaginatedBaseDTO;
import kc.framework.extension.StringExtensions;

@Service
public class AppSettingService extends ServiceBase implements IAppSettingService {

	private IAppSettingRepository _appSettingRepository;
	private IAppSettingPropertyRepository _appSettingPropertyRepository;
	private IApplicationLogRepository _applicationLogRepository;
	private AppSettingMapping _appSettingMapping;

	@Autowired
	public AppSettingService(IGlobalConfigApiService globalConfigApiService,
							 IAppSettingRepository _appSettingRepository,
							 IAppSettingPropertyRepository _appSettingPropertyRepository,
							 IApplicationLogRepository _applicationLogRepository,
							 AppSettingMapping _appSettingMapping) {
		super(globalConfigApiService);
		this._appSettingRepository = _appSettingRepository;
		this._appSettingPropertyRepository = _appSettingPropertyRepository;
		this._applicationLogRepository = _applicationLogRepository;
		this._appSettingMapping = _appSettingMapping;
	}

	@Override
	public List<AppSettingDTO> findAll() {
		List<AppSetting> data = _appSettingRepository.findAll();
		return _appSettingMapping.fromAppSettingList(data);
	}
	@Override
	public PaginatedBaseDTO<AppSettingDTO> findPaginatedAppSettingsByAppIdAndName(int pageIndex, int pageSize, String applicationId, String name) {
		Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
		Page<AppSetting> data = null;
		if (!StringExtensions.isNullOrEmpty(applicationId) && !StringExtensions.isNullOrEmpty(name)) {
			data = _appSettingRepository.findAllByNameAndApplication_ApplicationId(name, UUID.fromString(applicationId), pageable);
		} else if (!StringExtensions.isNullOrEmpty(applicationId)) {
			data = _appSettingRepository.findAllByApplication_ApplicationId(UUID.fromString(applicationId), pageable);
		} else {
			data = _appSettingRepository.findAll(pageable);
		}

		int total = data.getSize();
		List<AppSettingDTO> rows = _appSettingMapping.fromAppSettingList(data.getContent());
		return new PaginatedBaseDTO<>(pageIndex, pageSize, total, rows);
	}
	@Override
	public AppSettingDTO GetAppSettingById(int id) {
		AppSetting data = _appSettingRepository.findByPropertyId(id);
		return _appSettingMapping.fromAppSetting(data);
	}
	@Override
	public boolean SaveAppSetting(AppSettingDTO model) {
		AppSetting data = _appSettingMapping.toAppSetting(model);
		_appSettingRepository.save(data);
		//添加日志
		ApplicationLog log = new ApplicationLog();
		log.setAppLogType(AppLogType.AppSetting.getIndex());
		log.setReferenceId(String.valueOf(data.getPropertyId()));
		log.setReferenceName(String.valueOf(data.getName()));
		log.setOperatorId(model.getCreatedBy());
		log.setOperator(model.getCreatedName());
		log.setRemark(model.isEditMode()
				? String.format("编辑表单定义数据: %s", data.getName())
				: String.format("新增表单定义数据: %s", data.getName()));
		_applicationLogRepository.saveAndFlush(log);
		return true;
	}
	@Override
	public boolean SoftRemoveAppSettingById(int id, String currentUserId, String currentUserName) {
		AppSetting data = _appSettingRepository.findByPropertyId(id);
		data.setDeleted(true);
		_appSettingRepository.save(data);

		//添加日志
		ApplicationLog log = new ApplicationLog();
		log.setAppLogType(AppLogType.AppSetting.getIndex());
		log.setReferenceId(String.valueOf(data.getPropertyId()));
		log.setReferenceName(String.valueOf(data.getName()));
		log.setOperatorId(currentUserId);
		log.setOperator(currentUserName);
		log.setRefObjectJson(SerializeHelper.ToJson(data));
		log.setRemark(String.format("删除表单定义数据: %s", data.getName()));
		return true;
	}


	@Override
	public List<AppSettingPropertyDTO> GetAppSettingPropertiesBySettingId(int settingId) {
		List<AppSettingProperty> data = _appSettingPropertyRepository.findByAppSettingId(settingId);
		return _appSettingMapping.fromAppSettingPropertyList(data);
	}
	@Override
	public AppSettingPropertyDTO GetPropertyById(int id) {
		AppSettingProperty data = _appSettingPropertyRepository.getById(id);
		return _appSettingMapping.fromAppSettingProperty(data);
	}
	@Override
	public boolean SaveAppSettingProperty(AppSettingPropertyDTO data) {
		AppSettingProperty model = _appSettingMapping.toAppSettingProperty(data);
		_appSettingPropertyRepository.saveAndFlush(model);
		return true;
	}
	@Override
	public boolean SoftRemoveAppSettingPropertyById(int id) {
		AppSettingProperty data = _appSettingPropertyRepository.getById(id);
		data.setDeleted(true);
		_appSettingPropertyRepository.saveAndFlush(data);
		return true;
	}
}
