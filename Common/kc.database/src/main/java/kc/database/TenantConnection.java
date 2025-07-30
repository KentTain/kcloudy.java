package kc.database;

import kc.framework.base.TupleFour;
import kc.framework.enums.DatabaseType;
import kc.framework.extension.StringExtensions;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Dictionary;
import java.util.Enumeration;

@Data
public class TenantConnection {
    private final static Logger logger = LoggerFactory.getLogger(TenantConnection.class.getName());
    private String TenantName;
    private DatabaseType DatabaseType;
    private String ConnectionString;

    public static String DEFAULT_SQLSERVER_DRIVER_CLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    public static String DEFAULT_MYSQL_DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";
    public static String DEFAULT_ORACLE_DRIVER_CLASS = "oracle.jdbc.driver.OracleDriver";
    public static String DEFAULT_POSTGRESQL_DRIVER_CLASS = "org.postgresql.Driver";

    public final static String convertConnection(DatabaseType databaseType, String conntionString) {
        if (StringExtensions.isNullOrEmpty(conntionString))
            return null;

        String result = "";
        try {
            TupleFour<String, String, String, String> items = GetDatabaseConnectionItems(databaseType, conntionString);
            switch (databaseType) {
                case MySql:
                    String mysqlConn = "jdbc:mysql://%s:3306/%s?user=%s&password=%s&serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=true";
                    result = String.format(mysqlConn, items.Item1, items.Item2, items.Item3, items.Item4);
                case SqlServer:
                    String sqlConn = "jdbc:sqlserver://%s;databaseName=%s;user=%s;password=%s";
                    result = String.format(sqlConn, items.Item1, items.Item2, items.Item3, items.Item4);
                case Oracle:
                    String oracleConn = "jdbc:oracle:thin:@//%s:1521/%s?user=%s&password=%s";
                    result = String.format(oracleConn, items.Item1, items.Item2, items.Item3, items.Item4);
                case PostgreSQL:
                    String pgSqlConn = "jdbc:postgresql://%s:5432/%s?user=%s&password=%s";
                    result = String.format(pgSqlConn, items.Item1, items.Item2, items.Item3, items.Item4);
                default:
                    break;
            }

            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return result;
        }
    }

    /**
     * 获取数据库服务器的连接信息：TupleFour<server, database, user, password>
     *
     * @param databaseType  数据类型
     * @param connectString 链接字符串
     * @return
     */
    public final static TupleFour<String, String, String, String> GetDatabaseConnectionItems(DatabaseType databaseType, String connectString) {
        if (StringExtensions.isNullOrEmpty(connectString))
            return null;

        String server = "";
        String database = "";
        String user = "";
        String password = "";

        if (databaseType == kc.framework.enums.DatabaseType.SqlServer) {
            // jdbc:sqlserver://localhost:1433;databaseName=master;user=sa;password=P@ssw0rd
            Dictionary<String, String> dicCon = StringExtensions.keyValuePairFromConnectionString(connectString);
            Enumeration<String> keys = dicCon.keys();
            while (keys.hasMoreElements()) {
                String key = keys.nextElement();
                if (key.equals("server")) {
                    server = dicCon.get("server");
                } else if (key.equals("data source")) {
                    server = dicCon.get("data source");
                } else if (key.equals("jdbc:sqlserver:")) {
                    server = dicCon.get("jdbc:sqlserver:");
                } else if (key.equals("jdbc:mysql:")) {
                    server = dicCon.get("jdbc:mysql:");
                }

                if (key.equals("database")) {
                    database = dicCon.get("database");
                } else if (key.equals("databasename")) {
                    database = dicCon.get("databasename");
                }

                if (key.equals("user id")) {
                    user = dicCon.get("user id");
                } else if (key.equals("uid")) {
                    user = dicCon.get("uid");
                } else if (key.equals("user")) {
                    user = dicCon.get("user");
                }

                if (key.equals("password")) {
                    password = dicCon.get("password");
                } else if (key.equals("pwd")) {
                    password = dicCon.get("pwd");
                }
            }
            return new TupleFour<>(server, database, user, password);
        }

        String[] serverKeyPairs = connectString.split("\\?");
        String[] databaseKeyPairs = new String[] {"", ""};
        switch (databaseType) {
            case MySql:
                //jdbc:mysql://%s:3306/%s?user=%s&password=%s&serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=true
                databaseKeyPairs = serverKeyPairs[0].replace("jdbc:mysql:////", "").split("/");
                break;
            case Oracle:
                //jdbc:oracle:thin:@//%s:1521/%s?user=%s&password=%s
                databaseKeyPairs = serverKeyPairs[0].replace("jdbc:oracle:thin:@////", "").split("/");
                break;
            case PostgreSQL:
                //jdbc:postgresql://%s:5432/%s?user=%s&password=%s
                databaseKeyPairs = serverKeyPairs[0].replace("jdbc:postgresql:////", "").split("/");
                break;
            default:
                break;
        }

        server = databaseKeyPairs[0];
        if (databaseKeyPairs.length > 1) {
            database = databaseKeyPairs[1];
        }

        if (serverKeyPairs.length > 1) {
            Dictionary<String, String> dicCon = StringExtensions.keyValuePairFromConnectionString(serverKeyPairs[1], "&", "=");
            Enumeration<String> keys = dicCon.keys();
            while (keys.hasMoreElements()) {
                String key = keys.nextElement();
                if (key.equals("user id")) {
                    user = dicCon.get("user id");
                } else if (key.equals("uid")) {
                    user = dicCon.get("uid");
                } else if (key.equals("user")) {
                    user = dicCon.get("user");
                }

                if (key.equals("password")) {
                    password = dicCon.get("password");
                } else if (key.equals("pwd")) {
                    password = dicCon.get("pwd");
                }
            }
        }

        return new TupleFour<>(server, database, user, password);
    }

}
