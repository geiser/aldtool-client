package br.usp.icmc.caed.ald.client.model;

import com.google.gwt.core.client.Callback;
import com.google.gwt.place.shared.Place;
import com.google.gwt.resources.client.ImageResource;

public class Navigation {

  public String prefix;
  public final String title;
  public Place place;
  public ImageResource icon;
  public Callback<Navigation, Throwable> callback;
  
  public Navigation(String title) {
    this(title, null, null, null);
  }
  
  public Navigation(String title, Place place) {
    this(title, place, null, null);
  }
  
  public Navigation(String title, Place place, ImageResource icon) {
    this(title, place, icon, null);
  }
  
  public Navigation(String title, Place place, ImageResource icon, String prefix) {
    this.title = title;
    this.place = place;
    this.icon = icon;
    this.prefix = prefix;
  }
  
  
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Navigation n = (Navigation) o;
    boolean eqTitle = (title!=null ? title.equals(n.title) : n.title==null);
    boolean eqPrefix = (prefix!=null ? prefix.equals(n.prefix) : n.prefix==null);
    if (!eqTitle || !eqPrefix) return false;
    return true;
  }

  @Override
  public int hashCode() {
    int titleHash = (title != null ? title.hashCode() : 0);
    return (prefix != null ? prefix.hashCode()*titleHash : titleHash);
  }

}
