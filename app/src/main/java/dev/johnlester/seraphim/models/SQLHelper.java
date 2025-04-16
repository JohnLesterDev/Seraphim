/*
 * This file is part of Seraphim - Universal Secure Vault Overseer.
 * 
 * Seraphim is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Seraphim is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Seraphim.  If not, see <https://www.gnu.org/licenses/>.
 * 
 * Copyright (C) 2025 JohnLesterDev
 */

package dev.johnlester.seraphim.models;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public final class SQLHelper {
    private static final String DB_PATH = System.getProperty("user.home") + "/.local/share/seraphim/seraphim.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_PATH;

    private static boolean initialized = false;

    private static ThreadLocal<Connection> threadLocalSession = new ThreadLocal<Connection>() {
        @Override
        protected Connection initialValue() {
            try {
                return connect();
            } catch (SQLException e) {
                throw new IllegalStateException("Unable to create a connection.", e);
            }
        }
    };


    private static void requireInitialization() {
        if (!initialized) {
            throw new IllegalStateException("SQLHelper is not initialized. Call initialize() first.");
        }
    }


    public static void initialize() {
        File dir = new File(DB_PATH).getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }

        initialized = true;

    }


    public static Connection getSession() throws SQLException {
        Connection conn = threadLocalSession.get();
        if (conn == null || conn.isClosed()) {
            throw new IllegalStateException("No session is open.");
        }
        return conn;
    }
    
    public static void endSession() throws SQLException {
        Connection conn = threadLocalSession.get();
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
        threadLocalSession.remove(); 
    }


    public static Connection connect() throws SQLException {
        requireInitialization();
        return DriverManager.getConnection(DB_URL);
    }
    

    public static int executeUpdate(String sql) throws SQLException {
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            return stmt.executeUpdate(sql);
        }
    }


    public static ResultSet executeQuery(String sql) throws SQLException {
        try (Connection conn = connect(); 
             Statement stmt = conn.createStatement()) {
            return stmt.executeQuery(sql);
        }
    }    


    private static List<Map<String, Object>> queryToListCommon(String sql, Object[] params, Connection conn) throws SQLException {
        List<Map<String, Object>> resultList = new ArrayList<>();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    pstmt.setObject(i + 1, params[i]);
                }
            }
    
            try (ResultSet rs = pstmt.executeQuery()) {
                ResultSetMetaData meta = rs.getMetaData();
                int columnCount = meta.getColumnCount();
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.put(meta.getColumnName(i), rs.getObject(i));
                    }
                    resultList.add(row);
                }
            }
        }
        return resultList;
    }
    
    public static List<Map<String, Object>> queryToList(String sql) throws SQLException {
        return queryToListCommon(sql, null, connect());
    }
    
    public static List<Map<String, Object>> preparedQueryToList(String sql, Object[] params) throws SQLException {
        return queryToListCommon(sql, params, connect());
    }
    
    public static List<Map<String, Object>> queryToList(String sql, Connection conn) throws SQLException {
        return queryToListCommon(sql, null, conn);
    }
    
    public static List<Map<String, Object>> preparedQueryToList(String sql, Object[] params, Connection conn) throws SQLException {
        return queryToListCommon(sql, params, conn);
    }

    public static boolean recordExists(String sql, Object[] params) throws SQLException {
        return !preparedQueryToList(sql, params).isEmpty();
    }
    
    public static boolean recordExists(String sql, Object[] params, Connection conn) throws SQLException {
        return !preparedQueryToList(sql, params, conn).isEmpty();
    }
    
    
    public static int executePreparedUpdate(String sql, Object[] params) throws SQLException {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            return pstmt.executeUpdate();
        }
    }

    
    public static boolean tableExists(String tableName) throws SQLException {
        String checkTableSQL = "SELECT name FROM sqlite_master WHERE type='table' AND name=?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(checkTableSQL)) {
            pstmt.setString(1, tableName);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();  
        }
    }
    
    
    public static void startTransaction(Connection conn) throws SQLException {
        if (conn != null) {
            conn.setAutoCommit(false);
        }
    }

    
    public static void commitTransaction(Connection conn) throws SQLException {
        if (conn != null) {
            conn.commit();
            conn.setAutoCommit(true);
        }
    }


    public static void rollbackTransaction(Connection conn) {
        try {
            if (conn != null) {
                conn.rollback();
            }
        } catch (SQLException e) {
            System.out.println("Error rolling back transaction: " + e.getMessage());
        }
    }
    

    public static String getDatabaseMetadata() throws SQLException {
        try (Connection conn = connect()) {
            DatabaseMetaData metaData = conn.getMetaData();
            return "Database Product Name: " + metaData.getDatabaseProductName() +
                   "\nDatabase Product Version: " + metaData.getDatabaseProductVersion();
        }
    }


    public static void createTableIfNotExists(String tableName, String tableSchema) throws SQLException {
        if (!tableExists(tableName)) {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " (" + tableSchema + ");";
            executeUpdate(createTableSQL);
        }
    }


    public static List<String> getAllTables() throws SQLException {
        List<String> tables = new ArrayList<>();
        String sql = "SELECT name FROM sqlite_master WHERE type='table'";
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                tables.add(rs.getString("name"));
            }
        }
        return tables;
    }


    public static void dropTable(String tableName) throws SQLException {
        String dropTableSQL = "DROP TABLE IF EXISTS " + tableName;
        executeUpdate(dropTableSQL);
    }


    private SQLHelper() {
        throw new AssertionError("SQLHelper is a utility class and cannot be instantiated.");   
    }
 }
 
