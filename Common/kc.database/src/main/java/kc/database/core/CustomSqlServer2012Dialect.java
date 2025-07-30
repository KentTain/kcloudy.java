package kc.database.core;

import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.IntegerType;

import java.sql.Types;


public class CustomSqlServer2012Dialect extends org.hibernate.dialect.SQLServer2012Dialect  {
    public static final String SQL_FUNCTION_BITAND = "bitand";
    public static final String SQL_FUNCTION_BITOR  = "bitor";
    public static final String SQL_FUNCTION_BITXOR = "bitxor";
    public CustomSqlServer2012Dialect() {
        super();
        registerColumnType(Types.VARCHAR, "nvarchar($l)");
        registerFunction(SQL_FUNCTION_BITAND, new SQLFunctionTemplate(IntegerType.INSTANCE, "(?1 & ?2)"));
        registerFunction(SQL_FUNCTION_BITOR, new SQLFunctionTemplate(IntegerType.INSTANCE, "(?1 | ?2)"));
        registerFunction(SQL_FUNCTION_BITXOR, new SQLFunctionTemplate(IntegerType.INSTANCE, "(?1 ^ ?2)"));
    }
}
