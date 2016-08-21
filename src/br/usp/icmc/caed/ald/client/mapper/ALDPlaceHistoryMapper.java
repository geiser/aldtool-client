package br.usp.icmc.caed.ald.client.mapper;

import br.usp.icmc.caed.ald.client.place.HomePlace;
import br.usp.icmc.caed.ald.client.place.HZInsDetailPlace;
import br.usp.icmc.caed.ald.client.place.HZInsListPlace;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;

@WithTokenizers({
  HomePlace.Tokenizer.class,
  
  HZInsListPlace.Tokenizer.class,
  HZInsDetailPlace.Tokenizer.class,
})
public interface ALDPlaceHistoryMapper extends PlaceHistoryMapper {

}
