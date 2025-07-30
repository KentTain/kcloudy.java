package kc.framework.extension;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public final class IterableExtensions {
	/**
	 * 根据筛选条件，获取结果集
	 * 
	 * @param source    源集合
	 * @param predicate 筛选条件
	 * @return
	 */
	public static <T> Iterable<T> where(Iterable<T> source, Predicate<T> predicate) {
		List<T> result = new ArrayList<T>();
		for (T item : source) {
			if (predicate.test(item)) {
				result.add(item);
			}
		}
		return result;
	}

	/**
	 * 获取对象集合中该对象的某些属性集合
	 * 
	 * @param source 源集合
	 * @param func   对象的属性lambda表达式
	 * @return
	 */
	public static <T, R> Iterable<R> select(Iterable<T> source, Function<T, R> func) {
		List<R> result = new ArrayList<R>();
		for (T item : source) {
			result.add(func.apply(item));
		}
		return result;
	}

	/**
	 * 获取对象集合中该对象的某些属性集合
	 * 
	 * @param source    源集合
	 * @param func      对象的属性lambda表达式
	 * @param predicate 筛选条件
	 * @return
	 */
	public static <T, R> Iterable<R> select(Iterable<T> source, Function<T, R> func, Predicate<T> predicate) {
		List<R> result = new ArrayList<R>();
		for (T item : source) {
			if (predicate.test(item)) {
				result.add(func.apply(item));
			}
		}
		return result;
	}

	/**
	 * 将Iterable对象转换为List对象
	 * 
	 * @param source 源集合
	 * @return
	 */
	public static <T> List<T> toList(Iterable<T> source) {
		List<T> result = new ArrayList<T>();
		source.forEach(result::add);

		return result;
	}

	public static <T> T getByIndex(Iterable<T> source, int index) {
		//iterate the HashSet
		int currentIndex = 0;
		Iterator<T> iterator = source.iterator();
		while(iterator.hasNext()){

			if(currentIndex == index)
				return iterator.next();

			iterator.next();
			currentIndex++;
		}

		return null;
	}


}
