package kc.database.core;

import org.hibernate.dialect.MySQL55Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.IntegerType;


public class CustomMysql5Dialect extends MySQL55Dialect  {
    public static final String SQL_FUNCTION_BITAND = "bitand";
    public static final String SQL_FUNCTION_BITOR  = "bitor";
    public static final String SQL_FUNCTION_BITXOR = "bitxor";
    public CustomMysql5Dialect() {
        super();
        registerFunction(SQL_FUNCTION_BITAND, new SQLFunctionTemplate(IntegerType.INSTANCE, "(?1 & ?2)"));
        registerFunction(SQL_FUNCTION_BITOR, new SQLFunctionTemplate(IntegerType.INSTANCE, "(?1 | ?2)"));
        registerFunction(SQL_FUNCTION_BITXOR, new SQLFunctionTemplate(IntegerType.INSTANCE, "(?1 ^ ?2)"));
    }
}
