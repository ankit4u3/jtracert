package com.google.code.jtracert.sequenceDiagram;

import java.awt.*;
import java.awt.geom.PathIterator;
import java.awt.geom.AffineTransform;

interface Paintable extends Shape {

    void paint(Graphics g);

    PathIterator getPathIterator(AffineTransform at) throws UnsupportedOperationException;

    PathIterator getPathIterator(AffineTransform at, double flatness) throws UnsupportedOperationException;

    boolean isSelected();

    void setSelected(boolean selected);

}
