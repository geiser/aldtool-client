
package br.usp.icmc.caed.ald.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.place.shared.Place;

public abstract class BaseActivity<P extends Place> extends AbstractActivity {
  
  protected P place;

  public P getPlace() {
    return place;
  }

  public void setPlace(P place) {
    this.place = place;
  }
  
}