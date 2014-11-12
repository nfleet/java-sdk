package fi.cosky.sdk;
/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

public class ErrorData extends BaseData {
	public static final String MimeType = "application/vnd.jyu.nfleet.error";
	public static final double MimeVersion = 2.0;
	
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

	public int getCode() {
		return Code;
	}

	public String getMessage() {
		return Message;
	}
}
