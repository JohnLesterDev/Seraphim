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

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import dev.johnlester.seraphim.utils.effect.EffectManager;

public class SLabel extends JLabel {
    private Image outlineIcon;
    private Image solidIcon;
    private float currentAlpha = 1.0f;
    private float currentRotation = 0f;
    private boolean isSolid = false;

    public SLabel(Image outlineIcon, Image solidIcon) {
        this.outlineIcon = outlineIcon;
        this.solidIcon = solidIcon;
        setIcon(new ImageIcon(outlineIcon));
        setOpaque(false);
    }

    public void transitionIcons(int durationMs, EffectManager.EasingFunction easing) {
        Image from = isSolid ? solidIcon : outlineIcon;
        Image to = isSolid ? outlineIcon : solidIcon;
    
        new EffectManager<>(
            this,
            new EffectManager.ImageWithAlpha(from, to, 0f),
            new EffectManager.ImageWithAlpha(from, to, 1f),
            durationMs,
            EffectManager.IMAGE_INTERPOLATOR,
            imageWithAlpha -> {
                Image blended = blendImages(
                    from,
                    to,
                    imageWithAlpha.progress
                );
                setIcon(new ImageIcon(blended));
            },
            easing,
            60
        ).play();
    
        isSolid = !isSolid; // Flip state
    }

    public void transitionRotations(float toDeg, int durationMs, EffectManager.EasingFunction easing) {
        float startRotation = currentRotation;
    
        new EffectManager<>(
            this,
            startRotation,
            toDeg,
            durationMs,
            EffectManager.FLOAT_INTERPOLATOR,
            rotation -> {
                currentRotation = rotation;
                repaint();
            },
            easing,
            60
        ).play();
    }

    private Image blendImages(Image img1, Image img2, float progress) {
        int width = Math.max(img1.getWidth(null), img2.getWidth(null));
        int height = Math.max(img1.getHeight(null), img2.getHeight(null));
        BufferedImage blended = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = blended.createGraphics();

        // Draw first image with inverse alpha
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f - progress));
        g2.drawImage(img1, 0, 0, null);

        // Draw second image with forward alpha
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, progress));
        g2.drawImage(img2, 0, 0, null);

        g2.dispose();
        return blended;
    }

    public void setAlpha(float alpha) {
        this.currentAlpha = alpha;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        // Apply rotation around the center of the component
        int cx = getWidth() / 2;
        int cy = getHeight() / 2;
        g2.rotate(Math.toRadians(currentRotation), cx, cy);

        // Apply alpha transparency
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, currentAlpha));

        // Paint the icon using the transformed graphics context
        super.paintComponent(g2);

        g2.dispose();
    }
}
