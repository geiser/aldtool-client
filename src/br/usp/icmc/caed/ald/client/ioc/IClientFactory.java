package br.usp.icmc.caed.ald.client.ioc;

import br.usp.icmc.caed.ald.client.activity.BaseActivity;
import br.usp.icmc.caed.ald.client.activity.HZInsDetailActivity;
import br.usp.icmc.caed.ald.client.activity.HZInsListActivity;
import br.usp.icmc.caed.ald.client.place.HZInsDetailPlace;
import br.usp.icmc.caed.ald.client.place.HZInsListPlace;
import br.usp.icmc.caed.ald.client.view.IView.INavigationView;
import br.usp.icmc.caed.ald.client.view.IView.IShellView;

import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;

public interface IClientFactory {
  
  public interface Provider<P extends Place, A extends BaseActivity<P>> {
    public A get(P place);
  }
  
  public void setEventBus(EventBus eventBus);
  public EventBus getEventBus();
  
  public void setPlaceController(PlaceController placeController);
  public PlaceController getPlaceController();
  
  public void setDefaultPlace(Place place);
  public Place getDefaultPlace();
  
  public void setMainNavigation(INavigationView view);
  public INavigationView getMainNavigation();
  
  public void setShellView(IShellView view);
  public IShellView getShellView();
  
  public void setHZDepInsListActivityProvider(Provider<HZInsListPlace, HZInsListActivity> provider);
  public Provider<HZInsListPlace, HZInsListActivity> getHZDepInsListActivityProvider();
  
  public void setHZDepInsDetailActivityProvider(Provider<HZInsDetailPlace, HZInsDetailActivity> provider);
  public Provider<HZInsDetailPlace, HZInsDetailActivity> getHZDepInsDetailActivityProvider();
  
  public void setMainActivityMapper(ActivityMapper mapper);
  public ActivityMapper getMainActivityMapper();
  
  public void setLeftActivityMapper(ActivityMapper mapper);
  public ActivityMapper getLeftActivityMapper(boolean isCaching);

}
