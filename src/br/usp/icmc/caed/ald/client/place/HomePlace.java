package br.usp.icmc.caed.ald.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class HomePlace extends Place {

  @Prefix("")
  public static class Tokenizer implements PlaceTokenizer<HomePlace> {

    public HomePlace getPlace(String token) {
      return new HomePlace();
    }

    public String getToken(HomePlace place) {
      return "";
    }

  }

}
