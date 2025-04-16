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


package dev.johnlester.seraphim.utils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Utility class for retrieving information about the system's monitors.
 * Provides methods to get monitor count, default monitor index, monitor indexes,
 * monitor dimensions, and monitor bounds.
 * 
 * This class cannot be instantiated.
 * 
 * @author JohnLesterDev
 */
public final class MonitorUtils {

    private MonitorUtils() {
        throw new AssertionError("MonitorUtils is a utility class and cannot be instantiated.");
    }

    /**
     * Returns the number of monitors available in the system.
     *
     * @return the number of monitors
     */
    public static int getMonitorCount() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices().length;
    }

    /**
     * Returns the index of the default monitor.
     *
     * @return the index of the default monitor, or -1 if not found
     */
    public static int getDefaultMonitorIndex() {
        GraphicsDevice defaultDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        GraphicsDevice[] devices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();

        for (int i = 0; i < devices.length; i++) {
            if (devices[i].equals(defaultDevice)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns a list of monitor indexes.
     *
     * @return a list containing the indexes of all monitors
     */
    public static List<Integer> getMonitorIndexes() {
        int count = getMonitorCount();
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            indexes.add(i);
        }
        return indexes;
    }

    /**
     * Returns a list of dimensions for each monitor.
     *
     * @return a list of dimensions, one for each monitor
     */
    public static List<Dimension> getMonitorDimensions() {
        GraphicsDevice[] devices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        List<Dimension> dimensions = new ArrayList<>();

        for (GraphicsDevice device : devices) {
            DisplayMode mode = device.getDisplayMode();
            dimensions.add(new Dimension(mode.getWidth(), mode.getHeight()));
        }
        return dimensions;
    }

    /**
     * Returns a list of bounds (rectangles) for each monitor.
     *
     * @return a list of rectangles representing the bounds of each monitor
     */
    public static List<Rectangle> getMonitorBounds() {
        GraphicsDevice[] devices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        List<Rectangle> boundsList = new ArrayList<>();

        for (GraphicsDevice device : devices) {
            boundsList.add(device.getDefaultConfiguration().getBounds());
        }
        return boundsList;
    }
}
