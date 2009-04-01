package com.google.code.jtracert.sequenceDiagram;

import java.awt.*;
import java.awt.font.TextLayout;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.List;

class DiagramElementsBuilder {

    private final Graphics g;
    private final Rectangle2D templateStringBounds;

    private List<ClassShape> classShapes = new LinkedList<ClassShape>();
    private Map<String, ClassShape> classShapesMap = new HashMap<String, ClassShape>();
    private List<MethodShape> methodShapesStack = new LinkedList<MethodShape>();

    //private Collection<DiagramElement> paintableShapes = new LinkedList<DiagramElement>();
    private Collection<DiagramElement> paintableShapes = new TreeSet<DiagramElement>(

            new Comparator<DiagramElement>() {

                public int compare(DiagramElement o1, DiagramElement o2) {

                    int l1 = o1.getLevel();
                    int l2 = o2.getLevel();

                    return l1 == l2 ? o1.hashCode() - o2.hashCode() : l1 - l2;

                }

            }

    );

    static final int LEFT_PADDING = 5;
    static final int TOP_PADDING = 5;

    static final int CLASS_HORIZONTAL_MARGIN = 10;
    static final int CLASS_HORIZONTAL_PADDING = 5;
    static final int CLASS_VERTICAL_MARGIN = 5;
    static final int CLASS_VERTICAL_PADDING = 2;

    static final int CLASS_BORDER_WIDTH = 1;

    static final int METHOD_NAME_VERTICAL_PADDING = 2;
    static final int METHOD_CALL_ARROW_WIDTH = 1;
    static final int METHOD_CALL_VERTICAL_MARGIN = 1;
    static final int METHOD_CALL_ARROW_SIZE = 5;

    static final int METHOD_SHAPE_WIDTH = 9;

    static final int METHOD_SHAPE_VERTICAL_DISTANCE = 10;

    DiagramElementsBuilder(Graphics g, Rectangle2D templateStringBounds) {
        this.g = g;
        this.templateStringBounds = templateStringBounds;
    }

    public Collection<? extends Paintable> buildPaintableShapes(MethodCall rootMethodCall) {

        ClassShape classShape = createClassShape("User");

        paintableShapes.add(classShape);

        MethodShape methodShape = createMethodShape(classShape);

        paintableShapes.add(methodShape);

        return buildPaintableShapes(methodShape, rootMethodCall);

    }

    private Collection<DiagramElement> buildPaintableShapes(
            MethodShape contextMethodShape,
            MethodCall methodCall) {

        String className = methodCall.getResolvedClassName();

        ClassShape classShape = createClassShape(className);

        paintableShapes.add(classShape);

        // Create method shape

        MethodShape methodShape = createMethodShape(classShape);

        classShape.currentMethodsStack.add(methodShape);

        paintableShapes.add(methodShape);

        // Create method call shape

        if (contextMethodShape.getX() < methodShape.getX()) {

            MethodCallShape methodCallShape = createMethodCallShape(
                    contextMethodShape,
                    methodCall,
                    classShape,
                    methodShape);

            paintableShapes.add(methodCallShape);
            
        } else {

            MethodReturnShape methodReturnShape = createMethodReturnShape(
                    contextMethodShape,
                    methodCall,
                    classShape,
                    methodShape);

            paintableShapes.add(methodReturnShape);

        }

        // Paint callees

        methodShapesStack.add(methodShape);

        for (MethodCall callee : methodCall.getCallees()) {
            buildPaintableShapes(methodShape, callee);
        }

        methodShapesStack.remove(methodShape);
        classShape.currentMethodsStack.remove(methodShape);

        return paintableShapes;

    }

