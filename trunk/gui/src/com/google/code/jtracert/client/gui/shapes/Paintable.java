package com.google.code.jtracert.client.gui.shapes;

import java.awt.*;
import java.awt.geom.PathIterator;
import java.awt.geom.AffineTransform;

public interface Paintable extends Shape {

    void paint(Graphics g);

    PathIterator getPathIterator(AffineTransform at) throws UnsupportedOperationException;

    PathIterator getPathIterator(AffineTransform at, double flatness) throws UnsupportedOperationException;

}
