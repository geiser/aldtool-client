package br.usp.icmc.caed.ald.client.data.tmp;

public class ResourceModel {

	private String id;
	private String title;
	private String body;

	public ResourceModel() {
	}

	public ResourceModel(String id, String title, String body) {
		this.setId(id);
		this.setTitle(title);
		this.setBody(body);
	}

	public ResourceModel setId(String id) {
		this.id = id;
		return this;
	}

	public String getId() {
		return this.id;
	}

	public ResourceModel setTitle(String title) {
		this.title = title;
		return this;
	}

	public String getTitle() {
		return this.title;
	}

	public ResourceModel setBody(String body) {
		this.body = body;
		return this;
	}

	public String getBody() {
		return this.body;
	}

	public String toJson() {
		String toReturn ="{\"id\": \""+this.id+"\"";
		if (this.title!=null) toReturn += ", \"title\": \""+this.title+"\"";
		if (this.body!=null) toReturn += ", \"body\": \""+this.body+"\"";
		return toReturn+"}";
	}
	
	@Override
	public String toString() {
		return "{\"resource\": "+this.toJson()+"}";
	}
	
	@Override
	public int hashCode() {
		int toReturn = 1;
		if (this.id!=null) return this.id.hashCode();
		if (this.title!=null) toReturn *= this.title.hashCode();
		if (this.body!=null) toReturn *= this.body.hashCode();
		return toReturn;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) { return true; }
		if (!(obj instanceof ResourceModel) || (obj == null)) { return false; }
		ResourceModel _obj = (ResourceModel) obj;
		if (this.id!=null && _obj.getId()!=null && this.id.equals(_obj.getId())) return true;
		
		if (this.id!=null && !this.id.equals(_obj.getId())) return false;
		if (this.title!=null && !this.title.equals(_obj.getTitle())) return false;
		if (this.body!=null && !this.body.equals(_obj.getBody())) return false;
		
		if (_obj.getId()!=null && !_obj.getId().equals(this.id)) return false;
		if (_obj.getTitle()!=null && !_obj.getTitle().equals(this.title)) return false;
		if (_obj.getBody()!=null && !_obj.getBody().equals(this.body)) return false;
		return true;
	}
	
}
