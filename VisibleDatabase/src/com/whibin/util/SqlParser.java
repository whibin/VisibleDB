package com.whibin.util;

import com.whibin.constant.SqlType;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.TablesNamesFinder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author whibin
 * @date 2020/5/3 19:56
 * @Description: 解析sql的工具类
 */

public class SqlParser {
    /**
     * 获取操作的表名
     * @param sql
     * @return
     */
    public static String getTableName(String sql) throws JSQLParserException {
        return getTableName(sql, getSqlType(sql));
    }

    /**
     * 获取sql语句操作的字段
     * @param sql
     * @return
     * @throws JSQLParserException
     */
    public static List<String> getFields(String sql) throws JSQLParserException {
        switch (getSqlType(sql)) {
            case SELECT:
                return getSelectFields(sql);
            case UPDATE:
                return getUpdateFields(sql);
            case INSERT:
                return getInsertFields(sql);
            default:
                return null;
        }
    }

    /**
     * 获取where条件
     * @param sql
     * @return
     * @throws JSQLParserException
     */
    public static String getWhere(String sql) throws JSQLParserException {
        switch (getSqlType(sql)) {
            case UPDATE:
                return ((Update) CCJSqlParserUtil.parse(sql)).getWhere().toString();
            case DELETE:
                return ((Delete) CCJSqlParserUtil.parse(sql)).getWhere().toString();
            case SELECT:
                return getSelectWhere(sql);
            default:
                return null;
        }
    }

    /**
     * 获取sql类型
     * @param sql
     * @return
     */
    public static SqlType getSqlType(String sql) {
        sql = sql.trim().toLowerCase();
        if (sql.startsWith("select")) {
            return SqlType.SELECT;
        }
        if (sql.startsWith("update")) {
            return SqlType.UPDATE;
        }
        if (sql.startsWith("delete")) {
            return SqlType.DELETE;
        }
        if (sql.startsWith("insert")) {
            return SqlType.INSERT;
        }
        return null;
    }

    /**
     * 获取字段所对应的值
     * @param sql
     * @return
     * @throws JSQLParserException
     */
    public static List<String> getFieldValue(String sql) throws JSQLParserException {
        switch (getSqlType(sql)) {
            case INSERT:
                return getInsertFieldValue(sql);
            case UPDATE:
                return getUpdateFieldValue(sql);
            default:
                return null;
        }
    }

    private static List<String> getSelectFields(String sql) throws JSQLParserException {
        // 获取select语句的的字段
        Select stmt = (Select) CCJSqlParserUtil.parse(sql);
        List<String> list = new ArrayList<>();
        for (SelectItem selectItem : ((PlainSelect)stmt.getSelectBody()).getSelectItems()) {
            selectItem.accept(new SelectItemVisitorAdapter() {
                @Override
                public void visit(SelectExpressionItem item) {
                    list.add(item.getExpression().toString());
                }
            });
        }
        return list;
    }

    private static String getTableName(String sql, SqlType type) throws JSQLParserException {
        // 获取表名
        switch (type) {
            case SELECT:
                return new TablesNamesFinder().getTableList((Select) CCJSqlParserUtil.parse(sql)).get(0);
            case DELETE:
                return new TablesNamesFinder().getTableList((Delete) CCJSqlParserUtil.parse(sql)).get(0);
            case INSERT:
                return new TablesNamesFinder().getTableList((Insert) CCJSqlParserUtil.parse(sql)).get(0);
            case UPDATE:
                return new TablesNamesFinder().getTableList((Update) CCJSqlParserUtil.parse(sql)).get(0);
            default:
                return null;
        }
    }

    private static List<String> getUpdateFields(String sql) throws JSQLParserException {
        // 获取update语句的字段
        Update stmt = (Update) CCJSqlParserUtil.parse(sql);
        List<String> fields = new ArrayList<>();
        for (Column column : stmt.getColumns()) {
            fields.add(column.getColumnName());
        }
        return fields;
    }

    private static List<String> getInsertFields(String sql) throws JSQLParserException {
        // 获取insert语句的字段
        Insert stmt = (Insert) CCJSqlParserUtil.parse(sql);
        List<String> fields = new ArrayList<>();
        List<Column> columns = stmt.getColumns();
        // 如果为null，说明没有指定字段
        if (columns == null) {
            return null;
        }
        for (Column column : columns) {
            fields.add(column.getColumnName());
        }
        return fields;
    }

    private static String getSelectWhere(String sql) throws JSQLParserException {
        // 获取select语句的where条件
        Select stmt = (Select) CCJSqlParserUtil.parse(sql);
        Expression where = ((PlainSelect) stmt.getSelectBody()).getWhere();
        if (where == null) {
            return null;
        }
        return where.toString();
    }

    private static List<String> getInsertFieldValue(String sql) throws JSQLParserException {
        // 获取insert语句插入的值
        Insert stmt = (Insert) CCJSqlParserUtil.parse(sql);
        String[] itemsList = stmt.getItemsList().toString().replaceAll(" ","").split(",");
        System.out.println(Arrays.toString(itemsList));
        List<String> fieldValue = new ArrayList<>();
        for (int i = 0; i < itemsList.length; i++) {
            if (i == 0 && itemsList.length == 1) {
                fieldValue.add(itemsList[i].substring(1,itemsList[i].lastIndexOf(")")));
            }
            if (i == 0) {
                fieldValue.add(itemsList[i].substring(1));
            } else if (i == itemsList.length - 1) {
                fieldValue.add(itemsList[i].substring(0,itemsList[i].indexOf(")")));
            } else {
                fieldValue.add(itemsList[i]);
            }
        }
        return fieldValue;
    }

    private static List<String> getUpdateFieldValue(String sql) throws JSQLParserException {
        // 获取update语句要修改的值
        Update stmt = (Update) CCJSqlParserUtil.parse(sql);
        List<Expression> expressions = stmt.getExpressions();
        List<String> fieldValues = new ArrayList<>();
        for (Expression expression : expressions) {
            fieldValues.add(expression.toString());
        }
        return fieldValues;
    }
}
