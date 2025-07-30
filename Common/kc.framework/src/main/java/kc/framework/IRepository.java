package kc.framework;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import kc.framework.base.EntityBase;


public interface IRepository<T extends EntityBase> {
	List<T> FindAll();
    <K> List<T> FindAll(Function<T, K> keySelector, boolean ascending);
    
    List<T> Find(Predicate<T> predicate);
    <K  extends EntityBase> List<T> Find(Predicate<T> predicate, Function<T, K> keySelector, boolean ascending);
    
    boolean Add(T entity, boolean isSave);
    boolean Modify(T entity, boolean isSave);

    boolean Remove(T entity, boolean isSave);
    int Remove(List<T> entities, boolean isSave);
    boolean RemoveAll();
}
