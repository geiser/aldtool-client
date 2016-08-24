package br.usp.icmc.caed.ald.client.data.tmp;

public class PropertyModel {
	
	public static final class NAME {
		public static final String HAS_SKILL = "hasSkill";
		public static final String HAS_KNOWLEDGE = "hasKnowledge";
		public static final String HAS_ATTITUDE = "hasAttitude";
		public static final String HAS_COMPETENCY = "hasCompetency";
		public static final String HAS_PREREQUISITE = "hasPrerequisite";
		public static final String HAS_LEARNING_OBJECTIVE = "hasLearningObjective";
		public static final String HAS_MOTIVATION = "hasMotivation";
		public static final String HAS_ANXIETY = "hasAnxiety";
		public static final String HAS_ALREADY_SEEN = "hasAlreadySeen";
		public static final String HAS_PERSONALITY = "hasPersonality";
		public static final String HAS_CL_EXPERIENCE = "hasCLExperience";
		public static final String HAS_DIFFICULTY = "hasDifficulty";
		public static final String HAS_EDUCATIONAL_LEVEL = "hasEducationalLevel";
		public static final String HAS_CONTEXT = "hasContext";
	};
	
	private String name;
	private MetadataModel dest;
	private String value;
	
	public PropertyModel(String name, MetadataModel dest, String value) {
		this.setName(name);
		this.setDest(dest);
		this.setValue(value);
	}
	
	public PropertyModel(String name, MetadataModel dest) {
		this(name, dest, null);
	}
	
	public PropertyModel(String name, String value) {
		this(name, null, value);
	}
	
	public PropertyModel setName(String name) {
		this.name = name;
		return this;
	}
	
	public String getName() {
		return this.name;
	}
	
	public PropertyModel setDest(MetadataModel dest) {
		this.dest = dest;
		return this;
	}
	
	public MetadataModel getDest() {
		return this.dest;
	}
	
	public PropertyModel setValue(String value) {
		this.value=value;
		return this;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public String toJson() {
		String toReturn = "{\"name\": '"+this.name+"'";
		if (this.value!=null) {
			if (this.dest!=null) toReturn += ", \"dest\": \""+this.dest.getId()+"\"";
			toReturn += ", \"value\": \""+this.value+"\"";
		} else {
			toReturn += ", \"value\": \""+this.dest.getId()+"\"";
		}
		return toReturn + "}";
	}
	
	@Override
	public String toString() {
		return "{\"Property\": "+this.toJson()+"}";
	}
	
	@Override
	public int hashCode() {
		int toReturn = 1;
		if (this.name != null) toReturn *= this.name.hashCode();
		if (this.dest != null) toReturn *= this.dest.hashCode();
		if (this.value != null) toReturn *= this.value.hashCode();
		return toReturn;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj==this) return true;
		if (!(obj instanceof PropertyModel) || (obj==null)) return false;
		PropertyModel _obj = (PropertyModel) obj;
		if (this.name!=null && !this.name.equals(_obj.getName())) return false;
		if (this.dest!=null && !this.dest.equals(_obj.getDest())) return false;
		if (this.value!=null && !this.value.equals(_obj.getValue())) return false;
		if (_obj.getName()!=null && !_obj.getName().equals(this.name)) return false;
		if (_obj.getDest()!=null && !_obj.getDest().equals(this.dest)) return false;
		if (_obj.getValue()!=null && !_obj.getValue().equals(this.value)) return false;
		return true;
	}
	
}
