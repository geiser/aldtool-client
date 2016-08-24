package br.usp.icmc.caed.ald.client.activity.tmp;


import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

import br.usp.icmc.caed.ald.client.view.hz.tmp.TreeScenarioView;

public class TreeScenarioActivity extends AbstractActivity implements Activity {
	
	@Override
	public void start(AcceptsOneWidget container, EventBus eventBus) {
		container.setWidget(new TreeScenarioView());
	}

}
