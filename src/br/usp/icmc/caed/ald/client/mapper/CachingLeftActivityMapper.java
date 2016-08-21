package br.usp.icmc.caed.ald.client.mapper;


import br.usp.icmc.caed.ald.client.place.HZInsDetailPlace;
import br.usp.icmc.caed.ald.client.place.HZInsListPlace;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.activity.shared.CachingActivityMapper;
import com.google.gwt.activity.shared.FilteredActivityMapper;
import com.google.gwt.place.shared.Place;

public class CachingLeftActivityMapper implements ActivityMapper {

  private final ActivityMapper filteredActivityMapper;
  
  public CachingLeftActivityMapper(ActivityMapper mapper) {
    FilteredActivityMapper.Filter filter = new FilteredActivityMapper.Filter() {
      public Place filter(Place place) {
        if (place instanceof HZInsDetailPlace) {
          String hzMainId = ((HZInsDetailPlace) place).mainId;
          String hzMainType = ((HZInsDetailPlace) place).mainType;
          return new HZInsListPlace(hzMainType, hzMainId);
        }
        //        if (place instanceof LDElementDetailPlace) {
        //          return new LDElementListPlace(((LDElementDetailPlace) place).getElementType(),
        //              ((LDElementDetailPlace) place).isAvoidCache());
        //        } else if (place instanceof DomainElementDetailPlace) {
        //          return new DomainElementListPlace(((DomainElementDetailPlace) place).getElementType(),
        //              ((DomainElementDetailPlace) place).isAvoidCache());
        //        } else if (place instanceof LearnerDetailPlace) {
        //          return new LearnerListPlace(((LearnerDetailPlace) place).getLearnerType(),
        //              ((LearnerDetailPlace) place).isAvoidCache());
        //        }
        return place;
      }
    };
    CachingActivityMapper cachingActivityMapper = new CachingActivityMapper(mapper);
    filteredActivityMapper = new FilteredActivityMapper(filter, cachingActivityMapper);
  }
  
  @Override
  public Activity getActivity(Place place) {
    return this.filteredActivityMapper.getActivity(place);
  }
  
}
