/*
 * Copyright 2014 Goblom.
 *
 * All Rights Reserved unless otherwise explicitly stated.
 */

package lib.simplecache.threads;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Goblom
 */
public class ThreadedUpdate extends Thread {
    private Connection conn;
    private final String sqlQuery;

    public ThreadedUpdate(Connection conn, String sqlQuery) {
        this.conn = conn;
        this.sqlQuery = sqlQuery;
    }

    @Override
    public void run() {
        try {
            if (!this.isInterrupted()) {
                Statement state = conn.createStatement();
                state.execute(sqlQuery);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}