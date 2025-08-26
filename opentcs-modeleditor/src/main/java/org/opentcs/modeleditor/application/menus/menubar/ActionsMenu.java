// SPDX-FileCopyrightText: The openTCS Authors
// SPDX-License-Identifier: MIT
package org.opentcs.modeleditor.application.menus.menubar;

import static java.util.Objects.requireNonNull;

import jakarta.inject.Inject;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.opentcs.guing.common.components.drawing.OpenTCSDrawingEditor;
import org.opentcs.modeleditor.application.action.ViewActionMap;
import org.opentcs.modeleditor.application.menus.MenuFactory;
import org.opentcs.modeleditor.util.I18nPlantOverviewModeling;
import org.opentcs.thirdparty.guing.common.jhotdraw.util.ResourceBundleUtil;

/**
 * The application's menu for run-time actions.
 */
public class ActionsMenu
    extends
      JMenu {
  /**
   * A menu item for calculating the euclidean distance for paths.
   */
  private final JMenuItem calculatePathLength;

  /**
   * Creates a new instance.
   *
   * @param actionMap The application's action map.
   * @param drawingEditor The application's drawing editor.
   * @param menuFactory A factory for menu items.
   */
  @Inject
  @SuppressWarnings("this-escape")
  public ActionsMenu(
      ViewActionMap actionMap,
      OpenTCSDrawingEditor drawingEditor,
      MenuFactory menuFactory
  ) {
    requireNonNull(actionMap, "actionMap");
    requireNonNull(drawingEditor, "drawingEditor");
    requireNonNull(menuFactory, "menuFactory");

    final ResourceBundleUtil labels
        = ResourceBundleUtil.getBundle(I18nPlantOverviewModeling.MENU_PATH);

    this.setText(labels.getString("actionsMenu.text"));
    this.setToolTipText(labels.getString("actionsMenu.tooltipText"));
    this.setMnemonic('A');

    calculatePathLength = menuFactory.createCalculatePathLengthMenuItem();
    add(calculatePathLength);
  }

}
