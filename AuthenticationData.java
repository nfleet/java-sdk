import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: japelait
 * Date: 18.4.2013
 * Time: 8:41
 * To change this template use File | Settings | File Templates.
 */
public class AuthenticationData extends GeneralDataType {
    private boolean Success;
    private int UserId;
    private String TokenType;
    private String AccessToken;
    private int ExpirationIn;

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean success) {
        Success = success;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getTokenType() {
        return TokenType;
    }

    public void setTokenType(String tokenType) {
        TokenType = tokenType;
    }

    public String getAccessToken() {
        return AccessToken;
    }

    public void setAccessToken(String accessToken) {
        AccessToken = accessToken;
    }

    public int getExpirationIn() {
        return ExpirationIn;
    }

    public void setExpirationIn(int expirationIn) {
        ExpirationIn = expirationIn;
    }
}
