package com.google.code.jtracert.sequenceDiagram;

import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.awt.*;
import java.awt.font.TextLayout;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

class ClassShape extends DiagramElement {

    protected String className;

    protected int captionHorizontalPadding;
    protected int captionHeight;

    public List<MethodShape> currentMethodsStack = new ArrayList<MethodShape>(5);
    public List<MethodShape> methodShapes = new LinkedList<MethodShape>();

    @Override
    public void paint(Graphics g) {

        g.setColor(new Color(0xFF,0xFF,0xFF));

        g.fillRect(
                x,
                y,
                width,
                captionHeight);

        g.setColor(isSelected() ? new Color(0xFF,0,0) : new Color(0,0,0));

        g.drawRect(
                x,
                y,
                width,
                captionHeight);

        g.drawString(
                className,
                x + captionHorizontalPadding + DiagramElementsBuilder.CLASS_BORDER_WIDTH,
                y + captionHeight - DiagramElementsBuilder.CLASS_VERTICAL_PADDING - DiagramElementsBuilder.CLASS_BORDER_WIDTH);
        
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
