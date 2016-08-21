package br.usp.icmc.caed.ald.client.view.hz;

import br.usp.icmc.caed.ald.client.ioc.InjectConstants;
import br.usp.icmc.caed.ald.client.view.JsonTreeView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;

public class HZTreeView extends JsonTreeView {

	private static final InjectConstants constants = GWT.create(InjectConstants.class);

	private static final String ID_KEY = constants.hzIDKey();
	private static final String LBL_KEY = constants.hzLabelKey();
	private static final String TYPE_KEY = constants.hzTypeKey();
	private static final String ROLE_KEY = constants.hzRoleKey();
	private static final String VALUE_KEY = constants.hzValueKey();

	public HZTreeView() {
		super(ID_KEY, new ValueProvider<JSONObject, String>() {

			@Override
      public String getValue(JSONObject hzObj) {
	      String lbl = hzObj.get(LBL_KEY).isString().stringValue();
	      String type = hzObj.get(TYPE_KEY).isString().stringValue();
	      if (hzObj.containsKey(TYPE_KEY) &&
	      		"slot-instance".equals(type) &&
	      		hzObj.containsKey(ROLE_KEY)) {
	      	JSONObject roleIns = hzObj.get(ROLE_KEY).isObject() ;
	      	lbl = roleIns.get(LBL_KEY).isString().stringValue();
	      	if (hzObj.containsKey(VALUE_KEY)) {
	      		lbl += ": " + hzObj.get(VALUE_KEY).isString().stringValue();
	      	}
	      }
				return lbl;
      }
			
			@Override
      public void setValue(JSONObject hzObj, String value) { }

			@Override
      public String getPath() {
	      return "label";
      }
			
		});
	}

	public HZTreeView setHeight(int height) {
		flc.setHeight(height);
		flc.setScrollMode(ScrollMode.ALWAYS);
		return this;
	}
	
	public HZTreeView setHeight(String height) {
		flc.setHeight(height);
		flc.setScrollMode(ScrollMode.ALWAYS);
		return this;
	}
}
