package kc.component.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface IRedisHelper<T> {

    Long listLPush(String key, T domain);
    T listRPop(String key);
    T listRPop(String key, long validTime);
    long listLength(String key);
    List<T> listFindAll(String key);

    void publish(String pubChannel, String value);
    void publish(String pubChannel, T value);

    boolean stringSet(String key, T value);
    boolean stringSet(String key, T value, long validTime);
    T stringGet(String key, Class<T> clazz);
    String stringGet(String key);

    void del(String key);
    void batchDel(String prefix);
    void batchDel(Collection<String> keys);

    boolean exist(String key);

    Set<String> keys(String prefix);

    boolean setExpire(String key, long validTime);

    boolean setNx(String key, T value, long validTime);
    boolean setNx(String key, T value, long validTime, TimeUnit timeUnit);
}
