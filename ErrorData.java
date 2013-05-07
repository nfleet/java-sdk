/**
 * Created with IntelliJ IDEA.
 * User: japelait
 * Date: 21.3.2013
 * Time: 13:37
 * To change this template use File | Settings | File Templates.
 */
public class ErrorData extends GeneralDataType {
    private int Code;
    private String Message;
    private String InvalidValue;

    public ErrorData() {
    }

    public void setCode(int code) {
        this.Code = code;
    }

    public void setMessage(String message) {
        this.Message = message;
    }

    public void setInvalidValue(String message) {
        this.InvalidValue = message;
    }

    public String toString() {
        return "Code: " + Code + " Message: " + Message + " InvalidValue: " + InvalidValue;
    }
}
