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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.util.function.Consumer;
import java.awt.event.ActionListener;

import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.BorderFactory;
import javax.swing.JComponent;

public class EffectManager<T> {
    private final JComponent component;
    private final int duration;
    private final int fps;
    private final T fromValue;
    private final T toValue;
    private final TransitionInterpolator<T> interpolator;
    private final Consumer<T> updater;
    private final EasingFunction easing;
    
    public interface TransitionInterpolator<T> {
        T interpolate(T from, T to, float progress);
    }
    
    // Constructor with easing
    public EffectManager(
            JComponent component, 
            T fromValue, 
            T toValue, 
            int durationMillis,
            TransitionInterpolator<T> interpolator, 
            Consumer<T> updater,
            EasingFunction easing,
            int fps
            ) {
        this.component = component;
        this.fps = 60;
        this.fromValue = fromValue;
        this.toValue = toValue;
        this.duration = durationMillis;
        this.interpolator = interpolator;
        this.updater = updater;
        this.easing = easing;
    }
    
    // Constructor with default linear easing
    public EffectManager(JComponent component, T fromValue, T toValue, int durationMillis,
                     TransitionInterpolator<T> interpolator, Consumer<T> updater) {
        this(component, fromValue, toValue, durationMillis, interpolator, updater, LINEAR, 60);
    }
    
    public void play() {
        if (component == null) return;
        
        Timer existingTimer = (Timer) component.getClientProperty("transitionTimer");
        if (existingTimer != null && existingTimer.isRunning()) {
            existingTimer.stop();
        }
        
        int frameRate = Math.max(1, 1000 / Math.max(1, fps)); 
        int frames = Math.max(1, duration / frameRate);
        final float[] rawProgress = {0.0f};
        
        Timer timer = new Timer(frameRate, null);
        ActionListener listener = e -> {
            rawProgress[0] += 1.0f / frames;
            boolean finished = rawProgress[0] >= 1.0f;
            
            if (finished) {
                rawProgress[0] = 1.0f;
                timer.stop();
                component.putClientProperty("transitionTimer", null);
            }
            
            // Apply easing function to the raw progress
            float easedProgress = easing.ease(rawProgress[0]);
            
            T currentValue = interpolator.interpolate(fromValue, toValue, easedProgress);
            updater.accept(currentValue);
            component.repaint();
        };
        
        timer.addActionListener(listener);
        component.putClientProperty("transitionTimer", timer);
        timer.start();
    }

    /**
     * Interpolates between two Colors.
     */
    public static TransitionInterpolator<Color> COLOR_INTERPOLATOR = (from, to, progress) -> {
        int r = (int) (from.getRed() * (1 - progress) + to.getRed() * progress);
        int g = (int) (from.getGreen() * (1 - progress) + to.getGreen() * progress);
        int b = (int) (from.getBlue() * (1 - progress) + to.getBlue() * progress);
        int a = (int) (from.getAlpha() * (1 - progress) + to.getAlpha() * progress);
        return new Color(r, g, b, a);
    };

    /**
     * Interpolates between two Images.
     */
    public static TransitionInterpolator<ImageWithAlpha> IMAGE_INTERPOLATOR = (from, to, progress) -> {
        return new ImageWithAlpha(from.image, to.image, progress);
    };

    public static class ImageWithAlpha {
        public Image image;
        public final Image fromImage;
        public final Image toImage;
        public final float progress;
        
        public ImageWithAlpha(Image fromImage, Image toImage, float progress) {
            this.fromImage = fromImage;
            this.toImage = toImage;
            this.progress = progress;
        }
    }

    public static final TransitionInterpolator<Float> ROTATION_INTERPOLATOR =
    (from, to, progress) -> from + (to - from) * progress;

    public static void setRotation(JComponent component, float angleDegrees) {
        component.putClientProperty("rotationAngle", angleDegrees);
        component.repaint();
    }
    
    public static float getRotation(JComponent component) {
        Object value = component.getClientProperty("rotationAngle");
        return value instanceof Float ? (Float) value : 0f;
    }

