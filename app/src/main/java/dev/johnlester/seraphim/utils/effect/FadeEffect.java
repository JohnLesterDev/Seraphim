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

package dev.johnlester.seraphim.utils.effect;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.Timer;

public class FadeEffect {
    private final JComponent component;
    private final float fromAlpha;
    private final float toAlpha;
    private final int duration;

    private static final String TIMER_PROPERTY = "fadeEffectTimer";
    private static final String ALPHA_PROPERTY = "alpha";

    public FadeEffect(JComponent component, float fromAlpha, float toAlpha, int durationMillis) {
        this.component = component;
        this.fromAlpha = fromAlpha;
        this.toAlpha = toAlpha;
        this.duration = durationMillis;
    }

    public void play() {
        if (component == null) return;

        Timer existing = (Timer) component.getClientProperty(TIMER_PROPERTY);
        if (existing != null && existing.isRunning()) {
            existing.stop();
        }

        int frameRate = 16;
        int frames = duration / frameRate;
        float step = (toAlpha - fromAlpha) / frames;

        final float[] alpha = {fromAlpha};
        component.putClientProperty(ALPHA_PROPERTY, fromAlpha);

        Timer timer = new Timer(frameRate, null);
        ActionListener listener = e -> {
            alpha[0] += step;
            boolean finished = (step > 0 && alpha[0] >= toAlpha) || (step < 0 && alpha[0] <= toAlpha);

            if (finished) {
                alpha[0] = toAlpha;
                timer.stop();
                component.putClientProperty(TIMER_PROPERTY, null);
            }

            component.putClientProperty(ALPHA_PROPERTY, alpha[0]);
            component.repaint();
        };

        timer.addActionListener(listener);
        component.putClientProperty(TIMER_PROPERTY, timer);
        timer.start();
    }

    public void playReverse() {
        new FadeEffect(component, toAlpha, fromAlpha, duration).play();
    }

    public static Graphics2D applyAlpha(JComponent component, Graphics g) {
        Float alpha = (Float) component.getClientProperty(ALPHA_PROPERTY);
        if (alpha == null) alpha = 1f;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        return g2;
    }
}