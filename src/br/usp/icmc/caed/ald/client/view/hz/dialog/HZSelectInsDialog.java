package br.usp.icmc.caed.ald.client.view.hz.dialog;

import java.util.Arrays;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.usp.icmc.caed.ald.client.data.IDataStore;
import br.usp.icmc.caed.ald.client.delegate.hz.HZInsTreeDelegate;
import br.usp.icmc.caed.ald.client.ioc.InjectConstants;
import br.usp.icmc.caed.ald.client.view.IView.IJsonTreeView;
import br.usp.icmc.caed.ald.client.view.hz.HZTreeView;
import br.usp.icmc.caed.ald.client.view.hz.dialog.IHZDialog.IHZSelectInsDialog;
import br.usp.icmc.caed.ald.client.view.resource.Resource;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.box.PromptMessageBox;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent.DialogHideHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;

public class HZSelectInsDialog extends HZDialog<JSONObject> implements IHZSelectInsDialog {

  private static final Logger logger = Logger.getLogger(HZSelectInsDialog.class.getName());
  private static final InjectConstants constants = GWT.create(InjectConstants.class);
  
  private static final String LBL_KEY = constants.hzLabelKey();
  private static final String IS_CONCEPT_KEY = constants.isConcept();

  private JSONObject result;

  private final IDataStore ds;
  private final TabPanel tabPanel = new TabPanel();
  private final TextField selectedTextField = new TextField();

  public HZSelectInsDialog(IDataStore ds) {
    super();
    this.ds = ds;
    selectedTextField.setAllowBlank(false);
    selectedTextField.setEnabled(false);

    VerticalLayoutContainer container = new VerticalLayoutContainer();
    container.add(tabPanel, new VerticalLayoutData(1, -1));
    container.add(new FieldLabel(selectedTextField, "Selected"), new VerticalLayoutData(1, -1));
    this.add(container);
    this.setWidth("250px");
  }

  @Override
  public void setConstraints(JSONArray constraints) {
    if (constraints!=null) {
      tabPanel.clear();
      for (int i=0; i<constraints.size(); i++) {
        final JSONObject constr = constraints.get(i).isObject();
        final String lbl = constr.get(LBL_KEY).isString().stringValue();

        final IJsonTreeView tree = new HZTreeView().setHeight(355);
        final HZInsTreeDelegate delegate = new HZInsTreeDelegate(ds, constr);
        tree.setDelegate(delegate);
        
        tree.addSelectionChanged(new Callback<Entry<JSONObject,JSONObject>, Throwable>() {
          @Override
          public void onSuccess(Entry<JSONObject,JSONObject> entry) {
            JSONObject selected = entry.getKey();
            logger.log(Level.FINEST, "Selected: " + selected);
            if (selected.containsKey(IS_CONCEPT_KEY) &&
                selected.get(IS_CONCEPT_KEY).isBoolean().booleanValue()) {
              selectedTextField.setValue("");
              result = null;
            } else {
              String lbl = selected.get(LBL_KEY).isString().stringValue();
              selectedTextField.setValue(lbl);
              result = selected;
            }
          }
          @Override
          public void onFailure(Throwable e) {
            logger.log(Level.SEVERE, "Unable to select an instance", e);
            Window.alert("Some error occurred select an instance");
          }
        });

        tree.addButtonToolBar("Add", Resource.ICONS.add(),
            new Callback<Entry<JSONObject, JSONObject>, Throwable>() {
          @Override
          public void onSuccess(final Entry<JSONObject, JSONObject> entry) {
            final PromptMessageBox msgBox = new PromptMessageBox("Instance in "+lbl,
                "Please enter a label for the new instance of "+lbl);
            msgBox.addDialogHideHandler(new DialogHideHandler() {

              @Override
              public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton() == PredefinedButton.OK) {
                  String newLbl = msgBox.getValue();
                  if (newLbl!=null && !newLbl.trim().isEmpty()) {
                    newLbl = newLbl.trim();
                    
                    JSONObject parent = null;
                    if (entry.getKey()!=null &&
                        entry.getKey().containsKey(IS_CONCEPT_KEY) &&
                        entry.getKey().get(IS_CONCEPT_KEY).isBoolean().booleanValue()) {
                      parent = entry.getKey();
                    } else if (entry.getValue()!=null) {
                      parent = entry.getValue();
                    }

                    JSONObject toSave = new JSONObject();
                    toSave.put(LBL_KEY, new JSONString(newLbl));                    
                    
                    tree.setData(parent, Arrays.asList(toSave));
                    
                  } else {
                    AlertMessageBox alert = new AlertMessageBox("Label not empty",
                        "The label for the new instance not must be empty");
                    alert.addDialogHideHandler(new DialogHideHandler() {
                      @Override
                      public void onDialogHide(DialogHideEvent event) {
                        msgBox.show();
                      }
                    });
                    alert.show();
                  }
                }
              }
            });
            msgBox.show();
          }

          @Override
          public void onFailure(Throwable e) {
            logger.log(Level.SEVERE, "Unable to add an instance", e);
            Window.alert("Some error occurred add an instance");
          }
        });
        tree.addButtonToolBar("Delete", Resource.ICONS.del(),
            new Callback<Entry<JSONObject,JSONObject>, Throwable>() {
          
          @Override
          public void onSuccess(final Entry<JSONObject, JSONObject> entry) {
            final JSONObject selected = entry.getKey();
            if (selected==null ||
                (selected.containsKey(IS_CONCEPT_KEY) &&
                    selected.get(IS_CONCEPT_KEY).isBoolean().booleanValue())) {
              new AlertMessageBox("It isn't an instance",
                  "You must select an instance to be deleted").show();
            } else {
              JSONObject parent = null;
              if (entry.getValue()!=null) parent = entry.getValue();
              tree.delData(selected, parent);
              selectedTextField.setValue("");
              result = null;
            }
          }
          
          @Override
          public void onFailure(Throwable e) {
            logger.log(Level.SEVERE, "Unable to del an instance", e);
            Window.alert("Some error occurred del an instance");
          }
        });

        tabPanel.add(tree, new TabItemConfig(lbl, false));

      }
    }
  }

  @Override
  public JSONObject getResult() {
    return this.result;
  }

}
