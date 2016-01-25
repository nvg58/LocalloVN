package com.locol.locol.db;

import android.database.sqlite.SQLiteStatement;

import java.sql.*;

/**
 * Created by nvg58 on 12/5/15.
 * <p/>
 * healthkee.side
 */
public class SQLHelper {
    public static void bind(SQLiteStatement stmt, int col, String var) throws SQLException {
        if (var == null)
            stmt.bindNull(col);
        else
            stmt.bindString(col, var);
    }

    public static void bind(SQLiteStatement stmt, int col, Integer var) throws SQLException {
        if (var == null)
            stmt.bindNull(col);
        else
            stmt.bindLong(col, var);
    }

    public static void bind(SQLiteStatement stmt, int col, Long var) throws SQLException {
        if (var == null)
            stmt.bindNull(col);
        else
            stmt.bindLong(col, var);
    }

    public static void bind(SQLiteStatement stmt, int col, Double var) throws SQLException {
        if (var == null)
            stmt.bindNull(col);
        else
            stmt.bindDouble(col, var);
    }

    public static void bind(SQLiteStatement stmt, int col, Float var) throws SQLException {
        if (var == null)
            stmt.bindNull(col);
        else
            stmt.bindDouble(col, var);
    }
}
