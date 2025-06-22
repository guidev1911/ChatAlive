package com.guidev1911.ChatAlive.dto;

public class ConfirmCodeDTO {

    private String email;
    private String code;

    public ConfirmCodeDTO() {}

    public ConfirmCodeDTO(String email, String code) {
        this.email = email;
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