    private MethodReturnShape createMethodReturnShape(MethodShape contextMethodShape, MethodCall methodCall, ClassShape classShape, MethodShape methodShape) {
        MethodReturnShape methodCallShape = new MethodReturnShape();

        // Set width & height

        int methodCallWidth = contextMethodShape.getX() - methodShape.getX() - methodShape.getWidth();
        int methodCallHeight = intCeil(templateStringBounds.getHeight()) +
                2 * METHOD_NAME_VERTICAL_PADDING +
                METHOD_CALL_ARROW_WIDTH +
                METHOD_CALL_ARROW_SIZE +
                2 * METHOD_CALL_VERTICAL_MARGIN;

        methodCallShape.setWidth(methodCallWidth);
        methodCallShape.setHeight(methodCallHeight);

        // Set x & y
        methodCallShape.setX(methodShape.getX() + methodShape.getWidth());
        methodCallShape.setY(methodShape.getY());

        // Set caption height & method name

        Rectangle methodNameBounds = getTextBounds(methodCall.getResolvedMethodName());

        methodCallShape.setCaptionHeight(intCeil(methodNameBounds.getHeight()));

        methodCallShape.setMethodName(methodCall.getResolvedMethodName());

        // adjust width

        int captionWidth = intCeil(methodNameBounds.getWidth());

        if (captionWidth + METHOD_CALL_ARROW_SIZE + 5 + 5 > methodCallWidth) {
            int widthIncrement = captionWidth + METHOD_CALL_ARROW_SIZE + 5 + 5 - methodCallWidth;
            classShape.incrementX(widthIncrement);
            methodCallShape.setWidth(captionWidth + METHOD_CALL_ARROW_SIZE + 5 + 5);
        }

        // adjust height

        int heightDifference =
                methodShape.getY() + methodShape.getHeight() -
                        contextMethodShape.getY() - contextMethodShape.getHeight();

        if (heightDifference > 0) {
            for (MethodShape methodShapeFromStack : methodShapesStack) {
                methodShapeFromStack.incrementHeight(heightDifference);
            }
        }

        // set methodd call incoming

        methodShape.addIncomingMethodCallShape(methodCallShape);

        return methodCallShape;
    }

    private MethodCallShape createMethodCallShape(MethodShape contextMethodShape, MethodCall methodCall, ClassShape classShape, MethodShape methodShape) {
        MethodCallShape methodCallShape = new MethodCallShape();

        // Set width & height
        int methodCallWidth = methodShape.getX() - contextMethodShape.getX() - contextMethodShape.getWidth();
        int methodCallHeight = intCeil(templateStringBounds.getHeight()) +
                2 * METHOD_NAME_VERTICAL_PADDING +
                METHOD_CALL_ARROW_WIDTH +
                METHOD_CALL_ARROW_SIZE +
                2 * METHOD_CALL_VERTICAL_MARGIN;

        methodCallShape.setWidth(methodCallWidth);
        methodCallShape.setHeight(methodCallHeight);

        // Set x & y
        methodCallShape.setX(contextMethodShape.getX() + contextMethodShape.getWidth());
        methodCallShape.setY(methodShape.getY());

        // Set caption height & method name

        Rectangle methodNameBounds = getTextBounds(methodCall.getResolvedMethodName());

        methodCallShape.setCaptionHeight(intCeil(methodNameBounds.getHeight()));

        methodCallShape.setMethodName(methodCall.getResolvedMethodName());

        // adjust width

        int captionWidth = intCeil(methodNameBounds.getWidth());

        if (captionWidth + METHOD_CALL_ARROW_SIZE + 5 + 5 > methodCallWidth) {
            int widthIncrement = captionWidth + METHOD_CALL_ARROW_SIZE + 5 + 5 - methodCallWidth;
            classShape.incrementX(widthIncrement);
            methodCallShape.setWidth(captionWidth + METHOD_CALL_ARROW_SIZE + 5 + 5);
        }

        // adjust height

        int heightDifference =
                methodShape.getY() + methodShape.getHeight() -
                        contextMethodShape.getY() - contextMethodShape.getHeight();

        if (heightDifference > 0) {
            for (MethodShape methodShapeFromStack : methodShapesStack) {
                methodShapeFromStack.incrementHeight(heightDifference);
            }
        }

        // set methodd call incoming

        methodShape.addIncomingMethodCallShape(methodCallShape);

        return methodCallShape;
    }

