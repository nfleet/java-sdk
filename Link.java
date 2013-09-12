/**
 * Created with IntelliJ IDEA.
 * User: japelait
 * Date: 5.4.2013
 * Time: 10:37
 * To change this template use File | Settings | File Templates.
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
