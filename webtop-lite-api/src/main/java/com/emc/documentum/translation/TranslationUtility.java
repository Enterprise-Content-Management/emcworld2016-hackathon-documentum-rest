package com.emc.documentum.translation;

import java.util.ArrayList;

import org.apache.commons.collections4.BidiMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.emc.documentum.filemanager.dtos.DocumentumObject;
import com.emc.documentum.filemanager.dtos.DocumentumProperty;

@Component
public class TranslationUtility {

	@Autowired
	private TranslationMapWrapper mapWrapper;

	@Value("${translation.enabled}")
	boolean enabled;

	public DocumentumObject translateFromRepo(DocumentumObject object) {
		if (enabled) {
			return translate(object, true);
		}

		return object;
	}

	public DocumentumObject translateToRepo(DocumentumObject object) {
		if (enabled) {
			return translate(object, false);
		}
		return object;
	}

	public ArrayList<DocumentumObject> translateFromRepo(ArrayList<DocumentumObject> objectList) {

		if (enabled) {
			for (DocumentumObject docObject : objectList) {

				translate(docObject, true);
			}
		}

		return objectList;
	}

	public ArrayList<DocumentumObject> translateToRepo(ArrayList<DocumentumObject> objectList) {
		if (enabled) {
			for (DocumentumObject docObject : objectList) {

				translate(docObject, false);
			}
		}

		return objectList;
	}

	private DocumentumObject translate(DocumentumObject object, boolean directionFromRepo) {
		ArrayList<DocumentumProperty> objectProperties = object.getProperties();
		String translation = null;
		for (int i = 0; i < objectProperties.size(); i++) {
			//todo: remove core rest identifier
			BidiMap<String, String> map = mapWrapper.bidiMapsMap.get("corerest" + ".mapping.properties");
			if (directionFromRepo) {
				translation = map.get(objectProperties.get(i).getLocalName());
			} else {
				translation = map.getKey(objectProperties.get(i).getLocalName());
			}

			if (translation == null) {
				translation = objectProperties.get(i).getLocalName();
			}

			// set the translation in object ...
			objectProperties.get(i).setLocalName(translation);
		}
		return object;
	}
}