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

package dev.johnlester.seraphim.utils.ui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;
import javax.swing.Timer;

import dev.johnlester.seraphim.utils.effect.FadeEffect;

public class FadePanel extends JPanel {
    private Color currentColor = Color.RED;

    public FadePanel() {
        putClientProperty("alpha", 1.0f);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = FadeEffect.applyAlpha(this, g);
        g2.setColor(currentColor);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
    }

    public void changeColor(Color newColor, float fromAlpha, float toAlpha, int durationMillis) {
        new FadeEffect(this, fromAlpha, toAlpha, durationMillis) {
            @Override
            public void play() {
                super.play();
                Timer delay = new Timer(durationMillis / 2, e -> {
                    currentColor = newColor;
                    repaint();
                });
                delay.setRepeats(false);
                delay.start();
            }
        }.play();
    }
}
