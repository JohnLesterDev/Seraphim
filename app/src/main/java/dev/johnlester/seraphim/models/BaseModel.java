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

import java.sql.SQLException;


/**
 * This abstract class provides a common interface for all models in
 * the application. It contains methods for performing CRUD (Create,
 * Read, Update, Delete) operations on the database and for defining
 * the table schema.
 * 
 * @author JohnLesterDev
 */
public abstract class BaseModel {
    protected String tableSchema;
    protected String tableName;


    /**
     * Constructs a new instance of the BaseModel class. This constructor
     * initializes the table name and table schema by calling the
     * defineTableName() and defineTableSchema() methods.
     */
    public BaseModel() {
        this.tableName = defineTableName();
        this.tableSchema = defineTableSchema();
    }

    
    /**
     * Returns the name of the table associated with this model.
     * 
     * @return the table name as a String
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * Returns the schema of the table associated with this model.
     * 
     * @return the table schema as a String
     */
    public String getTableSchema() {
        return tableSchema;
    }

    /**
     * Creates the table associated with this model if it does not exist.
     * 
     * This method calls SQLHelper.createTableIfNotExists() to create the
     * table associated with this model. If the table already exists, this
     * method does not do anything.
     * 
     * @throws SQLException if there is an error creating the table
     */
    public void initializeTable() {
        try {
            SQLHelper.createTableIfNotExists(tableName, tableSchema);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected abstract String defineTableName();
    protected abstract String defineTableSchema();

}