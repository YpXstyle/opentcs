// SPDX-FileCopyrightText: The openTCS Authors
// SPDX-License-Identifier: MIT
package org.opentcs.operationsdesk.application.menus.menubar;

import static java.util.Objects.requireNonNull;
import static org.opentcs.operationsdesk.event.KernelStateChangeEvent.State.LOGGED_IN;

import jakarta.inject.Inject;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.opentcs.customizations.ApplicationEventBus;
import org.opentcs.guing.common.application.OperationMode;
import org.opentcs.guing.common.application.menus.menubar.ViewPluginPanelsMenu;
import org.opentcs.guing.common.components.drawing.DrawingOptions;
import org.opentcs.operationsdesk.application.action.ViewActionMap;
import org.opentcs.operationsdesk.application.action.view.AddDrawingViewAction;
import org.opentcs.operationsdesk.application.action.view.AddPeripheralJobViewAction;
import org.opentcs.operationsdesk.application.action.view.AddTransportOrderSequenceViewAction;
import org.opentcs.operationsdesk.application.action.view.AddTransportOrderViewAction;
import org.opentcs.operationsdesk.application.action.view.RestoreDockingLayoutAction;
import org.opentcs.operationsdesk.application.action.view.ShowEnvelopesAction;
import org.opentcs.operationsdesk.event.KernelStateChangeEvent;
import org.opentcs.operationsdesk.util.I18nPlantOverviewOperating;
import org.opentcs.thirdparty.guing.common.jhotdraw.util.ResourceBundleUtil;
import org.opentcs.util.event.EventHandler;
import org.opentcs.util.event.EventSource;

/**
 * The application's menu for view-related operations.
 */
public class ViewMenu
    extends
      JMenu
    implements
      EventHandler {

  /**
   * A menu item for adding a drawing view.
   */
  private final JMenuItem menuAddDrawingView;
  /**
   * A menu item for adding a transport order view.
   */
  private final JMenuItem menuTransportOrderView;
  /**
   * A menu item for adding an order sequence view.
   */
  private final JMenuItem menuOrderSequenceView;
  /**
   * A menu item for adding a peripheral job view.
   */
  private final JMenuItem menuPeripheralJobView;
  /**
   * A checkbox menu item for showing vehicle envelopes at allocated and claimed resources.
   */
  private final JCheckBoxMenuItem menuShowEnvelopes;
  /**
   * A menu for showing/hiding plugin panels.
   */
  private final ViewPluginPanelsMenu menuPluginPanels;
  /**
   * A menu item for restoring the default GUI layout.
   */
  private final JMenuItem menuItemRestoreDockingLayout;

  /**
   * Creates a new instance.
   *
   * @param actionMap The application's action map.
   * @param menuPluginPanels A menu for showing/hiding plugin panels.
   */
  @Inject
  @SuppressWarnings("this-escape")
  public ViewMenu(
      ViewActionMap actionMap,
      ViewPluginPanelsMenu menuPluginPanels,
      DrawingOptions drawingOptions,
      @ApplicationEventBus
      EventSource eventSource
  ) {
    requireNonNull(actionMap, "actionMap");
    requireNonNull(menuPluginPanels, "menuPluginPanels");
    requireNonNull(drawingOptions, "drawingOptions");

    final ResourceBundleUtil labels
        = ResourceBundleUtil.getBundle(I18nPlantOverviewOperating.MENU_PATH);

    this.setText(labels.getString("viewMenu.text"));
    this.setToolTipText(labels.getString("viewMenu.tooltipText"));
    this.setMnemonic('V');

    // Menu item View -> Add course view
    menuAddDrawingView = new JMenuItem(actionMap.get(AddDrawingViewAction.ID));
    add(menuAddDrawingView);

    // Menu item View -> Add transport order view
    menuTransportOrderView = new JMenuItem(actionMap.get(AddTransportOrderViewAction.ID));
    add(menuTransportOrderView);

    // Menu item View -> Add transport order sequence view
    menuOrderSequenceView = new JMenuItem(actionMap.get(AddTransportOrderSequenceViewAction.ID));
    add(menuOrderSequenceView);

    menuPeripheralJobView = new JMenuItem(actionMap.get(AddPeripheralJobViewAction.ID));
    add(menuPeripheralJobView);

    addSeparator();

    menuShowEnvelopes = new JCheckBoxMenuItem(actionMap.get(ShowEnvelopesAction.ID));
    menuShowEnvelopes.setSelected(drawingOptions.isEnvelopesVisible());
    add(menuShowEnvelopes);

    addSeparator();

    // Menu item View -> Plugins
    this.menuPluginPanels = menuPluginPanels;
    menuPluginPanels.setOperationMode(OperationMode.OPERATING);
    menuPluginPanels.setEnabled(false);
    add(menuPluginPanels);

    // Menu item View -> Restore docking layout
    menuItemRestoreDockingLayout = new JMenuItem(actionMap.get(RestoreDockingLayoutAction.ID));
    menuItemRestoreDockingLayout.setText(
        labels.getString("viewMenu.menuItem_restoreWindowArrangement.text")
    );
    add(menuItemRestoreDockingLayout);

    eventSource.subscribe(this);
  }

  @Override
  public void onEvent(Object event) {
    if (event instanceof KernelStateChangeEvent kernelStateChangeEvent) {
      handleKernelStateChangeEvent(kernelStateChangeEvent);
    }
  }

  private void handleKernelStateChangeEvent(KernelStateChangeEvent event) {
    switch (event.getNewState()) {
      case LOGGED_IN:
        menuPluginPanels.setEnabled(true);
        break;
      case DISCONNECTED:
        menuPluginPanels.setEnabled(false);
        break;
      default:
        // Do nothing.
    }
  }
}
