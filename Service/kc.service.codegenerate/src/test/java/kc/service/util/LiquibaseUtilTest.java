package kc.service.util;

import kc.service.util.LiquibaseUtil;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

@Disabled
@DisplayName("Liquibase：数据库脚本迁移")
class LiquibaseUtilTest {

    @Test
    void test_generateSqlByChangeLogXml() throws Exception {
        String databaseTypeUrl = "mysql";
        String changeLogFilePath = "src\\main\\resources\\liquibase\\changelog_mysql.xml";
        String outputSqlFilePath = "src\\main\\resources\\liquibase\\mysql.sql";
        String sql = LiquibaseUtil.generateSqlByChangeLogXml(databaseTypeUrl, changeLogFilePath, outputSqlFilePath, false);
        System.out.println(sql);
    }

    @Test
    void test_liquibase_xml(){
        String userId = UUID.randomUUID().toString();
        String projectId = UUID.randomUUID().toString();
        String changeLogFilePath = "changelog-mysql-" + projectId + ".xml";
        String outputSqlFilePath = "sql-mysql-" + projectId + ".sql";
        //String changeLogFilePath = "classpath:liquibase\\changelog-" + databaseTypeUrl + "-" + currentProjectId + ".xml";
        //String outputSqlFilePath = "classpath:liquibase\\sql-" + databaseTypeUrl + "-" + currentProjectId + ".sql";

        //生成ChangedLog.xml：https://docsstage.liquibase.com/change-types/home.html
        Document document = DocumentHelper.createDocument();
        Namespace proNamespace = new Namespace("pro", "http://www.liquibase.org/xml/ns/pro");
        Element root = document.addElement("databaseChangeLog", "http://www.liquibase.org/xml/ns/dbchangelog");
        root.add(proNamespace);
        root.addAttribute("xmlns:ext","http://www.liquibase.org/xml/ns/dbchangelog-ext");
        //root.addAttribute("xmlns:pro","http://www.liquibase.org/xml/ns/pro");
        root.addAttribute("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance");
        root.addAttribute("xsi:schemaLocation","http://www.liquibase.org/xml/ns/dbchangelog-ext http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-ext.xsd http://www.liquibase.org/xml/ns/pro http://www.liquibase.org/xml/ns/pro/liquibase-pro-4.1.xsd http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-4.1.xsd");

        LiquibaseUtil.createLiquibaseDatabaseChangeLog(root, userId);
    }
}