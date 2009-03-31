package com.google.code.jtracert.sequenceDiagram;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.lang.reflect.Method;

abstract class DiagramElement implements Paintable, Serializable {

    protected int x;
    protected int y;
    protected int width;
    protected int height;

    private volatile boolean selected = false;
    private boolean isContainsPointMethodOverriden;

    private final static String CONTAINS_POINT_METHOD_NAME = "contains";

    protected DiagramElement() {
        try {
            Class<? extends DiagramElement> actualClass = getClass();
            Method containsPointMethod =
                    actualClass.getMethod(CONTAINS_POINT_METHOD_NAME, double.class, double.class);
            isContainsPointMethodOverriden =
                    actualClass.equals(containsPointMethod.getDeclaringClass());
        } catch (NoSuchMethodException e) {
            isContainsPointMethodOverriden = false;
        }
    }

    public int getLevel() {
        return 0;
    }

    public abstract void paint(Graphics g);

    public boolean contains(double x, double y) {

        return (x >= this.x &&
                x <= this.x + this.width &&
                y >= this.y &&
                y <= this.y + this.height);

    }

    public boolean contains(double x, double y, double width, double height) {

        if (isContainsPointMethodOverriden) {

            for (double i = 0; i < width; i++) {
                for (double j = 0; j < height; j++) {
                    if (!contains(x+i, y+j)) return false;
                }
            }

            return true;

        } else {

            return (x >= this.x &&
                    x + width <= this.x + this.width &&
                    y >= this.y &&
                    y + height <= this.y + this.height);

        }

    }

    public boolean intersects(double x, double y, double width, double height) {

        if (isContainsPointMethodOverriden) {

            for (double i = 0; i < width; i++) {
                for (double j = 0; j < height; j++) {
                    if (contains(x+i, y+j)) return true;
                }
            }

            return false;

        } else {

            return contains(x, y) || contains(x + width, y + height);

        }


    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width + 1, height + 1);
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

   /* @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DiagramElement)) return false;

        DiagramElement that = (DiagramElement) o;

        return height == that.height && width == that.width && x == that.x && y == that.y;

    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + width;
        result = 31 * result + height;
        return result;
    }*/

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void incrementX(int xIncrement) {
        setX(getX() + xIncrement);
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

    public void incrementHeight(int heightIncrement) {
        setHeight(getHeight() + heightIncrement);
    }

    public void incrementWidth(int widthIncrement) {
        setWidth(getWidth() + widthIncrement);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
