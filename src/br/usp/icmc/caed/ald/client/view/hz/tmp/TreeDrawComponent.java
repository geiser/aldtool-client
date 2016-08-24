package br.usp.icmc.caed.ald.client.view.hz.tmp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.sencha.gxt.chart.client.draw.DrawComponent;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.path.LineTo;
import com.sencha.gxt.chart.client.draw.path.MoveTo;
import com.sencha.gxt.chart.client.draw.path.PathSprite;
import com.sencha.gxt.chart.client.draw.sprite.EllipseSprite;
import com.sencha.gxt.chart.client.draw.sprite.RectangleSprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;
import com.sencha.gxt.widget.core.client.event.CollapseItemEvent;
import com.sencha.gxt.widget.core.client.event.CollapseItemEvent.CollapseItemHandler;
import com.sencha.gxt.widget.core.client.event.ExpandItemEvent;
import com.sencha.gxt.widget.core.client.event.ExpandItemEvent.ExpandItemHandler;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;
import com.sencha.gxt.widget.core.client.tree.Tree;

public class TreeDrawComponent<M> extends DrawComponent implements CollapseItemHandler<M>, SelectionChangedHandler<M>, ExpandItemHandler<M> {

	public interface LabelProvider<M> {
		/**
		 * This function must have a list of values that be used to generate
		 * the labels in the way or func node. 
		 */
		public List<String> getValue(M model);

		/**
		 * This function must have a string that must be used to indicate the
		 * kind of each node. In SMARTIES, it's defined as #def property  
		 */
		public String getDef(M model);
	}

	public abstract class NodeView {
		public static final double wideS = 3.0;
		public static final double defaultS = 1.0;
		public static final String font = "Sans-Serif";

		public double x=0, y=0;
		public boolean open = true;
		public boolean visible = true;
		public boolean selected = false;
		public RGB image_color = RGB.BLACK;

		public final M model;

		public NodeView(M model) {
			this.model = model;
		}

		public String toJson() {
			String toReturn = "{\"x\": \""+this.x+"\", \"y\": \""+this.y+"\"";
			toReturn += ", \"open\": \""+this.open+"\"";
			toReturn += ", \"visible\": \""+this.visible+"\"";
			toReturn += ", \"selected\": \""+this.selected+"\"";
			toReturn += ", \"image_color\": \""+this.image_color.toString()+"\"";
			if (this.model!=null) {
				toReturn += ", \"model\": \""+this.model.toString()+"\"";
			}
			return toReturn + "}";
		}

		@Override
		public String toString() {
			return "{\"NodeView\": "+this.toJson()+"}";
		}

	}

	public class WayView extends NodeView {
		public static final int way_w = 10;
		public static final int way_h = 10;
		public static final int fontsize = 8;

		public final FuncView main_func;
		public final List<FuncView> sub_funcs = new ArrayList<FuncView>();

		public WayView(M model, FuncView main_func) {
			super(model);
			this.main_func = main_func;
			this.open = tree.isExpanded(model);
			Iterator<M> it = tree.getSelectionModel().getSelectedItems().iterator();
			while (!this.selected && it.hasNext()) {
				if (it.next().equals(model)) this.selected = true;
			}
			//..iteration in child
			if (!tree.isLeaf(model) && tree.getStore().hasChildren(model)) {
				for (M child : tree.getStore().getChildren(model)) {
					FuncView func = new FuncView(child);
					func.parent = this;
					this.sub_funcs.add(func);
				}
			}
		}

		@Override
		public String toString() {
			return "{\"WayView\": "+super.toJson()+"}";
		}

		public void setWayLoc(int level) {
			if (!this.sub_funcs.isEmpty()) {
				double sub_funcs_x=0;
				for (FuncView func : this.sub_funcs) sub_funcs_x += func.x;
				this.x = sub_funcs_x/this.sub_funcs.size();
			} else {
				this.visible = false;
				this.x = this.main_func.x;
			}
			this.y = (FuncView.diff_func_h*level+FuncView.diff_func_h*(level+1))/2;
		}

