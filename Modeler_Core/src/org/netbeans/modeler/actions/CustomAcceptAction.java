/**
 * Copyright [2014] Gaurav Gupta
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.netbeans.modeler.actions;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import javax.swing.JComponent;
import org.netbeans.api.visual.action.AcceptProvider;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.config.document.FlowDimensionType;
import org.netbeans.modeler.config.palette.SubCategoryNodeConfig;
import org.netbeans.modeler.core.NBModelerUtil;
import org.netbeans.modeler.label.ILabelConnectionWidget;
import org.netbeans.modeler.specification.model.document.IContainerElement;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.specification.model.document.IRootElement;
import org.netbeans.modeler.specification.model.document.core.IBaseElement;
import org.netbeans.modeler.specification.model.document.widget.IBounadryFlowNodeWidget;
import org.netbeans.modeler.specification.model.document.widget.IFlowElementWidget;
import org.netbeans.modeler.specification.model.document.widget.IModelerSubScene;
import org.netbeans.modeler.widget.node.INodeWidget;
import org.netbeans.modeler.widget.node.NodeWidget;
import org.netbeans.modeler.widget.node.info.NodeWidgetInfo;
import org.netbeans.modeler.widget.transferable.MoveWidgetTransferable;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;

/**
 *
 *
 */
public class CustomAcceptAction implements AcceptProvider {

    IModelerScene scene;

    public CustomAcceptAction(IModelerScene scene) {
        this.scene = scene;
    }

    @Override
    public ConnectorState isAcceptable(Widget widget, Point point, Transferable transferable) {
        ConnectorState retVal = ConnectorState.ACCEPT;
//((MoveDropTargetDropEvent)transferable).
        if (isWidgetMove(transferable)) {
            Widget[] target = new Widget[]{getWidget(transferable)};
            for (Widget curWidget : target) {
                if (curWidget instanceof NodeWidget) {
                    NodeWidget nodeWidget = (NodeWidget) curWidget;
                    if (nodeWidget.getNodeWidgetInfo().getModelerDocument().getFlowDimension() == FlowDimensionType.BOUNDARY) {
                        retVal = ConnectorState.REJECT;
                        return retVal;
                    }
                }
            }
        } else if (isPaletteItem(transferable)) {
            SubCategoryNodeConfig subCategoryInfo = getSubCategory(transferable);
            Image dragImage = subCategoryInfo.getImage();
            JComponent view = scene.getView();

            Graphics2D g2 = (Graphics2D) view.getGraphics();
            Rectangle visRect = scene.getBounds();//view.getVisibleRect();//
            view.paintImmediately(0, 0, visRect.width - visRect.x, visRect.height - visRect.y);//(visRect.x, visRect.y, visRect.width, visRect.height);
            g2.drawImage(dragImage,
                    AffineTransform.getTranslateInstance(point.getLocation().getX() - visRect.x,
                            point.getLocation().getY() - visRect.y),
                    null);

            if (subCategoryInfo.getModelerDocument().getFlowDimension() == FlowDimensionType.BOUNDARY) {
                retVal = ConnectorState.REJECT;
                return retVal;
            }
        }

        return retVal;

    }

