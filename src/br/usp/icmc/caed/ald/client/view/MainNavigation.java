package br.usp.icmc.caed.ald.client.view;

import java.util.List;

import br.usp.icmc.caed.ald.client.model.Navigation;
import br.usp.icmc.caed.ald.client.view.IView.INavigationView;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

public class MainNavigation implements INavigationView, SelectionHandler<Item>, SelectHandler { //PlaceChangeEvent.Handler, 

  private static final String NAV_KEY = "navigation";

  private final TreeStore<Navigation> navigation;

  private PlaceController placeController;
  private VerticalLayoutContainer container;

  
  public MainNavigation(PlaceController placeController) { 
    this.placeController = placeController;
    this.navigation = new TreeStore<Navigation>(new ModelKeyProvider<Navigation>() {
      @Override
      public String getKey(Navigation item) {
        return (item.prefix!=null ? item.prefix+":"+item.title : item.title);
      }
    });
  }

  private void refreshContainer() {
    if (container == null) {
      container = new VerticalLayoutContainer();
    }
    container.clear();
    container.add(this.createMenu(), new VerticalLayoutData(1, -1));
  }  

  @Override
  public Widget asWidget() {
    if (container == null) {
      this.refreshContainer();
    }
    return container;
  }
  
  private MenuItem createItem(Navigation nav) {
    MenuItem item = new MenuItem();
    item.setText(nav.title);
    item.setData(NAV_KEY, nav);
    if (nav.icon!=null) item.setIcon(nav.icon);
    Menu subMenu = this.createSubMenu(nav);
    if (subMenu!=null) item.setSubMenu(subMenu);
    return item;
  }
  
  private Menu createSubMenu(Navigation parent) {
    Menu menu = null;
    if (parent!=null && navigation.hasChildren(parent)) {
      menu = new Menu();
      for (Navigation nav: navigation.getChildren(parent)) {
        menu.add(this.createItem(nav));
        menu.addSelectionHandler(this);
      }
    }
    return menu;
  }

  private ToolBar createMenu() {
    ToolBar menu = new ToolBar();
    for (Navigation nav : navigation.getRootItems()) {
      TextButton button = new TextButton(nav.title);
      button.setData(NAV_KEY, nav);
      if (nav.icon!=null) button.setIcon(nav.icon);
      button.addSelectHandler(this);
      Menu subMenu = this.createSubMenu(nav);
      if (subMenu!=null) button.setMenu(subMenu);
      menu.add(button);
    }
    return menu;
  }

  @Override
  public void onSelection(SelectionEvent<Item> event) {
    Navigation nav = event.getSelectedItem().getData(NAV_KEY);
    if (nav.place!=null) {
      this.placeController.goTo(nav.place);
    }
    if (nav.callback!=null) {
      nav.callback.onSuccess(nav);
    }
  }

  @Override
  public void onSelect(SelectEvent event) {
    Navigation nav = ((TextButton) event.getSource()).getData(NAV_KEY);
    if (nav.place!=null) {
      this.placeController.goTo(nav.place);
    }
    if (nav.callback!=null) {
      nav.callback.onSuccess(nav);
    }
  }

  @Override
  public void setData(Navigation nav, List<Navigation> data) {
    if (nav!=null) {
      if (navigation.findModel(nav) == null) navigation.add(nav);
      navigation.add(nav, data); 
    } else { navigation.add(data); }
    this.refreshContainer();
  }

}