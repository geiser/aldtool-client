package br.usp.icmc.caed.ald.client.ioc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.usp.icmc.caed.ald.client.activity.HZInsDetailActivity;
import br.usp.icmc.caed.ald.client.activity.HZInsListActivity;
import br.usp.icmc.caed.ald.client.data.IDataStore;
import br.usp.icmc.caed.ald.client.data.IDataStore.ITreeDAO;
import br.usp.icmc.caed.ald.client.mapper.CachingLeftActivityMapper;
import br.usp.icmc.caed.ald.client.mapper.LeftActivityMapper;
import br.usp.icmc.caed.ald.client.mapper.MainActivityMapper;
import br.usp.icmc.caed.ald.client.model.Navigation;
import br.usp.icmc.caed.ald.client.place.HZInsDetailPlace;
import br.usp.icmc.caed.ald.client.place.HZInsListPlace;
import br.usp.icmc.caed.ald.client.place.HomePlace;
import br.usp.icmc.caed.ald.client.view.IView.IHZInsDetailView;
import br.usp.icmc.caed.ald.client.view.IView.IJsonTreeView;
import br.usp.icmc.caed.ald.client.view.IView.INavigationView;
import br.usp.icmc.caed.ald.client.view.IView.IShellView;
import br.usp.icmc.caed.ald.client.view.MainNavigation;
import br.usp.icmc.caed.ald.client.view.ShellView;
import br.usp.icmc.caed.ald.client.view.hz.HZInsDetailView;
import br.usp.icmc.caed.ald.client.view.hz.HZTreeView;
import br.usp.icmc.caed.ald.client.view.resource.Resource;

import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;

public class ClientFactory implements IClientFactory {

	private static final IDataStore ds = GWT.create(IDataStore.class);
	private static final InjectConstants constants = GWT.create(InjectConstants.class);
	private static final Logger logger = Logger.getLogger(ClientFactory.class.getName());

	private static final String ID_KEY = constants.hzIDKey();
	private static final String LBL_KEY = constants.hzLabelKey();
	private static final String ROLE_KEY = constants.hzRoleKey();
	private static final String SLOTS_KEY = constants.hzSlotsKey();
	private static final String CONSTRS_KEY = constants.hzConstraintsKey();

	private static PlaceController placeController;
	private static EventBus eventBus;
	private static Place defaultPlace;

	private static INavigationView mainNavigation;
	private static IShellView shellView;

	private static Provider<HZInsListPlace, HZInsListActivity> hzDepInsListActivityProvider;
	private static Provider<HZInsDetailPlace, HZInsDetailActivity> hzDepInsDetailActivityProvider;

	private static ActivityMapper mainActivityMapper;
	private static ActivityMapper leftActivityMapper;


	@Override
	public void setEventBus(EventBus eventBus) {
		ClientFactory.eventBus = eventBus;
	}

	@Override
	public EventBus getEventBus() {
		if (ClientFactory.eventBus == null) {
			setEventBus(new SimpleEventBus());
		}
		return ClientFactory.eventBus;
	}

	@Override
	public void setPlaceController(PlaceController placeController) {
		ClientFactory.placeController = placeController;
	}

	@Override
	@SuppressWarnings("deprecation")
	public PlaceController getPlaceController() {
		if (ClientFactory.placeController == null) {
			setPlaceController(new PlaceController(getEventBus()));
		}
		return ClientFactory.placeController;
	}


	@Override
	public void setDefaultPlace(Place place) {
		ClientFactory.defaultPlace = place;
	}

	@Override
	public Place getDefaultPlace() {
		if (ClientFactory.defaultPlace == null) {
			setDefaultPlace(new HomePlace());
		}
		return ClientFactory.defaultPlace;
	}

	private Navigation asNavigation(JSONObject obj, String prefix) {
		String title = obj.get("label").isString().stringValue();
		String conceptType = obj.get("type").isString().stringValue();
		String conceptId = obj.get("id").isString().stringValue();
		Place place = new HZInsListPlace(conceptType, conceptId);
		Navigation nav = new Navigation(title, place, null, prefix);
		nav.callback = new Callback<Navigation, Throwable>() {
			@Override
			public void onSuccess(Navigation nav) {
				getShellView().setLeftHeading("List of "+nav.title);
				getShellView().setMainHeading("Details of "+nav.title);
			}
			@Override
			public void onFailure(Throwable reason) {
				// TODO Auto-generated method stub
			}
		};
		return nav;
	}

	@Override
	public void setMainNavigation(INavigationView view) {
		ClientFactory.mainNavigation = view;
	}

