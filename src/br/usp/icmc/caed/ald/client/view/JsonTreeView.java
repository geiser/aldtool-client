package br.usp.icmc.caed.ald.client.view;

import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.usp.icmc.caed.ald.client.view.IView.IJsonTreeView;

import com.google.gwt.core.client.Callback;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.loader.ChildTreeStoreBinding;
import com.sencha.gxt.data.shared.loader.DataProxy;
import com.sencha.gxt.data.shared.loader.TreeLoader;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent.DialogHideHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import com.sencha.gxt.widget.core.client.tree.Tree;

public class JsonTreeView implements IJsonTreeView, ValueProvider<JSONObject, String> {

  private static final Logger logger = Logger.getLogger(JsonTreeView.class.getName());

  private Delegate delegate;

  protected VerticalLayoutContainer container;
  protected FlowLayoutContainer flc = new FlowLayoutContainer();

  private final ToolBar toolBar;
  private final String valuePath;
  private final TreeStore<JSONObject> store;
  private final Tree<JSONObject, String> tree;
  
  public JsonTreeView(final String keyId, ValueProvider<JSONObject, String> valueProvider) {
  	this.valuePath = null;
    this.store = new TreeStore<JSONObject>(new ModelKeyProvider<JSONObject>() {
      @Override
      public String getKey(JSONObject item) {
        return item.get(keyId).isString().stringValue();
      }
    });
    this.tree = new Tree<JSONObject, String>(store, valueProvider);
    tree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    this.toolBar = new ToolBar();
  }

  public JsonTreeView(final String keyId, String valuePath) {
  	this.valuePath = valuePath;
    this.store = new TreeStore<JSONObject>(new ModelKeyProvider<JSONObject>() {
      @Override
      public String getKey(JSONObject item) {
        return item.get(keyId).isString().stringValue();
      }
    });
    this.tree = new Tree<JSONObject, String>(store, this);
    tree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    this.toolBar = new ToolBar();
  }

  @Override
  public Widget asWidget() {
    if (container == null) {
      container = new VerticalLayoutContainer();

      DataProxy<JSONObject, List<JSONObject>> proxy = new DataProxy<JSONObject,
          List<JSONObject>>() {
        @Override
        public void load(final JSONObject node,
            Callback<List<JSONObject>, Throwable> callback) {
          delegate.getChildren(node, callback);
        }
      };

      TreeLoader<JSONObject> loader = new TreeLoader<JSONObject>(proxy) {
        @Override
        public boolean hasChildren(JSONObject node) {
          if (node.containsKey("hasChildren") &&
              node.get("hasChildren").isBoolean()!=null) {
            return node.get("hasChildren").isBoolean().booleanValue();
          }
          return true;
        }
      };
      loader.addLoadHandler(new ChildTreeStoreBinding<JSONObject>(store));
      tree.setLoader(loader);

      flc.add(tree, new MarginData(0));
      container.add(toolBar, new VerticalLayoutData(1, -1));
      container.add(flc, new VerticalLayoutData(1, -1));
    }
    return container;
  }

  @Override
  public String getValue(JSONObject obj) {
    String result = store.getKeyProvider().getKey(obj);
    if (this.valuePath!=null && !this.valuePath.isEmpty()) {
      JSONValue val = obj;
      String[] array = this.valuePath.split("/");
      for (String var : array) {
        try {
          int i = Integer.parseInt(var);
          if (val.isArray()!=null) {
            val = val.isArray().get(i);
          }
        } catch (Exception e) {
          if (val.isObject()!=null) {
            val = val.isObject().get(var);
          }
        }
      }

      if (val.isString()!=null) {
        result = val.isString().stringValue();
      } else result = val.toString();
    }
    return result;
  }

  @Override
  public void setValue(JSONObject obj, String value) {

  }

  @Override
  public String getPath() {
    return "label";
  }

  @Override
  public void setDelegate(Delegate delegate) {
    this.delegate = delegate;
  }

  @Override
  public void setData(final JSONObject parent, List<JSONObject> children) {
    if (children!=null && !children.isEmpty()) {
      if (parent!=null && store.findModel(parent)!=null) store.update(parent);
      for (JSONObject node : children) {
        delegate.create(node, parent, new Callback<JSONObject, Throwable>() {
          @Override
          public void onSuccess(JSONObject child) {
            if (parent!=null) store.add(parent, child);
            else store.add(child);
          }
          @Override
          public void onFailure(Throwable e) {
            logger.log(Level.SEVERE, "Unable to add a new instance", e);
            Window.alert("Some error occurred add a new instance");
          }
        });
      }
    }
  }

  @Override
  public void addSelectionChanged(final Callback<Entry<JSONObject, JSONObject>, Throwable> callback) {
    tree.getSelectionModel().addSelectionChangedHandler(
        new SelectionChangedHandler<JSONObject>() {
          @Override
          public void onSelectionChanged(SelectionChangedEvent<JSONObject> e) {
            if (e.getSelection()!=null && !e.getSelection().isEmpty()) {
              final JSONObject selected = e.getSelection().get(0);
              final JSONObject parent = store.getParent(selected);
              
              callback.onSuccess(new Entry<JSONObject, JSONObject>() {
                @Override
                public JSONObject setValue(JSONObject value) {
                  return null;
                }
                @Override
                public JSONObject getValue() {
                  return parent;
                }
                @Override
                public JSONObject getKey() {
                  return selected;
                }
              });
            }
          }
        });
  }

  @Override
  public void addButtonToolBar(String lbl,
      final Callback<Entry<JSONObject, JSONObject>, Throwable> callback) {
    this.addButtonToolBar(lbl, null, callback);
  }

  @Override
  public void addButtonToolBar(String lbl, ImageResource img,
      final Callback<Entry<JSONObject, JSONObject>, Throwable> callback) {
    TextButton btn = new TextButton(lbl, new SelectHandler() {
      @Override
      public void onSelect(SelectEvent event) {
        callback.onSuccess(new Entry<JSONObject, JSONObject>() {
          @Override
          public JSONObject setValue(JSONObject value) {
            // TODO Auto-generated method stub
            return null;
          }
          @Override
          public JSONObject getValue() {
            JSONObject selected = tree.getSelectionModel().getSelectedItem();
            if (selected!=null) {
              return store.getParent(selected);
            }
            return null;
          }
          @Override
          public JSONObject getKey() {
            return tree.getSelectionModel().getSelectedItem();
          }
        });
      }
    });
    if (img!=null) btn.setIcon(img);
    toolBar.add(btn);
  }

  @Override
  public void delData(final JSONObject node, final JSONObject parent) {
    final ConfirmMessageBox msgBox = new ConfirmMessageBox("Confirm delData",
        "Are you sure you want to remove the item: "+getValue(node)+" ?");
    msgBox.addDialogHideHandler(new DialogHideHandler() {
      @Override
      public void onDialogHide(DialogHideEvent event) {
        if (event.getHideButton()==PredefinedButton.YES) {
          delegate.delete(node, parent, new Callback<JSONObject, Throwable>() {
            @Override
            public void onSuccess(JSONObject result) {
              store.remove(result);
            }
            @Override
            public void onFailure(Throwable e) {
              logger.log(Level.SEVERE, "Unable to delete an instance", e);
              Window.alert("Some error occurred delete an instance");
            }
          });
        }
      }
    });
    msgBox.show();
  }

}
