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


package dev.johnlester.seraphim.views;

import java.awt.Color;
import java.awt.LayoutManager2;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

import dev.johnlester.seraphim.utils.ui.ComponentBuilder;
import dev.johnlester.seraphim.utils.ui.components.FadePanel;


public class AuthenticationView extends BaseView {

    public AuthenticationView(JFrame frame) {
        super(frame); 
    }
    
    @Override
    protected void initWindow() {
        setWindowIcon("icons/favicons/android-chrome-192x192.png");

        //TODO print
        System.out.println(String.format(
            "W:%d H:%d", 
            getDimension().width, 
            getDimension().height
            )
        );

        frame.setUndecorated(true);
    }

    @Override
    protected void initView() {
        int parentWidth = getDimension().width;
    int parentHeight = getDimension().height;

    

    // Calculate the relative position and size
    int x = (int) (parentWidth * 0.866); // X = 86.6% of parent width
    int y = (int) (parentHeight * 0.02); // Y = 2% of parent height
    int width = (int) (parentWidth * 0.093); // Width = 9.3% of parent width
    int height = (int) (parentHeight * 0.083); // Height = 8.3% of parent height

    // Create and configure the panel using ComponentBuilder
    final FadePanel panel = new ComponentBuilder<>(new FadePanel())
            .setBounds(x, y, width, height)
            .setOpaque(false)
            .setBackground(Color.RED)
            .addTo(this)
            .get();
            
            panel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    // On hover in, fade from red to blue
                    panel.changeColor(Color.RED, 0.0f, 1.0f, 500);
                }
            
                @Override
                public void mouseExited(MouseEvent e) {
                    // On hover out, fade from blue back to red
                    panel.changeColor(Color.RED, 1.0f, 0.0f, 500);
                }
            }
        );
    }
    

    @Override
    protected float defineWidthScale() {
        return 0.4f;
    }
    @Override
    protected float defineHeightScale() {
        return 0.55f;
    }

    @Override
    protected LayoutManager2 defineLayoutManager() {
        return null;
    }
}

