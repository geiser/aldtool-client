package br.usp.icmc.caed.ald.client.activity;

import java.util.logging.Level;
import java.util.logging.Logger;

import br.usp.icmc.caed.ald.client.data.IDataStore;
import br.usp.icmc.caed.ald.client.data.IDataStore.IDAO;
import br.usp.icmc.caed.ald.client.ioc.InjectConstants;
import br.usp.icmc.caed.ald.client.place.HZInsDetailPlace;
import br.usp.icmc.caed.ald.client.place.HomePlace;
import br.usp.icmc.caed.ald.client.view.IView.IHZInsDetailView;
import br.usp.icmc.caed.ald.client.view.IView.IHZInsDetailView.Delegate;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class HZInsDetailActivity extends BaseActivity<HZInsDetailPlace> implements Delegate, Callback<JSONObject, Throwable> {

	private static final Logger logger = Logger.getLogger(HZInsDetailActivity.class.getName());
	private static final InjectConstants constants = GWT.create(InjectConstants.class);
	private static final String ID_KEY = constants.hzIDKey();

	private final PlaceController placeController;
	private final IHZInsDetailView view;
	private final IDataStore ds;
	
	private IDAO<String, JSONObject> dao;

	public HZInsDetailActivity(IHZInsDetailView view, IDataStore ds,
			PlaceController placeController) {
		this.ds = ds;
		this.view = view;
		this.placeController = placeController;
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		dao = ds.getHZDepInsDAO(place.conceptType, place.conceptId);
		if (place.instanceId!=null) {
			dao.retrieveById(place.instanceId, new Callback<JSONObject, Throwable>() {
				@Override
				public void onSuccess(JSONObject ins) {
					view.setInstance(ins);
				}
				@Override
				public void onFailure(Throwable e) {
					logger.log(Level.SEVERE, "Unable to load instance in the provider"
							+ " for HZDepInsDetailActivity", e);
					Window.alert("Some error occurred load instance in the provider");
				}
			});
		} else {
			view.setInstance(new JSONObject());
		}
		
		view.setDelegate(this);
		panel.setWidget(view);
	}

	@Override
	public void save(final JSONObject ins) {
		if (ins.containsKey(ID_KEY) &&
				ins.get(ID_KEY).isString()!=null) {
			dao.update(ins, this);
		} else {
			dao.create(ins, this);
		}
	}

	@Override
	public void onFailure(Throwable e) {
		logger.log(Level.SEVERE, "Unable to update or create an instance", e);
		Window.alert("Some error occurred update or create an instance");    

	}

	@Override
	public void onSuccess(JSONObject ins) {
		logger.log(Level.FINEST, "Save operation sucess!!. Instance: "+ins);
		this.placeController.goTo(new HomePlace());
	}

}
