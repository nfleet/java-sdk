package fi.cosky.sdk;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

import java.util.HashMap;

public class ObjectCache {
	private HashMap<String, Object> relationVersion;
		
	public ObjectCache() {
		relationVersion = new HashMap<String, Object>();
	}
	
	/**
	 * Call after calling containsUri
	 * @return object contained in cache.
	 * 		   
	 */
	Object getObject(String uri) {
		return relationVersion.get(uri);
	}
	
	void addUri(String uri, Object object) {
		if (containsUri(uri)) updateVersion(uri, object);
		else relationVersion.put(uri, object);
	}
	
	void updateVersion(String uri, Object object) {
		relationVersion.remove(uri);
		relationVersion.put(uri, object);
	}
	
	boolean containsUri(String uri) {
		return relationVersion.containsKey(uri);
	}
}
