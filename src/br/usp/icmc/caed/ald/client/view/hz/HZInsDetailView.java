package br.usp.icmc.caed.ald.client.view.hz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.TextButtonCell;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent.DialogHideHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.HtmlEditor;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

import br.usp.icmc.caed.ald.client.data.IDataStore;
import br.usp.icmc.caed.ald.client.ioc.InjectConstants;
import br.usp.icmc.caed.ald.client.view.IView.IHZInsDetailView;
import br.usp.icmc.caed.ald.client.view.hz.dialog.HZSlotInsDialog;
import br.usp.icmc.caed.ald.client.view.hz.dialog.IHZDialog.IHZSlotInsDialog;
import br.usp.icmc.caed.ald.client.view.resource.Resource;

public class HZInsDetailView implements IHZInsDetailView {

  private static final Logger logger = Logger.getLogger(HZInsDetailView.class.getName());
  private static final InjectConstants constants = GWT.create(InjectConstants.class);
  
  private static final String ID_KEY = constants.hzIDKey();
  private static final String LBL_KEY = constants.hzLabelKey();  
  private static final String DESCR_KEY = constants.hzDescriptionKey();
  private static final String SLOTS_KEY = constants.hzSlotsKey();
  private static final String ROLE_KEY = constants.hzRoleKey();
  private static final String VALUE_KEY = constants.hzValueKey();
  private static final String CONSTRS_KEY = constants.hzConstraintsKey();
  private static final String ONT_OBJ_KEY = constants.hzOntologyObjectKey();
  private static final String CARD_MAX_KEY = constants.hzCardinalityMaxKey();
  private static final String CARD_MIN_KEY = constants.hzCardinalityMinKey();

  private Delegate delegate;
  private FramedPanel container;

  private JSONObject instance;
  private JSONObject concept;

  private Map<String, Integer> roleCounter = new HashMap<String, Integer>();

  private ToolBar toolBar = new ToolBar();
  private TextField lblTextField = new TextField();
  private HtmlEditor descrEditor = new HtmlEditor();

  private final ListStore<JSONObject> slotInsStore = new ListStore<JSONObject>(
      new ModelKeyProvider<JSONObject>() {
        @Override
        public String getKey(JSONObject slot) {
          if (slot.containsKey(ID_KEY)) {
            return slot.get(ID_KEY).isString().stringValue();
          } 
          return slot.toString();
        }
      });

  private final IDataStore ds;

  public HZInsDetailView(IDataStore ds) {
    this.ds = ds;
  }

  @Override
  public void setDelegate(Delegate delegate) {
    this.delegate = delegate;
  }

  @Override
  public Widget asWidget() {
    if (container == null) {
      container = new FramedPanel();
      
      lblTextField.setEmptyText("Label is allways required...");
      lblTextField.setAllowBlank(false);

      VerticalLayoutContainer vlcInfo = new VerticalLayoutContainer();
      vlcInfo.add(new FieldLabel(lblTextField, "Label"), new VerticalLayoutData(1, -1));
      vlcInfo.add(new FieldLabel(descrEditor, "Description"), new VerticalLayoutData(1, -1));

      VerticalLayoutContainer vlcSlot = new VerticalLayoutContainer();
      vlcSlot.add(toolBar, new VerticalLayoutData(1, -1));
      vlcSlot.add(createSlotGrid() , new VerticalLayoutData(1, 1));

      TabPanel panel = new TabPanel();
      panel.add(vlcInfo, new TabItemConfig("Info", false));
      panel.add(vlcSlot, new TabItemConfig("Slots", false));

      container.setBorders(false);
      container.setHeaderVisible(false);
      container.setSize("100%", "100%");
      container.add(panel);
      TextButton saveBtn = new TextButton("Save");
      saveBtn.addSelectHandler(new SelectHandler() {
        @Override
        public void onSelect(SelectEvent event) {
          if (instance==null) { instance = new JSONObject(); }
          int index = 0;
          JSONArray slotInsances = new JSONArray();
          for (JSONObject slotIns : slotInsStore.getAll()) {
            slotInsances.set(index, slotIns); index++;
          }

          if (lblTextField.getValue()!=null) {
            instance.put(LBL_KEY, new JSONString(lblTextField.getValue()));
          }
          instance.put(DESCR_KEY, new JSONString(descrEditor.getValue()));
          instance.put(SLOTS_KEY, slotInsances);
          if (validateInstance()) {
          	logger.log(Level.FINEST, "Instance to save: " + instance);
            delegate.save(instance);
          }
        }
      });
      container.addButton(saveBtn);
      container.addButton(new TextButton("Cancel"));
    }
    return container;
  }

