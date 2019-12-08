/**
 * Copyright 2013-2019 Gaurav Gupta
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
package org.netbeans.modeler.properties.combobox;

import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import org.netbeans.modeler.core.ModelerFile;
import org.netbeans.modeler.properties.spec.ComboBoxValue;
import org.netbeans.modeler.properties.EntityComponent;
import org.openide.windows.WindowManager;

public class JComboBoxPanel extends javax.swing.JPanel {

    private final ActionHandler actionHandler;
    private final ModelerFile modelerFile;

    /**
     * Creates new form NewJPanel1
     *
     * @param modelerFile
     * @param actionHandler
     */
    public JComboBoxPanel(ModelerFile modelerFile, ActionHandler actionHandler) {
        this.actionHandler = actionHandler;
        this.modelerFile = modelerFile;
        initComponents();
        loadMenuComponent();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        comboBox = new javax.swing.JComboBox();
        setting_Button = new javax.swing.JButton();

        comboBox.setAlignmentX(0.0F);
        comboBox.setAlignmentY(0.0F);

        setting_Button.setForeground(new java.awt.Color(255, 255, 255));
        setting_Button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/netbeans/modeler/properties/resource/settings.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(setting_Button, org.openide.util.NbBundle.getMessage(JComboBoxPanel.class, "JComboBoxPanel.setting_Button.text")); // NOI18N
        setting_Button.setToolTipText(org.openide.util.NbBundle.getMessage(JComboBoxPanel.class, "JComboBoxPanel.setting_Button.toolTipText")); // NOI18N
        setting_Button.setAlignmentY(0.0F);
        setting_Button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        setting_Button.setPreferredSize(new java.awt.Dimension(16, 16));
        setting_Button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                setting_ButtonMousePressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(comboBox, 0, 97, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(setting_Button, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(setting_Button, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(comboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void setting_ButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_setting_ButtonMousePressed
        setting_PopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
    }//GEN-LAST:event_setting_ButtonMousePressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox comboBox;
    private javax.swing.JButton setting_Button;
    // End of variables declaration//GEN-END:variables

    private javax.swing.JMenuItem createItem_MenuItem;
    private javax.swing.JMenuItem deleteItem_MenuItem;
    private javax.swing.JMenuItem editItem_MenuItem;
    private javax.swing.JPopupMenu setting_PopupMenu;

    private void loadMenuComponent() {

        setting_PopupMenu = new javax.swing.JPopupMenu();
        if (actionHandler.getCreateAction() != null) {
            createItem_MenuItem = new javax.swing.JMenuItem();
            createItem_MenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/netbeans/modeler/properties/resource/icon_plus.png"))); // NOI18N
            org.openide.awt.Mnemonics.setLocalizedText(createItem_MenuItem, org.openide.util.NbBundle.getMessage(JComboBoxPanel.class, "JComboBoxPanel.createItem_MenuItem.text")); // NOI18N
            createItem_MenuItem.setToolTipText(org.openide.util.NbBundle.getMessage(JComboBoxPanel.class, "JComboBoxPanel.createItem_MenuItem.toolTipText")); // NOI18N
            createItem_MenuItem.addActionListener(this::createItem_MenuItemActionPerformed);
            setting_PopupMenu.add(createItem_MenuItem);
        }
        if (actionHandler.getUpdateAction() != null) {
            editItem_MenuItem = new javax.swing.JMenuItem();
            editItem_MenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/netbeans/modeler/properties/resource/edit.png"))); // NOI18N
            org.openide.awt.Mnemonics.setLocalizedText(editItem_MenuItem, org.openide.util.NbBundle.getMessage(JComboBoxPanel.class, "JComboBoxPanel.editItem_MenuItem.text")); // NOI18N
            editItem_MenuItem.setToolTipText(org.openide.util.NbBundle.getMessage(JComboBoxPanel.class, "JComboBoxPanel.editItem_MenuItem.toolTipText")); // NOI18N
            editItem_MenuItem.addActionListener(this::editItem_MenuItemActionPerformed);
            setting_PopupMenu.add(editItem_MenuItem);
        }

        if (actionHandler.getDeleteAction() != null) {
            deleteItem_MenuItem = new javax.swing.JMenuItem();
            deleteItem_MenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/netbeans/modeler/properties/resource/delete.png"))); // NOI18N
            org.openide.awt.Mnemonics.setLocalizedText(deleteItem_MenuItem, org.openide.util.NbBundle.getMessage(JComboBoxPanel.class, "JComboBoxPanel.deleteItem_MenuItem.text")); // NOI18N
            deleteItem_MenuItem.setToolTipText(org.openide.util.NbBundle.getMessage(JComboBoxPanel.class, "JComboBoxPanel.deleteItem_MenuItem.toolTipText")); // NOI18N
            deleteItem_MenuItem.addActionListener(this::deleteItem_MenuItemActionPerformed);
            setting_PopupMenu.add(deleteItem_MenuItem);
        }
    }

    private void deleteItem_MenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        ComboBoxValue comboBoxValue = (ComboBoxValue) comboBox.getSelectedItem();
        if (comboBoxValue.getValue() == null) {
            JOptionPane.showMessageDialog(WindowManager.getDefault().getMainWindow(), "No element selected !", "", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int option = actionHandler.getRemoveMessage().getAsInt();
        if (option == JOptionPane.OK_OPTION) {
            actionHandler.getDeleteAction().accept(comboBoxValue);
            comboBox.removeItem(comboBoxValue);
            modelerFile.getModelerPanelTopComponent().changePersistenceState(false);
        }
    }

    private void editItem_MenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        ComboBoxValue comboBoxValue = (ComboBoxValue) comboBox.getSelectedItem();
        if (comboBoxValue.getValue() == null) {
            JOptionPane.showMessageDialog(WindowManager.getDefault().getMainWindow(), "No element selected !", "", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        EntityComponent itemComponent = actionHandler.getItemProducer();
        if (itemComponent != null) {
            if (!itemComponent.isLoaded()) {
                itemComponent.postConstruct();
                itemComponent.setLoaded();
            }
            itemComponent.init();
            itemComponent.updateEntity(comboBoxValue);
            itemComponent.setVisible(true);
        } else {
            throw new IllegalStateException("EntityComponent required for update action");
        }
        actionHandler.getUpdateAction().accept(comboBoxValue);
        modelerFile.getModelerPanelTopComponent().changePersistenceState(false);
    }

    private void createItem_MenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        EntityComponent itemComponent = actionHandler.getItemProducer();
        ComboBoxValue comboBoxValue = null;
        if (itemComponent != null) {
             if (!itemComponent.isLoaded()) {
                itemComponent.postConstruct();
                itemComponent.setLoaded();
            }
            itemComponent.init();
            itemComponent.createEntity(ComboBoxValue.class);
            itemComponent.setVisible(true);

            if (itemComponent.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
                comboBoxValue = (ComboBoxValue) itemComponent.getEntity();
            }
        } else {
            try {
            comboBoxValue = (ComboBoxValue) actionHandler.getItemSupplier().get();
            } catch(IllegalStateException ex){
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", ERROR_MESSAGE);
            }
        }

        if (comboBoxValue != null) {
            comboBox.addItem(comboBoxValue);
            comboBox.getModel().setSelectedItem(comboBoxValue);
            repaint();
            updateUI();

            actionHandler.getCreateAction().accept(comboBoxValue);//.createItem(comboBoxValue);
            modelerFile.getModelerPanelTopComponent().changePersistenceState(false);
        }

    }

    /**
     * @return the comboBox
     */
    public javax.swing.JComboBox getComboBox() {
        return comboBox;
    }
}