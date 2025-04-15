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


package dev.johnlester.seraphim.controllers;

import dev.johnlester.seraphim.models.SQLiteHelper;
import dev.johnlester.seraphim.views.PasswordManagerView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PasswordManagerController {
    private PasswordManagerView view;

    public PasswordManagerController(PasswordManagerView view) {
        this.view = view;
        
        // Initialize the database and create users table
        SQLiteHelper.createUsersTable();
        
        // Add action listeners for buttons
        view.addLoginButtonListener(new LoginAction());
        view.addRegisterButtonListener(new RegisterAction());
    }

    // Login button action
    private class LoginAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = view.getUsername();
            String password = view.getPassword();

            if (SQLiteHelper.checkUserCredentials(username, password)) {
                view.showSuccessMessage("Login Successful!");
            } else {
                view.showErrorMessage("Invalid credentials.");
            }
        }
    }

    // Register button action
    private class RegisterAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = view.getUsername();
            String password = view.getPassword();

            if (SQLiteHelper.addUser(username, password)) {
                view.showSuccessMessage("User Registered!");
            } else {
                view.showErrorMessage("Registration Failed!");
            }
        }
    }
}
