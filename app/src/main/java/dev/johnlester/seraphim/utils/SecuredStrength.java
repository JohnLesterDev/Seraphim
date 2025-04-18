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


/**
 * Enumerates the strength of a given password
 * 
 * <ul>
 * <li>UNACCEPTABLE: Password is not secure at all</li>
 * <li>WEAK: Password is weak and should be changed</li>
 * <li>MEDIUM: Password is medium strength</li>
 * <li>STRONG: Password is strong and secure</li>
 * </ul>
 */
public enum SecuredStrength {
    UNACCEPTABLE, WEAK, MEDIUM, STRONG
}