	@Override
	public INavigationView getMainNavigation() {
		if (ClientFactory.mainNavigation == null) {
			final INavigationView navigation =  new MainNavigation(getPlaceController());
			navigation.setData(null, Arrays.asList(
					new Navigation("Design", null, Resource.ICONS.design()),
					new Navigation("Domain", null, Resource.ICONS.domain()),
					new Navigation("Learner", null, Resource.ICONS.learner()),
					new Navigation("Expert", null, Resource.ICONS.expert()),
					new Navigation("Setting", null, Resource.ICONS.setting())
					));

			final ITreeDAO<String, JSONObject> wcTreeDAO = ds.getHZOntoTreeDAO("whole-concept");

			//..setting domain menu
			JSONObject toFind = new JSONObject();
			toFind.put(LBL_KEY, new JSONString("What to learn"));
			wcTreeDAO.retrieveTree(toFind,
					new Callback<Entry<JSONObject, List<JSONObject>>, Throwable>() {
				
				@Override
				public void onSuccess(Entry<JSONObject, List<JSONObject>> result) {
					List<Navigation> children = new ArrayList<Navigation>();
					for (JSONObject obj : result.getValue()) {
						children.add(asNavigation(obj, "Domain"));
					}
					if ("What to learn".equals(
							result.getKey().get("label").isString().stringValue())) {
						navigation.setData(new Navigation("Domain"), children);
					} else {
						navigation.setData(asNavigation(result.getKey(), "Domain"), children);
					}
				}
				
				@Override
				public void onFailure(Throwable e) {
					logger.log(Level.SEVERE, "Unable to build navigation for Skill", e);
					Window.alert("Some error occurred building navigation for Skill");
				}
			});

			//..setting design menu
			toFind = new JSONObject();
			toFind.put(LBL_KEY, new JSONString("CL Scenario"));
			wcTreeDAO.retrieveTree(toFind,
					new Callback<Entry<JSONObject,List<JSONObject>>, Throwable>() {
				
				@Override
				public void onSuccess(Entry<JSONObject, List<JSONObject>> result) {
					Navigation root = asNavigation(result.getKey(), "Design:CL Scenario");
					if ("CL Scenario".equals(root.title)) {
						navigation.setData(new Navigation("Design"), Arrays.asList(root));
					}

					List<Navigation> children = new ArrayList<Navigation>();
					for (JSONObject obj : result.getValue()) {
						children.add(asNavigation(obj, "Design:CL Scenario"));
					}
					navigation.setData(root, children);
				}
				
				@Override
				public void onFailure(Throwable e) {
					logger.log(Level.SEVERE, "Unable to build navigation for CL Scenario", e);
					Window.alert("Some error occurred building navigation for CL Scenario");
				}
			});

			//..setting learner menu
			toFind = new JSONObject();
			toFind.put(LBL_KEY, new JSONString("Learning event"));
			ds.getHZOntoDAO("whole-concept").retrieve(toFind,
					new Callback<List<JSONObject>, Throwable>() {

				@Override
				public void onSuccess(List<JSONObject> result) {
					String id = result.get(0).get(ID_KEY).isString().stringValue();
					ds.getHZOntoDAO("whole-concept").retrieveById(id,
							new Callback<JSONObject, Throwable>() {

						@Override
						public void onSuccess(JSONObject leOnt) {
							JSONArray constraints = new JSONArray();

							//.. search constraints
							JSONArray slots = leOnt.get(SLOTS_KEY).isArray();
							for (int i=0; i<slots.size(); i++) {
								JSONObject slot = slots.get(i).isObject();
								if (slot.containsKey(ROLE_KEY)) {
									String roleLbl = slot.get(ROLE_KEY).isObject().get(LBL_KEY).isString().stringValue();
									if ("Learner".equals(roleLbl)) {
										constraints = slot.get(CONSTRS_KEY).isArray();
										break;
									}
								}
							}

							//.. add constraints in navigation
							for (int i=0; i<constraints.size(); i++) {
								final JSONObject constr = constraints.get(i).isObject();
								final String constrLbl = constr.get(LBL_KEY).isString().stringValue();
								wcTreeDAO.retrieveTree(constr,
										new Callback<Entry<JSONObject,List<JSONObject>>, Throwable>() {
									
									@Override
									public void onSuccess(Entry<JSONObject, List<JSONObject>> result) {
										Navigation root = asNavigation(result.getKey(), "Learner:"+constrLbl);
										if (constrLbl.equals(root.title)) {
											navigation.setData(new Navigation("Learner"), Arrays.asList(root));
										}

										List<Navigation> children = new ArrayList<Navigation>();
										for (JSONObject obj : result.getValue()) {
											children.add(asNavigation(obj, "Learner:"+constrLbl));
										}
										navigation.setData(root, children);
									}
									
									@Override
									public void onFailure(Throwable e) {
										logger.log(Level.SEVERE, "Unable to build navigation in Learner", e);
										Window.alert("Some error occurred building navigation in Learner");
									}
								});
							}
							
						}

						@Override
						public void onFailure(Throwable e) {
							logger.log(Level.SEVERE, "Unable to retrieve Learning event", e);
							Window.alert("Some error occurred retrieved Learning event info");
						}
					});
				}

				@Override
				public void onFailure(Throwable e) {
					logger.log(Level.SEVERE, "Unable to retrieve Learning event", e);
					Window.alert("Some error occurred retrieved Learning event info");				
				}
			});

			setMainNavigation(navigation);
		}
		return ClientFactory.mainNavigation;
	}

