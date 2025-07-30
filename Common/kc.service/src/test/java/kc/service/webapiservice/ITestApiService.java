package kc.service.webapiservice;

import java.util.List;

public interface ITestApiService {
	List<String> Get();
	String GetString();
	String GetById(int userId);
}
