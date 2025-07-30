package kc.service.webapiservice;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;

import kc.framework.tenant.ApplicationConstant;
import kc.service.webapiservice.impl.IdSrvWebApiServiceBase;

@Service
public class TestApiService extends IdSrvWebApiServiceBase implements ITestApiService {
	private final static String ServiceName = "kc.service.webapi.TestApiService";
	public TestApiService() {
	}

	@Override
	public List<String> Get() {
		List<String> result = new ArrayList<String>();
		result = WebSendGet(
			new TypeReference<List<String>>() {},
            ServiceName + ".Get",
            AccountApiUrl() + "TestApi",
            ApplicationConstant.AccScope,
            callback ->
            {
            	return callback;
            },
            failCallback ->
            {
            	failCallback.getErrMsg();
            },
            true);

         return result;
	}
	
	@Override
	public String GetString() {
		String result = null;
		result = WebSendGet(
			new TypeReference<String>() {},
            ServiceName + ".Get",
            AccountApiUrl() + "TestApi",
            ApplicationConstant.AccScope,
            callback ->
            {
            	return callback;
            },
            failCallback ->
            {
            	failCallback.getErrMsg();
            },
            true);

         return result;
	}

	@Override
	public String GetById(int userId) {
		String result = null;
		result = WebSendGet(
			new TypeReference<String>() {},
            ServiceName + ".GetById",
            AccountApiUrl() + "TestApi/" + userId,
            ApplicationConstant.AccScope,
            callback ->
            {
            	return callback;
            },
            failCallback ->
            {
            	failCallback.getErrMsg();
            },
            true);

         return result;
	}

}
