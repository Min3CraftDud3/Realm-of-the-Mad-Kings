/*
 * Copyright 2014 Goblom.
 *
 * All Rights Reserved unless otherwise explicitly stated.
 */

package lib.simplecache.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Goblom
 */
public class ResultParser {
    private final ResultSet rs;

    public ResultParser(ResultSet rs, boolean check) throws SQLException {
        if (check) {
            this.rs = this.check(rs);
        } else {
            this.rs = rs;
        }
    }

    final ResultSet check(ResultSet rs) throws SQLException {
        if (rs == null) return null;
        if (rs.isAfterLast()) return null;
        if (rs.isBeforeFirst()) rs.next();
        return rs;
    }

    public ResultSet get() {
        return rs;
    }

    public String getString() {
        try {
            return rs.getString(1);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String getString(String column) {
        try {
            return rs.getString(column);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public int getInt() {
        try {
            return rs.getInt(1);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    public int getInt(String column) {
        try {
            return rs.getInt(column);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    public long getLong() {
        try {
            return rs.getLong(1);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    public long getLong(String column) {
        try {
            return rs.getLong(column);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    public float getFloat() {
        try {
            return rs.getFloat(1);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    public float getFloat(String column) {
        try {
            return rs.getFloat(column);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    public boolean getBoolean() {
        try {
            return rs.getBoolean(1);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean getBoolean(String column) {
        try {
            return rs.getBoolean(column);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public List<Object> getList() {
        try {
            List<Object> list = new ArrayList<Object>();
            while (rs.next()) {
                list.add(rs.getObject(1));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Object> getList(String column) {
        try {
            List<Object> list = new ArrayList<Object>();
            while (rs.next()) {
                list.add(rs.getObject(column));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> getStringList() {
        try {
            List<String> list = new ArrayList<String>();
            while (rs.next()) {
                list.add(rs.getString(1));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> getStringList(String column) {
        try {
            List<String> list = new ArrayList<String>();
            while (rs.next()) {
                list.add(rs.getString(column));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
