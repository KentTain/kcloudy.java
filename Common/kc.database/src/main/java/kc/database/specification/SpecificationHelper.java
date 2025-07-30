package kc.database.specification;

import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.StringTokenizer;

/**
 * https://blog.csdn.net/tianyaleixiaowu/article/details/72876732
 */
@SuppressWarnings("deprecation")
public class SpecificationHelper {
    //private static Logger log = LoggerFactory.getLogger(SpecificationHelper.class);

    public static <ATTR, E> Path<ATTR> getPath(Root<E> root, String attrName) {
        From<E, ?> f = root;
        String[] strs = attrName.split("\\.");
        String attr = attrName;
        if (strs.length > 1) {
            for(int i = 0; i < strs.length; i ++) {
                attr = strs[i];
                if(i < strs.length - 1) {
                    boolean hasAttribute = false;
                    if (root.getJoins() != null) {
                        for (Join<E, ?> join : root.getJoins()) {
                            if (attr.equals(join.getAttribute().getName())) {
                                f = join;
                                hasAttribute = true;
                                break;
                            }
                        }
                    }
                    if(!hasAttribute) {
                        f = f.join(attr);
                    }
                }
            }
        }
        return f.get(attr);
    }

    public static <E> From<E, ?> fromCollection(Root<E> root, String attrName) {
        From<E, ?> f = root;
        String attr = attrName;
        StringTokenizer tokenizer = new StringTokenizer(attrName, ".");
        while (tokenizer.hasMoreTokens()) {
            attr = tokenizer.nextToken();
            boolean hasAttribute = false;
            if (root.getJoins() != null) {
                for (Join<E, ?> join : root.getJoins()) {
                    if (attr.equals(join.getAttribute().getName())) {
                        f = join;
                        hasAttribute = true;
                        break;
                    }
                }
            }
            if(!hasAttribute) {
                f = f.join(attr);
            }
        }
        return f;
    }

//    public static <T> Specifications<T> and(List<Specification<T>> specList) {
//        return SpecificationGroup.and(specList);
//    }
//
//    public static <T> Specifications<T> or(List<Specification<T>> specList) {
//        return SpecificationGroup.or(specList);
//    }

}

