package kc.service.util;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

import kc.dto.codegenerate.ModelDefFieldDTO;
import kc.enums.codegenerate.PrimaryKeyType;
import kc.framework.enums.AttributeDataType;
import kc.framework.extension.StringExtensions;
import liquibase.integration.commandline.LiquibaseCommandLine;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LiquibaseUtil {
    private static final Logger logger = LoggerFactory.getLogger(LiquibaseUtil.class);
    /**
     * 调用liquibase的命令行，如：
     * liquibase-4.7.0\liquibase
     *      --url=offline:mysql?outputLiquibaseSql=true
     *      --changeLogFile="src\main\resources\liquibase\changelog_1.xml"
     *      --outputFile="src\main\resources\liquibase\liquibase2.sql"
     *      updatesql
     * @param databaseTypeUrl 数据库类型：mysql、oracle、mssql
     * @param changeLogFilePath 数据库变动Xml文件路径
     * @param outputSqlFilePath 生成的Sql的文件路径
     */
    public static String generateSqlByChangeLogXml(String databaseTypeUrl, String changeLogFilePath, String outputSqlFilePath, boolean addDbChangeLog) throws Exception {
        List<String> argList = new ArrayList<>();
        argList.add("--url=offline:" + databaseTypeUrl + "?outputLiquibaseSql=true");
        argList.add("--changeLogFile=" + changeLogFilePath);
        argList.add("--outputFile=" + outputSqlFilePath);
        argList.add("updatesql");

        try {
            boolean isMySql = databaseTypeUrl.equalsIgnoreCase("mysql");
            LiquibaseCommandLine cli = new LiquibaseCommandLine();
            cli.execute(argList.toArray(new String[0]));

            File file = new File(outputSqlFilePath);
            if(addDbChangeLog){
                String content = new String(Files.readAllBytes(file.toPath()));
                file.delete();
                return content;
            }

            List<String> lines =  Files.readAllLines(file.toPath());
            StringBuilder  sbResult = new StringBuilder ();
            for (String line : lines) {
                // 去除空行
                if(StringExtensions.isNullOrEmpty(line))
                    continue;
                // 去除DatabaseChangeLog表
                if(line.startsWith("INSERT INTO DATABASECHANGELOG") || line.startsWith("CREATE TABLE DATABASECHANGELOG"))
                    continue;
                // mysql创建表时，会同时生成编辑列语句（ALTER TABLE tableNam MODIFY COLUMN fieldName INT COMMENT '排序';），导致NotNull、Unique规则失效
                if(isMySql && line.startsWith("ALTER TABLE") && line.contains(" MODIFY COLUMN ") && line.contains(" COMMENT "))
                    continue;

                sbResult.append(line);
                sbResult.append(System.getProperty("line.separator"));
            }

            file.delete();
            return sbResult.toString();
        } catch (FileNotFoundException var5) {
            throw new Exception(outputSqlFilePath + " is not exists!");
        } catch (IOException var6) {
            throw var6;
        }
    }


    /**
     * 创建表：https://docsstage.liquibase.com/change-types/create-table.html
     * <changeSet id="1" author="liquibase">
     *    <createTable tableName="department">
     *        <column name="id" type="int">
     *            <constraints primaryKey="true"/>
     *        </column>
     *        <column name="dept" type="varchar(${dep.size})">
     *            <constraints nullable="false"/>
     *        </column>
     *        <column name="emp_id" type="int">
     *            <constraints nullable="false"/>
     *        </column>
     *    </createTable>
     * </changeSet>
     * @param root
     * @param tableName
     * @param author
     * @param objectAttrLogs
     */
    public static void createTableChangeSet(Element root, String databaseTypeUrl, String tableName, String author, List<ModelDefFieldDTO> objectAttrLogs, Namespace proNamespace){
        boolean isMySql = databaseTypeUrl.equalsIgnoreCase("mysql");
        boolean isMsSql = databaseTypeUrl.equalsIgnoreCase("mssql");
        boolean isOracle = databaseTypeUrl.equalsIgnoreCase("oracle");
        Optional<ModelDefFieldDTO> primaryKeyAttrLogOpt = objectAttrLogs.stream()
                .filter(m -> m.getIsPrimaryKey() && m.getPrimaryKeyType() == PrimaryKeyType.IDENTITY)
                .findFirst();
        //针对Oracle 11g以下，通过序列及触发器设置主键的自增列
        if(isOracle && primaryKeyAttrLogOpt.isPresent()){
            createSequenceChangeSet(root, tableName, author);
        }
        //创建表
        Element createTableSet = root.addElement("changeSet")
                .addAttribute("id", UUID.randomUUID().toString())
                .addAttribute("author", author);
        Element createTable = createTableSet.addElement("createTable")
                .addAttribute("tableName", tableName);

        for (ModelDefFieldDTO objectAttrLog : objectAttrLogs){
            //创建字段
            String filedName = objectAttrLog.getName();
            AttributeDataType dataType = objectAttrLog.getDataType();
            Integer dataLength = Default_data_length;
            String columnType = convertDataTypeToLiquibaseDataType(dataType, dataLength, Default_data_precision);
            Element column = createTable.addElement("column")
                    .addAttribute("name", filedName)
                    .addAttribute("type", columnType);

            //主键
            Boolean isPrimaryKey = objectAttrLog.getIsPrimaryKey();
            if(isPrimaryKey){
                PrimaryKeyType primaryType = objectAttrLog.getPrimaryKeyType();
                // fix：1580081 【IT-后端-实体模型-导出DDL脚本】主键自增，导出脚本执行后没有设置自增成功
                if(primaryType == PrimaryKeyType.IDENTITY){
                    if(!isOracle){
                        column.addAttribute("autoIncrement", "true");
                    }else{
                        //Oracle 12g可用，Oracle 11g需要替换为: createSequenceChangeSet + executeSqlChangeSet
                        //column.addAttribute("defaultValueComputed", "SEQ_TABLE_" + tableName + ".NEXTVAL");
                    }
                }else if(primaryType == PrimaryKeyType.UUID){

                }
                //创建主键约束
                column.addElement("constraints")
                        .addAttribute("nullable", "false")
                        .addAttribute("primaryKey", "true");
            }

            //非空及唯一
            boolean isUnique = objectAttrLog.getIsUnique();
            boolean isNotNull = objectAttrLog.getIsNotNull();
            boolean canCreateIndex = canCreateIndex(databaseTypeUrl, dataType, dataLength);
            if(!isPrimaryKey && isNotNull && isUnique){
                if(canCreateIndex)
                    column.addElement("constraints")
                            .addAttribute("nullable", "false")
                            .addAttribute("unique", "true");
                else
                    column.addElement("constraints")
                            .addAttribute("nullable", "false");
            } else if(!isPrimaryKey && isNotNull){
                column.addElement("constraints")
                        .addAttribute("nullable", "false");
            } else if(!isPrimaryKey && isUnique){
                if(canCreateIndex)
                    column.addElement("constraints")
                            .addAttribute("unique", "true");
            }

            //默认值
//            String defaultValue = objectAttrLog.getDefaultValue();
//            if(!StringExtensions.isNullOrEmpty(defaultValue)){
//                column.addAttribute("defaultValue", defaultValue);
//            }

            //注释
            String filedDesc = objectAttrLog.getDisplayName();
            if(!StringExtensions.isNullOrEmpty(filedDesc) && !isMsSql){
                column.addAttribute("remarks", filedDesc);
            }

            //创建索引
            // fix：1580087 【IT-后端-实体模型-导出DDL脚本】同时选择了唯一索引和勾选了是否唯一时，ddl脚本会生成2个索引
//            String indexType = objectAttrLog.getIndexType();//uniqueIndex、generalIndex
//            if(!isUnique && canCreateIndex && !StringExtensions.isNullOrEmpty(indexType)){
//                addIndexChangeSet(root, databaseTypeUrl, tableName, author, filedName, indexType);
//            }
        }

        //针对Oracle 11g以下，通过序列及触发器设置主键的自增列
        //https://www.codenong.com/45038378/
        if(isOracle && primaryKeyAttrLogOpt.isPresent()){
            //createTrigger为收费功能，替换为：executeSqlChangeSet
            //createTriggerChangeSet(root, tableName, author, primaryKeyAttrLogOpt.get(), proNamespace);

            //https://docs.liquibase.com/concepts/advanced/enddelimiter-sql-attribute.html
//            String triggerSql = "CREATE OR REPLACE TRIGGER TRIGGER_" + tableName + " BEFORE INSERT ON " + tableName + " FOR EACH ROW " + "\n" +
//                "BEGIN\n" +
//                "  SELECT SEQ_TABLE_" + tableName + ".NEXTVAL\n" +
//                "  INTO   :new.id" +
//                "  FROM   " + tableName + ";\n" +
//                "END;";
//            executeSqlChangeSet(root, author, "oracle", triggerSql, "\\n/");
        }
    }
    /**
     * 删除表：https://docsstage.liquibase.com/change-types/drop-table.html
     * <changeSet  author="liquibase-docs"  id="dropTable-example">
     *     <dropTable  cascadeConstraints="true"
     *             catalogName="cat"
     *             schemaName="public"
     *             tableName="person"/>
     * </changeSet>
     * @param root
     * @param tableName
     * @param author
     */
    public static void deleteTableChangeSet(Element root, String tableName, String author){
        Element deleteTableSet = root.addElement("changeSet")
                .addAttribute("id", UUID.randomUUID().toString())
                .addAttribute("author", author);
        Element dropTable = deleteTableSet.addElement("dropTable")
                .addAttribute("cascadeConstraints", "true")
                .addAttribute("tableName", tableName);
    }

    public static final Integer Default_data_length = 30;
    public static final Integer Default_data_precision = 4;
    public static final String Sequence_prefix = "SEQ_";
    /**
     * 创建序列：https://docs.liquibase.com/change-types/create-sequence.html
     * <changeSet  author="liquibase-docs"  id="createSequence-example">
     *     <createSequence  cacheSize="371717"
     *             catalogName="cat"
     *             cycle="true"
     *             dataType="int"
     *             incrementBy="2"
     *             maxValue="1000"
     *             minValue="10"
     *             ordered="true"
     *             schemaName="public"
     *             sequenceName="seq_id"
     *             startValue="5"/>
     * </changeSet>
     * @param root
     * @param tableName
     * @param author
     */
    public static void createSequenceChangeSet(Element root, String tableName, String author){
        Element changeSet = root.addElement("changeSet")
                .addAttribute("id", UUID.randomUUID().toString())
                .addAttribute("author", author);
        Element createSequence = changeSet.addElement("createSequence")
                .addAttribute("cacheSize", "500000")
                .addAttribute("cycle", "false")
                .addAttribute("incrementBy", "1")
                .addAttribute("sequenceName", Sequence_prefix + tableName.replace("t_", ""))
                .addAttribute("startValue", "1");
    }
    /**
     * 删除序列：https://docs.liquibase.com/change-types/drop-sequence.html
     * <changeSet  author="liquibase-docs"  id="dropSequence-example">
     *     <dropSequence  catalogName="cat"
     *             schemaName="public"
     *             sequenceName="seq_id"/>
     * </changeSet>
     * @param root
     * @param tableName
     * @param author
     */
    public static void dropSequenceChangeSet(Element root, String tableName, String author){
        Element changeSet = root.addElement("changeSet")
                .addAttribute("id", UUID.randomUUID().toString())
                .addAttribute("author", author);
        Element dropSequence = changeSet.addElement("dropSequence")
                .addAttribute("sequenceName", Sequence_prefix + tableName.replace("t_", ""));
    }


    /**
     * 创建触发器：https://docs.liquibase.com/change-types/create-trigger.html
     * 【收费功能】：xmlns:pro="http://www.liquibase.org/xml/ns/pro
     * <changeSet  author="liquibase-docs" id="createTrigger-example">
     *     <pro:createTrigger catalogName="cat"
     *            comments="A String"
     *            dbms="h2, !oracle, mysql"
     *            disabled="true"
     *            encoding="UTF-8"
     *            path="com/example/my-logic.sql"
     *            relativeToChangelogFile="true"
     *            replaceIfExists="false"
     *            schemaName="public"
     *            scope="A String"
     *            tableName="person"
     *            triggerName="A String">A String</pro:createTrigger>
     * 	</changeSet>
     * @param root
     * @param tableName
     * @param author
     * @param proNamespace
     */
    public static void createTriggerChangeSet(Element root, String tableName, String author, Namespace proNamespace){
        Element changeSet = root.addElement("changeSet")
                .addAttribute("id", UUID.randomUUID().toString())
                .addAttribute("author", author);
        Element createTrigger = changeSet.addElement(new QName("createTrigger", proNamespace))
                .addAttribute("dbms", "oracle")
                .addAttribute("tableName", tableName)
                .addAttribute("encoding", "UTF-8")
                .addAttribute("disabled", "false")
                .addAttribute("replaceIfExists", "true")
                .addAttribute("triggerName", "TRIGGER_TABLE_" + tableName);

        String triggerBody = "SELECT SEQ_TABLE_" + tableName + ".NEXTVAL" +
                "  INTO :new.id" +
                "  FROM " + tableName + ";";
        createTrigger.addText(triggerBody);
    }
    /**
     * 删除触发器：https://docs.liquibase.com/change-types/drop-trigger.html
     * 【收费功能】：xmlns:pro="http://www.liquibase.org/xml/ns/pro
     * <changeSet  author="liquibase-docs"  id="dropTrigger-example">
     *     <pro:dropTrigger  catalogName="department"
     *           schemaName="public"
     *           scope="A String"
     *           tableName="person"
     *           triggerName="A String"/>
     * </changeSet>
     * @param root
     * @param tableName
     * @param author
     * @param proNamespace
     */
    public static void dropTriggerChangeSet(Element root, String tableName, String author, Namespace proNamespace){
        Element changeSet = root.addElement("changeSet")
                .addAttribute("id", UUID.randomUUID().toString())
                .addAttribute("author", author);
        Element dropTrigger = changeSet.addElement(new QName("dropTrigger", proNamespace))
                .addAttribute("dbms", "oracle")
                .addAttribute("tableName", tableName)
                .addAttribute("triggerName", "TRIGGER_TABLE_" + tableName);
    }


    /**
     * 添加字段：https://docsstage.liquibase.com/change-types/add-column.html
     * <changeSet author="liquibase-docs" id="addColumn-example">
     * 	 <addColumn catalogName="cat"
     * 			schemaName= "public"
     * 			tableName="person" >
     * 		<column name="address"
     * 			position="2"
     * 			type="varchar(255)"/>
     * 		<column afterColumn="id"
     * 			name="name"
     * 			type="varchar(50)" >
     * 			<constraints nullable="false" />
     * 		</column>
     * 	 </addColumn>
     * </changeSet>
     * @param root
     * @param tableName
     * @param author
     * @param objectAttrLogs
     */
    public static void addColumnChangeSet(Element root, String databaseTypeUrl, String tableName, String author, List<ModelDefFieldDTO> objectAttrLogs){
        boolean isMsSql = databaseTypeUrl.equalsIgnoreCase("mssql");
        Element addColumnSet = root.addElement("changeSet")
                .addAttribute("id", UUID.randomUUID().toString())
                .addAttribute("author", author);
        Element addColumn = addColumnSet.addElement("addColumn")
                .addAttribute("tableName", tableName);

        for (ModelDefFieldDTO objectAttrLog : objectAttrLogs){
            //创建字段
            String filedName = objectAttrLog.getName();
            AttributeDataType dataType = objectAttrLog.getDataType();
            Integer dataLength = Default_data_length;
            String columnType = convertDataTypeToLiquibaseDataType(objectAttrLog.getDataType(), Default_data_length, Default_data_precision);
            Element column = addColumn.addElement("column")
                    .addAttribute("name", filedName)
                    .addAttribute("type", columnType);

            //非空及唯一
            boolean isUnique = objectAttrLog.getIsUnique();
            boolean isNotNull = objectAttrLog.getIsNotNull();
            boolean canCreateIndex = canCreateIndex(databaseTypeUrl, dataType, dataLength);
            if(isNotNull && isUnique){
                if(canCreateIndex)
                    column.addElement("constraints")
                            .addAttribute("nullable", "false")
                            .addAttribute("unique", "true");
                else
                    column.addElement("constraints")
                            .addAttribute("nullable", "false");
            } else if(isNotNull){
                column.addElement("constraints")
                        .addAttribute("nullable", "false");
            } else if(isUnique){
                if(canCreateIndex)
                    column.addElement("constraints")
                            .addAttribute("unique", "true");
            }

            //默认值
//            String defaultValue = objectAttrLog.getDefaultValue();
//            if(!StringExtensions.isNullOrEmpty(defaultValue)){
//                column.addAttribute("defaultValue", defaultValue);
//            }

            //注释
            String filedDesc = objectAttrLog.getDisplayName();
            if(!StringExtensions.isNullOrEmpty(filedDesc) && !isMsSql){
                column.addAttribute("remarks", filedDesc);
            }

            //创建索引
//            String indexType = objectAttrLog.getIndexType();//uniqueIndex、generalIndex
//            if(!isUnique && canCreateIndex && !StringExtensions.isNullOrEmpty(indexType)){
//                addIndexChangeSet(root, databaseTypeUrl, tableName, author, filedName, indexType);
//            }
        }
    }
    /**
     * 删除字段：https://docsstage.liquibase.com/change-types/add-column.html
     * <changeSet  author="liquibase-docs"  id="dropColumn-example">
     *     <dropColumn  catalogName="cat"
     *             columnName="id"
     *             schemaName="public"
     *             tableName="person">
     *         <column  name="address"/>
     *     </dropColumn>
     * </changeSet>
     * @param root
     * @param tableName
     * @param author
     * @param objectAttrLogs
     */
    public static void dropColumnChangeSet(Element root, String databaseTypeUrl, String tableName, String author, List<ModelDefFieldDTO> objectAttrLogs){
        Element columnSet = root.addElement("changeSet")
                .addAttribute("id", UUID.randomUUID().toString())
                .addAttribute("author", author);
        Element dropColumn = columnSet.addElement("dropColumn")
                .addAttribute("tableName", tableName);

        for (ModelDefFieldDTO objectAttrLog : objectAttrLogs) {
            String filedName = objectAttrLog.getName();
            Element column = dropColumn.addElement("column")
                    .addAttribute("name", filedName);
        }
    }
    /**
     * 字段重命名：https://docsstage.liquibase.com/change-types/rename-column.html
     * <changeSet  author="liquibase-docs"  id="renameColumn-example">
     *             <renameColumn  catalogName="cat"
     *                     columnDataType="int"
     *                     newColumnName="full_name"
     *                     oldColumnName="name"
     *                     remarks="A String"
     *                     schemaName="public"
     *                     tableName="person"/>
     *  </changeSet>
     * @param root
     * @param tableName
     * @param author
     * @param columnType
     * @param oldValue
     * @param newValue
     */
    public static void renameColumnChangeSet(Element root, String databaseTypeUrl, String tableName, String author, String columnType, String oldValue, String newValue){
        Element columnSet = root.addElement("changeSet")
                .addAttribute("id", UUID.randomUUID().toString())
                .addAttribute("author", author);

        Element renameColumn = columnSet.addElement("renameColumn")
                .addAttribute("tableName", tableName)
                .addAttribute("columnDataType", columnType)
                .addAttribute("newColumnName", newValue)
                .addAttribute("oldColumnName", oldValue);

    }
    /**
     * 更改字段类型：https://docsstage.liquibase.com/change-types/modify-data-type.html
     * <changeSet  author="liquibase-docs"  id="modifyDataType-example">
     *             <modifyDataType  catalogName="cat"
     *                     columnName="id"
     *                     newDataType="int"
     *                     schemaName="public"
     *                     tableName="person"/>
     * </changeSet>
     * @param root
     * @param tableName
     * @param author
     * @param filedName
     * @param columnType
     */
    public static void modifyColumnTypeChangeSet(Element root, String databaseTypeUrl, String tableName, String author, String filedName, String columnType){
        //当数据库为Oracle时，不能将字段类更改为：Blob、Clob，已在前端编辑字段的数据类型中过滤两种类型
        //Oracle错误：ORA-22858: invalid alteration of datatype
        boolean isOracle = databaseTypeUrl.equalsIgnoreCase("oracle");

        Element columnSet = root.addElement("changeSet")
                .addAttribute("id", UUID.randomUUID().toString())
                .addAttribute("author", author);

        Element modifyDataType = columnSet.addElement("modifyDataType")
                .addAttribute("tableName", tableName)
                .addAttribute("columnName", filedName)
                .addAttribute("newDataType", columnType);

    }
    /**
     * 设置字段备注：https://docsstage.liquibase.com/change-types/set-column-remarks.html
     * <changeSet author="liquibase-docs" id="setColumnRemarks-example">
     *             <setColumnRemarks catalogName="department"
     *                     columnName="id"
     *                     remarks="A String"
     *                     schemaName="public"
     *                     tableName="person"/>
     * </changeSet>
     * @param root
     * @param tableName
     * @param author
     * @param filedName
     * @param remark
     */
    public static void setColumnRemarksChangeSet(Element root, String databaseTypeUrl, String tableName, String author, String filedName, String remark){
        Element columnSet = root.addElement("changeSet")
                .addAttribute("id", UUID.randomUUID().toString())
                .addAttribute("author", author);

        Element setColumnRemarks = columnSet.addElement("setColumnRemarks")
                .addAttribute("tableName", tableName)
                .addAttribute("columnName", filedName)
                .addAttribute("remarks", remark);
    }


    /**
     * 创建索引：https://docsstage.liquibase.com/change-types/create-index.html
     * <changeSet author="bob" id="1_createIndex_index_one">
     *      <createIndex indexName="index_one" tableName="department">
     *          <column name="col_1"/>
     *          <column name="col_2"/>
     *          <column name="col_3"/>
     *      </createIndex>
     *  </changeSet>
     * @param root
     * @param tableName
     * @param author
     * @param filedName
     * @param indexType
     */
    public static void addIndexChangeSet(Element root, String databaseTypeUrl, String tableName, String author, String filedName, String indexType){
        //fix：1580089 【IT-后端-实体模型-导出DDL脚本】索引名称未遵循命名规则
        String indexName = formatIndexName(databaseTypeUrl, indexType, tableName, filedName);
        if(!StringExtensions.isNullOrEmpty(indexName)){
            Element createIndexSet = root.addElement("changeSet")
                    .addAttribute("id", UUID.randomUUID().toString())
                    .addAttribute("author", author);
            Element createIndex = createIndexSet.addElement("createIndex")
                    .addAttribute("tableName", tableName)
                    .addAttribute("indexName", indexName);
            createIndex.addElement("column")
                    .addAttribute("name", filedName);
        }
    }
    /**
     * 删除索引：https://docsstage.liquibase.com/change-types/drop-index.html
     * <changeSet author="liquibase-docs" id="dropIndex-example">
     *     <dropIndex indexName="idx_address"
     *             schemaName="public"
     *             tableName="person"/>
     * </changeSet>
     * @param root
     * @param tableName
     * @param author
     * @param filedName
     * @param indexType
     */
    public static void dropIndexChangeSet(Element root, String databaseTypeUrl, String tableName, String author, String filedName, String indexType){
        String indexName = formatIndexName(databaseTypeUrl, indexType, tableName, filedName);
        if(StringExtensions.isNullOrEmpty(indexName)){
            Element createIndexSet = root.addElement("changeSet")
                    .addAttribute("id", UUID.randomUUID().toString())
                    .addAttribute("author", author);
            Element dropIndex = createIndexSet.addElement("dropIndex")
                    .addAttribute("tableName", tableName)
                    .addAttribute("indexName", indexName);
        }
    }

    /**
     * 创建非空限制：https://docs.liquibase.com/change-types/add-not-null-constraint.html
     * <changeSet author="liquibase-docs"  id="addNotNullConstraint-example">
     *     <addNotNullConstraint catalogName="cat"
     *            columnDataType="int"
     *            columnName="id"
     *            constraintName="const_name"
     *            defaultNullValue="A String"
     *            schemaName="public"
     *            tableName="person"
     *            validate="true"/>
     * </changeSet>
     * @param root
     * @param tableName
     * @param author
     * @param filedName
     * @param columnType
     */
    public static void addNotNullChangeSet(Element root, String databaseTypeUrl, String tableName, String author, String filedName, String columnType){
        boolean isOracle = databaseTypeUrl.equalsIgnoreCase("oracle");

        Element columnSet = root.addElement("changeSet")
                .addAttribute("id", UUID.randomUUID().toString())
                .addAttribute("author", author);

        String constraintName = "cst_" + tableName + "_" + filedName;
        Element modifyDataType = columnSet.addElement("addNotNullConstraint")
                .addAttribute("tableName", tableName)
                .addAttribute("columnName", filedName)
                .addAttribute("columnDataType", columnType)
                .addAttribute("constraintName", constraintName);
    }
    /**
     * 删除非空：https://docs.liquibase.com/change-types/drop-not-null-constraint.html
     * <changeSet  author="liquibase-docs"  id="dropNotNullConstraint-example">
     *     <dropNotNullConstraint  catalogName="cat"
     *             columnDataType="int"
     *             columnName="id"
     *             schemaName="public"
     *             tableName="person"/>
     * </changeSet>
     * @param root
     * @param tableName
     * @param author
     * @param filedName
     * @param columnType
     */
    public static void dropNotNullChangeSet(Element root, String databaseTypeUrl, String tableName, String author, String filedName, String columnType){
        String constraintName = "cst_" + tableName + "_" + filedName;
        Element createIndexSet = root.addElement("changeSet")
                .addAttribute("id", UUID.randomUUID().toString())
                .addAttribute("author", author);
        Element dropIndex = createIndexSet.addElement("dropNotNullConstraint")
                .addAttribute("tableName", tableName)
                .addAttribute("columnName", filedName)
                .addAttribute("columnDataType", columnType);
    }


    /**
     * 使用条件判断，创建变更表：https://docs.liquibase.com/concepts/advanced/preconditions.html
     * 创建变更表：DATABASECHANGELOG
     * <changeSet author="author" id="1">
     *    <preConditions onFail="MARK_RAN">
     *        <not>
     *            <tableExists tableName="MY_NEW_TABLE"/>
     *        </not>
     *    </preConditions>
     *        <createTable tableName="MY_NEW_TABLE">
     *            <column name="IDX" type="${integer.type}">
     *                <constraints nullable="false"/>
     *            </column>
     *            <column name="INTVAL" type="${integer.type}"/>
     *        </createTable>
     * </changeSet>
     */
    public static void createLiquibaseDatabaseChangeLog(Element root, String author){
        Element columnSet = root.addElement("changeSet")
                .addAttribute("id", UUID.randomUUID().toString())
                .addAttribute("author", author);
        //处理条件
        Element preConditions = columnSet.addElement("preConditions")
                .addAttribute("onFail", "MARK_RAN");
        Element notElement = preConditions.addElement("not");
        Element tableExistsElement = notElement.addElement("tableExists")
                .addAttribute("tableName", "DATABASECHANGELOG");
        //创建表：DATABASECHANGELOG
        Element createTable = columnSet.addElement("createTable")
                .addAttribute("tableName", "DATABASECHANGELOG");
        Element column1 = createTable.addElement("column")
                .addAttribute("name", "ID")
                .addAttribute("type", "VARCHAR(255)");
        column1.addElement("constraints")
                .addAttribute("nullable", "false");
        Element column2 = createTable.addElement("column")
                .addAttribute("name", "AUTHOR")
                .addAttribute("type", "VARCHAR(255)");
        column2.addElement("constraints")
                .addAttribute("nullable", "false");
        Element column3 = createTable.addElement("column")
                .addAttribute("name", "FILENAME")
                .addAttribute("type", "VARCHAR(255)");
        column3.addElement("constraints")
                .addAttribute("nullable", "false");
        Element column4 = createTable.addElement("column")
                .addAttribute("name", "DATEEXECUTED")
                .addAttribute("type", "datetime");
        column4.addElement("constraints")
                .addAttribute("nullable", "false");
        Element column5 = createTable.addElement("column")
                .addAttribute("name", "ORDEREXECUTED")
                .addAttribute("type", "INT");
        column5.addElement("constraints")
                .addAttribute("nullable", "false");
        Element column6 = createTable.addElement("column")
                .addAttribute("name", "EXECTYPE")
                .addAttribute("type", "VARCHAR(10)");
        column6.addElement("constraints")
                .addAttribute("nullable", "false");

        createTable.addElement("column")
                .addAttribute("name", "MD5SUM")
                .addAttribute("type", "VARCHAR(35)");
        createTable.addElement("column")
                .addAttribute("name", "DESCRIPTION")
                .addAttribute("type", "VARCHAR(255)");
        createTable.addElement("column")
                .addAttribute("name", "COMMENTS")
                .addAttribute("type", "VARCHAR(255)");
        createTable.addElement("column")
                .addAttribute("name", "TAG")
                .addAttribute("type", "VARCHAR(255)");
        createTable.addElement("column")
                .addAttribute("name", "LIQUIBASE")
                .addAttribute("type", "VARCHAR(20)");
        createTable.addElement("column")
                .addAttribute("name", "CONTEXTS")
                .addAttribute("type", "VARCHAR(255)");
        createTable.addElement("column")
                .addAttribute("name", "LABELS")
                .addAttribute("type", "VARCHAR(255)");
        createTable.addElement("column")
                .addAttribute("name", "DEPLOYMENT_ID")
                .addAttribute("type", "VARCHAR(20)");
    }
    /**
     * 执行Sql: https://docs.liquibase.com/change-types/sql.html
     * 处理Oracle执行分隔符【/】: https://stackoverflow.com/questions/31025799/how-to-translate-sql-trigger-to-liquibase
     * <changeSet  author="liquibase-docs"  id="sql-example">
     *   <sql dbms="!h2, oracle, mysql"
     *    endDelimiter="\nGO"
     *    splitStatements="true"
     *    stripComments="true">insert into person (name) values ('Bob')
     *
     * <comment>What about Bob?</comment>
     *    </sql>
     * </changeSet>
     */
    public static void executeSqlChangeSet(Element root, String author, String dbms, String sql, String endDelimiter){
        Element columnSet = root.addElement("changeSet")
                .addAttribute("id", UUID.randomUUID().toString())
                .addAttribute("author", author);

        Element sqlChange = columnSet.addElement("sql")
                .addAttribute("dbms", dbms)
                .addAttribute("stripComments", "true")
                .addAttribute("splitStatements", "true")
                .addAttribute("endDelimiter", endDelimiter);
        sqlChange.addText(sql);
    }

    /**
     * 转换为liquibase支持的数据类型
     * @param dataType 业务类型
     * @param dataLength 数据长度
     * @param dataPrecision 数据精度
     * @return liquibase支持的数据类型
     */
    private static String convertDataTypeToLiquibaseDataType(AttributeDataType dataType, Integer dataLength, Integer dataPrecision){
        switch (dataType){
            case Bool:
                return "BIT(1)";
            case DateTime:
                return "DATETIME";
            case String:
            case Image:
            case File:
                return "VARCHAR(255)";
            case Text:
                return "VARCHAR(4000)";
            case Int:
                return "BIGINT";
            case Decimal:
                if(dataLength != null && dataPrecision != null)
                    return "DECIMAL(" + dataLength + ", " + dataPrecision + ")";
                else if(dataLength != null)
                    return "DECIMAL(" + dataLength + ", 0)";
                else if(dataPrecision != null)
                    return "DECIMAL(64, " + dataLength + ")";
                else
                    return "DECIMAL(64, 4)";
            case RichText:
                return "CLOB";
            default:
                return "VARCHAR(255)";
        }
    }

    /**
     * 判断是否能创建索引--说明：1. MySql索引，字段长度限制为756个字符；2. Blob\Clob不能创建索引
     * @param databaseTypeUrl
     * @param dataType
     * @param dataLength
     * @return
     */
    private static boolean canCreateIndex(String databaseTypeUrl, AttributeDataType dataType, Integer dataLength){
        boolean result = true;
        boolean isMySql = databaseTypeUrl.equalsIgnoreCase("mysql");
        switch (dataType){
            case Text:
            case String:
                if(isMySql && dataLength >= 756)
                    result = false;
                else
                    result = true;
                break;
//            case BLOB:
//            case CLOB:
//                result = false;
//                break;
            default:
                result = true;
                break;
        }

        return result;
    }

    /**
     * 格式化索引名称
     *   索引名称小于等于30字符：
     *      uniqueIndex--->uk_t_my_table_name_my_field_name
     *      generalIndex--->idx_t_my_table_name_my_field_name
     *   索引名称大于30字符：
     *      uniqueIndex--->uk_tmtn_mfn
     *      generalIndex--->idx_tmtn_mfn
     * @param indexType uniqueIndex/generalIndex
     * @param tableName 例如：t_my_table_name
     * @param fieldName 例如：my_field_name
     * @return
     */
    private static String formatIndexName(String databaseTypeUrl, String indexType, String tableName, String fieldName){
        boolean isOracle = databaseTypeUrl.equalsIgnoreCase("oracle");
        String result = !indexType.equalsIgnoreCase("uniqueIndex")
                ? indexType.equalsIgnoreCase("generalIndex")
                ? "idx_" + tableName + "_" + fieldName
                : ""
                : "uk_" + tableName + "_" + fieldName;
        // Oracle表名/索引名等等名称30字符限制，错误：ORA-00972: Identifier is too long
        if(isOracle && result.length() > 30){
            tableName = getFirstDbTextName(tableName);
            fieldName = getFirstDbTextName(fieldName);
            result = !indexType.equalsIgnoreCase("uniqueIndex")
                    ? indexType.equalsIgnoreCase("generalIndex")
                    ? "idx_" + tableName + "_" + fieldName
                    : ""
                    : "uk_" + tableName + "_" + fieldName;
        }
        return result;
    }

    private static String getFirstDbTextName(String dbTextName){
        List<String> txtList = Arrays.asList(dbTextName.split("_"));
        List<String> result = txtList.stream().map(m -> String.valueOf(m.charAt(0))).collect(Collectors.toList());
        return result.stream().collect(Collectors.joining(""));
    }
}
