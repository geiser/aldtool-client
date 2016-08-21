package br.usp.icmc.caed.ald.client;

import br.usp.icmc.caed.ald.client.ioc.IClientFactory;
import br.usp.icmc.caed.ald.client.view.IView.IShellView;

import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ALD implements EntryPoint {

  //private static final Logger logger = Logger.getLogger(ALD.class.getName());
  private static final IClientFactory factory = GWT.create(IClientFactory.class);

  private final ApplicationController appController;

  public ALD() {
    PlaceController placeController = factory.getPlaceController();
    EventBus eventBus = factory.getEventBus();
    IShellView shellView = factory.getShellView();
    Place defaultPlace = factory.getDefaultPlace();
    ActivityMapper mainActivityMapper = factory.getMainActivityMapper();
    ActivityMapper leftActivityMapper = factory.getLeftActivityMapper(true);
    this.appController = new ApplicationController(placeController, eventBus,
        shellView, defaultPlace, mainActivityMapper, leftActivityMapper);
    //    GWT.runAsync(new RunAsyncCallback() {
    //      @Override
    //      public void onSuccess() {
    //        ALDGuiGinjector ginjector = GWT.create(ALDGuiGinjector.class);
    //        ginjector.applicationController().start(RootPanel.get());
    //      }      
    //      @Override
    //      public void onFailure(Throwable reason) {
    //        logger.log(Level.SEVERE, "Unable to start application", reason);
    //        Window.alert("Some error occurred while starting application");
    //      }
    //    });
  }


  @Override
  public void onModuleLoad() {
    appController.start(RootPanel.get());    
  }

}
