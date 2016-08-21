package br.usp.icmc.caed.ald.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class HZInsListPlace extends HZPlace {
  
  private static final String PREFIX = "hz-instances";

  @Prefix(PREFIX)
  public static class Tokenizer implements PlaceTokenizer<HZInsListPlace> {

    public HZInsListPlace getPlace(String token) {
      String[] params = token.split(":");
      return new HZInsListPlace(params[0], params[1]);
    }

    public String getToken(HZInsListPlace place) {
      return place.mainType+":"+place.mainId;
    }
    
  }
  
  public HZInsListPlace(String hzMainType, String hzMainId) {
    super(hzMainType, hzMainId);
  }
  
  /**
   * equality test based on Class type, to let different instance of this
   * Place class to be equals for CachingActivityMapper test on Place equality
   *
   * @param obj the place to compare with
   * @return true if this place and otherPlace are of the same Class type
   */
  @Override
  public boolean equals(Object obj) {
    //return this == obj || (!this.isAvoidCache() && obj!=null && getClass() == obj.getClass());
    if (this==obj) return true;
    if (obj==null || !(obj instanceof HZInsListPlace)) return false;
    HZInsListPlace _obj = ((HZInsListPlace) obj);
    if (mainType!=null && !mainType.equals(_obj.mainType)) return false;
    if (mainId!=null && !mainId.equals(_obj.mainId)) return false;
    return true;
  }

}
