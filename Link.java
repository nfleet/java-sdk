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

    public Link(String rel, String uri, String method) {
        this.Uri = uri;
        this.Method = method;
        this.Rel = rel;
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
                '}';
    }

    public String getMethod() {
        return this.Method;
    }

    public String getRel() {
        return this.Rel;
    }
}
