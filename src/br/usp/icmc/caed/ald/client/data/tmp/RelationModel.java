package br.usp.icmc.caed.ald.client.data.tmp;

public class RelationModel {

	private String name;
	private MetadataModel dest;

	public RelationModel(String name, MetadataModel dest) {
		this.setName(name);
		this.setDest(dest);
	}

	public RelationModel setName(String name) {
		this.name = name;
		return this;
	}

	public String getName() {
		return this.name;
	}

	public RelationModel setDest(MetadataModel dest) {
		this.dest = dest;
		return this;
	}

	public MetadataModel getDest() {
		return this.dest;
	}

	public String toJson() {
		String toReturn = "{\"name\": '"+this.name+"'";
		if (this.dest!=null) toReturn += ", \"dest\": \""+this.dest.getId()+"\"";
		return toReturn+"}";
	}

	@Override
	public String toString() {
		return "{\"relation\": "+this.toJson()+"}";
	}
	
	@Override
	public int hashCode() {
		int toReturn = 1;
		if (this.name != null) toReturn *= this.name.hashCode();
		if (this.dest != null) toReturn *= this.dest.hashCode();
		return toReturn;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj==this) return true;
		if (!(obj instanceof PropertyModel) || (obj==null)) return false;
		PropertyModel _obj = (PropertyModel) obj;
		if (this.name!=null && !this.name.equals(_obj.getName())) return false;
		if (this.dest!=null && !this.dest.equals(_obj.getDest())) return false;
		if (_obj.getName()!=null && !_obj.getName().equals(this.name)) return false;
		if (_obj.getDest()!=null && !_obj.getDest().equals(this.dest)) return false;
		return true;
	}

}
