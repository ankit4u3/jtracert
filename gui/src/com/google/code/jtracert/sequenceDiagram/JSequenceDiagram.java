package com.google.code.jtracert.sequenceDiagram;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.Collections;

public class JSequenceDiagram extends JComponent implements MouseListener, MouseMotionListener {

    private static final String TEMPLATE_STRING =
            "qwertyuiopasdfghjklzxcvbnm" +
                    "QWERTYUIOPASDFGHJKLZXCVBNM" +
                    "1234567890" + "" +
                    ",():";

    private Collection<? extends Paintable> paintableShapes;
    private Rectangle2D templateStringBounds;

    private Paintable selectedShape;

    private SequenceDiagramModel model;

    public JSequenceDiagram() {
        super();
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public JSequenceDiagram(SequenceDiagramModel model) {
        this();
        this.model = model;
    }

    public SequenceDiagramModel getModel() {
        return model;
    }

    public void setModel(SequenceDiagramModel model) {
        this.model = model;
    }

    public void mouseMoved(MouseEvent e) {

        Paintable selectedShape = getSelectedShape(e);

        if (null == selectedShape) {
            if (null != this.selectedShape) {
                this.selectedShape.setSelected(false);
                Rectangle previoudlySelectedShapeCounds = this.selectedShape.getBounds();
                RepaintManager.currentManager(this).addDirtyRegion(
                        this,
                        (int) previoudlySelectedShapeCounds.getX(),
                        (int) previoudlySelectedShapeCounds.getY(),
                        (int) previoudlySelectedShapeCounds.getWidth() + 1,
                        (int) previoudlySelectedShapeCounds.getHeight() + 1
                );
                this.selectedShape = null;
                RepaintManager.currentManager(this).paintDirtyRegions();
            }
        } else {
            if (null != this.selectedShape) {
                this.selectedShape.setSelected(false);
                Rectangle previoudlySelectedShapeCounds = this.selectedShape.getBounds();
                RepaintManager.currentManager(this).addDirtyRegion(
                        this,
                        (int) previoudlySelectedShapeCounds.getX(),
                        (int) previoudlySelectedShapeCounds.getY(),
                        (int) previoudlySelectedShapeCounds.getWidth() + 1,
                        (int) previoudlySelectedShapeCounds.getHeight() + 1
                );
            }
            selectedShape.setSelected(true);
            Rectangle selectedShapeCounds = selectedShape.getBounds();
            RepaintManager.currentManager(this).addDirtyRegion(
                    this,
                    (int) selectedShapeCounds.getX(),
                    (int) selectedShapeCounds.getY(),
                    (int) selectedShapeCounds.getWidth() + 1,
                    (int) selectedShapeCounds.getHeight() + 1
            );
            RepaintManager.currentManager(this).paintDirtyRegions();
            this.selectedShape = selectedShape;
        }

    }

    public void mouseExited(MouseEvent e) {
        if (null != this.selectedShape) {
            this.selectedShape.setSelected(false);
            Rectangle previoudlySelectedShapeCounds = this.selectedShape.getBounds();
            RepaintManager.currentManager(this).addDirtyRegion(
                    this,
                    (int) previoudlySelectedShapeCounds.getX(),
                    (int) previoudlySelectedShapeCounds.getY(),
                    (int) previoudlySelectedShapeCounds.getWidth() + 1,
                    (int) previoudlySelectedShapeCounds.getHeight() + 1
            );
            this.selectedShape = null;
            RepaintManager.currentManager(this).paintDirtyRegions();
        }
    }

    public void mouseClicked(MouseEvent e) {
        if (null != this.selectedShape) {
            JOptionPane.showMessageDialog(this, this.selectedShape);
        }
    }

    private Paintable getSelectedShape(MouseEvent e) {
        Paintable selectedShape = null;
        for (Paintable paintableShape : getPaintalbeShapes()) {
            if (paintableShape.contains(e.getPoint())) {
                selectedShape = paintableShape;
                break;
            }
        }
        return selectedShape;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Paintable paintableShape : getPaintalbeShapes(g)) {
            paintableShape.paint(g);
        }
    }

    private Iterable<? extends Paintable> getPaintalbeShapes() {
        Graphics g = getComponentGraphics(getGraphics());
        if (null == g) {
            return Collections.emptySet();
        } else {
            return getPaintalbeShapes(g);
        }
    }

    private Iterable<? extends Paintable> getPaintalbeShapes(Graphics g) {

        FontMetrics fontMetrics = g.getFontMetrics();
        Rectangle2D templateStringBounds = fontMetrics.getStringBounds(TEMPLATE_STRING, g);

        if (this.templateStringBounds == null || !templateStringBounds.equals(this.templateStringBounds)) {
            this.templateStringBounds = templateStringBounds;
            this.paintableShapes = createPaintableShapes(g);
        }

        return this.paintableShapes;

    }

    private Collection<? extends Paintable> createPaintableShapes(Graphics g) {

        if (null == getModel()) {
            return Collections.emptySet();
        } else {
            MethodCall rootMethodCall = getModel().getRootMethodCall();
            if (null == rootMethodCall) {
                return Collections.emptySet();
            } else {
                DiagramElementsBuilder diagramElementsBuilder =
                        new DiagramElementsBuilder(g, templateStringBounds);
                return diagramElementsBuilder.buildPaintableShapes(rootMethodCall);
            }
        }

    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseDragged(MouseEvent e) {

    }

}
