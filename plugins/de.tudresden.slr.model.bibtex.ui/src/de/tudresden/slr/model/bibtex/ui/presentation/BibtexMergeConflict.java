package de.tudresden.slr.model.bibtex.ui.presentation;

import java.util.*;
import java.util.stream.Collectors;

import de.tudresden.slr.model.bibtex.impl.DocumentImpl;
import de.tudresden.slr.model.bibtex.impl.DocumentImpl.DocumentImplFields;

public class BibtexMergeConflict {
	
	private DocumentImpl resource1;
	private DocumentImpl resource2;
	private DocumentImpl finalResource;
	private Map<String, String> fields;
	private Map<String, Pair<String, String>> duplicatedFields;
	
	public BibtexMergeConflict(DocumentImpl resource1, DocumentImpl resource2) {
		this.resource1 = resource1;
		this.resource2 = resource2;
		fields = new HashMap<>();
		duplicatedFields = new HashMap<>();
		
		for (DocumentImplFields field : DocumentImplFields.values()) {
			String value1 = field.getFunction().apply(resource1);
			String value2 = field.getFunction().apply(resource2);
			
			if (value1 != null)
				fields.put(field.name(), value1);
			if (value1 != null && value2 != null && !value1.equals(value2))
				duplicatedFields.put(field.getName(), new Pair<String, String>(value1, value2));
			else if (value2 != null && value1 == null)
				fields.put(field.getName(), value2);
		}
		
		// additional fields
		for (String field : resource1.getAdditionalFields().keySet().stream().map(key -> key.toLowerCase()).collect(Collectors.toList()))
			fields.put(field, resource1.getAdditionalFields().get(field));
		for (String field : resource2.getAdditionalFields().keySet().stream().map(key -> key.toLowerCase()).collect(Collectors.toList())) {
			if (!fields.containsKey(field))
				fields.put(field, resource2.getAdditionalFields().get(field));
			else if (!fields.get(field).equals(resource2.getAdditionalFields().get(field)))
				duplicatedFields.put(field, 
						new Pair<String, String>(resource1.getAdditionalFields().get(field), 
								resource2.getAdditionalFields().get(field)));
		}		
	}
	
	public String printConflict() {
		String result = "{\n";
		
		// write out all fields
		for (String field : getAllFieldsOrdered()) {
			if (duplicatedFields.containsKey(field))
				result += createBibtexLineForField(field, duplicatedFields.get(field).getFirst()) +
						createBibtexLineForField(field, duplicatedFields.get(field).getSecond());
			else if (fields.containsKey(field))
				result += createBibtexLineForField(field, fields.get(field));
		}
		
		result += "}";
		return result;
	}
	
	private String createBibtexLineForField(String field, String value) {
		return "\t" + field + "={" + value + "}\n";
	}
	
	private List<String> getAllFieldsOrdered() {
		List<String> fieldNames = Arrays.asList(DocumentImplFields.values())
				.stream()
				.map(DocumentImplFields::getName)
				.collect(Collectors.toList());
		for (String fieldName : fields.keySet())
			if (!fieldNames.contains(fieldName.toLowerCase()))
				fieldNames.add(fieldName);
		for (String fieldName : duplicatedFields.keySet())
			if (!fieldNames.contains(fieldName.toLowerCase()))
				fieldNames.add(fieldName);
		return fieldNames;
	}
	
	public List<String> getConflictedFields() {
		List<String> conflictedFields = new ArrayList<>();
		for (String field : getAllFieldsOrdered())
			if (duplicatedFields.containsKey(field))
				conflictedFields.add(field);
		return conflictedFields;
	}
	
	public String getConflictForField(String field) {
		return duplicatedFields.containsKey(field) ? 
			createBibtexLineForField(field, duplicatedFields.get(field).getFirst()) +
				createBibtexLineForField(field, duplicatedFields.get(field).getSecond())
			: null;
	}

	public void setField(String field, boolean first) {
		if (duplicatedFields.isEmpty() || !duplicatedFields.containsKey(field)) {
			System.out.println("WARNING: Set field that was not conflicted: " + field);
			return;
		}
		
		fields.put(field, first ? duplicatedFields.get(field).getFirst() : duplicatedFields.get(field).getSecond());
		duplicatedFields.remove(field);
	}
	
	public DocumentImpl getResource1() {
		return resource1;
	}
	
	public void setResource1(DocumentImpl resource1) {
		this.resource1 = resource1;
	}
	
	public DocumentImpl getResource2() {
		return resource2;
	}
	
	public void setResource2(DocumentImpl resource2) {
		this.resource2 = resource2;
	}
	
	public DocumentImpl getFinalResource() {
		return finalResource;
	}
	
	public void setFinalResource(DocumentImpl finalResource) {
		this.finalResource = finalResource;
	}
	
	public boolean isConflicted() {
		return !duplicatedFields.isEmpty();
	}
}
