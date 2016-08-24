package br.usp.icmc.caed.ald.client.place.tmp;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class TreeScenarioPlace extends Place {
	
	@Prefix("tree-scenario")
	public static class Tokenizer implements PlaceTokenizer<TreeScenarioPlace> {
		public TreeScenarioPlace getPlace(String token) {
			return new TreeScenarioPlace();
		}    
		public String getToken(TreeScenarioPlace place) {
			return "tree-scenario";
		}
	}
}
