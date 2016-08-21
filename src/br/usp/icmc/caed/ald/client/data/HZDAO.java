package br.usp.icmc.caed.ald.client.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.usp.icmc.caed.ald.client.data.IDataStore.IDAOWithMetadata;
import br.usp.icmc.caed.ald.client.ioc.InjectConstants;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Window;

public abstract class HZDAO extends JsonRestDAO<JSONObject> implements IDAOWithMetadata<String, JSONObject, JSONObject> {

  private static final Logger logger = Logger.getLogger(HZDAO.class.getName());
  private static final InjectConstants constants = GWT.create(InjectConstants.class);
  private static final String ID_KEY = constants.hzIDKey();
  private static final String DATA_KEY = constants.hzDataKey();
  private static final String META_KEY = constants.hzMetadataKey();
  private static final String HZ_API_URL = constants.hzApiURL();
  
  protected Map<String, JSONObject> metadatas = new HashMap<String, JSONObject>();

  public HZDAO(String baseURL) {
    super(baseURL, ID_KEY);
    this.baseURL += (baseURL.endsWith("/") ? "" : "/") + HZ_API_URL;
    this.params.put(META_KEY, "true");
    this.params.put("format", "json");
  }
  
  @Override
  protected JSONObject convert(JSONObject obj) {
    if (obj.containsKey(META_KEY)) {
      JSONObject data = obj.get(DATA_KEY).isObject();
      JSONObject meta = obj.get(META_KEY).isObject();
      this.metadatas.put(getId(data), meta);
      return data;
    }
    return obj;
  }

  @Override
  public void create(final JSONObject obj, final JSONObject meta,
      final Callback<JSONObject, Throwable> callback) {
    create(obj, new Callback<JSONObject, Throwable>() {
      @Override
      public void onSuccess(JSONObject result) {
        metadatas.put(getId(result), meta);
        callback.onSuccess(result);
      }
      @Override
      public void onFailure(Throwable e) {
        logger.log(Level.SEVERE, "Unable to create data: "+obj+" with metadata: "+meta, e);
        Window.alert("Some error occurred creating data with metadata");
      }
    });
  }

  @Override
  public void retrieveWithMetadata(
      final Callback<Map<JSONObject, JSONObject>, Throwable> callback) {
    retrieve(new Callback<List<JSONObject>, Throwable>() {
      @Override
      public void onSuccess(List<JSONObject> result) {
        Map<JSONObject, JSONObject> map = new HashMap<JSONObject, JSONObject>();
        if (result!=null && !result.isEmpty()) {
          for (JSONObject obj : result) {
            map.put(obj, metadatas.get(getId(obj)));
          }
        }
        callback.onSuccess(map);
      }
      @Override
      public void onFailure(Throwable e) {
        logger.log(Level.SEVERE, "Unable to retrieve datas with metadata", e);
        Window.alert("Some error occurred retrieving datas with metadata");
      }
    });
  }

  @Override
  public void retrieveWithMetadata(final JSONObject toFind,
      final Callback<Map<JSONObject, JSONObject>, Throwable> callback) {
    retrieve(toFind, new Callback<List<JSONObject>, Throwable>() {
      @Override
      public void onSuccess(List<JSONObject> result) {
        Map<JSONObject, JSONObject> map = new HashMap<JSONObject, JSONObject>();
        if (result!=null && !result.isEmpty()) {
          for (JSONObject obj : result) {
            map.put(obj, metadatas.get(getId(obj)));
          }
        }
        callback.onSuccess(map);
      }
      @Override
      public void onFailure(Throwable e) {
        logger.log(Level.SEVERE, "Unable to retrieve datas with using searching of "+toFind, e);
        Window.alert("Some error occurred retrieving datas with metadata using searching");
      }
    });
  }

  @Override
  public void retrieveByIdWithMetadata(final String id,
      final Callback<Entry<JSONObject, JSONObject>, Throwable> callback) {
    retrieveById(id, new Callback<JSONObject, Throwable>() {
      @Override
      public void onSuccess(final JSONObject result) {
        callback.onSuccess(new Entry<JSONObject, JSONObject>(){
          @Override
          public JSONObject getKey() {
            return result;
          }
          @Override
          public JSONObject getValue() {
            return metadatas.get(getId(result));
          }
          @Override
          public JSONObject setValue(JSONObject value) {
            return metadatas.put(getId(result), value);
          }
        });
      }
      @Override
      public void onFailure(Throwable e) {
        logger.log(Level.SEVERE, "Unable to retrieve datas with metadata by id"+id, e);
        Window.alert("Some error occurred retrieving datas with metadata by id");
      }
    });
  }

  @Override
  public void update(final JSONObject obj, final JSONObject meta,
      final Callback<JSONObject, Throwable> callback) {
    update(obj, new Callback<JSONObject, Throwable>() {
      @Override
      public void onSuccess(JSONObject result) {
        metadatas.put(getId(result), meta);
        callback.onSuccess(result);
      }
      @Override
      public void onFailure(Throwable e) {
        logger.log(Level.SEVERE, "Unable to update data: "+obj+", with metadata: "+meta, e);
        Window.alert("Some error occurred update data with metadata");
      }
    });
  }

}
