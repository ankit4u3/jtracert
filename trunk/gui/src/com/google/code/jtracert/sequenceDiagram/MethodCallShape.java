package com.google.code.jtracert.sequenceDiagram;

import com.google.code.jtracert.sequenceDiagram.DiagramElement;

import java.awt.*;

class MethodCallShape extends DiagramElement {

    protected String methodName;

    protected int captionHeight;

    @Override
    public int getLevel() {
        return 1000;
    }

    @Override
    public void paint(Graphics g) {

        g.setColor(isSelected() ? new Color(0xFF,0,0) : new Color(0,0,0));

        g.drawString(
                methodName,
                x + 5,
                y + captionHeight - 2
        );

        g.drawLine(
                x,
                y + captionHeight,
                x + width,
                y + captionHeight);

        g.drawLine(
                x + width - DiagramElementsBuilder.METHOD_CALL_ARROW_SIZE,
                y + captionHeight - DiagramElementsBuilder.METHOD_CALL_ARROW_SIZE,
                x + width,
                y + captionHeight
        );

        g.drawLine(
                x + width - DiagramElementsBuilder.METHOD_CALL_ARROW_SIZE,
                y + captionHeight + DiagramElementsBuilder.METHOD_CALL_ARROW_SIZE,
                x + width,
                y + captionHeight
        );

    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public int getCaptionHeight() {
        return captionHeight;
    }

    public void setCaptionHeight(int captionHeight) {
        this.captionHeight = captionHeight;
    }

}