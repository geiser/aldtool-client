package br.usp.icmc.caed.ald.client.data;

import com.google.gwt.json.client.JSONObject;

public class DataStore implements IDataStore {

  private static String baseURL;

  @Override
  public void setBaseURL(String baseURL) {
    DataStore.baseURL = baseURL;
  }

  @Override
  public String getBaseURL() {
    if (DataStore.baseURL == null) {
      setBaseURL("http://localhost:3000");
    }
    return DataStore.baseURL;
  }

  @Override
  public ITreeDAO<String, JSONObject> getHZOntoTreeDAO(String type) {
    return new HZOntoDAO(getBaseURL(), type);
  }

  @Override
  public IDAO<String, JSONObject> getHZDepInsDAO(String type, String id) {
    return new HZDepInsDAO(getBaseURL(), type, id);
  }

	@Override
  public IDAO<String, JSONObject> getHZOntoDAO(String type) {
	  return new HZOntoDAO(getBaseURL(), type);
  }



}
