
package br.usp.icmc.caed.ald.client.view;

import br.usp.icmc.caed.ald.client.view.IView.IShellView;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.MarginData;

public class ShellView implements IShellView, ResizeHandler {
  private static final int WIDTH_THRESHOLD = 743;

  private BorderLayoutContainer container;
  private ContentPanel leftDisplay = new ContentPanel();
  private ContentPanel mainDisplay = new ContentPanel();

  private final INavigationView mainNavigation;

  public ShellView(INavigationView mainNavigation) {
    this.mainNavigation = mainNavigation;
    Window.addResizeHandler(this);
  }

  @Override
  public Widget asWidget() {
    if (container == null) {
      container = new BorderLayoutContainer();

      this.mainDisplay.setHeaderVisible(true);
      container.setCenterWidget(this.mainDisplay, new MarginData(0));

      BorderLayoutData westData = new BorderLayoutData(232);
      westData.setSplit(true);
      westData.setCollapsible(true);
      container.setWestWidget(this.leftDisplay, westData);

      BorderLayoutData northData = new BorderLayoutData(28);
      container.setNorthWidget(this.mainNavigation, northData);

      Scheduler.get().scheduleDeferred(new ScheduledCommand() {
        @Override
        public void execute() {
          handleResize(Window.getClientWidth(), Window.getClientHeight());
        }
      });
    }
    return container;
  }

  @Override
  public AcceptsOneWidget getMainDisplay() {
    return new AcceptsOneWidget() {
      @Override
      public void setWidget(IsWidget w) {
        mainDisplay.setWidget(w);
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
          @Override
          public void execute() {
            mainDisplay.forceLayout();
          }
        });
      }
    };
  }

  @Override
  public AcceptsOneWidget getLeftDisplay() {
    return new AcceptsOneWidget() {
      @Override
      public void setWidget(IsWidget w) {
        leftDisplay.setWidget(w);
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
          @Override
          public void execute() {
            leftDisplay.forceLayout();
          }
        });
      }
    };
  }

  @Override
  public void onResize(ResizeEvent resizeEvent) {
    //    if (GXT.isTablet()) {
    //      handleResize(resizeEvent.getWidth(), resizeEvent.getHeight());
    //    } else {
    handleResize(resizeEvent.getWidth(), WIDTH_THRESHOLD);
    //    }
  }

  private void handleResize(int width, int threshold) {
    //    if (width > threshold) {
    //      container.hide(LayoutRegion.NORTH);
    //      container.show(LayoutRegion.WEST);
    //    } else {
    //      container.hide(LayoutRegion.WEST);
    //      container.show(LayoutRegion.NORTH);
    //    }
  }

  @Override
  public void setLeftHeading(String lbl) {
    leftDisplay.setHeading(lbl);
  }

  @Override
  public void setMainHeading(String lbl) {
    mainDisplay.setHeading(lbl);
  }


}
