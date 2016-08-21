package br.usp.icmc.caed.ald.client.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.usp.icmc.caed.ald.client.data.IDataStore.IDAO;

import com.google.gwt.core.client.Callback;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestBuilder.Method;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;


public abstract class JsonRestDAO<Model extends JSONObject> implements IDAO<String, Model> {

  private static final Logger logger = Logger.getLogger(JsonRestDAO.class.getName());

  protected String keyId;
  protected String baseURL;
  protected Map<String,String> params = new HashMap<String, String>();

  public JsonRestDAO(String baseURL, String keyId) {
    this.keyId = keyId;
    this.baseURL = baseURL;
  }

  protected abstract String buildListURL();
  protected abstract String buildEntryURL(String id);
  protected abstract String buildSearchURL(Model toFind);

  public String getId(Model obj) {
    if (obj.containsKey(this.keyId) && obj.get(this.keyId).isString()!=null) {
      return obj.get(this.keyId).isString().stringValue();
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  protected Model convert(JSONObject obj) {
    return (Model) obj;
  }

  protected List<Model> convert(JSONArray arr) {
    List<Model> result = new ArrayList<Model>();
    for (int i=0; i<arr.size(); i++) {
      if (arr.get(i).isObject()!=null) {
        result.add(this.convert(arr.get(i).isObject()));
      }
    }
    return result;
  }

  protected List<Model> convert(Response resp, Model obj) {
    JSONValue val = JSONParser.parseStrict(resp.getText());
    if (val.isArray()!=null) {
      return this.convert(val.isArray());
    } else if (val.isObject()!=null) {
      return Arrays.asList(this.convert(val.isObject()));
    } else if ((obj!=null) && (val.isString()!=null || val.isNumber()!=null)) {
      obj.put(keyId, new JSONString(val.toString()));
      return Arrays.asList(obj);
    }
    return null;
  }

  private void createAndSendRequest(String url, Method method, String reqData,
      RequestCallback callback) {

    if (!params.isEmpty()) {
      String partialURL = "";
      for (String key : params.keySet()) {
        partialURL += "&"+(key+"="+URL.encode(params.get(key)));
      }
      url += (url.contains("?") ? "" : "?") + partialURL.substring(1); 
    }
    //if (reqData!=null) reqData = URL.encode(reqData);

    logger.log(Level.FINEST, "create and send request with URL: "+url);
    RequestBuilder rb = new RequestBuilder(method, url);
    rb.setHeader("Content-Type", "application/json");
    try {
      rb.sendRequest(reqData, callback);
    } catch (RequestException e) {
      logger.log(Level.SEVERE, "Unable to send request for URL: "+url, e);
      Window.alert("Some error occurred sending an request");
    }
  }

  @Override
  public void create(final Model obj, final Callback<Model, Throwable> callback) {
    final String url = buildListURL();
    createAndSendRequest(url , RequestBuilder.POST, obj.toString(),
        new RequestCallback() {
      @Override
      public void onResponseReceived(Request req, Response resp) {
        logger.log(Level.FINEST, "For URL: "+url+" with method POST, Response: "+resp.getText());
        callback.onSuccess(convert(resp, obj).get(0));
      }
      @Override
      public void onError(Request req, Throwable e) {
        logger.log(Level.SEVERE, "Unable to read response for URL: "+url, e);
        Window.alert("Some error occurred reading a response");
      }
    });
  }

  @Override
  public void retrieve(final Callback<List<Model>, Throwable> callback) {
    final String url = buildListURL();
    createAndSendRequest(url, RequestBuilder.GET, null,
        new RequestCallback() {
      @Override
      public void onResponseReceived(Request req, Response resp) {
        logger.log(Level.FINEST, "For URL: "+url+" with method GET, Response: "+resp.getText());
        callback.onSuccess(convert(resp, null));
      }
      @Override
      public void onError(Request req, Throwable e) {
        logger.log(Level.SEVERE, "Unable to read response for URL: "+url, e);
        Window.alert("Some error occurred reading a response");
      }
    });
  }

  @Override
  public void retrieve(Model toFind, final Callback<List<Model>, Throwable> callback) {
    final String url = buildSearchURL(toFind);
    createAndSendRequest(url, RequestBuilder.GET, null,
        new RequestCallback() {
      @Override
      public void onResponseReceived(Request req, Response resp) {
        logger.log(Level.FINEST, "For URL: "+url+" with method GET, Response: "+resp.getText());
        callback.onSuccess(convert(resp, null));
      }
      @Override
      public void onError(Request req, Throwable e) {
        logger.log(Level.SEVERE, "Unable to read response for URL: "+url, e);
        Window.alert("Some error occurred reading a response");
      }
    });
  }

  @Override
  public void retrieveById(String id, final Callback<Model, Throwable> callback) {
    final String url = buildEntryURL(id);
    createAndSendRequest(url , RequestBuilder.GET, null,
        new RequestCallback() {
      @Override
      public void onResponseReceived(Request req, Response resp) {
        logger.log(Level.FINEST, "For URL: "+url+" with method GET, Response: "+resp.getText());
        callback.onSuccess(convert(resp, null).get(0));
      }
      @Override
      public void onError(Request req, Throwable e) {
        logger.log(Level.SEVERE, "Unable to read response for URL: "+url, e);
        Window.alert("Some error occurred reading a response");
      }
    });
  }

  @Override
  public void update(final Model obj, final Callback<Model, Throwable> callback) {
    final String url = buildEntryURL(getId(obj));
    createAndSendRequest(url, RequestBuilder.PUT, obj.toString(),
        new RequestCallback() {
      @Override
      public void onResponseReceived(Request req, Response resp) {
        logger.log(Level.FINEST, "For URL: "+url+" with method PUT, Response: "+resp.getText());
        callback.onSuccess(convert(resp, obj).get(0));
      }
      @Override
      public void onError(Request req, Throwable e) {
        logger.log(Level.SEVERE, "Unable to read response for URL: "+url, e);
        Window.alert("Some error occurred reading a response");
      }
    });
  }

  private void deleteWithRecursiveTail(final List<Model>modelsForDelete,
      final Callback<List<Model>, Throwable> callback, final List<Model>toReturn) {
    if (modelsForDelete==null || modelsForDelete.isEmpty()) {
      callback.onSuccess(toReturn);
    } else {
      final Model obj = modelsForDelete.remove(0);
      _deleteById(getId(obj), obj,
          new Callback<Model, Throwable>() {
        @Override
        public void onSuccess(Model toDelete) {
          toReturn.add(toDelete);
          deleteWithRecursiveTail(modelsForDelete, callback, toReturn);
        }
        @Override
        public void onFailure(Throwable e) {
          logger.log(Level.SEVERE, "Unable to delete "+obj, e);
          Window.alert("Some error occurred deleting an object");
        }
      });
    }
  }

  private void _deleteById(String id, final Model obj, final Callback<Model, Throwable> callback) {
    final String url = buildEntryURL(id);
    createAndSendRequest(url, RequestBuilder.DELETE, null,
        new RequestCallback() {
      @Override
      public void onResponseReceived(Request req, Response resp) {
        logger.log(Level.FINEST, "For URL: "+url+" with method DELETE, Response: "+resp.getText());
        callback.onSuccess(convert(resp, obj).get(0));
      }
      @Override
      public void onError(Request req, Throwable e) {
        logger.log(Level.SEVERE, "Unable to read response for URL: "+url, e);
        Window.alert("Some error occurred reading a response");
      }
    });
  }

  @Override
  public void delete(final Model toFind, final Callback<List<Model>, Throwable> callback) {
    this.retrieve(toFind, new Callback<List<Model>, Throwable>() {
      @Override
      public void onSuccess(List<Model> result) {
        deleteWithRecursiveTail(result, callback, new ArrayList<Model>());
      }
      @Override
      public void onFailure(Throwable e) {
        logger.log(Level.SEVERE, "Unable to retrieve object: "+toFind+" for deleting it", e);
        Window.alert("Some error occurred retriving a object to delete");
      }
    });
  }

  @Override
  public void deleteById(final String id, final Callback<Model, Throwable> callback) {
    retrieveById(id, new Callback<Model, Throwable>() {
      @Override
      public void onSuccess(Model obj) {
        _deleteById(id, obj, callback);
      }
      @Override
      public void onFailure(Throwable e) {
        logger.log(Level.SEVERE, "Unable to retrieve object with id: "+id+" for deleting it", e);
        Window.alert("Some error occurred retriving a object to delte");
      }
    });
  }

}
