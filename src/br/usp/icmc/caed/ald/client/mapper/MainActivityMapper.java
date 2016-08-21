package br.usp.icmc.caed.ald.client.mapper;

import br.usp.icmc.caed.ald.client.activity.BaseActivity;
import br.usp.icmc.caed.ald.client.activity.HZInsDetailActivity;
import br.usp.icmc.caed.ald.client.activity.HZInsListActivity;
import br.usp.icmc.caed.ald.client.ioc.IClientFactory.Provider;
import br.usp.icmc.caed.ald.client.place.HZInsDetailPlace;
import br.usp.icmc.caed.ald.client.place.HZPlace;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;


public class MainActivityMapper implements ActivityMapper {

  private final Provider<HZInsDetailPlace, HZInsDetailActivity> hzDepInsDetailActivityProvider;

  public MainActivityMapper(
      Provider<HZInsDetailPlace, HZInsDetailActivity> hzDepInsDetailActivityProvider) {
    this.hzDepInsDetailActivityProvider = hzDepInsDetailActivityProvider;
  }

  //  @Override
  //  @SuppressWarnings("rawtypes")
  //  protected BaseActivity getBaseActivity(Place place) {
  //    if (place instanceof ProfitAndLossPlace) {
  //      activity = this.profitAndLossActivityProvider.get();
  //    } else if (place instanceof DesignPlace) {
  //      activity = this.designActivityProvider.get();
  //    }
  //    
  //    if (place instanceof HZDepInsPlace) {
  //      return this.hzDepInsDetailActivityProvider.get();
  //    }
  //    
  //    if (place instanceof DomainElementPlace) {
  //          return this.domainElementsActivityMapper.getActivity(place);
  //      } else if (place instanceof LearnerPlace) {
  //          return this.usersActivityMapper.getActivity(place);
  //      } else if (place instanceof TreeScenarioPlace) {
  //          return new TreeScenarioActivity(this.factory);
  //      } else if (place instanceof WholeConceptPlace) {
  //      activity = (BaseActivity) this.wholeConceptActivityMapper.getActivity(place);
  //    }
  //    return null;
  //  }

  @Override
  public Activity getActivity(Place place) {
    if (place instanceof HZInsDetailPlace) {
      return this.hzDepInsDetailActivityProvider.get((HZInsDetailPlace) place);
    }
    return null;
  }

}
