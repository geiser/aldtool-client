package br.usp.icmc.caed.ald.client.data;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.core.client.Callback;
import com.google.gwt.json.client.JSONObject;

public interface IDataStore {

  public interface IDAO<Key, Model> {

    /**
     * Create a new model in the DataStore
     * @param obj that will be created
     * @param callback with the created model
     */
    public void create(Model obj, Callback<Model, Throwable> callback);

    /**
     * Retrieve the existing models in the DataStore
     * @param callback with the set of found models
     */
    public void retrieve(Callback<List<Model>, Throwable> callback);

    /**
     * Retrieve models that are similar to model in the DAODataStore
     * @param toFind with the information that will be searching
     * @param callback with the set of found models
     */
    public void retrieve(Model toFind, Callback<List<Model>, Throwable> callback);

    /**
     * Retrieve an existing model of DataStore
     * @param id or key of model that will be retrieved
     * @param callback with the found model or null if the element doesn't exist
     */
    public void retrieveById(Key id, Callback<Model, Throwable> callback);

    /**
     * Update a model in the DataStore
     * @param obj that will be updated
     * @param callback with the model update
     */
    public void update(Model obj, Callback<Model, Throwable> callback);

    /**
     * Delete a set of models in the DataStore
     * @param obj that contains the set of properties to be deleted 
     * @param callback with the deleted models 
     */
    public void delete(Model obj, Callback<List<Model>, Throwable> callback);

    /**
     * Delete a model by the id in the DataStore
     * @param id or key of model that will be deleted
     * @param callback with the deleted model
     */
    public void deleteById(Key id, Callback<Model, Throwable> callback);

  }

  public interface ITreeDAO<Key, Model> extends IDAO<Key, Model> {

    /**
     * Retrieve the data from HZServer using recursive calls in each call the
     * response is the callback with pairs of (root, children) values
     * @param toFind
     * @param callback
     */
    public void retrieveTree(JSONObject toFind,
        final Callback<Entry<JSONObject, List<JSONObject>>, Throwable> callback);

  }

  public interface IDAOWithMetadata<Key, Model, Metadata> extends IDAO<Key, Model> {

    /**
     * Create a new model with its metadata in the DataStore
     * @param obj that will be created
     * @param meta that will association with obj in the DAO
     * @param callback with the created model
     */
    public void create(Model obj, Metadata meta, Callback<Model, Throwable> callback);

    /**
     * Retrieve the existing pairs of models and metadatas in the DataStore
     * @param callback with the pairs of found models and metadatas
     */
    public void retrieveWithMetadata(Callback<Map<Model, Metadata>, Throwable> callback);

    /**
     * Retrieve pairs of models and metadatas that are related in the DataStore
     * @param toFind with the information that will be searching
     * @param callback with the pairs of found models and metadatas
     */
    public void retrieveWithMetadata(Model toFind, Callback<Map<Model, Metadata>, Throwable> callback);

    /**
     * Retrieve an existing pair of model and metadata of DataStore
     * @param id or key of model that will be retrieved
     * @param callback with the found model or null if the element doesn't exist
     */
    public void retrieveByIdWithMetadata(Key id, Callback<Entry<Model, Metadata>, Throwable> callback);

    /**
     * Update a model in the DataStore
     * @param obj that will be updated
     * @param meta that will be associated with the obj
     * @param callback with the model update
     */
    public void update(Model obj, Metadata meta, Callback<Model, Throwable> callback);

  }
  
  public void setBaseURL(String baseURL);
  public String getBaseURL();
  
  public ITreeDAO<String, JSONObject> getHZOntoTreeDAO(String type);
  public IDAO<String, JSONObject> getHZDepInsDAO(String type, String id);
	public IDAO<String, JSONObject> getHZOntoDAO(String type);

}
