package com.azolution.empresshr.model;

import com.google.gson.annotations.SerializedName;

public class ApiResponseMessage {

    @SerializedName("Code")
    private String Code;

    @SerializedName("Message")
    private String Message;

    public String getCode() {
        return Code;
    }

    public String getMessage() {
        return Message;
    }
}
