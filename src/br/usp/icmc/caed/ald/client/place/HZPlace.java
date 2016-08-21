package br.usp.icmc.caed.ald.client.place;

import com.google.gwt.place.shared.Place;

public class HZPlace extends Place {
  
  public final String mainId;
  public final String mainType;
  
  public HZPlace(String mainType, String mainId) {
    this.mainId = mainId;
    this.mainType = mainType;
  }

}
