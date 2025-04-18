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

import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;


/**
 * Utility class for retrieving resources from the classpath.
 * 
 * @author JohnLesterDev
 */
public final class ResourceUtils {
    public static String[] strList;

    /**
     * Get an InputStream from a resource file
     * @param path Path to the resource file
     * @return InputStream of the resource file
     */
    public static InputStream getResourceFileStream(String path) {
        InputStream inputStream = Thread.currentThread()
                                        .getContextClassLoader()
                                        .getResourceAsStream(path);

        if (inputStream == null) {
            throw new RuntimeException("Resource not found: " + path);
        }
        return inputStream;
    }

    /**
     * Get an InputStreamReader from a resource file using UTF-8 encoding.
     * 
     * @param path Path to the resource file
     * @return InputStreamReader of the resource file
     * @throws RuntimeException if the resource file is not found
     */
    public static InputStreamReader getResourceFileStreamReader(String path) {
        InputStreamReader reader = new InputStreamReader(
            Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(path)
                , StandardCharsets.UTF_8
                );
        
        return reader;
    }

    /**
     * Get a URL of a resource file
     * @param path Path to the resource
     * @return URL of the resource
     */
    public static URL getResourceFile(String path) {
        URL resourceUrl = Thread.currentThread().getContextClassLoader().getResource(path);
        if (resourceUrl == null) {
            throw new RuntimeException("Resource not found: " + path);
        }
        return resourceUrl;
    }

    /**
     * Given a list name, reads from a resource file and returns a list of strings.
     * 
     * @param fileList List to append to
     * @param list Name of the list to read from
     * @return List of strings
     */
    public static List<String> getResourceList(List<String> fileList, String list) {
        try (BufferedReader br = new BufferedReader(getResourceFileStreamReader("list/" + list + ".list"))) {
            br.lines().map(String::trim).forEach(fileList::add);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileList;
    }

    private ResourceUtils() {}
}