  private boolean validateInstance() {

    String error = "";
    if (!instance.containsKey(LBL_KEY) ||
        instance.get(LBL_KEY).isString().stringValue().trim().isEmpty()) {
      error += "\n Label can't be empty";
    }

    //..counter for roles in slots
    if (concept.containsKey(SLOTS_KEY) && concept.get(SLOTS_KEY).isArray()!=null) {
      JSONArray slots = concept.get(SLOTS_KEY).isArray();
      for (int i=0; i<slots.size(); i++) {
        JSONObject slot = slots.get(i).isObject();

        int maxCard = 0;
        if (slot.get(CARD_MAX_KEY).isNumber()!=null) {
          maxCard = (int) slot.get(CARD_MAX_KEY).isNumber().doubleValue();
        } else if (slot.get(CARD_MAX_KEY).isString()!=null) { 
          maxCard = new Integer(slot.get(CARD_MAX_KEY).isString().stringValue()).intValue();
        }
        int minCard = 0;
        if (slot.get(CARD_MIN_KEY).isNumber()!=null) {
          minCard = (int) slot.get(CARD_MIN_KEY).isNumber().doubleValue();
        } else if (slot.get(CARD_MIN_KEY).isString()!=null) { 
          minCard = new Integer(slot.get(CARD_MIN_KEY).isString().stringValue()).intValue();
        }

        JSONObject role = slot.get(ROLE_KEY).isObject();
        String roleLbl = role.get(LBL_KEY).isString().stringValue();
        int count = 0;
        if (roleCounter.containsKey(roleLbl)) {
          count = roleCounter.get(roleLbl).intValue();
        }
        if (count<minCard || count>maxCard) {
          error += "\n The cardinality for "+roleLbl+" must be between "+minCard+" and "+maxCard;
        }
      }
    }

    if (!error.trim().isEmpty()) {
      new AlertMessageBox("Alert", "Errors in the validation: "+error).show();
      return false;
    }
    return true;
  }