    private MethodShape createMethodShape(ClassShape classShape) {
        MethodShape methodShape = new MethodShape();

        // Set X

        if (classShape.currentMethodsStack.size() > 0) {
            methodShape.setX(classShape.getX() + (classShape.getWidth() ) / 2 + classShape.currentMethodsStack.size() * 1);
        } else {
            methodShape.setX(classShape.getX() + (classShape.getWidth() - METHOD_SHAPE_WIDTH) / 2);
        }

        // Set Y
        methodShape.setY(classShape.getY() + classShape.getHeight() + METHOD_SHAPE_VERTICAL_DISTANCE);

        // Set Width

        methodShape.setWidth(METHOD_SHAPE_WIDTH);

        // Set Height

        methodShape.setHeight(
                intCeil(templateStringBounds.getHeight()) +
                2 * METHOD_NAME_VERTICAL_PADDING +
                METHOD_CALL_ARROW_WIDTH +
                METHOD_CALL_ARROW_SIZE +
                2 * METHOD_CALL_VERTICAL_MARGIN);

        if (classShape.currentMethodsStack.size() > 0) {
            for (MethodShape currentMethodsStackMethodShape : classShape.currentMethodsStack) {
                currentMethodsStackMethodShape.incrementHeight(3);
            }
        } else {
            classShape.incrementHeight(methodShape.getHeight() + METHOD_SHAPE_VERTICAL_DISTANCE);
        }

        classShape.addMethodShape(methodShape);

        return methodShape;
    }

    private ClassShape createClassShape(String className) {

        if (!classShapesMap.containsKey(className)) {

            // Create class shape

            ClassShape classShape = new ClassShape();

            // Set class shape x and y

            int x = getNewClassShapeX();

            classShape.setX(x);
            classShape.setY(TOP_PADDING);

            // Determine class caption height

            int templateStringHeight = intCeil(templateStringBounds.getHeight());

            classShape.setCaptionHeight(
                    templateStringHeight +
                    2 * CLASS_VERTICAL_PADDING +
                    2 * CLASS_BORDER_WIDTH
            );

            // Determine class caption vertical padding

            Rectangle classNameStringBounds = getTextBounds(className);

            int captionWidth = intCeil(classNameStringBounds.getWidth());
            int captionHeight = intCeil(classNameStringBounds.getHeight());

            int classVerticalPadding;

            classVerticalPadding = CLASS_VERTICAL_PADDING + (templateStringHeight - captionHeight) / 2;

            // Set class caption paddings

            classShape.setCaptionVerticalPadding(classVerticalPadding);
            classShape.setCaptionHorizontalPadding(CLASS_HORIZONTAL_PADDING);

            // Set class width & height

            classShape.setWidth(
                    captionWidth +
                    2 * CLASS_HORIZONTAL_PADDING +
                    2 * CLASS_BORDER_WIDTH);
            classShape.setHeight(classShape.getCaptionHeight() + CLASS_VERTICAL_MARGIN);

            // Set class name

            classShape.setClassName(className);

            // Add class shape to appropriate collections

            classShapes.add(classShape);
            classShapesMap.put(className, classShape);

        }

        return classShapesMap.get(className);

    }

    private Rectangle getTextBounds(String text) {

        Rectangle textBounds;

        TextLayout textLayout = new TextLayout(
                text,
                g.getFont(),
                new FontRenderContext(null, false, true));

        Shape textShape = textLayout.getOutline(null);

        textBounds = textShape.getBounds();

        return textBounds;

    }

    private static int intCeil(double value) {
        return new Long(Math.round(Math.ceil(value))).intValue();
    }

    private int getNewClassShapeX() {
        int x;

        x = LEFT_PADDING;

        for (ClassShape existingClassShape : classShapes) {
            x += existingClassShape.getWidth();
            x += CLASS_HORIZONTAL_MARGIN;
        }
        
        return x;
    }



}
