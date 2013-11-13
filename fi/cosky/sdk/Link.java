package fi.cosky.sdk;
/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

public class Link {
    private String Uri;
    private String Method;
    private String Rel;
    private boolean Enabled;


    public Link(String rel, String uri, String method) {
        this.Uri = uri;
        this.Method = method;
        this.Rel = rel;
        this.Enabled = true;
    }
    public Link(String rel, String uri, String method, boolean enabled) {
        this.Uri = uri;
        this.Method = method;
        this.Rel = rel;
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
}