  private Grid<JSONObject> createSlotGrid() {
    List<ColumnConfig<JSONObject, ?>> cfgs = new ArrayList<ColumnConfig<JSONObject,?>>();
    //..setting the string columns for role and value
    cfgs.add(new ColumnConfig<JSONObject, String>(
        new ValueProvider<JSONObject, String>() {
          @Override
          public String getValue(JSONObject slotIns) {
            JSONObject roleIns = slotIns.get(ROLE_KEY).isObject();
            return roleIns.get(LBL_KEY).isString().stringValue();
          }
          @Override
          public void setValue(JSONObject slotIns, String lbl) { }
          @Override
          public String getPath() {
            return ROLE_KEY+"/"+LBL_KEY;
          }
        }, 125, "Role Ins"));
    cfgs.add(new ColumnConfig<JSONObject, String>(
        new ValueProvider<JSONObject, String>() {
          @Override
          public String getValue(JSONObject slotIns) {
            String result = "";
            if (slotIns.containsKey(VALUE_KEY)) {
              result = slotIns.get(VALUE_KEY).isString().stringValue();
            }
            return result;
          }
          @Override
          public void setValue(JSONObject slotIns, String value) { }
          @Override
          public String getPath() {
            return VALUE_KEY;
          }
        }, 125, "Value"));
    
    //..setting edit button for each row
    ColumnConfig<JSONObject, String> editColumnCfg = new ColumnConfig<JSONObject, String>(
        new ValueProvider<JSONObject, String>() {
          @Override
          public String getValue(JSONObject slotIns) {
            return "Edit";
          }
          @Override
          public void setValue(JSONObject slotIns, String value) { }
          @Override
          public String getPath() {
            return "";
          }
        });

    TextButtonCell editButtonCell = new TextButtonCell();
    editButtonCell.addSelectHandler(new SelectHandler() {
      @Override
      public void onSelect(SelectEvent event) {
        final int row = event.getContext().getIndex();
        final JSONObject slotIns = slotInsStore.get(row);

        JSONObject roleIns = slotIns.get(ROLE_KEY).isObject();
        String roleInsLbl = roleIns.get(LBL_KEY).isString().stringValue();
        
        JSONArray slots = concept.get(SLOTS_KEY).isArray();
        JSONArray constraints = null;
        for (int i=0; i<slots.size(); i++) {
          JSONObject slot = slots.get(i).isObject();
          JSONObject role = slot.get(ROLE_KEY).isObject();
          String roleLbl = role.get(LBL_KEY).isString().stringValue();
          if (roleLbl.equals(roleInsLbl)) {
            constraints = slot.get(CONSTRS_KEY).isArray();
            break;
          }
        }

        if (constraints!=null) {
          IHZSlotInsDialog dialog = new HZSlotInsDialog(ds);
          dialog.setSlotInstance(slotIns);
          dialog.setConstraints(constraints);
          dialog.setOkHandler(new Callback<JSONObject, Throwable>() {
            @Override
            public void onSuccess(JSONObject slotIns) {
              slotInsStore.update(slotIns);
            }
            @Override
            public void onFailure(Throwable e) {
              logger.log(Level.SEVERE, "Unable to add slot-instance", e);
              Window.alert("Some error occurred add new slot-instance");
            }
          });
          dialog.show();
        }
      }
    });
    editColumnCfg.setWidth(75);
    editColumnCfg.setHorizontalAlignment(ColumnConfig.ALIGN_CENTER);
    editColumnCfg.setCell(editButtonCell);
    cfgs.add(editColumnCfg);

    //..setting delete button for each row
    ColumnConfig<JSONObject, String> delOpColumnConfig = new ColumnConfig<JSONObject, String>(
        new ValueProvider<JSONObject, String>() {
          @Override
          public String getValue(JSONObject slotIns) {
            return "Delete";
          }
          @Override
          public void setValue(JSONObject slotIns, String value) { }
          @Override
          public String getPath() {
            return ""; 
          }
        });

    TextButtonCell delButtonCell = new TextButtonCell();
    delButtonCell.addSelectHandler(new SelectHandler() {
      @Override
      public void onSelect(SelectEvent event) {
        final int row = event.getContext().getIndex();
        ConfirmMessageBox messageBox = new ConfirmMessageBox("Confirm",
            "Are you sure you want to remove this item?");
        messageBox.addDialogHideHandler(new DialogHideHandler() {
          @Override
          public void onDialogHide(DialogHideEvent event) {
            if (event.getHideButton() == PredefinedButton.YES) {
              reduceRoleCounter(slotInsStore.get(row));
              slotInsStore.remove(row);
            }
          }
        });
        messageBox.show();
      }
    });
    delOpColumnConfig.setWidth(75);
    delOpColumnConfig.setHorizontalAlignment(ColumnConfig.ALIGN_CENTER);
    delOpColumnConfig.setCell(delButtonCell);
    cfgs.add(delOpColumnConfig);

    //..setting grid for the properties
    Grid<JSONObject> grid = new Grid<JSONObject>(slotInsStore,
    		new ColumnModel<JSONObject>(cfgs));
    grid.setColumnReordering(false);
    
    return grid;
  }
  
