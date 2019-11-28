package com.teecoin.javastellarsdk.stellar.exceptions;

import java.io.IOException;

public class DestinationNotFoundException extends IOException {

    private String accountId;


    public DestinationNotFoundException(){

    }

    public DestinationNotFoundException(String accountId){
        this.accountId = accountId;
    }

}
