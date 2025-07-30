package kc.service.webapiservice;

import kc.dto.message.MemberRemindMessageDTO;
import kc.dto.message.NewsBulletinDTO;
import kc.enums.MessageStatus;
import kc.enums.NewsBulletinType;

import java.util.List;

public interface IMessageApiService {
	List<NewsBulletinDTO> LoadLatestNewsBulletins(NewsBulletinType type);

	List<MemberRemindMessageDTO> LoadTop10UserMessages(String userid, MessageStatus status);

	boolean ReadRemindMessage(int id);
}
