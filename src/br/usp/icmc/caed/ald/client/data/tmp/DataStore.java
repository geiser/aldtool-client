package br.usp.icmc.caed.ald.client.data.tmp;

import java.util.List;


import com.google.gwt.core.client.Callback;
import com.google.gwt.json.client.JSONObject;

public interface DataStore {
	
	public interface DAO<Key, Model> {
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
		 * Retrieve models that are similar to model in the DataStore
		 * @param obj with the information that will be searching
		 * @param callback with the set of found models
		 */
		public void retrieve(Model obj, Callback<List<Model>, Throwable> callback);
		
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
	
	public DAO<String, MetadataModel> getDomainModelDAO();
	
	public DAO<String, MetadataModel> getLearnerModelDAO();
	
	public DAO<String, JSONObject> getOntologyDAO(); 
	
}
