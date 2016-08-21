package br.usp.icmc.caed.ald.client.view;

import java.util.List;
import java.util.Map.Entry;

import br.usp.icmc.caed.ald.client.model.Navigation;

import com.google.gwt.core.client.Callback;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

public interface IView extends IsWidget {

  interface INavigationView extends IView {
    void setData(Navigation node, List<Navigation> children);
  }
  
  interface IShellView extends IView {
    AcceptsOneWidget getLeftDisplay();
    AcceptsOneWidget getMainDisplay();
    void setLeftHeading(String lbl);
    void setMainHeading(String lbl);
  }
  
  interface IJsonTreeView extends IView {
    
    interface Delegate {
      void getChildren(JSONObject node,
          Callback<List<JSONObject>, Throwable> callback);
      void create(JSONObject node, JSONObject parent,
          Callback<JSONObject, Throwable> callback);
      void delete(JSONObject node, JSONObject parent,
          Callback<JSONObject, Throwable> callback);
    }
    
    void setDelegate(Delegate delegate);
    
    void delData(JSONObject node, JSONObject parent);
    
    void setData(JSONObject parent, List<JSONObject> children);
    
    void addSelectionChanged(Callback<Entry<JSONObject, JSONObject>, Throwable> callback);
    
    /**
     * Add a button in the tool bar with a callback method in their function
     * @param lbl
     * @param img 
     * @param callback that return in the entry <SelectedItem, ParentSelectedItem>
     */
    void addButtonToolBar(String lbl, Callback<Entry<JSONObject, JSONObject>, Throwable> callback);
    
    /**
     * Add a button in the tool bar with a callback method in their function
     * @param lbl
     * @param img 
     * @param callback that return in the entry <SelectedItem, ParentSelectedItem>
     */
    void addButtonToolBar(String lbl, ImageResource img, Callback<Entry<JSONObject, JSONObject>, Throwable> callback);
  }
  
  interface IHZInsDetailView extends IView {
    interface Delegate {
      void save(JSONObject instance);
    }
    void setInstance(JSONObject ins);
    void setConcept(JSONObject concept);
    void setDelegate(Delegate delegate);
  }

  
}