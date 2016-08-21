package br.usp.icmc.caed.ald.client.ioc;

import com.google.gwt.i18n.client.Constants;

public interface InjectConstants extends Constants {
  
  @DefaultStringValue("id")
  String hzIDKey();

  @DefaultStringValue("label")
  String hzLabelKey();

  @DefaultStringValue("type")
  String hzTypeKey();

  @DefaultStringValue("upper-concept")
  String hzUpperKey();

  @DefaultStringValue("role")
  String hzRoleKey();

  @DefaultStringValue("value")
  String hzValueKey();

  @DefaultStringValue("description")
  String hzDescriptionKey();

  @DefaultStringValue("slots")
  String hzSlotsKey();

  @DefaultStringValue("constraints")
  String hzConstraintsKey();

  @DefaultStringValue("constraint")
  String hzConstraintKey();

  @DefaultStringValue("card-max")
  String hzCardinalityMaxKey();

  @DefaultStringValue("ont")
  String hzOntologyURL();

  @DefaultStringValue("tree")
  String hzTreeURL();

  @DefaultStringValue("mdl/instances")
  String hzInstancesURL();

  @DefaultStringValue("data")
  String hzDataKey();

  @DefaultStringValue("meta")
  String hzMetadataKey();

  @DefaultStringValue("hz-api")
  String hzApiURL();

  @DefaultStringValue("mdl")
  String hzModelURL();

  @DefaultStringValue("card-min")
  String hzCardinalityMinKey();

  @DefaultStringValue("ontology-object")
  String hzOntoObjKey();

  @DefaultStringValue("ontology-object")
	String hzOntologyObjectKey();
  
  @DefaultStringValue("isConcept")
  String isConcept();
  
  @DefaultStringValue("isSlot")
  String isSlot();
  
}
