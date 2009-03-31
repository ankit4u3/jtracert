package com.google.code.jtracert.sequenceDiagram;

import java.awt.*;
import java.awt.font.TextLayout;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Map;

class DiagramElementsBuilder {

    private final Graphics g;
    private final Rectangle2D templateStringBounds;

    private java.util.List<ClassShape> classShapes = new LinkedList<ClassShape>();
    private Map<String, ClassShape> classShapesMap = new HashMap<String, ClassShape>();

    private Collection<Paintable> paintableShapes = new LinkedList<Paintable>();

    static final int LEFT_PADDING = 5;
    static final int TOP_PADDING = 5;

    static final int CLASS_HORIZONTAL_MARGIN = 10;
    static final int CLASS_HORIZONTAL_PADDING = 5;
    static final int CLASS_VERTICAL_MARGIN = 20;
    static final int CLASS_VERTICAL_PADDING = 2;

    static final int CLASS_BORDER_WIDTH = 1;

    static final int METHOD_NAME_VERTICAL_PADDING = 2;
    static final int METHOD_CALL_ARROW_WIDTH = 1;
    static final int METHOD_CALL_VERTICAL_MARGIN = 1;
    static final int METHOD_CALL_ARROW_SIZE = 5;

    DiagramElementsBuilder(Graphics g, Rectangle2D templateStringBounds) {
        this.g = g;
        this.templateStringBounds = templateStringBounds;
    }

    public Collection<Paintable> buildPaintableShapes(MethodCall rootMethodCall) {

        ClassShape classShape = createClassShape("User");

        paintableShapes.add(classShape);

        MethodShape methodShape = new MethodShape();

        methodShape.setX(classShape.getX() + classShape.getWidth() / 2 - 1);
        methodShape.setY(classShape.getY() + classShape.getHeight());
        methodShape.setWidth(2);
        methodShape.setHeight((int)
                templateStringBounds.getHeight() +
                2 * METHOD_NAME_VERTICAL_PADDING +
                METHOD_CALL_ARROW_WIDTH +
                METHOD_CALL_ARROW_SIZE +
                2 * METHOD_CALL_VERTICAL_MARGIN);

        paintableShapes.add(methodShape);

        return buildPaintableShapes(methodShape, rootMethodCall);

    }

    private Collection<Paintable> buildPaintableShapes(
            MethodShape contextMethodShape,
            MethodCall methodCall) {

        return paintableShapes;

    }

    private ClassShape createClassShape(String className) {

        if (!classShapesMap.containsKey(className)) {

            Rectangle classNameStringBounds;

            TextLayout classNameTextLayout = new TextLayout(
                    className,
                    g.getFont(),
                    new FontRenderContext(null, false, true));
            Shape classNameTextShape = classNameTextLayout.getOutline(null);

            classNameStringBounds = classNameTextShape.getBounds();

            int captionWidth =
                    new Long(Math.round(Math.ceil(classNameStringBounds.getWidth()))).intValue();
            int captionHeight =
                    new Long(Math.round(Math.ceil(classNameStringBounds.getHeight()))).intValue();

            ClassShape classShape = new ClassShape();

            int x = getNewClassShapeX();

            classShape.setX(x);
            classShape.setY(TOP_PADDING);

            int templateStringHeight =
                new Long(Math.round(Math.ceil(templateStringBounds.getHeight()))).intValue();

            classShape.setCaptionHeight(
                    templateStringHeight +
                    2 * CLASS_VERTICAL_PADDING +
                    2 * CLASS_BORDER_WIDTH
            );

            int classVerticalPadding;

            classVerticalPadding = CLASS_VERTICAL_PADDING + (templateStringHeight - captionHeight) / 2;

            classShape.setCaptionHorizontalPadding(CLASS_HORIZONTAL_PADDING);
            classShape.setCaptionVerticalPadding(classVerticalPadding);

            classShape.setWidth(
                    captionWidth +
                    2 * CLASS_HORIZONTAL_PADDING +
                    2 * CLASS_BORDER_WIDTH);
            classShape.setHeight(classShape.getCaptionHeight() + CLASS_VERTICAL_MARGIN);

            classShape.setClassName(className);

            classShapes.add(classShape);
            classShapesMap.put(className, classShape);

        }

        return classShapesMap.get(className);

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
