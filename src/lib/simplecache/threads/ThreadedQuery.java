/*
 * Copyright 2014 Goblom.
 *
 * All Rights Reserved unless otherwise explicitly stated.
 */

package lib.simplecache.threads;

import lib.simplecache.events.TableRecieveEvent;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Goblom
 */
public class ThreadedQuery extends Thread {

    private Connection conn;
    private final String sqlQuery;
    private final DataHandler handler;

    public ThreadedQuery(Connection conn, String sqlQuery, DataHandler handler) {
        this.conn = conn;
        this.sqlQuery = sqlQuery;
        this.handler = handler;
    }

    @Override
    public void run() {
        try {
            if (!this.isInterrupted()) {
                Statement state = conn.createStatement();
                ResultSet rs = state.executeQuery(sqlQuery);

                TableRecieveEvent dre = new TableRecieveEvent(rs, sqlQuery);
                handler.onDataRecieve(dre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public interface DataHandler {
        void onDataRecieve(TableRecieveEvent event);
    }
}
