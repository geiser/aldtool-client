package br.usp.icmc.caed.ald.client.view.hz.tmp.resource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public class Resource {
  
  public interface Icons extends ClientBundle {
    @Source("icon_add.gif")
    ImageResource add();
    @Source("icon_album.gif")
    ImageResource album();
    
    @Source("icon_calendar.gif")
    ImageResource calendar();
    @Source("icon_connect.png")
    ImageResource connect();
    @Source("icon_del.gif")
    ImageResource del();
    @Source("icon_form.png")
    ImageResource form();
    @Source("icon_list.gif")
    ImageResource list();
    @Source("icon_menu.gif")
    ImageResource menu();
    @Source("icon_music.png")
    ImageResource music();
    
    @Source("icon_table.png")
    ImageResource table();
    @Source("icon_text.png")
    ImageResource text();
    @Source("icon_user_add.png")
    ImageResource userAdd();
    @Source("icon_user_del.png")
    ImageResource userDel();
    
    @Source("icon_app.gif")
    ImageResource design();
    @Source("icon_network.gif")
    ImageResource domain();
    @Source("icon_users.png")
    ImageResource learner();
    @Source("icon_brain.gif")
    ImageResource expert();
    @Source("icon_setting.png")
    ImageResource setting();
    @Source("icon_arrow.png")
    ImageResource item();
  }
  
  public static final Icons ICONS = GWT.create(Icons.class);
  

}
