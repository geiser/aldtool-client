package br.usp.icmc.caed.ald.client.view.hz.dialog;

import java.util.logging.Level;
import java.util.logging.Logger;

import br.usp.icmc.caed.ald.client.data.IDataStore;
import br.usp.icmc.caed.ald.client.ioc.InjectConstants;
import br.usp.icmc.caed.ald.client.view.hz.dialog.IHZDialog.IHZSlotInsDialog;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;

public class HZSlotInsDialog extends HZDialog<JSONObject> implements IHZSlotInsDialog {

  private static final Logger logger = Logger.getLogger(HZSlotInsDialog.class.getName());
  private static final InjectConstants constants = GWT.create(InjectConstants.class);
  //private static final String ID_KEY = constants.hzIDKey();
  private static final String LBL_KEY = constants.hzLabelKey();
  private static final String ROLE_KEY = constants.hzRoleKey();
  private static final String VALUE_KEY = constants.hzValueKey();
  private static final String CONSTR_KEY = constants.hzConstraintKey();

  private JSONObject slotIns;
  private final IDataStore ds;

  //private final ITreeJsonView selectTreeView;
  private final TextField roleTextField = new TextField();
  private final TextField valueTextField = new TextField();
  private final TextButton selectTextButton = new TextButton("Select");

  public HZSlotInsDialog(IDataStore ds) {
    super();
    this.ds = ds;
    roleTextField.setEnabled(false);
    valueTextField.setEnabled(false);

    VerticalLayoutContainer container = new VerticalLayoutContainer();
    container.add(new FieldLabel(roleTextField, "Role"), new VerticalLayoutData(1, -1));
    HorizontalPanel hPanel = new HorizontalPanel();
    hPanel.add(valueTextField);
    hPanel.add(selectTextButton);
    container.add(new FieldLabel(hPanel, "Constr. value"), new VerticalLayoutData(1, -1));

    this.setHeading("Slot Information");
    this.add(container);
  }

  @Override
  public void setSlotInstance(JSONObject slotIns) {
  	JSONObject roleIns = slotIns.get(ROLE_KEY).isObject();
  	String roleLbl = roleIns.get(LBL_KEY).isString().stringValue();
  	
    if (slotIns.containsKey(ROLE_KEY)) { roleTextField.setValue(roleLbl); }
    if (slotIns.containsKey(VALUE_KEY)) {
      valueTextField.setValue(slotIns.get(VALUE_KEY).isString().stringValue());
    }
    this.slotIns = slotIns;
  }
  
  @Override
  public void setConstraints(final JSONArray constraints) {
    this.selectTextButton.addSelectHandler(new SelectHandler() {
      @Override
      public void onSelect(SelectEvent event) {
        IHZSelectInsDialog dlg = new HZSelectInsDialog(ds);
        dlg.setOkHandler(new Callback<JSONObject, Throwable>() {
          @Override
          public void onSuccess(JSONObject ins) {
            if (slotIns==null) slotIns = new JSONObject();
            slotIns.put(CONSTR_KEY, ins);
            slotIns.put(VALUE_KEY, ins.get(LBL_KEY));
            setSlotInstance(slotIns);
          }
          @Override
          public void onFailure(Throwable reason) {
            logger.log(Level.SEVERE, "Unable to select a constraint", reason);
            Window.alert("Some error occurred select a constraint for a slot");
          }
        });
        dlg.setConstraints(constraints);
        dlg.show();
      }
    });
  }

  @Override
  public JSONObject getResult() {
    return this.slotIns;
  }


}


