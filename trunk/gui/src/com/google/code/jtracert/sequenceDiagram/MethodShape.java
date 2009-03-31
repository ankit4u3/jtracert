package com.google.code.jtracert.sequenceDiagram;

import com.google.code.jtracert.sequenceDiagram.DiagramElement;

import java.awt.*;
import java.util.Collection;
import java.util.LinkedList;

class MethodShape extends DiagramElement {

    protected int rightSlotWidth;
    protected int rightSlotHeight;

    protected ClassShape classShape;

    private Collection<DiagramElement> incomingMethodCallShapes = new LinkedList<DiagramElement>();

    @Override
    public int getLevel() {
        return 100;
    }

    @Override
    public void paint(Graphics g) {

        Polygon p = createMethodShapePolygon();

        g.setColor(new Color(0xFF,0xFF,0xFF));

        g.fillPolygon(p);

        g.setColor(isSelected() ? new Color(0xFF,0,0) : new Color(0,0,0));

        g.drawPolygon(p);

    }

    @Override
    public boolean contains(double x, double y) {

        Polygon p = createMethodShapePolygon();

        return p.contains(x, y);

    }

    private Polygon createMethodShapePolygon() {
        Polygon p = new Polygon();

        p.addPoint(x, y);
        p.addPoint(x + width, y);

        if (0 != rightSlotHeight) {
            p.addPoint(x + width, y + (height - rightSlotHeight) / 2);
            p.addPoint(x + width - rightSlotWidth, y + (height - rightSlotHeight) / 2);
            p.addPoint(x + width - rightSlotWidth, y + (height + rightSlotHeight) / 2);
            p.addPoint(x + width, y + (height + rightSlotHeight) / 2);
        }

        p.addPoint(x + width, y + height);
        p.addPoint(x, y + height);
        return p;
    }

    public int getRightSlotWidth() {
        return rightSlotWidth;
    }

    public void setRightSlotWidth(int rightSlotWidth) {
        this.rightSlotWidth = rightSlotWidth;
    }

    public int getRightSlotHeight() {
        return rightSlotHeight;
    }

    public void setRightSlotHeight(int rightSlotHeight) {
        this.rightSlotHeight = rightSlotHeight;
    }

    public ClassShape getClassShape() {
        return classShape;
    }

    public void setClassShape(ClassShape classShape) {
        this.classShape = classShape;
    }

    @Override
    public void incrementHeight(int heightIncrement) {
        super.incrementHeight(heightIncrement);
        if (null != classShape) {
            classShape.incrementHeight(heightIncrement);
        }
    }

    @Override
    public void incrementX(int xIncrement) {
        super.incrementX(xIncrement);
        for (DiagramElement incomingMethodCallShape : incomingMethodCallShapes) {
            incomingMethodCallShape.incrementWidth(xIncrement);
        }
    }

    public void addIncomingMethodCallShape(DiagramElement incomingMethodCallShape) {
        incomingMethodCallShapes.add(incomingMethodCallShape);
    }

}