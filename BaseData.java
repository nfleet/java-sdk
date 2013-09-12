import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: japelait
 * Date: 8.4.2013
 * Time: 12:09
 * To change this template use File | Settings | File Templates.
 */
public class BaseData {
    private List<Link> Meta;
    private Link self;
   // private int UserId = 1;

    public Link getLink(String key) {
        if (self == null) {
            findSelf();
        }

        if (key.equals("self")) return self;

        for (Link l : Meta) {
            if (l.getRel().equals(key)) {
                return concatLink(self, l);
            }
        }
        return null;
    }

    private Link concatLink(Link self, Link rel) {
        return new Link(rel.getRel(), self.getUri()+rel.getUri(), rel.getMethod(), rel.isEnabled());
    }

    @Override
    public String toString() {
         return API.gson.toJson(this);
    }

    public void findSelf() {
        for (Link l : Meta) {
            if (l.getRel().equals("self")) {
                self = l;
                break;
            }
        }
    }

    public List<Link> getLinks() {
        return this.Meta;
    }


}
