package com.google.code.jtracert.client.gui;

import com.google.code.jtracert.client.gui.shapes.ClassShape;
import com.google.code.jtracert.client.gui.shapes.Paintable;
import com.google.code.jtracert.client.gui.shapes.MethodShape;
import com.google.code.jtracert.client.model.MethodCall;

import java.awt.*;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

public class ShapesBuilder {

    private Graphics2D g;

    private Collection<Paintable> shapes = new LinkedList<Paintable>();

    private Map<String,ClassShape> classShapesMap = new HashMap<String,ClassShape>();
    private int x;
    private static final int BORDER_WIDTH = 1;

    private ShapesBuilder(Graphics2D g) {
        this.g = g;
        x = 10;
    }

    private void buildShapes(MethodCall call) {

        String className = call.getResolvedClassName();

        if (!classShapesMap.containsKey(className)) {
            buildClassShape(className);
        }

        ClassShape classShape = classShapesMap.get(className);

        int width = 10;

        MethodShape methodShape = new MethodShape();
        methodShape.setX(classShape.getX() + (classShape.getWidth() / 2) - (width / 2));
        methodShape.setY(classShape.getY() + classShape.getHeight() - 20);
        methodShape.setWidth(width);
        methodShape.setHeight(30);

        methodShape.setRightSlotHeight(20);
        methodShape.setRightSlotWidth(3);

        shapes.add(methodShape);

        if (null != call.getCallees()) {
            for (MethodCall callee : call.getCallees()) {
                buildShapes(callee);
            }
        }

    }

    private void buildClassShape(String className) {

        TextLayout classNameTextLayout = new TextLayout(className, g.getFont(), g.getFontRenderContext());
        Rectangle2D classNameTextBounds = classNameTextLayout.getBounds();

        double captionWidth = classNameTextBounds.getWidth();
        double captionHeight = classNameTextBounds.getHeight();

        ClassShape classShape = new ClassShape();

        classShape.setX(x);
        classShape.setY(5);
        classShape.setWidth((int)captionWidth + 4 + 2 * BORDER_WIDTH);
        classShape.setHeight((int)captionHeight + 100);

        classShape.setClassName(className);

        classShape.setCaptionHeight((int) captionHeight + 4 + 2 * BORDER_WIDTH);
        classShape.setCaptionHorizontalPadding(2);

        shapes.add(classShape);
        classShapesMap.put(className, classShape);

        x += (captionWidth + 4 + 2 * BORDER_WIDTH + 5);

    }

    public static Collection<Paintable> buildShapes(Graphics2D g, MethodCall call){

        ShapesBuilder shapesBuilder = new ShapesBuilder(g);

        shapesBuilder.buildShapes(call);

        return shapesBuilder.shapes;
        
    }

}