		public void paint1(double offset_x, double offset_y, DrawComponent draw) {
			if (this.visible) this.paint1_body(offset_x, offset_y, draw);
		}

		private void paint1_body(double offset_x, double offset_y, DrawComponent draw) {
			double X = this.x+offset_x;
			double Y = this.y+offset_y;

			FuncView func = this.sub_funcs.get(0);

			PathSprite psWay = new PathSprite();
			psWay.addCommand(new MoveTo(X, Y));
			psWay.addCommand(new LineTo(this.main_func.x+offset_x, this.main_func.y+FuncView.func_h+offset_y));
			for (int j=0; j<this.sub_funcs.size(); j++) {
				func = this.sub_funcs.get(j);
				psWay.addCommand(new MoveTo(X, Y));
				psWay.addCommand(new LineTo(func.x+offset_x, func.y-FuncView.func_h+offset_y));
			}
			psWay.setStrokeWidth(defaultS);
			psWay.setStroke(this.image_color);
			if (this.selected) {
				psWay.setStroke(RGB.RED);
				psWay.setStrokeWidth(wideS);
			}
			draw.addSprite(psWay);

			RectangleSprite rsWay = new RectangleSprite(way_w, way_h, X-way_w/2, Y-way_h/2);
			rsWay.setFill(this.image_color);
			rsWay.setStroke(this.image_color);
			rsWay.setStrokeWidth(defaultS);
			if (this.selected) {
				rsWay.setStroke(RGB.RED);
				rsWay.setStrokeWidth(wideS);
			}
			draw.addSprite(rsWay);

			List<String> labels = lpWay.getValue(model);
			double diff_t = 96*((double)fontsize/72);
			double top_y = Y-(labels.size()*diff_t/2);
			for (int i=0; i<labels.size(); i++) {
				String label = labels.get(i);
				TextSprite tsLabel = new TextSprite(label);
				tsLabel.setY(top_y+i*diff_t);
				tsLabel.setX(X+way_w+diff_t/4);
				tsLabel.setFont(font);
				tsLabel.setFontSize(fontsize);
				draw.addSprite(tsLabel);
			}

			// ードの右下に出てくるところの処理, 文字列の先頭がaかどうか調べる
			TextSprite tsDef = new TextSprite(lpWay.getDef(model));
			tsDef.setY(Y-diff_t/2);//+(diff_t/2));
			tsDef.setFont(font);
			tsDef.setFontSize(fontsize);
			tsDef.setX(X-FuncView.diff_func_w/2);
			draw.addSprite(tsDef);
		}
	}

	public class FuncView extends NodeView {
		public static final int func_h = 30; //78;	//60
		public static final int func_w = 40; //104;	//80
		public static final int fontsize = 10;
		public static final String font = "Sans-Serif";
		public static final double diff_func_h = func_h*4;
		public static final double diff_func_w = func_w*2.5;

		public WayView parent = null;
		public final List<WayView> ways = new ArrayList<WayView>(); 

		public FuncView(M model) {
			super(model);
			this.image_color = RGB.CYAN;
			this.open = tree.isExpanded(model);
			Iterator<M> it = tree.getSelectionModel().getSelectedItems().iterator();
			while (!this.selected && it.hasNext()) {
				if (it.next().equals(model)) this.selected = true;
			}
			//..iteration in child
			if (tree.isExpanded(model) && !tree.isLeaf(model)) {
				for (M child : tree.getStore().getChildren(model)) {
					this.ways.add(new WayView(child, this));
				}
			}
		}

		@Override
		public String toString() {
			return "{\"FuncView\": "+super.toJson()+"}";
		}

		public FuncView setRootLoc() {
			this.setChildLoc2(0, func_w);
			return this;
		}

