
package br.usp.icmc.caed.ald.client;

import br.usp.icmc.caed.ald.client.mapper.ALDPlaceHistoryMapper;
import br.usp.icmc.caed.ald.client.view.IView.IShellView;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.ui.HasWidgets;

import com.sencha.gxt.widget.core.client.container.Viewport;

public class ApplicationController {

  private static final ALDPlaceHistoryMapper historyMapper = GWT.create(ALDPlaceHistoryMapper.class);
  
  private final PlaceController placeController;
  private final EventBus eventBus;
  private final IShellView shellView;
  private final Place defaultPlace;
  
  private final ActivityMapper mainActivityMapper;
  private final ActivityMapper leftActivityMapper;
  
  public ApplicationController(PlaceController placeController,
      EventBus eventBus, IShellView shellView, Place defaultPlace,
      ActivityMapper mainActivityMapper, ActivityMapper leftActivityMapper) {
    this.defaultPlace = defaultPlace;
    this.shellView = shellView;
    this.placeController = placeController;
    this.eventBus = eventBus;
    this.leftActivityMapper = leftActivityMapper;
    this.mainActivityMapper = mainActivityMapper;
  }


  @SuppressWarnings("deprecation")
  public void start(HasWidgets hasWidgets) {
    ActivityManager leftActivityManager = new ActivityManager(leftActivityMapper, eventBus);
    leftActivityManager.setDisplay(shellView.getLeftDisplay());
    
    ActivityManager mainActivityManager = new ActivityManager(mainActivityMapper, eventBus);
    mainActivityManager.setDisplay(shellView.getMainDisplay());
    
    PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);
    historyHandler.register(placeController, eventBus, defaultPlace);

    Viewport viewport = new Viewport();
    viewport.add(shellView);
    hasWidgets.add(viewport);
    
    historyHandler.handleCurrentHistory();
  }

}
