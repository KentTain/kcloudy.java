package kc.framework.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TupleTwo<A, B> implements java.io.Serializable {
    private static final long serialVersionUID = -3778556560399030040L;

    public final A Item1;

    public final B Item2;

    public TupleTwo(A a, B b) {
        Item1 = a;
        Item2 = b;
    }
}