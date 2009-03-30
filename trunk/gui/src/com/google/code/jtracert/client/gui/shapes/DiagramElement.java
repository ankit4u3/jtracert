package com.google.code.jtracert.client.gui.shapes;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D;
import java.awt.geom.PathIterator;
import java.awt.geom.AffineTransform;
import java.io.Serializable;
import java.util.Set;
import java.util.HashSet;

public abstract class DiagramElement implements Paintable, Serializable {

    public volatile boolean active = false;

    protected int x;
    protected int y;
    protected int width;
    protected int height;

    private Set<DiagramElement> elementsOnTheRight =
            new HashSet<DiagramElement>();

    public abstract void paint(Graphics g);

    public boolean contains(double x, double y) {

        return (x >= this.x &&
                x <= this.x + this.width &&
                y >= this.y &&
                y <= this.y + this.height);

    }

    public boolean contains(double x, double y, double w, double h) {

        for (double i = 0; i < w; i++) {
            for (double j = 0; j < h; j++) {
                if (!contains(x+i,y+j)) return false;
            }
        }

        return true;

    }

    public boolean intersects(double x, double y, double w, double h) {

        for (double i = 0; i < w; i++) {
            for (double j = 0; j < h; j++) {
                if (contains(x+i,y+j)) return true;
            }
        }

        return false;

    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public Rectangle2D getBounds2D() {
        return getBounds();
    }

    public boolean contains(Point2D p) {
        return contains(p.getX(), p.getY());
    }

    public boolean contains(Rectangle2D r) {
        return contains(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    public boolean intersects(Rectangle2D r) {
        return intersects(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    public PathIterator getPathIterator(AffineTransform at) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public PathIterator getPathIterator(AffineTransform at, double flatness) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DiagramElement)) return false;

        DiagramElement that = (DiagramElement) o;

        if (height != that.height) return false;
        if (width != that.width) return false;
        if (x != that.x) return false;
        if (y != that.y) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + width;
        result = 31 * result + height;
        return result;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void addElementOnTheRight(DiagramElement diagramElement) {
        elementsOnTheRight.add(diagramElement);
    }

    public Set<DiagramElement> getElementsOnTheRight() {
        return elementsOnTheRight;
    }

}
