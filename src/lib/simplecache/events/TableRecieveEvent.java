/*
 * Copyright 2014 Goblom.
 *
 * All Rights Reserved unless otherwise explicitly stated.
 */

package lib.simplecache.events;

import java.sql.ResultSet;

/**
 * @author Goblom
 */
public class TableRecieveEvent {

    private final String sqlQuery;
    private final ResultSet rs;

    public TableRecieveEvent(ResultSet rs, String sqlQuery) {
        this.rs = rs;
        this.sqlQuery = sqlQuery;
    }

    public String getQuery() {
        return sqlQuery;
    }

    public ResultSet getResult() {
        return rs;
    }
}