    @Override
    public void accept(Widget modelerScene, Point point, Transferable transferable) {
        if (isWidgetMove(transferable)) {
            boolean convertLocation = false;
            Widget[] target;
            try {
                target = new Widget[]{getWidget(transferable)};
                convertLocation = true;
            } catch (Exception e) {
                target = new Widget[0];
            }

            for (Widget curWidget : target) {

                if (curWidget instanceof IBounadryFlowNodeWidget
                        || curWidget instanceof ILabelConnectionWidget) {
                    continue;
                }
                if (curWidget.getParentWidget() != null) {
                    curWidget.getParentWidget().removeChild(curWidget);
                }

                Point curPt = curWidget.getPreferredLocation();
                if (curPt == null) {
                    curPt = curWidget.getLocation();
                }
                scene.addChild(curWidget);
                if (convertLocation == true) {
                    curWidget.setPreferredLocation(scene.convertSceneToLocal(curPt));
                }

//                if(!(curWidget instanceof BaseElementWidget)){
//                     break;
//                }
                IFlowElementWidget newNodeWidget = (IFlowElementWidget) curWidget;
                /*Manage Widget and Widget Specification Start */

                if (newNodeWidget.getFlowElementsContainer() instanceof IModelerSubScene) {
                    IModelerSubScene subProcessWidget = (IModelerSubScene) newNodeWidget.getFlowElementsContainer();

                    subProcessWidget.removeBaseElementElement(newNodeWidget);
                    scene.addBaseElement(newNodeWidget);

                    IRootElement rootElementSpec = scene.getRootElementSpec();
                    IContainerElement subProcessSpec = (IContainerElement) subProcessWidget.getBaseElementSpec();
                    IBaseElement baseElementSpec = newNodeWidget.getBaseElementSpec();

                    subProcessSpec.removeBaseElement(baseElementSpec);
                    rootElementSpec.addBaseElement(baseElementSpec);
                }

                /*Manage Widget and Widget Specification End */
                newNodeWidget.setFlowElementsContainer(modelerScene);

            }
//            scene.getModelerPanelTopComponent().changePersistenceState(false);
//            scene.revalidate();
        } else if (isPaletteItem(transferable)) {
            SubCategoryNodeConfig subCategoryInfo = getSubCategory(transferable);
            boolean status = true;
            if (subCategoryInfo.getModelerDocument().getFlowDimension() == FlowDimensionType.BOUNDARY) {
                status = false;
            }
            if (status) {
                INodeWidget widget = scene.createNodeWidget(new NodeWidgetInfo("_" + NBModelerUtil.getAutoGeneratedId().toString(), subCategoryInfo, point));
                scene.getModelerPanelTopComponent().changePersistenceState(false);
            }
//            scene.revalidate(); //!node.isExist() 
//            scene.validate();
        }

//        scene.validate();
    }

    private SubCategoryNodeConfig getSubCategory(Transferable transferable) {
        Object o = null;
        try {
            //o =  transferable.getTransferData(PaletteItemTransferable.FLAVOR);
            o = transferable.getTransferData(DataFlavor.imageFlavor);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } catch (UnsupportedFlavorException ex) {
            Exceptions.printStackTrace(ex);
        }
        if (o instanceof Node) {
            SubCategoryNodeConfig subCategoryInfo = (SubCategoryNodeConfig) ((Node) o).getValue("SubCategoryInfo");
            return subCategoryInfo;
        } else {
            return null;
        }
    }

    private Widget getWidget(Transferable transferable) {
        try {
            MoveWidgetTransferable data = (MoveWidgetTransferable) transferable.getTransferData(MoveWidgetTransferable.FLAVOR);
            return data.getWidget();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } catch (UnsupportedFlavorException ex) {
            Exceptions.printStackTrace(ex);
        }
        return null;
    }

    /**
     * Test if a widget is fully with in the bounds of the container widget.
     *
     * @param widget The widget to test
     * @return True if the widget is in the containers bounds.
     */
    protected boolean isFullyContained(Widget widget) {
        // Calling getPreferredBounds forces the bounds to be calculated if it
        // has not already been calculated.  For example when the Widget was
        // just created and therefore has not had a chance to be displayed.
        Rectangle area = widget.getClientArea();

        boolean retVal = false;
        if (area != null) {
            Rectangle sceneArea = widget.convertLocalToScene(area);

            Rectangle localArea = scene.convertSceneToLocal(sceneArea);
            Rectangle myArea = scene.getClientArea();
            retVal = myArea.contains(localArea);
        }

        return retVal;
    }

    protected boolean isWidgetMove(Transferable transferable) {
        return transferable.isDataFlavorSupported(MoveWidgetTransferable.FLAVOR);
    }

    protected boolean isPaletteItem(Transferable transferable) {
        return transferable.isDataFlavorSupported(DataFlavor.imageFlavor);
    }
//

}
