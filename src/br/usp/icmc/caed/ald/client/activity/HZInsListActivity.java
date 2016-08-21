package br.usp.icmc.caed.ald.client.activity;

import java.util.Arrays;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.usp.icmc.caed.ald.client.data.IDataStore;
import br.usp.icmc.caed.ald.client.delegate.hz.HZInsTreeDelegate;
import br.usp.icmc.caed.ald.client.ioc.InjectConstants;
import br.usp.icmc.caed.ald.client.place.HZInsDetailPlace;
import br.usp.icmc.caed.ald.client.place.HZInsListPlace;
import br.usp.icmc.caed.ald.client.view.IView.IJsonTreeView;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class HZInsListActivity extends BaseActivity<HZInsListPlace> {

  private static final Logger logger = Logger.getLogger(HZInsListActivity.class.getName());
  private static final InjectConstants constants = GWT.create(InjectConstants.class);

  private static final String ID_KEY = constants.hzIDKey();
  private static final String TYPE_KEY = constants.hzTypeKey();
  private static final String LBL_KEY = constants.hzLabelKey();
  private static final String IS_CONCEPT_KEY = constants.isConcept();

  private final IDataStore ds;
  private final IJsonTreeView view;
  private final PlaceController placeController;

  public HZInsListActivity(IDataStore ds, IJsonTreeView view,
      PlaceController placeController) {
    this.ds = ds;
    this.view = view;
    this.placeController = placeController;
  }

  @Override
  public void start(final AcceptsOneWidget panel, EventBus eventBus) {
    view.addSelectionChanged(new Callback<Entry<JSONObject,JSONObject>, Throwable>() {
      @Override
      public void onSuccess(Entry<JSONObject,JSONObject> entry) {
        JSONObject selected = entry.getKey();
        JSONObject conceptParent =  entry.getValue();
        
        if (!selected.containsKey(IS_CONCEPT_KEY) ||
            !selected.get(IS_CONCEPT_KEY).isBoolean().booleanValue()) {
          String mId = getPlace().mainId;
          String mType = getPlace().mainType;
          String cId = getPlace().mainId;
          String cType = getPlace().mainType;
          if (conceptParent!=null) {
            cId = conceptParent.get(ID_KEY).isString().stringValue();
            cType = conceptParent.get(TYPE_KEY).isString().stringValue();
          }
          String id = selected.get(ID_KEY).isString().stringValue();
          placeController.goTo(new HZInsDetailPlace(mType, mId, cType, cId, id));
        }
      }
      
      @Override
      public void onFailure(Throwable e) {
        logger.log(Level.SEVERE, "Unable to load place on selection", e);
        Window.alert("Some error occurred load place on selection");
      }
    });

    view.addButtonToolBar("Add new", new Callback<Entry<JSONObject, JSONObject>, Throwable>() {
      @Override
      public void onSuccess(Entry<JSONObject, JSONObject> entry) {
        JSONObject toCreate = entry.getKey();
        JSONObject conceptParent =  entry.getValue();
        
        if (toCreate==null) {
          toCreate = new JSONObject();
          toCreate.put(LBL_KEY, new JSONString("instance_new"));          
        } else if (entry.getKey().containsKey(IS_CONCEPT_KEY) &&
            entry.getKey().get(IS_CONCEPT_KEY).isBoolean().booleanValue()) {
          toCreate = new JSONObject();
          String str = entry.getKey().get(LBL_KEY).isString().stringValue();
          toCreate.put(LBL_KEY, new JSONString(str+"_new"));
          conceptParent = entry.getKey();
        }
        view.setData(conceptParent, Arrays.asList(toCreate));
      }
      @Override
      public void onFailure(Throwable e) {
        logger.log(Level.SEVERE, "Unable to load place for new instance", e);
        Window.alert("Some error occurred creating place for new instance");
      }
    });

    view.addButtonToolBar("Remove", new Callback<Entry<JSONObject, JSONObject>, Throwable>() {
      @Override
      public void onSuccess(Entry<JSONObject, JSONObject> entry) {
        JSONObject selected = entry.getKey();
        JSONObject conceptParent =  entry.getValue();
        if (!selected.containsKey(IS_CONCEPT_KEY) ||
            !selected.get(IS_CONCEPT_KEY).isBoolean().booleanValue()) {
          view.delData(selected, conceptParent);
        }
      }
      
      @Override
      public void onFailure(Throwable e) {
        logger.log(Level.SEVERE, "Unable to remove selected instance", e);
        Window.alert("Some error occurred removing selected instance");
      }
    });

    JSONObject hzConcept = new JSONObject();
    hzConcept.put(ID_KEY, new JSONString(getPlace().mainId));
    hzConcept.put(TYPE_KEY, new JSONString(getPlace().mainType));
    view.setDelegate(new HZInsTreeDelegate(ds, hzConcept).setDetail(true));
    panel.setWidget(view);
  }


}
