/**
 * Created with IntelliJ IDEA.
 * User: japelait
 * Date: 21.3.2013
 * Time: 13:37
 * To change this template use File | Settings | File Templates.
 */
public class ErrorData extends BaseData {
    private int Code;
    private String Message;
        public ErrorData() {
    }

    public void setCode(int code) {
        this.Code = code;
    }

    public void setMessage(String message) {
        this.Message = message;
    }


    public String toString() {
        return "Code: " + Code + " Message: " + Message;
    }
}
