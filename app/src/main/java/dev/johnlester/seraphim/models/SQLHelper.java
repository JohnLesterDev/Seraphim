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
import java.util.List;


/**
 * This class contains static methods for interacting with the Seraphim
 * database. You must call {@link #initialize()} before you can use any of
 * the methods of this class.
 *
 * @author JohnLesterDev
 */
public class SQLHelper {
    private static final String DB_PATH = System.getProperty("user.home") + "/.local/share/seraphim/seraphim.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_PATH;

    private static boolean initialized = false;


    /**
     * Verifies that the database has been initialized. If not, throws an
     * IllegalStateException. You should call initialize() before using any
     * methods of this class.
     */
    private static void requireInitialization() {
        if (!initialized) {
            throw new IllegalStateException("SQLHelper is not initialized. Call initialize() first.");
        }
    }


    /**
     * Initializes the database by creating a directory at ~/.local/share/seraphim/
     * and creating the users table if it does not exist. You should call this
     * method before using any methods of this class.
     */
    public static void initialize() {
        File dir = new File(DB_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        initialized = true;

    }


    /**
     * Establishes a connection to the database.
     * 
     * @return a new Connection to the database
     * @throws SQLException if there is a problem connecting to the database
     */
    public static Connection connect() throws SQLException {
        requireInitialization();
        return DriverManager.getConnection(DB_URL);
    }
    

    /**
     * Executes an SQL UPDATE statement. The statement is executed with a
     * connection returned by connect(), and the Connection is closed
     * automatically when the statement is executed.
     * 
     * @param sql the SQL statement to execute
     * @return the number of rows affected by the statement
     * @throws SQLException if a database error occurs
     */
    public static int executeUpdate(String sql) throws SQLException {
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            return stmt.executeUpdate(sql);
        }
    }

