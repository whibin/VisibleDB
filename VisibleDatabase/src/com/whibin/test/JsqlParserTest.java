package com.whibin.test;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.update.Update;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author whibin
 * @date 2020/5/3 18:57
 * @Description: 用于测试JsqlParser工具类
 */

public class JsqlParserTest {
    @Test
    public void test() throws JSQLParserException {
        Select stmt = (Select) CCJSqlParserUtil.parse("select * from user where id=1 and password=2");

        List<String> list = new ArrayList<>();
        for (SelectItem selectItem : ((PlainSelect)stmt.getSelectBody()).getSelectItems()) {
            selectItem.accept(new SelectItemVisitorAdapter() {
                @Override
                public void visit(SelectExpressionItem item) {
                    list.add(item.getExpression().toString());
                }
            });
        }
        Expression where = ((PlainSelect) stmt.getSelectBody()).getWhere();
        System.out.println(where.toString().replaceAll(" ",""));
        System.out.println(list);

    }

    @Test
    public void test2() throws JSQLParserException {
        Update stmt = (Update) CCJSqlParserUtil.parse("update user set username=1, password=2");

        List<Column> columns = stmt.getColumns();
        List<Expression> expressions = stmt.getExpressions();
        System.out.println(expressions);
        List<String> fields = new ArrayList<>();
        for (Column column : columns) {
            fields.add(column.getColumnName());
        }

        System.out.println(fields);
        System.out.println(stmt.getWhere());
    }

    @Test
    public void test3() throws JSQLParserException {
        Insert stmt = (Insert) CCJSqlParserUtil.parse("insert into user values(?,?,?)");
        List<Column> columns = stmt.getColumns();
        ItemsList list = stmt.getItemsList();
        System.out.println(list);

        System.out.println(columns);
    }

    @Test
    public void test4() throws JSQLParserException {
        Delete stmt = (Delete) CCJSqlParserUtil.parse("delete from user where id=?");
        Expression where = stmt.getWhere();
        System.out.println(where);
    }
}
