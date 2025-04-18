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

import javax.swing.JFrame;
import javax.swing.JPanel;

import dev.johnlester.seraphim.utils.MonitorUtils;

/**
 * Class that manages the main window of the application.
 * 
 * @author JohnLesterDev
 */
public class ViewManager {
    /**
     * The main window of the application.
     */
    private static JFrame mainFrame;

    /**
     * Initializes the ViewManager by setting the main window.
     * 
     * @param frame The main window.
     */
    public static void init(JFrame frame) {
        mainFrame = frame;
    }

    /**
     * Switches the content pane of the main window to a new view.
     * 
     * @param newView The new view to be displayed in the content pane.
     * @param windowTitle The new title of the window.
     */
    public static void switchTo(JPanel newView, String windowTitle) {
        mainFrame.setTitle(windowTitle);
        mainFrame.setContentPane(newView);

        
        mainFrame.revalidate();
        mainFrame.pack();
        mainFrame.repaint();
    }

    /**
     * Switches the content pane of the main window to a new view and centers
     * the window on a specified monitor.
     * 
     * @param newView The new view to be displayed in the content pane.
     * @param windowTitle The new title of the window.
     * @param monitorIndex The index of the monitor to center the window on.
     */
    public static void switchTo(JPanel newView, String windowTitle, int monitorIndex) {
        mainFrame.setTitle(windowTitle);
        mainFrame.setContentPane(newView);
        
        mainFrame.revalidate();
        mainFrame.pack();

        MonitorUtils.centerFrameOnMonitor(mainFrame, monitorIndex);
        
        mainFrame.repaint();
    }
}