    public static void rotateComponent(JComponent comp, float fromDeg, float toDeg, int duration, EasingFunction easing) {
        new EffectManager<>(
            comp,
            fromDeg,
            toDeg,
            duration,
            ROTATION_INTERPOLATOR,
            angle -> setRotation(comp, angle),
            easing,
            60
        ).play();
    }

    /**
     * Interpolates between two Points.
     */
    public static TransitionInterpolator<Point> POINT_INTERPOLATOR = (from, to, progress) -> {
        int x = (int) (from.x + (to.x - from.x) * progress);
        int y = (int) (from.y + (to.y - from.y) * progress);
        return new Point(x, y);
    };

    /**
     * Interpolates between two Dimensions.
     */
    public static TransitionInterpolator<Dimension> DIMENSION_INTERPOLATOR = (from, to, progress) -> {
        int width = (int) (from.width + (to.width - from.width) * progress);
        int height = (int) (from.height + (to.height - from.height) * progress);
        return new Dimension(width, height);
    };

    /**
     * Interpolates between two Borders.
     */
    public static TransitionInterpolator<Border> BORDER_INTERPOLATOR = (from, to, progress) -> {
        if (from instanceof LineBorder && to instanceof LineBorder) {
            LineBorder fromBorder = (LineBorder) from;
            LineBorder toBorder = (LineBorder) to;
            
            Color color = COLOR_INTERPOLATOR.interpolate(
                fromBorder.getLineColor(), 
                toBorder.getLineColor(), 
                progress);
                
            int thickness = (int) (fromBorder.getThickness() + 
                (toBorder.getThickness() - fromBorder.getThickness()) * progress);
                
            return BorderFactory.createLineBorder(color, thickness);
        }

        return progress >= 0.5 ? to : from;
    };
    
    /**
     * Interpolates between two Floats.
     */
    public static TransitionInterpolator<Float> FLOAT_INTERPOLATOR = (from, to, progress) -> 
        from + (to - from) * progress;
    
    /**
     * Interpolates between two Integers.
     */
    public static TransitionInterpolator<Integer> INT_INTERPOLATOR = (from, to, progress) -> 
        (int)(from + (to - from) * progress);

    public interface EasingFunction {
        float ease(float progress);
    }
    
    /**
     * Linear easing function. Progress is directly proportional to the input.
     */
    public static final EasingFunction LINEAR = progress -> progress;
    
    /**
     * Ease in easing function. Progress accelerates from 0 to 1.
     */
    public static final EasingFunction EASE_IN = progress -> progress * progress;
    
    /**
     * Ease out easing function. Progress decelerates from 1 to 0.
     */
    public static final EasingFunction EASE_OUT = progress -> 1.0f - (1.0f - progress) * (1.0f - progress);
    
    /**
     * Ease in-out easing function. Progress accelerates from 0 to 0.5 and decelerates from 0.5 to 1.
     */
    public static final EasingFunction EASE_IN_OUT = progress -> {
        if (progress < 0.5f)
            return 2 * progress * progress;
        else
            return 1 - (float)Math.pow(-2 * progress + 2, 2) / 2;
    };
    

    /**
     * Cubic Bezier easing function. Calculates the point along the curve given by the control points (x1, y1) and (x2, y2) at the given progress.
     */
    public static EasingFunction cubicBezier(float x1, float y1, float x2, float y2) {
        return progress -> {
            float t = progress;
            for (int i = 0; i < 4; i++) {
                float x = 3 * t * (1 - t) * (1 - t) * x1 + 
                            3 * t * t * (1 - t) * x2 + 
                            t * t * t;
                if (Math.abs(x - progress) < 0.001f)
                    break;
                
                float dx = 3 * (1 - t) * (1 - t) * x1 + 
                            6 * t * (1 - t) * (x2 - x1) + 
                            3 * t * t * (1 - x2);
                if (dx == 0)
                    break;
                    
                t = t - (x - progress) / dx;
            }
            
            return 3 * t * (1 - t) * (1 - t) * y1 + 
                    3 * t * t * (1 - t) * y2 + 
                    t * t * t;
        };
    }
}