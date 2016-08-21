package br.usp.icmc.caed.ald.client.data;

import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.usp.icmc.caed.ald.client.data.IDataStore.ITreeDAO;
import br.usp.icmc.caed.ald.client.ioc.InjectConstants;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Window;

public class HZOntoDAO extends HZDAO implements ITreeDAO<String, JSONObject> {

  private static final Logger logger = Logger.getLogger(HZOntoDAO.class.getName());
  private static final InjectConstants constants = GWT.create(InjectConstants.class);
  private static final String ONT_URL = constants.hzOntologyURL();
  private static final String TREE_URL = constants.hzTreeURL();
  private static final String LABEl_KEY = constants.hzLabelKey();
  private static final String UPPER_KEY = constants.hzUpperKey();

  protected final String type;

  public HZOntoDAO(String baseURL, String type) {
    super(baseURL);
    this.type = type;
    this.baseURL += (baseURL.endsWith("/") ? "" : "/")+ONT_URL+"/"+this.type;
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
    String url = this.baseURL;
    if (toFind!=null && toFind.containsKey(UPPER_KEY)) {
      url+= "/"+TREE_URL;
      if (toFind.get(UPPER_KEY).isObject()!=null) {
        JSONObject upper = toFind.get(UPPER_KEY).isObject();
        if (upper!=null && this.getId(upper)!=null) {
          url+= "/"+URL.encode(this.getId(upper));
        }
      }
    }

    if (toFind!=null && toFind.containsKey(LABEl_KEY)) {
      String label = toFind.get(LABEl_KEY).isString().stringValue();
      this.params.put(LABEl_KEY, label);
    }
    return url;
  }

  private void _retrieveTree(final JSONObject root, final Callback<Entry<JSONObject, List<JSONObject>>, Throwable> callback) {
    final JSONObject toFind = new JSONObject();
    toFind.put(UPPER_KEY, root);
    this.retrieve(toFind, new Callback<List<JSONObject>, Throwable>() {
      @Override
      public void onSuccess(final List<JSONObject> result) {
        if (result!=null && !result.isEmpty()) {
          for (JSONObject obj : result) {
            _retrieveTree(obj, callback);
          }
          callback.onSuccess(new Entry<JSONObject, List<JSONObject>>() {
            @Override
            public List<JSONObject> setValue(List<JSONObject> paramV) {
              return null;
            }
            @Override
            public List<JSONObject> getValue() {
              return result;
            }
            @Override
            public JSONObject getKey() {
              return root;
            }
          });
        }
      }
      @Override
      public void onFailure(Throwable e) {
        logger.log(Level.SEVERE, "Unable to retrieve models by parent: "+root, e);
        Window.alert("Some error occurred retriving a model by parent");
      }
    });
  }

  @Override
  public void retrieveTree(final JSONObject toFind,
      final Callback<Entry<JSONObject, List<JSONObject>>, Throwable> callback) {
    this.retrieve(toFind, new Callback<List<JSONObject>, Throwable>() {
      @Override
      public void onSuccess(List<JSONObject> founds) {
        for (JSONObject obj : founds) {
          _retrieveTree(obj, callback);
        }
      }
      @Override
      public void onFailure(Throwable e) {   
        logger.log(Level.SEVERE, "Unable to retrieve tree searching: "+toFind, e);
        Window.alert("Some error occurred retriving a tree for a search");
      }
    });
  }

}
