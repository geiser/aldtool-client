package br.usp.icmc.caed.ald.client.data;

import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.usp.icmc.caed.ald.client.ioc.InjectConstants;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Window;

public class HZDepInsDAO extends HZOntoDAO {
  
  private static final Logger logger = Logger.getLogger(HZDepInsDAO.class.getName());
  private static final InjectConstants constants = GWT.create(InjectConstants.class);
  private static final String TYPE_KEY = constants.hzTypeKey();
  private static final String INS_URL = constants.hzInstancesURL();

  protected final String id;

  public HZDepInsDAO(String baseURL, JSONObject base) {
    super(baseURL, base.get(TYPE_KEY).isString().stringValue());
    this.id = getId(base);
    this.baseURL += "/"+this.id+"/"+INS_URL;
  }

  public HZDepInsDAO(String baseURL, String type, String id) {
    super(baseURL, type);
    this.id = id;
    this.baseURL += "/"+this.id+"/"+INS_URL;
  }

  @Override
  protected String buildListURL() {
    return this.baseURL;
  }

  @Override
  protected String buildEntryURL(String id) {
    return this.baseURL+"/"+id;
  }

  @Override
  protected String buildSearchURL(JSONObject toFind) {
    return this.baseURL;
  }

  @Override
  public void retrieve(JSONObject toFind,
      final Callback<List<JSONObject>, Throwable> callback) {
    // TODO It's necessary to rewrite to enable this operation
    logger.log(Level.SEVERE, "Retrieve instances using search doesn't enabled");
    Window.alert("Retrieve instances using search doesn't enabled");
  }
  
  
  @Override
  public void retrieveTree(final JSONObject toFind,
      final Callback<Entry<JSONObject, List<JSONObject>>, Throwable> callback) {
    // TODO It's necessary to rewrite to enable this operation
    logger.log(Level.SEVERE, "Retrieve tree of instances doesn't enabled");
    Window.alert("Retrieve tree of instances doesn't enabled");
  }

}
