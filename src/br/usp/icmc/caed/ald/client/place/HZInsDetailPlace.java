package br.usp.icmc.caed.ald.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class HZInsDetailPlace extends HZPlace {
  
  private static final String PREFIX = "hz-instance";
    
  @Prefix(PREFIX)
  public static class Tokenizer implements PlaceTokenizer<HZInsDetailPlace> {
    
    @Override
    public HZInsDetailPlace getPlace(String token) {
      String[] params = token.split(":");
      return new HZInsDetailPlace(params[0], params[1], params[2], params[3], params[4]);
    }
    
    @Override
    public String getToken(HZInsDetailPlace place) {
      return place.mainType+":"+place.mainId+":"+place.conceptType+":"+place.conceptId+":"+place.instanceId;
    }
    
  }
  
  public final String conceptId;
  public final String conceptType;
  public final String instanceId;
  
  public HZInsDetailPlace(String mainType, String mainId,
      String conceptType, String conceptId, String instanceId) {
    super(mainType, mainId);
    this.conceptId = conceptId;
    this.conceptType = conceptType;
    this.instanceId = instanceId;
  }
  
}
