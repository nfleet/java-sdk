package fi.cosky.sdk;

import java.util.HashMap;

public class ObjectCache {
	private HashMap<String, Object> relationVersion;
		
	public ObjectCache() {
		relationVersion = new HashMap<>();
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