		private double setChildLoc2(int level, double par_x) {
			this.y = level*diff_func_h;
			if (this.ways.isEmpty()) {
				this.x = par_x;
				if (!this.visible) return par_x;
				else return par_x+diff_func_w; // par_x+=(func_w+func_w/10);
			} else { //...
				for (WayView way : this.ways) {
					if (!way.sub_funcs.isEmpty()) {
						if (way.open) {
							for (FuncView subFunc : way.sub_funcs) {
								subFunc.setVisibleAll(true);
								par_x = subFunc.setChildLoc2(level+1, par_x);
							}
						} else {
							double spaceD = diff_func_w/way.sub_funcs.size();
							for (int i=0; i<way.sub_funcs.size(); i++){
								FuncView subFunc = way.sub_funcs.get(i);
								subFunc.x = par_x+(i*spaceD);
								subFunc.y = (level+1)*diff_func_h;
								subFunc.setVisibleAllChildren(false);
							}
							par_x = par_x+diff_func_w+diff_func_w/2; //par_x += this.getNodeWidth()*11/10;//par_x += func_w+func_w/10;
						}
						way.setWayLoc(level);
					} else {
						way.visible = false;
						way.open = false;
					}
				}

				WayView firstWay = null;
				int i=0;
				while (i<this.ways.size() && firstWay==null) {
					if (this.ways.get(i).visible && !this.ways.get(i).sub_funcs.isEmpty()) {
						firstWay = this.ways.get(i);
					} i++;
				}
				WayView lastWay = null;
				int j=this.ways.size()-1;
				while (i>=0 && lastWay==null) {
					if (this.ways.get(j).visible && !this.ways.get(j).sub_funcs.isEmpty()) {
						lastWay = this.ways.get(j);
					} i--;
				}
				if (firstWay!=null && lastWay!=null) {
					this.x = (lastWay.x+firstWay.x)/2; //+WayView.way_w/2-func_w/2;
				} else {
					this.x = par_x;
					par_x += diff_func_w; //par_x += (func_w + func_w / 10);
				}
				return par_x;
			}
		}

		public void setVisibleAll(boolean visible) {
			this.visible = visible;
			this.setVisibleAllChildren(visible);
		}

		public void setVisibleAllChildren(boolean visible) { 
			this.setVisibleSubWays(visible);
			this.setVisibleSubFuncs(visible);
		}

		public void setVisibleSubWays(boolean visible) {
			for(int i=0; i<this.ways.size(); i++){
				WayView way = this.ways.get(i);
				way.visible = visible;
			}
		}

		public void setVisibleSubFuncs(boolean visible) {
			List<FuncView> children = new ArrayList<FuncView>();
			for (WayView way : this.ways) {
				children.addAll(way.sub_funcs);
			}
			for(int i=0; i<children.size(); i++){
				FuncView subFunc = children.get(i);
				subFunc.setVisibleAll(visible);
			}
		}

		public void paintTree1(double offset_x, double offset_y, DrawComponent draw) {
			this.paint1(offset_x, offset_y, draw);
			if (this.open) {
				List<FuncView> children = new ArrayList<FuncView>(); //this.getChildren();
				for (WayView way : this.ways) children.addAll(way.sub_funcs);
				for (int i=0; i<children.size(); i++) {
					FuncView node = children.get(i);
					node.paintTree1(offset_x, offset_y, draw); // children_width+=node.getWidth();
				}
			}
		}

		private void paint1(double offset_x, double offset_y, DrawComponent draw) {
			double X = this.x+offset_x;
			double Y = this.y+offset_y;
			double width = TreeDrawComponent.this.getSurface().getWidth();
			double height = TreeDrawComponent.this.getSurface().getHeight();

			if (this.visible) { // && X+func_w>0 && Y+func_h>0 && X<width && Y<height) {
				this.paint1_body(offset_x, offset_y, draw);
			}
			this.paint1_parentWay(offset_x, offset_y, draw);
		}

		private void paint1_parentWay(double offset_x, double offset_y, DrawComponent draw) {
			if (this.parent!=null) {
				WayView way = this.parent;
				if (way.sub_funcs!=null && way.sub_funcs.get(way.sub_funcs.size()-1)!=null) {
					FuncView last = way.sub_funcs.get(way.sub_funcs.size()-1);
					if (this.equals(last)) {
						way.paint1(offset_x, offset_y, draw);
					}
				}
			}
		}

