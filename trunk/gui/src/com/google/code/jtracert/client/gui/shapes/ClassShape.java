package com.google.code.jtracert.client.gui.shapes;

import java.util.LinkedList;
import java.util.List;
import java.awt.*;

public class ClassShape extends DiagramElement {

    protected String className;

    protected int captionHorizontalPadding;
    protected int captionHeight;

    private static final int CAPTION_VERTICAL_PADDING = 2;

    public List<MethodShape> currentMethodsStack = new LinkedList<MethodShape>();

    @Override
    public void paint(Graphics g) {

        g.setColor(new Color(0xFF,0xFF,0xFF));

        g.fillRect(
                x,
                y,
                width,
                captionHeight);

        g.setColor(active ? new Color(0xFF,0,0) : new Color(0,0,0));

        g.drawRect(
                x,
                y,
                width,
                captionHeight);

        g.drawString(
                className,
                x + captionHorizontalPadding,
                y + captionHeight - CAPTION_VERTICAL_PADDING);
        
        g.drawLine(
                x + ( width / 2),
                y + captionHeight,
                x + ( width / 2),
                y + height
        );

    }

    @Override
    public boolean contains(double px, double py) {

        if (py >= y && py <= y + captionHeight) {
            return (x <= px && px <= x + width);
        }

        if (py > y + captionHeight && py <= y + height) {
            return px == x + (width / 2);
        }

        return false;

    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getCaptionHorizontalPadding() {
        return captionHorizontalPadding;
    }

    public void setCaptionHorizontalPadding(int captionHorizontalPadding) {
        this.captionHorizontalPadding = captionHorizontalPadding;
    }

    public int getCaptionHeight() {
        return captionHeight;
    }

    public void setCaptionHeight(int captionHeight) {
        this.captionHeight = captionHeight;
    }

}
