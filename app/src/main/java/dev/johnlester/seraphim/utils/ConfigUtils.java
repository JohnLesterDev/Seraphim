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

import java.io.*;
import java.util.Properties;

public final class ConfigUtils {
    private static final Properties properties = new Properties();
    private static final String configFilePath = System.getProperty("user.home") + 
    "/.local/share/seraphim/.config";
    private static final File configFile = new File(configFilePath);

    static {
        if (configFile.exists()) {
            loadProperties();
        } else {
            File parentDir = configFile.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs(); 
            }

            setDefaultProperties();
            saveProperties();
        }
    }

    private static void setDefaultProperties() {
        properties.setProperty("title", "Seraphim: Secure Vault Overseer");
        properties.setProperty("version", "0.0.1");
        properties.setProperty("homePath", configFilePath);
        properties.setProperty("rememberedUsername", "");
        properties.setProperty("defaultMonitorIndex", MonitorUtils.getDefaultMonitorIndex() + "");
    }

    private static void loadProperties() {
        try (FileInputStream fis = new FileInputStream(configFile)) {
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveProperties() {
        try (FileOutputStream fos = new FileOutputStream(configFile)) {
            properties.store(fos, "Seraphim Configuration File: Do Not Edit");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }

    public static int getInt(String key, int defaultValue) {
        try {
            return Integer.parseInt(properties.getProperty(key));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static void set(String key, String value) {
        properties.setProperty(key, value);
        saveProperties();
    }

    public static void setAndSave(String key, Object value) {
        properties.setProperty(key, String.valueOf(value));
        saveProperties();
    }

    public static void reload() {
        loadProperties();
    }
}
