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

 import java.sql.*;

 
 public class SQLiteHelper {
     private static final String DB_URL = "jdbc:sqlite:seraphim.db";
 
     public static Connection connect() throws SQLException {
         return DriverManager.getConnection(DB_URL);
     }
 
     public static void createUsersTable() {
         String createTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                 "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                 "username TEXT NOT NULL UNIQUE, " +
                 "password TEXT NOT NULL" +
                 ");";
 
         try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
             stmt.execute(createTableSQL);
         } catch (SQLException e) {
             System.out.println(e.getMessage());
         }
     }
 
     public static boolean addUser(String username, String password) {
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
 
