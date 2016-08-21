package br.usp.icmc.caed.ald.client.view.hz.dialog;

import com.google.gwt.core.client.Callback;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.IsWidget;

public interface IHZDialog<T> extends IsWidget {

  void setOkHandler(Callback<T, Throwable> callback);
  void show();
  
  interface IHZSelectInsDialog extends IHZDialog<JSONObject> {
//    interface Delegate {
//    }
//    void setDelegate(Delegate delegate);
    void setConstraints(JSONArray constraints); 
  }
  
  interface IHZSlotInsDialog extends IHZDialog<JSONObject> {
    void setSlotInstance(JSONObject slotIns);
    void setConstraints(JSONArray constraints);
  }
  
  
}