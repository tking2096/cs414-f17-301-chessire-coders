package edu.colostate.cs.cs414.chesshireCoders.jungleNetwork.responses;

import edu.colostate.cs.cs414.chesshireCoders.jungleNetwork.types.LoginStatus;

public class LoginResponse {

    private LoginStatus loginStatus;
    private String sessionToken;
    private long expiresOn;         // Expressed as milliseconds since Epoch

    public LoginStatus getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(LoginStatus loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public long getExpiresOn() {
        return expiresOn;
    }

    public void setExpiresOn(long expiresOn) {
        this.expiresOn = expiresOn;
    }

    public LoginResponse(LoginStatus loginStatus, String sessionToken, long expiresOn) {
        this.loginStatus = loginStatus;
        this.sessionToken = sessionToken;
        this.expiresOn = expiresOn;
    }
}
