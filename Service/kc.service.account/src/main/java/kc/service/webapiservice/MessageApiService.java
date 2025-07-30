package kc.service.webapiservice;

import com.fasterxml.jackson.core.type.TypeReference;
import kc.dto.message.MemberRemindMessageDTO;
import kc.dto.message.NewsBulletinDTO;
import kc.enums.MessageStatus;
import kc.framework.tenant.ApplicationConstant;
import kc.enums.NewsBulletinType;
import kc.service.base.ServiceResult;
import kc.service.webapiservice.impl.IdSrvWebApiServiceBase;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageApiService extends IdSrvWebApiServiceBase implements IMessageApiService {
	private final static String ServiceName = "kc.service.webapiservice.IMessageApiService";

	@Override
	public List<NewsBulletinDTO> LoadLatestNewsBulletins(NewsBulletinType type) {
		String t = type != null ? type.getIndex().toString() : "";
		ServiceResult<List<NewsBulletinDTO>> result = null;
		result = WebSendGet(
			new TypeReference<ServiceResult<List<NewsBulletinDTO>>>() {},
            ServiceName + ".LoadLatestNewsBulletins",
				MessageApiUrl() + "NewsBulletinApi/LoadLatestNewsBulletins?type=" + t,
            ApplicationConstant.MsgScope,
            callback ->
            {
            	return callback;
            },
            failCallback ->
            {
            	failCallback.getErrMsg();
            },
            true);

		if (result != null && result.isSuccess() && result.getResult() != null)
		{
			return result.getResult();
		}

		return null;
	}

	@Override
	public List<MemberRemindMessageDTO> LoadTop10UserMessages(String userId, MessageStatus status) {
		String s = status != null ? status.getIndex().toString() : "";
		ServiceResult<List<MemberRemindMessageDTO>> result = null;
		result = WebSendGet(
				new TypeReference<ServiceResult<List<MemberRemindMessageDTO>>>() {},
				ServiceName + ".LoadTop10UserMessages",
				MessageApiUrl() + "MessageApi/LoadTop10UserMessages?userId=" + userId + "&status=" + s,
				ApplicationConstant.MsgScope,
				callback ->
				{
					return callback;
				},
				failCallback ->
				{
					failCallback.getErrMsg();
				},
				true);

		if (result != null && result.isSuccess() && result.getResult() != null)
		{
			return result.getResult();
		}

		return null;
	}

	@Override
	public boolean ReadRemindMessage(int id) {
		ServiceResult<Boolean> result = null;
		result = WebSendGet(
			new TypeReference<ServiceResult<Boolean>>() {},
            ServiceName + ".ReadRemindMessage",
				MessageApiUrl() + "MessageApi/ReadRemindMessage?id=" + id,
            ApplicationConstant.MsgScope,
            callback ->
            {
            	return callback;
            },
            failCallback ->
            {
            	failCallback.getErrMsg();
            },
            true);

		if (result != null && result.isSuccess() && result.getResult() != null)
		{
			return result.getResult();
		}

		return false;
	}

}
