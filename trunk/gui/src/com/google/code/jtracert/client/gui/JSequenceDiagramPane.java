package com.google.code.jtracert.client.gui;

/*
import com.google.code.jtracert.sequenceDiagram.Paintable;
import com.google.code.jtracert.sequenceDiagram.DiagramElement;
import com.google.code.jtracert.sequenceDiagram.SequenceDiagramModel;
*/

import javax.swing.*;
/*
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
*/

public class JSequenceDiagramPane extends JPanel {

    /*private SequenceDiagramModel model;
    private Collection<Paintable> shapes;

    public JSequenceDiagramPane self() {
        return this;
    }

    public JSequenceDiagramPane() {

        addMouseListener(

                new MouseAdapter() {

                    @Override
                    public void mouseClicked(MouseEvent e) {

                        DiagramElement selectedElement = null;

                        for (Paintable p : shapes) {
                            if (p.contains(e.getPoint())) {
                                selectedElement = (DiagramElement) p;
                                break;
                            }
                        }

                        if (null != selectedElement) {
                            JOptionPane.showMessageDialog(self(),selectedElement);
                        }

                    }
                }

        );

        addMouseMotionListener(

                new MouseMotionAdapter() {

                    private DiagramElement previousSelectedElement = null;

                    @Override
                    public void mouseMoved(MouseEvent e) {

                        if (null == shapes) return;

                        DiagramElement selectedElement = null;

                        for (Paintable p : shapes) {
                            if (p.contains(e.getPoint())) {
                                selectedElement = (DiagramElement) p;
                                break;
                            }
                        }

                        if (null != previousSelectedElement) {
                            previousSelectedElement.setSelected(false);
                            RepaintManager.currentManager(self()).addDirtyRegion(
                                    self(),
                                    previousSelectedElement.getX(),
                                    previousSelectedElement.getY(),
                                    previousSelectedElement.getWidth() + 1,
                                    previousSelectedElement.getHeight() + 1
                            );
                        }

                        if (null != selectedElement) {
                            selectedElement.setSelected(true);
                            RepaintManager.currentManager(self()).addDirtyRegion(
                                    self(),
                                    selectedElement.getX(),
                                    selectedElement.getY(),
                                    selectedElement.getWidth() + 1,
                                    selectedElement.getHeight() + 1
                            );
                        }

                        previousSelectedElement = selectedElement;

                        RepaintManager.currentManager(self()).paintDirtyRegions();

                    }
                }

        );
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintSequenceDiagram(castGraphics(g));
    }

    private Graphics2D castGraphics(Graphics g) {
        return (Graphics2D) g;
    }

    protected void paintSequenceDiagram(Graphics2D g) {

        //g.setTransform(AffineTransform.getScaleInstance(1.2,1.2));

        Collection<Paintable> shapes = getShapes(g);

        for (Paintable shape : shapes) {
            shape.paint(g);
        }

    }

    private Collection<Paintable> getShapes(Graphics2D g) {

        if (shapes == null) {
            com.google.code.jtracert.sequenceDiagram.MethodCall call = getModel().getRootMethodCall();
            shapes = ShapesBuilder.buildShapes(g, call);
        }

        return shapes;

    }

    public SequenceDiagramModel getModel() {
        return model;
    }

    public void setModel(SequenceDiagramModel model) {
        this.model = model;
    }*/

}
