package com.google.code.jtracert.sequenceDiagram;

import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.awt.*;

class ClassShape extends DiagramElement {

    protected String className;

    protected int captionHorizontalPadding;
    protected int captionVerticalPadding;
    protected int captionHeight;

    public List<MethodShape> currentMethodsStack = new ArrayList<MethodShape>(5);
    public List<MethodShape> methodShapes = new LinkedList<MethodShape>();

    @Override
    public int getLevel() {
        return 10;
    }

    @Override
    public void incrementX(int xIncrement) {
        super.incrementX(xIncrement);
        for (MethodShape methodShape : methodShapes) {
            methodShape.incrementX(xIncrement);
        }
    }

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
                y + captionHeight - captionVerticalPadding - DiagramElementsBuilder.CLASS_BORDER_WIDTH);
        
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

    public int getCaptionVerticalPadding() {
        return captionVerticalPadding;
    }

    public void setCaptionVerticalPadding(int captionVerticalPadding) {
        this.captionVerticalPadding = captionVerticalPadding;
    }

    public int getCaptionHeight() {
        return captionHeight;
    }

    public void setCaptionHeight(int captionHeight) {
        this.captionHeight = captionHeight;
    }

    public void addMethodShape(MethodShape methodShape) {
        methodShapes.add(methodShape);
    }

}
