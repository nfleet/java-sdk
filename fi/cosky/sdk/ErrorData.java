package fi.cosky.sdk;
/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
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
