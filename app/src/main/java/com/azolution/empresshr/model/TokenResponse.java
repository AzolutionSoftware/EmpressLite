package com.azolution.empresshr.model;

import com.google.gson.annotations.SerializedName;

public class TokenResponse {
    @SerializedName("access_token")
    private String token;

    @SerializedName("token_type")
    private String type;

    @SerializedName("expires_in")
    private String expiresIn;

    @SerializedName("userName")
    private String name;

    @SerializedName(".issued")
    private String issuedDate;

    @SerializedName(".expires")
    private String expireDate;

    public String getToken() {
        return token;
    }

    public String getType() {
        return type;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public String getName() {
        return name;
    }

    public String getIssuedDate() {
        return issuedDate;
    }

    public String getExpireDate() {
        return expireDate;
    }
}
