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

import javax.swing.*;

import dev.johnlester.seraphim.utils.ConfigUtils;
import dev.johnlester.seraphim.utils.MonitorUtils;

import java.awt.*;

/**
 * This abstract class provides a common interface for all views in the application.
 * It contains basic setup for the view and some common functionality.
 * 
 * @author JohnLesterDev
 */
public abstract class BaseView extends JPanel {

    protected JFrame frame;
    protected Dimension dimension;

    public BaseView(JFrame frame) {
        this.frame = frame;

        convertDimensionPercentage(defineWidthScale(), defineHeightScale());
        setPreferredSize(dimension);
        setLayout(new BorderLayout());
        initView();
    }


    protected abstract void initView();
    protected abstract float defineWidthScale();
    protected abstract float defineHeightScale();


    public void convertDimensionPercentage(float widthPercentage, float heightPercentage) {
        // All scaling is based on monitor height
        int monitorIndex = ConfigUtils.getInt("defaultMonitorIndex", MonitorUtils.getDefaultMonitorIndex());
        Dimension fullDimension = MonitorUtils.getMonitorDimensions().get(monitorIndex);
    
        int heightBase = (int) fullDimension.getHeight();
        this.dimension = new Dimension(
            (int) (heightBase * widthPercentage),
            (int) (heightBase * heightPercentage)
        );
    }


    public void setWindowTitle(String title) {
        frame.setTitle(title);
    }


    public void refreshView() {
        frame.revalidate();
        frame.repaint();
    }


    public void resetView() {
        // For clearing or resetting fields, buttons, etc.
    }
}
