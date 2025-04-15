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



public class AuthenticationModel extends BaseModel {
    private static AuthenticationModel instance;

    private AuthenticationModel() {
        super();
    }

    public static synchronized AuthenticationModel getInstance() {
        if (instance == null) {
            instance = new AuthenticationModel();
        }
        return instance;
    }

    @Override
    protected String defineTableName() {
        return "Authentication";
    }

    @Override
    protected String defineTableSchema() {
        return "id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "username TEXT NOT NULL UNIQUE,\n" +
            "password TEXT NOT NULL,\n" +
            "pin TEXT NOT NULL\n" +
            "isPinSet BOOLEAN DEFAULT FALSE\n";
    }

}
