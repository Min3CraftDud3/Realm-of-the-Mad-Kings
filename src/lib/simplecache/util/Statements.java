/*
 * Copyright 2014 Goblom.
 *
 * All Rights Reserved unless otherwise explicitly stated.
 */

package lib.simplecache.util;

/**
 * @author Goblom
 */
public class Statements {
    /*
     * Parse Result
     * ---> new ResultParser(ResultSet rs).getInt("Count(*)");
     */
    public static String getRowsInTable(String table) {
        return "SELECT COUNT(*) FROM '" + table + "';";
    }

    /*
     * ParseResult
     * ---> new ResultParser(ResultSet rs).getInt(1) == 0
     * 
     * ---> If 0 then false
     * ---> If 1 then true
     */
    public static String tableContains(String table, String column, String value) {
        return "SELECT COUNT(" + column + ") AS " + column + " Count FROM " + table + " WHERE `" + column + "`='" + value + "'";
    }

    public static String createTable(String table, String... columns) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        sb.append("CREATE TABLE IF NOT EXISTS");
        sb.append(" ").append(table).append(" (");

        for (String column : columns) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(column);
            first = false;
        }

        sb.append(") ");
        return sb.toString();
    }

    public static String insertInto(String table, String[] columns, String[] values) {
        Validate.isTrue(columns.length == values.length, "Values to insert are not compatible.");

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ").append("`").append(table).append("` ");
        sb.append("(");
        boolean cf = true;
        for (String column : columns) {
            if (!cf) {
                sb.append(",");
            }
            sb.append("`").append(column).append("`");
            cf = false;
        }
        sb.append(") VALUES (");
        boolean vf = true;
        for (String value : values) {
            if (!vf) {
                sb.append(",");
            }
            sb.append("'").append(value).append("'");
            vf = false;
        }
        sb.append(");");

        return sb.toString();
    }

    public static String update(String table, String column, String value) {
        return "UPDATE " + table + " SET `" + column + "` = '" + value + "' ";
    }

    public static String where(String condition) {
        return "WHERE " + condition;
    }

    public enum ExampleStatements {
        /**
         * SELECT COUNT(*) FROM 'example_table';
         */
        getRowsInTable(Statements.getRowsInTable("example_table")),

        /**
         * SELECT COUNT(example_table) AS username Count FROM example_table WHERE `username`='Goblom'";
         */
        tableContains(Statements.tableContains("example_table", "username", "Goblom")),

        /**
         * CREATE TABLE IF NOT EXISTS example_table (id int(11) NOT NULL AUTO_INCREMENT, username varchar(16) NOT NULL, PRIMARY KEY (`id`))
         */
        createTable(Statements.createTable("example_table", "id int(11) NOT NULL AUTO_INCREMENT", "username varchar(16) NOT NULL", "PRIMARY KEY (`id`)")),

        /**
         * INSERT INTO `example_table` (`id`,`username`) VALUES ('NULL','Goblom');
         */
        insertInto(Statements.insertInto("example_table", new String[]{"id", "username"}, new String[]{"NULL", "Goblom"})),

        /**
         * UPDATE example_table SET username = NotGoblom
         */
        update(Statements.update("example_table", "username", "NotGoblom")),

        /**
         * UPDATE example_table SET username = NotGoblom WHERE `username`='Goblom'
         */
        updateWithWhere(Statements.update("example_table", "username", "NotGoblom") + Statements.where("`username`='Goblom'"));

        private final String statement;

        ExampleStatements(String statement) {
            this.statement = statement;
        }

        public String getSQLQuery() {
            return statement;
        }

        //        private String replace(String[]... replacements) {
        //            String s = statement;
        //            for (String[] replace : replacements) {
        //                s = s.replaceAll(replace[0], replace[1]);
        //            }
        //            return s;
        //        }
    }
}
