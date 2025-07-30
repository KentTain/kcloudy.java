package kc.framework.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TupleFour<A, B, C, D> implements java.io.Serializable {
	private static final long serialVersionUID = -3778556560399030042L;

	public final A Item1;

    public final B Item2;
    
    public final C Item3;
    
    public final D Item4;

    public TupleFour(A a, B b, C c, D d){
    	Item1 = a;
    	Item2 = b;
    	Item3 = c;
    	Item4 = d;
    }

    public String toString(){
        return "(" + Item1 + ", " + Item2 + ", " + Item3 + ", " + Item4 + ")";
    }
}