  @Override
  public void setConcept(JSONObject concept) {
    //..update concept
    toolBar.clear();
    if (concept.containsKey(SLOTS_KEY) && concept.get(SLOTS_KEY).isArray()!=null) {
      JSONArray slots = concept.get(SLOTS_KEY).isArray();
      for (int i=0; i<slots.size(); i++) {
        final JSONObject slot = slots.get(i).isObject();
        
        JSONObject role = slot.get(ROLE_KEY).isObject();
				final String roleLbl = role.get(LBL_KEY).isString().stringValue();
        TextButton btn = new TextButton(roleLbl, Resource.ICONS.add());

        btn.addSelectHandler(new SelectHandler() {
          @Override
          public void onSelect(SelectEvent event) {

            int count = 0;
            if (roleCounter.containsKey(roleLbl)) {
            	count = roleCounter.get(roleLbl).intValue();
            }
            int maxCard = 0;
            if (slot.get(CARD_MAX_KEY).isNumber()!=null) {
              maxCard = (int) slot.get(CARD_MAX_KEY).isNumber().doubleValue();
            } else if (slot.get(CARD_MAX_KEY).isString()!=null) { 
              maxCard = new Integer(slot.get(CARD_MAX_KEY).isString().stringValue()).intValue();
            }

            if (count<maxCard) {
            	
            	JSONObject newRoleIns = new JSONObject();
            	newRoleIns.put(LBL_KEY, new JSONString(roleLbl));
              newRoleIns.put(ONT_OBJ_KEY, slot.get(ROLE_KEY));
            	
              JSONObject newSlotIns = new JSONObject();
              newSlotIns.put(LBL_KEY, new JSONString(roleLbl));
              newSlotIns.put(ONT_OBJ_KEY, slot);
              newSlotIns.put(ROLE_KEY, newRoleIns);
              
              IHZSlotInsDialog dialog = new HZSlotInsDialog(ds);
              dialog.setSlotInstance(newSlotIns);
              if (slot.containsKey(CONSTRS_KEY)) {
                dialog.setConstraints(slot.get(CONSTRS_KEY).isArray());
              }
              dialog.setOkHandler(new Callback<JSONObject, Throwable>() {
                @Override
                public void onSuccess(JSONObject slotIns) {
                  slotInsStore.add(slotIns);
                  addRoleCounter(slotIns);
                }
                @Override
                public void onFailure(Throwable e) {
                  logger.log(Level.SEVERE, "Unable to add slot-instance", e);
                  Window.alert("Some error occurred add new slot-instance");
                }
              });
              dialog.show();
            } else {                
              new AlertMessageBox("Alert", "The max number of roles for the instance is "+
                  slot.get(CARD_MAX_KEY).toString()).show();
            }
          }
        });
        toolBar.add(btn);
      }
    }

    this.concept = concept;
  }

  @Override
  public void setInstance(JSONObject ins) {
  	logger.log(Level.FINEST, "SetInstance: " + ins);
    //..update label and description
    if (ins.containsKey(LBL_KEY)) {
      lblTextField.setValue(ins.get(LBL_KEY).isString().stringValue());
    }
    if (ins.containsKey(DESCR_KEY)) {
      descrEditor.setValue(ins.get(DESCR_KEY).isString().stringValue());
    }

    //..update slots
    slotInsStore.clear();
    if (ins.containsKey(SLOTS_KEY)) {
      JSONArray slotInstances = ins.get(SLOTS_KEY).isArray();
      for (int i=0; i<slotInstances.size(); i++) {
        JSONObject slotIns = slotInstances.get(i).isObject();
        addRoleCounter(slotIns);
        slotInsStore.add(slotIns);
      }
    }

    this.instance = ins;
  }

  private void addRoleCounter(JSONObject slotIns) {
    JSONObject roleIns = slotIns.get(ROLE_KEY).isObject();
    JSONObject role = roleIns.get(ONT_OBJ_KEY).isObject();
    String roleLbl = role.get(LBL_KEY).isString().stringValue();
    Integer count = new Integer(0);
    if (roleCounter.containsKey(roleLbl)) { count = roleCounter.get(roleLbl); }
    roleCounter.put(roleLbl, new Integer(count.intValue()+1));
  }

  private void reduceRoleCounter(JSONObject slotIns) {
    JSONObject roleIns = slotIns.get(ROLE_KEY).isObject();
    JSONObject role = roleIns.get(ONT_OBJ_KEY).isObject();
    String roleLbl = role.get(LBL_KEY).isString().stringValue();
    Integer count = new Integer(0);
    if (roleCounter.containsKey(roleLbl)) { count = roleCounter.get(roleLbl); }
    roleCounter.put(roleLbl, new Integer(count.intValue()-1));
  }

}
