package fi.cosky.sdk;
/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

public class Link {
    private String Uri;
    private String Method;
    private String Rel;
    private String Type;
    private boolean Enabled;

    @Deprecated
    public Link(String rel, String uri, String method) {
    	this.Uri = uri;
    	this.Rel = rel;
    	this.Method = method;
    	this.Enabled = true;
    }

    public Link(String rel, String uri, String method, String type) {
        this.Uri = uri;
        this.Method = method;
        this.Rel = rel;
        this.Type = type;
        this.Enabled = true;
    }
    public Link(String rel, String uri, String method,String type, boolean enabled) {
        this.Uri = uri;
        this.Method = method;
        this.Rel = rel;
        this.Type = type;
        this.Enabled = enabled;
    }

    public String getUri() {
        return this.Uri;
    }

    @Override
    public String toString() {
        return "Link{" +
                "Uri='" + Uri + '\'' +
                ", Method='" + Method + '\'' +
                ", Rel='" + Rel + '\'' +
                ", Enabled=" + Enabled +
                ", Type=" + Type +
                '}';
    }

    public String getMethod() {
        return this.Method;
    }

    public void setUri(String uri) {
        Uri = uri;
    }

    public boolean isEnabled() {
        return Enabled;
    }

    public void setEnabled(boolean enabled) {
        Enabled = enabled;
    }

    public void setMethod(String method) {
        Method = method;
    }

    public void setRel(String rel) {
        Rel = rel;
    }

    public String getRel() {
        return this.Rel;

    }
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
	}
}
