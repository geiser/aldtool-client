package br.usp.icmc.caed.ald.client.view.hz.tmp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.usp.icmc.caed.ald.client.data.tmp.*;

import br.usp.icmc.caed.ald.client.view.hz.tmp.TreeDrawComponent;
import br.usp.icmc.caed.ald.client.view.hz.tmp.TreeDrawComponent.LabelProvider;
import br.usp.icmc.caed.ald.client.view.hz.tmp.resource.Resource;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.chart.client.draw.Gradient;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.Rotation;
import com.sencha.gxt.chart.client.draw.Scaling;
import com.sencha.gxt.chart.client.draw.path.CurveTo;
import com.sencha.gxt.chart.client.draw.path.MoveTo;
import com.sencha.gxt.chart.client.draw.path.PathSprite;
import com.sencha.gxt.chart.client.draw.sprite.CircleSprite;
import com.sencha.gxt.chart.client.draw.sprite.EllipseSprite;
import com.sencha.gxt.chart.client.draw.sprite.ImageSprite;
import com.sencha.gxt.chart.client.draw.sprite.RectangleSprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.dom.ScrollSupport;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.loader.ChildTreeStoreBinding;
import com.sencha.gxt.data.shared.loader.DataProxy;
import com.sencha.gxt.data.shared.loader.TreeLoader;
import com.sencha.gxt.state.client.TreeStateHandler;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer.AccordionLayoutAppearance;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import com.sencha.gxt.widget.core.client.tree.Tree;

public class TreeScenarioView implements IsWidget {

	private final AccordionLayoutAppearance appearance = GWT.<AccordionLayoutAppearance>create(AccordionLayoutAppearance.class);

	DataProxy<MetadataModel, List<MetadataModel>> proxy =
			new DataProxy<MetadataModel, List<MetadataModel>>() {
		@Override
		public void load(MetadataModel loadCfg,
				final Callback<List<MetadataModel>, Throwable> callback) {
			String parentId = loadCfg.getId();
			String elementType = MetadataModel.TYPE.KNOWLEDGE;

			MetadataModel toFind = new MetadataModel().setType(elementType).setParentId(parentId);
			DataStoreTest.domainModelDAO.retrieve(toFind, new Callback<List<MetadataModel>, Throwable>() {
				@Override
				public void onSuccess(List<MetadataModel> result) {
					callback.onSuccess(new ArrayList<MetadataModel>(result));
				}
				@Override
				public void onFailure(Throwable reason) {
					callback.onFailure(reason);
				}
			});
		}
	};

	TreeLoader<MetadataModel> loader = new TreeLoader<MetadataModel>(proxy) {
		@Override
		public boolean hasChildren(MetadataModel parent) {
			return true;
		}
	};

	TreeStore<MetadataModel> store = new TreeStore<MetadataModel>(
			new ModelKeyProvider<MetadataModel>() {
				@Override
				public String getKey(MetadataModel meta) {
					return meta.getId();
				}
			});
	
	Tree<MetadataModel, String> treeView = new Tree<MetadataModel, String>(
			store,
			new ValueProvider<MetadataModel, String>() {
				@Override
				public String getValue(MetadataModel meta) {
					return meta.getLabel();
				}
				@Override
				public void setValue(MetadataModel meta, String value) { }
				@Override
				public String getPath() {
					return "title";
				}
			});
	TreeStateHandler<MetadataModel> stateHandler = new TreeStateHandler<MetadataModel>(treeView);
	
	TreeDrawComponent<MetadataModel>	treeDrawComponent;

	public TreeScenarioView() {
		loader.addLoadHandler(new ChildTreeStoreBinding<MetadataModel>(store));
		treeView.setLoader(loader);
		stateHandler.loadState();
		
		final String elementType = MetadataModel.TYPE.KNOWLEDGE;
		MetadataModel toFind = new MetadataModel().setType(elementType);
		DataStoreTest.domainModelDAO.retrieve(toFind, new Callback<List<MetadataModel>, Throwable>() {
			@Override
			public void onSuccess(List<MetadataModel> result) {
				for (MetadataModel element : result) {
					TreeScenarioView.this.store.add(element); 
				}
			}
			@Override
			public void onFailure(Throwable exception) {
				Window.alert("[ERROR] feching domain element's in TreeScenarioView."
						+ " "+exception.getMessage());
			}
		});
		
		//..treeDrawComponent.setSelectionModel(treeView.getSelectionModel());
		this.treeDrawComponent = new TreeDrawComponent<MetadataModel>(
				treeView,
				new LabelProvider<MetadataModel>() {
					@Override
          public List<String> getValue(MetadataModel model) {
	          return Arrays.asList(model.getId(), model.getLabel());
          }
					@Override
          public String getDef(MetadataModel model) {
	          return  model.getType();
          }},
          new LabelProvider<MetadataModel>(){
						@Override
            public List<String> getValue(MetadataModel model) {
	            return Arrays.asList(model.getId(), model.getLabel(), model.getType());
            }
						@Override
            public String getDef(MetadataModel model) {
	            return model.getType();
            } });
		this.treeDrawComponent.setSize("1024", "640");
	}

