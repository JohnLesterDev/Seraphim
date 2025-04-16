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

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import dev.johnlester.seraphim.utils.SecuredUtils;


public class AuthenticationModel extends BaseModel {
    private static AuthenticationModel instance;

    public static synchronized AuthenticationModel getInstance() {
        if (instance == null) {
            instance = new AuthenticationModel();
        }
        return instance;
    }

    private AuthenticationModel() {
        super();
    }


    @Override
    protected String defineTableName() {
        return "Authentication";
    }

    @Override
    protected String defineTableSchema() {
        return "id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "username TEXT NOT NULL UNIQUE,\n" +
            "passwordHash TEXT NOT NULL,\n" +
            "passwordSalt TEXT NOT NULL,\n" +
            "pin TEXT NULL,\n" +
            "pinSalt TEXT NULL,\n" +
            "isPinSet BOOLEAN DEFAULT FALSE,\n" +
            "createdAt DATETIME DEFAULT CURRENT_TIMESTAMP";
    }

    public Boolean usernameExists(String username) {
        try {
            Connection conn = SQLHelper.getSession();
            String sql = "SELECT * FROM " + defineTableName() + " WHERE username = ?";

            boolean exists = SQLHelper.recordExists(sql, new Object[] { username }, conn);
            SQLHelper.endSession();

            return exists;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public boolean createUser(String username, String password) {
        if (usernameExists(username)) {
            throw new IllegalArgumentException("Username already exists.");
        }
    
        String salt = SecuredUtils.generateSalt();
    
        try {
            String passwordHash = SecuredUtils.hashSHA256(password + salt);
    
            String sql = "INSERT INTO " + defineTableName() + 
            " (username, passwordHash, passwordSalt, pin, pinSalt, isPinSet) VALUES (?, ?, ?, ?, ?, ?)";
            Object[] params = new Object[] {
                username,
                passwordHash,
                salt,
                null,
                null,
                false
            };
    
            return SQLHelper.executePreparedUpdate(sql, params) > 0;    
        } catch (NoSuchAlgorithmException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating user.", e);
        }
    }


    public void createPIN(String username, String pin) {
        if (pin.length() != 6) {
            throw new IllegalArgumentException("PIN must be exactly 6 digits.");
        }
    
        String salt = SecuredUtils.generateSalt();
    
        try {
            String pinHash = SecuredUtils.hashSHA256(pin + salt);
    
            String sql = "UPDATE " + defineTableName() + " SET pin = ?, pinSalt = ?, isPinSet = TRUE WHERE username = ?";
            SQLHelper.executePreparedUpdate(sql, new Object[] { pinHash, salt, username });
    
        } catch (NoSuchAlgorithmException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error setting PIN for user: " + username);
        }
    }
    
    
    public Boolean validateCredentials(String username, String password) {
        try {
            String sql = "SELECT passwordHash, passwordSalt FROM " + defineTableName() + " WHERE username = ?";
            Object[] params = new Object[] { username };
            List<Map<String, Object>> result = SQLHelper.preparedQueryToList(sql, params);

            if (result.isEmpty()) {
                return false;
            }

            String passwordHash = (String) result.get(0).get("passwordHash");
            String passwordSalt = (String) result.get(0).get("passwordSalt");

            String providedPasswordHash = SecuredUtils.hashWithSalt(password, passwordSalt);

            return passwordHash.equals(providedPasswordHash);
        } catch (NoSuchAlgorithmException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public Boolean validatePIN(String username, String pin) {
        try {
            String sql = "SELECT pin, pinSalt FROM " + defineTableName() + " WHERE username = ?";
            Object[] params = new Object[] { username };
            List<Map<String, Object>> result = SQLHelper.preparedQueryToList(sql, params);

            if (result.isEmpty()) {
                return false;
            }

            String pinHash = (String) result.get(0).get("pin");
            String pinSalt = (String) result.get(0).get("pinSalt");

            String providedPinHash = SecuredUtils.hashWithSalt(pin, pinSalt);

            return pinHash.equals(providedPinHash);
        } catch (NoSuchAlgorithmException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
