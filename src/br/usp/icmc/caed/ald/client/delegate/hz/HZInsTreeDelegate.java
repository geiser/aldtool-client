package br.usp.icmc.caed.ald.client.delegate.hz;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Window;

import br.usp.icmc.caed.ald.client.data.IDataStore;
import br.usp.icmc.caed.ald.client.data.IDataStore.IDAO;
import br.usp.icmc.caed.ald.client.data.IDataStore.ITreeDAO;
import br.usp.icmc.caed.ald.client.ioc.InjectConstants;
import br.usp.icmc.caed.ald.client.view.IView.IJsonTreeView.Delegate;

public class HZInsTreeDelegate implements Delegate {

	private static final Logger logger = Logger.getLogger(HZInsTreeDelegate.class.getName());
	private static final InjectConstants constants = GWT.create(InjectConstants.class);

	private static final String ID_KEY = constants.hzIDKey();
	private static final String TYPE_KEY = constants.hzTypeKey();
	private static final String UPPER_KEY = constants.hzUpperKey();
	private static final String SLOTS_KEY = constants.hzSlotsKey();
	private static final String ONT_OBJ_KEY = constants.hzOntologyObjectKey();

	private static final String IS_CONCEPT_KEY = constants.isConcept();

	private final IDataStore ds;
	private final JSONObject mainConcept;
	private boolean isDetail = false;

	public HZInsTreeDelegate(IDataStore ds, JSONObject mainConcept) {
		this.ds = ds;
		this.mainConcept = mainConcept;
	}
	
	public HZInsTreeDelegate setDetail(boolean isDetail) {
		this.isDetail = isDetail;
		return this;
	}

	@Override
	public void getChildren(final JSONObject node,
			final Callback<List<JSONObject>, Throwable> callback) {
		boolean isConcept = true; 
		String cid = mainConcept.get(ID_KEY).isString().stringValue();
		String ctype = mainConcept.get(TYPE_KEY).isString().stringValue();

		if (node!=null) {
			if (node.containsKey(IS_CONCEPT_KEY) &&
					node.get(IS_CONCEPT_KEY).isBoolean().booleanValue()) {
				cid = node.get(ID_KEY).isString().stringValue();
			} else {
				JSONObject ont_obj = node.get(ONT_OBJ_KEY).isObject();
				cid = ont_obj.get(ID_KEY).isString().stringValue();
				ctype = ont_obj.get(TYPE_KEY).isString().stringValue();
				isConcept = false;
			}
		}
		
		final IDAO<String, JSONObject> insDAO = ds.getHZDepInsDAO(ctype, cid);
		final ITreeDAO<String, JSONObject> treeDAO = ds.getHZOntoTreeDAO(ctype);
		if (isConcept) {
			insDAO.retrieve(new Callback<List<JSONObject>, Throwable>() {
				@Override
				public void onSuccess(final List<JSONObject> instances) {
					JSONObject toFind = new JSONObject();
					toFind.put(UPPER_KEY, mainConcept);
					if (node!=null) {
						toFind.put(UPPER_KEY, node);
					}
					
					treeDAO.retrieve(toFind, new Callback<List<JSONObject>, Throwable>() {
						@Override
						public void onSuccess(List<JSONObject> subconcepts) {
							List<JSONObject> result = new ArrayList<JSONObject>();
							result.addAll(instances);
							for (JSONObject sub : subconcepts) {
								sub.put(IS_CONCEPT_KEY, JSONBoolean.getInstance(true));
								result.add(sub);
							}
							callback.onSuccess(result);
						}
						@Override
						public void onFailure(Throwable e) {
							logger.log(Level.SEVERE, "Unable to load subconcepts", e);
							Window.alert("Some error occurred load subconcepts");
						}
					});
				}
				@Override
				public void onFailure(Throwable e) {
					logger.log(Level.SEVERE, "Unable to load instances", e);
					Window.alert("Some error occurred load instances");
				}
			});
		} else if (isDetail) {
			String id = node.get(ID_KEY).isString().stringValue();
			insDAO.retrieveById(id, new Callback<JSONObject, Throwable>() {
				@Override
				public void onSuccess(JSONObject ins) {
					if (ins.containsKey(SLOTS_KEY) &&
							ins.get(SLOTS_KEY).isArray().size()>0) {
						List<JSONObject> result = new ArrayList<JSONObject>();
						JSONArray slots = ins.get(SLOTS_KEY).isArray();
						for (int i=0; i<slots.size();i++) {
							result.add(slots.get(i).isObject());
						}
						callback.onSuccess(result);
					} else {
						callback.onSuccess(new ArrayList<JSONObject>());
					}
				}
				@Override
				public void onFailure(Throwable e) {
					logger.log(Level.SEVERE, "Unable to load instance slots", e);
					Window.alert("Some error occurred load instance slots");
				}
			});
		} else {
			callback.onSuccess(new ArrayList<JSONObject>());
		}
	}

	@Override
	public void create(JSONObject child, JSONObject parent,
			final Callback<JSONObject, Throwable> callback) {
		String conceptId = mainConcept.get(ID_KEY).isString().stringValue();
		String conceptType = mainConcept.get(TYPE_KEY).isString().stringValue();
		if (parent!=null) {  
			conceptId = parent.get(ID_KEY).isString().stringValue();
			conceptType = parent.get(TYPE_KEY).isString().stringValue();
		}
		IDAO<String, JSONObject> dao = ds.getHZDepInsDAO(conceptType, conceptId);
		dao.create(child, callback);
	}

	@Override
	public void delete(JSONObject node, JSONObject parent,
			final Callback<JSONObject, Throwable> callback) {
		String conceptId = mainConcept.get(ID_KEY).isString().stringValue();
		String conceptType = mainConcept.get(TYPE_KEY).isString().stringValue();
		if (parent!=null) {  
			conceptId = parent.get(ID_KEY).isString().stringValue();
			conceptType = parent.get(TYPE_KEY).isString().stringValue();
		}
		IDAO<String, JSONObject> dao = ds.getHZDepInsDAO(conceptType, conceptId);
		dao.deleteById(node.get(ID_KEY).isString().stringValue(), callback);
	}

}
