package com.core.fmservice.models.ui;


import lombok.Data;

@Data
public class AuthunticationModel {
    private String client_id;
    private String username;
    private String tokenValue ;
    private String tokenType;
    private String sessionID;
    private boolean isAuthunticated;

}