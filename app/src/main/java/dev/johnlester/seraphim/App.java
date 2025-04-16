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

package dev.johnlester.seraphim;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import dev.johnlester.seraphim.controllers.ViewManager;
import dev.johnlester.seraphim.utils.ConfigUtils;
import dev.johnlester.seraphim.views.AuthenticationView;
 
/**
 * Main class for the Seraphim application.
 * 
 * @author JohnLesterDev
 */
public class App {
    /**
     * Main method for the application.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(ConfigUtils.get("title"));
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent windowEvent) {
                    beforeExit();
                    System.exit(0);
                }
            });
            
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            ViewManager.init(frame);

            AuthenticationView authenticationView = new AuthenticationView(frame);
            ViewManager.switchTo(authenticationView, frame.getTitle() + " - Login");

            frame.setVisible(true);
        });
    }

    public static void beforeExit() {
        ConfigUtils.saveProperties();
    }
}

 
