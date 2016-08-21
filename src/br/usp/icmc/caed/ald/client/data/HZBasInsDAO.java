package br.usp.icmc.caed.ald.client.data;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.usp.icmc.caed.ald.client.ioc.InjectConstants;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Window;

public class HZBasInsDAO extends HZDAO {
  
  private static final Logger logger = Logger.getLogger(HZBasInsDAO.class.getName());
  private static final InjectConstants constants = GWT.create(InjectConstants.class);
  private static final String MDL_URL = constants.hzModelURL();
  
  private final String type;

  public HZBasInsDAO(String baseURL, String type) {
    super(baseURL);
    this.type = type;
    this.baseURL += (baseURL.endsWith("/") ? "" : "/")+MDL_URL+"/"+this.type;
  }

  @Override
  protected String buildListURL() {
    return this.baseURL;
  }

  @Override
  protected String buildEntryURL(String id) {
    return this.baseURL+"/"+URL.encode(id);
  }

  @Override
  protected String buildSearchURL(JSONObject toFind) {
    return this.baseURL;
  }
  
  @Override
  public void retrieve(JSONObject toFind, final Callback<List<JSONObject>, Throwable> callback) {
    // TODO It's necessary to rewrite to enable this operation
    logger.log(Level.SEVERE, "Retrieve instances using search doesn't enabled");
    Window.alert("Retrieve instances using search doesn't enabled");
  }
  

}
