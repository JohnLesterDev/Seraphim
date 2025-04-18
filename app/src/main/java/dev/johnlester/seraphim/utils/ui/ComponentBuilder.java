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

package dev.johnlester.seraphim.utils.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.Container;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;

import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.AbstractButton;


public class ComponentBuilder<T extends JComponent> {
    private final T component;
    
    public ComponentBuilder(T component) {
        this.component = component;
    }

    public T get() {
        return component;
    }

    public ComponentBuilder<T> setForeground(Color color) {
        component.setForeground(color);
        return this;
    }

    public ComponentBuilder<T> setBackground(Color color) {
        component.setBackground(color);
        return this;
    }

    public ComponentBuilder<T> setOpaque(boolean opaque) {
        component.setOpaque(opaque);
        return this;
    }

    public ComponentBuilder<T> setFont(Font font) {
        component.setFont(font);
        return this;
    }

    public ComponentBuilder<T> setBorder(Border border) { 
        component.setBorder(border);
        return this;
    }

    public ComponentBuilder<T> setCursor(Cursor cursor) {
        component.setCursor(cursor);
        return this;
    }

    public ComponentBuilder<T> setToolTipText(String text) {
        component.setToolTipText(text);
        return this;
    }

    public ComponentBuilder<T> setVisible(boolean visible) {
        component.setVisible(visible);
        return this;
    }

    public ComponentBuilder<T> setEnabled(boolean enabled) {
        component.setEnabled(enabled);
        return this;
    }

    public ComponentBuilder<T> setFocusable(boolean focusable) {
        component.setFocusable(focusable);
        return this;
    }

    public ComponentBuilder<T> setRequestFocusEnabled(boolean enabled) {
        component.setRequestFocusEnabled(enabled);
        return this;
    }

    public ComponentBuilder<T> addActionListener(ActionListener listener) {
        if (component instanceof AbstractButton button) {
            button.addActionListener(listener);
        }

        return this;
    }

    public ComponentBuilder<T> addFocusListener(FocusListener listener) {
        component.addFocusListener(listener);
        return this;
    }

    public ComponentBuilder<T> addKeyListener(KeyListener listener) {
        component.addKeyListener(listener);
        return this;
    }

    public ComponentBuilder<T> addMouseListener(MouseListener listener) {
        component.addMouseListener(listener);
        return this;
    }

    public ComponentBuilder<T> addMouseMotionListener(MouseMotionListener listener) {
        component.addMouseMotionListener(listener);
        return this;
    }

    public ComponentBuilder<T> addMouseWheelListener(MouseWheelListener listener) {
        component.addMouseWheelListener(listener);
        return this;
    }

    public ComponentBuilder<T> addTo(Container container) {
        container.add(component);
        return this;
    }
    
    public ComponentBuilder<T> setBounds(Rectangle bounds) {
        component.setBounds(bounds);
        return this;
    }

    public ComponentBuilder<T> setBounds(Point location, Dimension size) {
        component.setBounds(new Rectangle(location, size));
        return this;
    }

    public ComponentBuilder<T> setBounds(int x, int y, int width, int height) {
        component.setBounds(x, y, width, height);
        return this;
    }

}