	private ContentPanel createTreePanel() {
		final TextButton newButton = new TextButton("New", Resource.ICONS.add());
		final TextButton childButton = new TextButton("Add child", Resource.ICONS.album());
		final TextButton delButton = new TextButton("Del", Resource.ICONS.del());

		
		//..setting new button
		newButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				delButton.disable();
				childButton.disable();
				treeView.getSelectionModel().deselectAll();
				//DomainElementListView.this.getPresenter().goTo(
				//		new DomainElementDetailPlace(elementType, null, null));
			}
		});

		//..setting add new child button
		childButton.disable();
		childButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				delButton.disable();
				childButton.disable();
				final MetadataModel parent = treeView.getSelectionModel().getSelectedItem();
				//				if (parent!=null) {
				//					DomainElementListView.this.getPresenter().goTo(
				//							new DomainElementDetailPlace(elementType, null, parent.getId()));
				//				}
			}
		});

		//..setting delete element button
		delButton.disable();
		delButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				final MetadataModel element = treeView.getSelectionModel().getSelectedItem();
				if (element!=null) {
					//					ConfirmMessageBox messageBox = new ConfirmMessageBox("Confirm",
					//							"Are you sure you want to remove this user?");
					//					messageBox.addDialogHideHandler(new DialogHideHandler() {
					//						@Override
					//						public void onDialogHide(DialogHideEvent event) {
					//							if (event.getHideButton()==PredefinedButton.YES) {
					//								DomainElementListView.this.getPresenter()
					//								.removeDomainElement(element.getId(),
					//										new Callback<MetadataModel, Throwable>() {
					//									@Override
					//									public void onSuccess(MetadataModel result) {
					//										treeView.getStore().remove(element);
					//										DomainElementListView.this.getPresenter().goTo(
					//												new DomainElementListPlace(elementType));
					//									}
					//									@Override
					//									public void onFailure(Throwable reason) {
					//										Window.alert("[ERROR] in del button for DomainElementListView. "+reason.getMessage());
					//									}
					//								});
					//							}
					//						}});
					//					messageBox.show();
				}
			}
		});

		// setting tool bar
		ToolBar tbButtons = new ToolBar();
		tbButtons.add(newButton);
		tbButtons.add(childButton);
		tbButtons.add(delButton);

		VerticalLayoutContainer vlc = new VerticalLayoutContainer();
		vlc.add(tbButtons, new VerticalLayoutData(1, -1, new Margins(0)));
		vlc.add(treeView, new VerticalLayoutData(1, 1, new Margins(0)));
		treeView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		//		treeView.getSelectionModel().addSelectionChangedHandler(
		//				new SelectionChangedHandler<MetadataModel>() {
		//					@Override
		//					public void onSelectionChanged(SelectionChangedEvent<MetadataModel> event) {
		//						delButton.disable();
		//						childButton.disable();
		//						if (event.getSelection().size()>0) {
		//							for (String key : DomainElementListView.this.treeViews.keySet()) {
		//								if (!key.equals(elementType)) {
		//									DomainElementListView.this.treeViews.get(key)
		//									.getSelectionModel().deselectAll();
		//								}
		//							}							
		//							MetadataModel element = event.getSelection().get(0);
		//							if (treeView.isLeaf(element)) { delButton.enable(); }
		//							childButton.enable();
		//							DomainElementListView.this.getPresenter().goTo(
		//									new DomainElementDetailPlace(elementType,
		//											element.getId(), element.getParentId()));
		//						}
		//					}
		//				});

		ContentPanel cp = new ContentPanel(this.appearance);
		cp.setAnimCollapse(true);
		cp.setHeading("Test using Knowledge datastore");
		cp.setWidget(vlc);
		return cp;
	}



	@Override
	public Widget asWidget() {
		
		TextSprite text = new TextSprite();
		text.setText("Hello\nWorld!");
		text.setX(10);
		text.setY(25);
		text.setFont("Helvetica");
		text.setFontSize(18);
		text.setFill(RGB.BLACK);

		Scaling scale = new Scaling();
		scale.setX(2);
		scale.setY(2);

		Gradient gradient = new Gradient(21);
		gradient.addStop(0, new Color("#79A933"));
		gradient.addStop(13, new Color("#70A333"));
		gradient.addStop(34, new Color("#559332"));
		gradient.addStop(58, new Color("#277B2F"));
		gradient.addStop(86, new Color("#005F27"));
		gradient.addStop(100, new Color("#005020"));

		final CircleSprite circle = new CircleSprite();
		circle.setCenterX(120);
		circle.setCenterY(100);
		circle.setRadius(25);
		circle.setScaling(scale);
		circle.setStroke(new Color("#999"));
		circle.setFill(gradient);
		circle.setStrokeWidth(3);

		Rotation rotate = new Rotation();
		rotate.setDegrees(45);
		rotate.setX(125 + 50 / 2);
		rotate.setY(75 + 50 / 2);

		RectangleSprite rect = new RectangleSprite();
		rect.setX(125);
		rect.setY(75);
		rect.setRotation(rotate);
		rect.setWidth(50);
		rect.setHeight(50);
		rect.setRadius(10);
		rect.setFill(new Color("#bf292f"));

		PathSprite path = new PathSprite();
		path.addCommand(new MoveTo(75, 75));
		path.addCommand(new CurveTo(0, -25, 50, 25, 50, 0, true));
		path.addCommand(new CurveTo(0, -25, -50, 25, -50, 0, true));
		path.setStroke(new Color("#000"));
		path.setStrokeWidth(2);
		path.setFill(new Color("#fc0"));
		path.setFillOpacity(0.25);


		final EllipseSprite ellipse = new EllipseSprite();
		ellipse.setCenterX(175);
		ellipse.setCenterY(100);
		ellipse.setRadiusX(25);
		ellipse.setRadiusY(40);
		//ellipse.setFillOpacity(0.56);
		ellipse.setStroke(new Color("#000000"));
		ellipse.setFill(new Color("#2fb92f"));
		ellipse.setStrokeWidth(5);

		TextSprite label = new TextSprite("Node A ▼ △ ▼ ");
		label.setFontSize(12);
		label.setX(ellipse.getCenterX());
		label.setY(ellipse.getCenterY()-5);

		TextSprite label2 = new TextSprite("label2 △");
		label.setFontSize(12*2);
		label2.setX(ellipse.getCenterX());
		label2.setY(ellipse.getCenterY()+10);

		rotate = new Rotation();
		rotate.setDegrees(315);
		rotate.setX(ellipse.getCenterX() + ellipse.getRadiusX());
		rotate.setY(ellipse.getCenterY() + ellipse.getRadiusY());
		ellipse.setRotation(rotate);

		ImageSprite imageSprite = new ImageSprite(Resource.ICONS.music());
		imageSprite.setX(50);
		imageSprite.setY(10);

		
		//component.addSprite(tree);
		treeDrawComponent.addSprite(text);
		treeDrawComponent.addGradient(gradient);
		treeDrawComponent.addSprite(circle);
		treeDrawComponent.addSprite(rect);
		treeDrawComponent.addSprite(path);
		treeDrawComponent.addSprite(ellipse);
		treeDrawComponent.addSprite(imageSprite);
		treeDrawComponent.redrawSurfaceForced();
		
		
		
		//		component.addSprite(label);
		//		component.addSprite(label2);
		//		component.addSpriteSelectionHandler(new SpriteSelectionHandler() {
		//			@Override
		//			public void onSpriteSelect(SpriteSelectionEvent event) {
		//				Sprite sprite = event.getSprite();
		//				if (sprite instanceof NodeFuncView) {
		//					if (tree.getFillOpacity() <= 0) {
		//						tree.setFillOpacity(1);
		//					} else { 
		//						tree.setFillOpacity(tree.getFillOpacity()-0.2);
		//					}
		//					tree.redraw();
		//				}
		//				if (sprite instanceof TextSprite) {
		//					Window.alert("Selected text sprite:: "+((TextSprite) sprite).getText());
		//				} else if (sprite instanceof CircleSprite) {
		//					if (!circle.getFill().equals(RGB.YELLOW)) {
		//						circle.setFill(RGB.YELLOW);
		//					} else {
		//						circle.setFill(RGB.MAGENTA);
		//					}
		//					circle.redraw();
		//					Scaling s = ellipse.getScaling();
		//					s.setX(s.getX()*2);
		//					s.setY(s.getY()*2);
		//					ellipse.setScaling(s);
		//					ellipse.redraw();
		//				}
		//			}
		//		});
		//component.setPixelSize(640, 600);
		
//		FlowLayoutContainer flcDrawing = new FlowLayoutContainer();
//		ScrollSupport scrollSupport = flcDrawing.getScrollSupport();
//		scrollSupport.setScrollMode(ScrollMode.ALWAYS);
//		flcDrawing.add(treeDrawComponent, new MarginData(5));

		HorizontalLayoutContainer mainPanel = new HorizontalLayoutContainer();
		mainPanel.add(this.createTreePanel(), new HorizontalLayoutData(-1, 1));
		mainPanel.add(treeDrawComponent, new HorizontalLayoutData(-1, 1));
		ScrollSupport sSupport = mainPanel.getScrollSupport();
		sSupport.setScrollMode(ScrollMode.ALWAYS);
		
		
		return mainPanel;
	}

}
