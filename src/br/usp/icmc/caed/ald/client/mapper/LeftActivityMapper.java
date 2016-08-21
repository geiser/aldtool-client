package br.usp.icmc.caed.ald.client.mapper;


import br.usp.icmc.caed.ald.client.activity.HZInsListActivity;
import br.usp.icmc.caed.ald.client.ioc.IClientFactory.Provider;
import br.usp.icmc.caed.ald.client.place.HZInsListPlace;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;

public class LeftActivityMapper implements ActivityMapper {

  //  private Provider<WholeConceptListActivity> wholeConceptListActivityProvider;
  //  private Provider<HZDepInsListActivity> hzDepInsListActivityProvider;

  private final Provider<HZInsListPlace, HZInsListActivity> hzDepInsListActivityProvider;

  public LeftActivityMapper(
      Provider<HZInsListPlace, HZInsListActivity> hzDepInsListActivityProvider) {
    this.hzDepInsListActivityProvider = hzDepInsListActivityProvider;
  }

  @Override
  public Activity getActivity(Place place) {
    if (place instanceof HZInsListPlace) {
      return hzDepInsListActivityProvider.get((HZInsListPlace) place);
    }
    return null;
  }

}