	@Override
	public void setShellView(IShellView view) {
		ClientFactory.shellView = view;
	}

	@Override
	public IShellView getShellView() {
		if (ClientFactory.shellView == null) {
			setShellView(new ShellView(getMainNavigation()));
		}
		return ClientFactory.shellView;
	}

	@Override
	public void setMainActivityMapper(ActivityMapper mapper) {
		ClientFactory.mainActivityMapper = mapper;
	}

	@Override
	public ActivityMapper getMainActivityMapper() {
		if (ClientFactory.mainActivityMapper == null) {
			setMainActivityMapper(new MainActivityMapper(getHZDepInsDetailActivityProvider()));
		}
		return ClientFactory.mainActivityMapper;
	}

	@Override
	public void setLeftActivityMapper(ActivityMapper mapper) {
		ClientFactory.leftActivityMapper = mapper;
	}

	@Override
	public ActivityMapper getLeftActivityMapper(boolean isCaching) {
		if (ClientFactory.leftActivityMapper == null) {
			Provider<HZInsListPlace, HZInsListActivity>
			hzDepInsListActivityProvider = getHZDepInsListActivityProvider();
			if (isCaching) {
				setLeftActivityMapper(new CachingLeftActivityMapper(
						new LeftActivityMapper(hzDepInsListActivityProvider)));
			} else {
				setLeftActivityMapper(new LeftActivityMapper(hzDepInsListActivityProvider));
			}
		}
		return ClientFactory.leftActivityMapper;
	}

	@Override
	public void setHZDepInsListActivityProvider(Provider<HZInsListPlace,
			HZInsListActivity> provider) {
		ClientFactory.hzDepInsListActivityProvider = provider;
	}

	@Override
	public Provider<HZInsListPlace, HZInsListActivity> getHZDepInsListActivityProvider() {
		if (ClientFactory.hzDepInsListActivityProvider == null) {
			setHZDepInsListActivityProvider(new Provider<HZInsListPlace,
					HZInsListActivity>() {
				@Override
				public HZInsListActivity get(HZInsListPlace place) {
					IJsonTreeView view = new HZTreeView().setHeight(800);
					HZInsListActivity activity = new HZInsListActivity(ds, view, getPlaceController());
					activity.setPlace(place);
					return activity;
				}
			});
		}
		return ClientFactory.hzDepInsListActivityProvider;
	}

	@Override
	public void setHZDepInsDetailActivityProvider(
			Provider<HZInsDetailPlace, HZInsDetailActivity> provider) {
		ClientFactory.hzDepInsDetailActivityProvider = provider;
	}

	@Override
	public Provider<HZInsDetailPlace, HZInsDetailActivity> getHZDepInsDetailActivityProvider() {
		if (ClientFactory.hzDepInsDetailActivityProvider == null) {
			setHZDepInsDetailActivityProvider(new Provider<HZInsDetailPlace,
					HZInsDetailActivity>() {
				@Override
				public HZInsDetailActivity get(HZInsDetailPlace place) {
					ITreeDAO<String, JSONObject> ontDAO = ds.getHZOntoTreeDAO(place.conceptType);

					final IHZInsDetailView view = new HZInsDetailView(ds);
					ontDAO.retrieveById(place.conceptId, new Callback<JSONObject, Throwable>() {
						@Override
						public void onSuccess(JSONObject concept) {
							view.setConcept(concept);
						}
						@Override
						public void onFailure(Throwable e) {
							logger.log(Level.SEVERE, "Unable to load concept in the provider"
									+ " for HZDepInsDetailActivity", e);
							Window.alert("Some error occurred load concept in the provider");
						}
					});

					HZInsDetailActivity activity = new HZInsDetailActivity(view, ds, getPlaceController());
					activity.setPlace(place);
					return activity;
				}
			});
		}
		return ClientFactory.hzDepInsDetailActivityProvider;
	}

}
