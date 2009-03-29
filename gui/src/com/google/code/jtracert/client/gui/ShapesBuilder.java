package com.google.code.jtracert.client.gui;

import com.google.code.jtracert.client.gui.shapes.ClassShape;
import com.google.code.jtracert.client.gui.shapes.Paintable;
import com.google.code.jtracert.client.gui.shapes.MethodShape;
import com.google.code.jtracert.client.gui.shapes.MethodCallShape;
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
    private static final int METHOD_SHAPE_VERTICAL_PADDING = 20;
    private static final int METHOD_SHAPE_DEFAULT_HEIGHT = 40;

    private ShapesBuilder(Graphics2D g) {
        this.g = g;
        x = 10;
    }

    private void buildShapes(MethodCall call, MethodShape previousMethodShape) {

        String className = call.getResolvedClassName();

        if (!classShapesMap.containsKey(className)) {
            buildClassShape(className);
        }

        ClassShape classShape = classShapesMap.get(className);

        ///

        MethodShape methodShape = buildMethodShape(classShape);

        shapes.add(methodShape);

        classShape.currentMethodsStack.add(methodShape);

        ///


        String methodName = call.getResolvedMethodName();

        double captionWidth;
        double captionHeight;

        if (null == methodName || "".equals(methodName)) {
            captionWidth = 0;
            captionHeight = 0;
        } else {
            TextLayout methodNameTextLayout = new TextLayout(methodName, g.getFont(), g.getFontRenderContext());
            Rectangle2D methodNameTextBounds = methodNameTextLayout.getBounds();

            captionWidth = methodNameTextBounds.getWidth();
            captionHeight = methodNameTextBounds.getHeight();
        }

        MethodCallShape methodCallShape = new MethodCallShape();

        if (null == previousMethodShape) {

            methodCallShape.setX(5);
            methodCallShape.setY(methodShape.getY());
            methodCallShape.setWidth((int) captionWidth + 15);


        } else {
            int methodCallX = previousMethodShape.getX() + previousMethodShape.getWidth();
            methodCallShape.setX(methodCallX);
            methodCallShape.setY(previousMethodShape.getY());

            int distance =
                methodShape.getX() -
                        previousMethodShape.getX() -
                        previousMethodShape.getWidth();

            methodCallShape.setWidth(distance);

            if (distance < captionWidth + 15) {
                methodShape.setX(methodCallX + (int)captionWidth + 15);
                methodCallShape.setWidth((int)captionWidth + 15);
            }

        }

        methodCallShape.setHeight((int) captionHeight + 5);

        methodCallShape.setCaptionHeight((int) captionHeight + 2);
        methodCallShape.setMethodName(methodName);

        shapes.add(methodCallShape);

        ///

        if (null != call.getCallees()) {
            for (MethodCall callee : call.getCallees()) {
                buildShapes(callee, methodShape);
            }
        }

        classShape.currentMethodsStack.remove(classShape.currentMethodsStack.size() - 1);

    }

    private MethodShape buildMethodShape(ClassShape classShape) {

        int width = 10;
        int height = METHOD_SHAPE_DEFAULT_HEIGHT;

        MethodShape methodShape = new MethodShape();

        int leftPadding = classShape.currentMethodsStack.size() * (width - 3);

        if (classShape.currentMethodsStack.size() > 0) {
            MethodShape parentMethodShape =
                    classShape.currentMethodsStack.get(classShape.currentMethodsStack.size() - 1);

            int topPadding = parentMethodShape.getHeight() / 2;

            methodShape.setY(parentMethodShape.getY() + topPadding);

        } else {
            methodShape.setY(classShape.getY() + classShape.getHeight());
        }

        methodShape.setX(classShape.getX() + (classShape.getWidth() / 2) - (width / 2) + leftPadding);

        methodShape.setWidth(width);
        methodShape.setHeight(height);

        for (MethodShape stackMethodShape : classShape.currentMethodsStack) {
            stackMethodShape.setHeight(stackMethodShape.getHeight() + height);
            stackMethodShape.setRightSlotHeight(stackMethodShape.getRightSlotHeight() + height);
            stackMethodShape.setRightSlotWidth(3);
        }

        return methodShape;
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
        classShape.setHeight((int)captionHeight + METHOD_SHAPE_VERTICAL_PADDING);

        classShape.setClassName(className);

        classShape.setCaptionHeight((int) captionHeight + 4 + 2 * BORDER_WIDTH);
        classShape.setCaptionHorizontalPadding(2);

        shapes.add(classShape);
        classShapesMap.put(className, classShape);

        x += (captionWidth + 4 + 2 * BORDER_WIDTH + 50);

    }

    public static Collection<Paintable> buildShapes(Graphics2D g, MethodCall call){

        ShapesBuilder shapesBuilder = new ShapesBuilder(g);

        shapesBuilder.buildShapes(call, null);

        return shapesBuilder.shapes;

    }

}