		// #Hayashi 060624, visible判定を入れるためにメソッド名変更
		protected void paint1_body(double offset_x, double offset_y, DrawComponent draw){
			double X = this.x+offset_x;
			double Y = this.y+offset_y;

			EllipseSprite esNode = new EllipseSprite(func_w, func_h, X, Y);
			esNode.setStroke(RGB.BLACK);
			esNode.setFill(this.image_color);
			esNode.setStrokeWidth(defaultS);
			if (this.selected) {
				esNode.setStroke(RGB.RED);
				esNode.setStrokeWidth(wideS);
			}
			draw.addSprite(esNode);

			List<String> labels = lpFunc.getValue(model);
			double diff_t = 96*((double)fontsize/72);
			double top_y = Y-(labels.size()*diff_t/2);
			for (int i=0; i<labels.size(); i++) {
				String label = labels.get(i);
				TextSprite tsLabel = new TextSprite(label);
				tsLabel.setY(top_y+i*diff_t);
				tsLabel.setX(X-func_w+diff_t/4);
				tsLabel.setFont(font);
				tsLabel.setFontSize(fontsize);
				draw.addSprite(tsLabel);
			}

			// ードの右下に出てくるところの処理, 文字列の先頭がaかどうか調べる
			TextSprite tsDef = new TextSprite(lpFunc.getDef(model));
			tsDef.setX(X-func_w-diff_t/4);
			tsDef.setY(Y+func_h);//+(diff_t/2));
			tsDef.setFont(font);
			tsDef.setFontSize(fontsize);
			draw.addSprite(tsDef);
		}

	}

	public final Tree<M, ?> tree;
	public final LabelProvider<M> lpWay;
	public final LabelProvider<M> lpFunc;

	public TreeDrawComponent(Tree<M, ?> tree, LabelProvider<M> lpWay, LabelProvider<M> lpFunc) {
		this.tree = tree;
		this.lpWay = lpWay;
		this.lpFunc = lpFunc;
		this.tree.addCollapseHandler(this);
		this.tree.addExpandHandler(this);
		this.tree.getSelectionModel().addSelectionChangedHandler(this);
	}

	protected M getRootModel(M model) {
		M parentModel = null, rootModel = model;
		do {
			parentModel = this.tree.getStore().getParent(rootModel);
			if (parentModel!=null) rootModel = parentModel;
		} while (parentModel!=null);
		return rootModel;
	}

	private void refreshSurface() {
		if (this.tree.getSelectionModel().getSelectedItems().size()>0) {
			this.clearSurface();
			M selectedModel = this.tree.getSelectionModel().getSelectedItem();
			M rootModel = this.getRootModel(selectedModel);
			FuncView vir_root = this.new FuncView(rootModel).setRootLoc();

			double offset_x = 10;//FuncView.func_w;
			double offset_y = FuncView.func_h+10;//FuncView.func_h;
			vir_root.paintTree1(offset_x, offset_y, this);

			RectangleSprite rec = new RectangleSprite(1022, 590, 2, 2);
			rec.setStroke(RGB.GREEN);
			rec.setFill(RGB.NONE);
			rec.setStrokeWidth(2.0);
			this.addSprite(rec);
			this.setWidth(1024);

			this.redrawSurfaceForced();
		}
	}

	@Override
	public void onCollapse(CollapseItemEvent<M> event) {
		M rootModel = this.getRootModel(event.getItem());
		M selectedModel = this.tree.getSelectionModel().getSelectedItem();
		if (selectedModel!=null &&
				rootModel.equals(this.getRootModel(selectedModel))) {
			this.refreshSurface();
		}
	}

	@Override
	public void onExpand(ExpandItemEvent<M> event) {
		M rootModel = this.getRootModel(event.getItem());
		M selectedModel = this.tree.getSelectionModel().getSelectedItem();
		if (selectedModel!=null &&
				rootModel.equals(this.getRootModel(selectedModel))) {
			this.refreshSurface();
		}
	}

	@Override
	public void onSelectionChanged(SelectionChangedEvent<M> event) {
		this.refreshSurface();
	}

}
