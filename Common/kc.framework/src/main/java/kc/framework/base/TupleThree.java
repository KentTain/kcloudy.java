package kc.framework.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TupleThree<A, B, C> implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3778556560399030041L;

	public final A Item1;

    public final B Item2;
    
    public final C Item3;

    public TupleThree(A a, B b, C c){
    	Item1 = a;
    	Item2 = b;
    	Item3 = c;
    }

    public String toString(){
        return "(" + Item1 + ", " + Item2 + ", " + Item3 + ")";
    }
}
