package br.usp.icmc.caed.ald.client.data.tmp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MetadataModel {
	
	public static final class TYPE {
		public static final String LEARNER = "Learner";
		public static final String SKILL = "Skill";
		public static final String KNOWLEDGE = "Knowledge";
		public static final String COMPETENCY = "Competency";
		public static final String LEARNING_MATERIAL = "LearningMaterial";
		public static final String UoL = "UoL";
	};
	
	public static final class RESOURCE_TYPE {
		public static final String IN_URL = "InURL";
		public static final String IN_DATASTORE = "InDataStore";
	}
	
	private String id;
	private String type;
	private String label;
	private String parentId;
	private boolean hasChildren = false;
	private String resourceType;
	private String url;
	private ResourceModel resource;

	private final Set<String> types = new HashSet<String>();
	private final Set<RelationModel> relations = new HashSet<RelationModel>();
	private final Set<PropertyModel> properties = new HashSet<PropertyModel>();
	
	public MetadataModel setId(String id) {
		this.id = id;
		return this;
	}

	public String getId() {
		return this.id;
	}	

	public MetadataModel setType(String type) {
		this.type = type;
		this.types.clear();
		this.types.add(type);
		return this;
	}

	public String getType() {
		return this.type;
	}

	public MetadataModel setLabel(String label) {
		this.label = label;
		return this;
	}

	public String getLabel() {
		return this.label;
	}

	public String getParentId() {
		return parentId;
	}

	public MetadataModel setParentId(String parentId) {
		this.parentId = parentId;
		return this;
	}

	public boolean hasChildren() {
		return hasChildren;
	}

	public MetadataModel setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
		return this;
	}

	public MetadataModel setResourceType(String resourceType) {
		this.resourceType = resourceType;
		return this;
	}

	public String getResourceType() {
		return this.resourceType;
	}

	public MetadataModel setUrl(String url) {
		this.url = url;
		return this;
	}

	public String getUrl() {
		return this.url;
	}

	public MetadataModel setResource(ResourceModel resource) {
		this.resource = resource;
		return this;
	}

	public ResourceModel getResource() {
		return this.resource;
	}
	
	public MetadataModel addType(String type) {
		this.types.add(type);
		return this;
	}
	
	public MetadataModel addTypes(Collection<String> types) {
		this.types.addAll(types);
		return this;
	}
	
	public MetadataModel setTypes(String type, Collection<String> types) {
		this.type = type;
		this.types.clear();
		this.types.addAll(types);
		return this;
	}
	
	public Set<String> getTypes() {
		return this.types;
	}

	public MetadataModel addProperty(PropertyModel property) {
		this.properties.add(property);
		return this;
	}

	public MetadataModel addProperties(Collection<PropertyModel> properties) {
		this.properties.addAll(properties);
		return this;
	}

	public MetadataModel setProperties(Collection<PropertyModel> properties) {
		this.properties.clear();
		this.properties.addAll(properties);
		return this;
	}

	public Set<PropertyModel> getProperties() {
		return this.properties;
	}

	public Set<PropertyModel> getProperties(String name) {
		Set<PropertyModel> toReturn = new HashSet<PropertyModel>();
		for (PropertyModel prop : this.properties) {
			if (name.equals(prop.getName())) { toReturn.add(prop); } 
		}
		return toReturn;
	}

	public Map<String, List<PropertyModel>> getMapProperties() {
		Map<String, List<PropertyModel>> result = new HashMap<String, List<PropertyModel>>();
		for (PropertyModel prop : this.properties) {
			List<PropertyModel> _properties = result.get(prop.getName());
			if (_properties==null) { _properties = new ArrayList<PropertyModel>(); }
			_properties.add(prop);
			result.put(prop.getName(), _properties);
		}
		return result;
	}

	public MetadataModel addRelation(RelationModel relation) {
		this.relations.add(relation);
		return this;
	}

	public MetadataModel addRelations(Collection<RelationModel> relations) {
		this.relations.addAll(relations);
		return this;
	}

	public MetadataModel setRelations(Collection<RelationModel> relations) {
		this.relations.clear();
		this.relations.addAll(relations);
		return this;
	}

	public Set<RelationModel> getRelations() {
		return this.relations;
	}

	public Set<RelationModel> getRelations(String name) {
		Set<RelationModel> toReturn = new HashSet<RelationModel>();
		for (RelationModel relation : this.relations) {
			if (name.equals(relation.getName())) { toReturn.add(relation); } 
		}
		return toReturn;
	}

	public MetadataModel addIsVariantOf(MetadataModel metadata) {
		RelationModel relation = new RelationModel("isVariantOf", metadata);
		if (!this.relations.contains(relation)) { this.addRelation(relation); }
		return this;
	}

	public MetadataModel setIsVariantOf(Collection<MetadataModel> metadatas) {
		Set<RelationModel> variants = new HashSet<RelationModel>();
		for (MetadataModel metadata : metadatas) {
			variants.add(new RelationModel("isVariantOf", metadata));
		}
		this.addRelations(variants);
		return this;
	}

	public Set<MetadataModel> getIsVariantOf() {
		Set<MetadataModel> toReturn = new HashSet<MetadataModel>();
		Set<RelationModel> _relations = this.getRelations("isVariantOf");
		for (RelationModel relation : _relations) {
			toReturn.add(relation.getDest());
		}
		return toReturn;
	}

	public MetadataModel addIsRequiredBy(MetadataModel metadata) {
		RelationModel relation = new RelationModel("isRequiredBy", metadata);
		if (!this.relations.contains(relation)) { this.addRelation(relation); }
		return this;
	}

	public MetadataModel setIsRequiredBy(Collection<MetadataModel> metadatas) {
		Set<RelationModel> requires = new HashSet<RelationModel>();
		for (MetadataModel metadata : metadatas) {
			requires.add(new RelationModel("isRequiredBy", metadata));
		}
		this.addRelations(requires);
		return this;
	}

	public Set<MetadataModel> getIsRequiredBy() {
		Set<MetadataModel> toReturn = new HashSet<MetadataModel>();
		Set<RelationModel> _relations = this.getRelations("isRequiredBy");
		for (RelationModel relation : _relations) {
			toReturn.add(relation.getDest());
		}
		return toReturn;
	}

	public String toJson() {
		String toReturn = "{\"id\": \"" + this.id + "\"";
		if (this.type!=null) toReturn += ", \"type\": \""+this.type+"\"";
		if (this.label!=null) toReturn += ", \"label\": \""+this.label+"\"";
		if (this.resourceType!=null) toReturn += ", \"resourceType\": \""+this.resourceType+"\"";
		if (this.url!=null) toReturn += ", \"url\": \""+this.url+"\"";
		if (this.resource!=null) toReturn += ", \"resource\": "+this.resource.toJson();

		if (this.types.size()>0) toReturn += ", \"types\": [";
		for (String type : this.types) {
			toReturn += "\""+type+ "\",";
		}
		if (this.types.size()>0) toReturn += "]";
		
		if (this.properties.size()>0) toReturn += ", \"properties\": [";
		for (PropertyModel property : this.properties) {
			toReturn += property.toJson() + ",";
		}
		if (this.properties.size()>0) toReturn += "]";
		
		if (this.relations.size()>0) toReturn += ", \"relations\": [";
		for (RelationModel relation : this.relations) {
			toReturn += relation.toJson() + ",";
		}
		if (this.relations.size()>0) toReturn += "]";
		return toReturn + "}";
	}

	@Override
	public String toString() {
		return "{\"Metadata\": "+this.toJson().toString()+"}";
	}

	@Override
	public int hashCode() {
		int toReturn = 1;
		if (this.id!=null) return this.id.hashCode();
		if (this.type!=null) toReturn *= this.type.hashCode();
		if (this.label!=null) toReturn *= this.label.hashCode();
		if (this.resourceType!=null) toReturn *= this.resourceType.hashCode();
		if (this.url!=null) toReturn *= this.url.hashCode();
		for (PropertyModel property : this.properties) {
			toReturn *= property.hashCode();
		}
		for (RelationModel relation : this.relations) {
			toReturn *= relation.hashCode();
		}
		return toReturn;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) { return true; }
		if (!(obj instanceof MetadataModel) || (obj == null)) { return false; }
		MetadataModel _obj = (MetadataModel) obj;
		if (this.id!=null && _obj.getId()!=null && this.id.equals(_obj.getId())) return true; 

		if (this.id!=null && !this.id.equals(_obj.getId())) return false;
		if (this.type!=null && !this.type.equals(_obj.getType())) return false;
		if (this.label!=null && !this.label.equals(_obj.getLabel())) return false;
		if (this.resourceType!=null && !this.resourceType.equals(_obj.getResourceType())) return false;
		if (this.url!=null && !this.url.equals(_obj.getUrl())) return false;
		if (this.resource!=null && !this.resource.equals(_obj.getResource())) return false;

		if (!this.relations.equals(_obj.getRelations())) return false;
		if (!this.properties.equals(_obj.getProperties())) return false;

		if (_obj.getId()!=null && !_obj.getId().equals(this.id)) return false;
		if (_obj.getType()!=null && !_obj.getType().equals(this.type)) return false;
		if (_obj.getLabel()!=null && !_obj.getLabel().equals(this.label)) return false;
		if (_obj.getResourceType()!=null && !_obj.getResourceType().equals(this.resourceType)) return false;
		if (_obj.getUrl()!=null && !_obj.getUrl().equals(this.url)) return false;
		if (_obj.getResource()!=null && !_obj.getResource().equals(this.resource)) return false;
		return true;
	}

}