    /**
     * Executes an SQL SELECT statement and returns the result as a ResultSet. 
     * The statement is executed with a connection returned by the connect() method. 
     * The caller is responsible for closing the ResultSet and the Connection.
     * <p>
     * The Statement used for the query is not automatically closed, 
     * so the caller is also responsible for closing the Statement after use.
     * </p>
     *
     * @param sql the SQL SELECT statement to execute
     * @return a ResultSet object that contains the data produced by the query
     * @throws SQLException if a database access error occurs
     */
    public static ResultSet executeQuery(String sql) throws SQLException {
        Connection conn = connect();
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(sql);
    }
    
    
    /**
     * Executes an SQL UPDATE statement using a prepared statement.
     * The statement is executed with a connection returned by the connect() method,
     * and the Connection is closed automatically when the statement is executed.
     * 
     * @param sql the SQL UPDATE statement to execute
     * @param params the parameters to be set in the prepared statement
     * @return the number of rows affected by the statement
     * @throws SQLException if a database access error occurs
     */
    public static int executePreparedUpdate(String sql, Object[] params) throws SQLException {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            return pstmt.executeUpdate();
        }
    }

    
    /**
     * Checks if a table exists in the database.
     *
     * @param tableName the name of the table to check for existence
     * @return true if the table exists, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public static boolean tableExists(String tableName) throws SQLException {
        String checkTableSQL = "SELECT name FROM sqlite_master WHERE type='table' AND name=?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(checkTableSQL)) {
            pstmt.setString(1, tableName);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();  
        }
    }


    /**
     * Closes the given database connection.
     * 
     * @param conn the Connection to be closed. If the connection is null,
     *             this method does nothing.
     *             If an SQLException occurs while closing, an error message
     *             is printed to the console.
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
        }
    }
    
    
    /**
     * Starts a database transaction.
     * 
     * This method sets the auto-commit to false for the given connection, which
     * means that any SQL statements executed with that connection will not be
     * committed until commitTransaction is called.
     * 
     * @param conn the Connection to start the transaction with
     * @throws SQLException if there is a problem setting the auto-commit mode
     */
    public static void startTransaction(Connection conn) throws SQLException {
        if (conn != null) {
            conn.setAutoCommit(false);
        }
    }

    
    /**
     * Commits the current database transaction.
     * 
     * This method commits any pending changes to the database and then
     * sets the auto-commit mode back to true, so that any subsequent
     * SQL statements will be committed immediately.
     * 
     * @param conn the Connection to commit the transaction with
     * @throws SQLException if there is a problem committing the transaction
     */
    public static void commitTransaction(Connection conn) throws SQLException {
        if (conn != null) {
            conn.commit();
            conn.setAutoCommit(true);
        }
    }


    /**
     * Rolls back the current database transaction.
     *
     * This method undoes all changes made in the current transaction
     * and sets the connection back to its previous state before the
     * transaction began. It should be called when a transaction
     * needs to be aborted due to an error or other condition.
     *
     * @param conn the Connection to rollback the transaction with.
     *             If the connection is null, this method does nothing.
     *             If an SQLException occurs during rollback, an error
     *             message is printed to the console.
     */
    public static void rollbackTransaction(Connection conn) {
        try {
            if (conn != null) {
                conn.rollback();
            }
        } catch (SQLException e) {
            System.out.println("Error rolling back transaction: " + e.getMessage());
        }
    }
    

    /**
     * Retrieves the database product name and version as a string.
     *
     * This method will open a connection to the database, retrieve the
     * database metadata, and close the connection. If an SQLException
     * occurs during this process, it will be thrown.
     *
     * @return the database product name and version.
     * @throws SQLException if an error occurs when connecting to the
     *         database or retrieving the metadata.
     */
    public static String getDatabaseMetadata() throws SQLException {
        try (Connection conn = connect()) {
            DatabaseMetaData metaData = conn.getMetaData();
            return "Database Product Name: " + metaData.getDatabaseProductName() +
                   "\nDatabase Product Version: " + metaData.getDatabaseProductVersion();
        }
    }


    /**
     * Creates a table with the given name and schema if the table does not exist.
     * 
     * @param tableName the name of the table to create
     * @param tableSchema the SQL code for the table schema
     * @throws SQLException if an error occurs when connecting to the database or
     *         executing the SQL statement.
     * 
     */
    public static void createTableIfNotExists(String tableName, String tableSchema) throws SQLException {
        if (!tableExists(tableName)) {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " (" + tableSchema + ");";
            executeUpdate(createTableSQL);
        }
    }


    /**
     * Returns a list of all tables in the database.
     * 
     * This method will connect to the database, execute a SQL query to
     * retrieve all table names, and return the results in a list.
     * 
     * @return a list of all table names in the database.
     * @throws SQLException if an error occurs when connecting to the
     *         database or executing the SQL statement.
     */
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

    
    /**
     * Drops a table with the given name.
     * 
     * This method will connect to the database, execute a SQL query to drop
     * the table, and return.
     * 
     * @param tableName the name of the table to drop
     * @throws SQLException if an error occurs when connecting to the
     *         database or executing the SQL statement.
     */
    public static void dropTable(String tableName) throws SQLException {
        String dropTableSQL = "DROP TABLE IF EXISTS " + tableName;
        executeUpdate(dropTableSQL);
    }
    
 
     public static boolean addUser(String username, String password) {
         requireInitialization();
         String insertSQL = "INSERT INTO users (username, password) VALUES (?, ?)";
 
         try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
             pstmt.setString(1, username);
             pstmt.setString(2, password);
             pstmt.executeUpdate();
             return true;
         } catch (SQLException e) {
             System.out.println(e.getMessage());
             return false;
         }
     }
 
     public static boolean checkUserCredentials(String username, String password) {
         requireInitialization();
         String selectSQL = "SELECT * FROM users WHERE username = ? AND password = ?";
 
         try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
             pstmt.setString(1, username);
             pstmt.setString(2, password);
             ResultSet rs = pstmt.executeQuery();
             return rs.next();
         } catch (SQLException e) {
             System.out.println(e.getMessage());
             return false;
         }
     }
 }
 
