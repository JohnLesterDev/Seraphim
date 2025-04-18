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

import java.awt.Image;
import java.awt.LayoutManager2;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import dev.johnlester.seraphim.utils.ResourceUtils;
import dev.johnlester.seraphim.utils.effect.EffectManager;
import dev.johnlester.seraphim.utils.ui.builder.ComponentBuilder;
import dev.johnlester.seraphim.utils.ui.components.SLabel;


public class AuthenticationView extends BaseView {
    private SLabel closeButton;

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
        // Load your icons (replace with actual paths)
        Image outlineIcon = new ImageIcon(ResourceUtils.getResourceFile("icons/ui/buttons/X/outline.png")).getImage();
        Image solidIcon = new ImageIcon(ResourceUtils.getResourceFile("icons/ui/buttons/X/solid.png")).getImage();


        int x = (int) (getDimension().width * 0.866);
        int y = (int) (getDimension().height * 0.02);
        int width = (int) (getDimension().width * 0.093);
        int height = (int) (getDimension().height * 0.083);
        
        closeButton = new ComponentBuilder<SLabel>(new SLabel(outlineIcon, solidIcon))
            .setBounds(x, y, width, height)
            .setIcon(new ImageIcon(outlineIcon))
            .get();

        closeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                closeButton.transitionIcons(
                    850, 
                    EffectManager.cubicBezier(0.21f,0.29f,0.0f,1.0f)
                );

                closeButton.transitionRotations(90f, 850, EffectManager.cubicBezier(0.21f,0.29f,0.0f,1.0f));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                closeButton.transitionIcons(
                    850, 
                    EffectManager.cubicBezier(0.21f,0.29f,0.0f,1.0f)
                );

                closeButton.transitionRotations(-90f, 850, EffectManager.cubicBezier(0.21f,0.29f,0.0f,1.0f));
            }
        });

        add(closeButton);
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
