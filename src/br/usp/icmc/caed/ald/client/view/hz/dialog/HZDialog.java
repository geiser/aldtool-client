package br.usp.icmc.caed.ald.client.view.hz.dialog;

import com.google.gwt.core.client.Callback;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

public abstract class HZDialog<T> extends Dialog implements IHZDialog<T> {
  
  public HZDialog() {
    this.setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
    this.getButton(PredefinedButton.CANCEL).addSelectHandler(new SelectHandler() {
      @Override
      public void onSelect(SelectEvent event) {
        hide();
      }
    });
    this.setModal(true);
    this.forceLayout();
  }
  
  @Override
  public void setOkHandler(final Callback<T, Throwable> callback) {
    this.getButton(PredefinedButton.OK).addSelectHandler(new SelectHandler() {
      @Override
      public void onSelect(SelectEvent event) {
        T toReturn = getResult();
        if (toReturn != null) {
          callback.onSuccess(toReturn);    
          hide();
        } else {
          new AlertMessageBox("Alert", "Error in the validation").show();
        }
      }
    });
    this.setModal(true);
    this.forceLayout();
  }
  
  public abstract T getResult();

}
