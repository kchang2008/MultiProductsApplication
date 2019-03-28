package com.multiproductsapplication;

/**
 * com.multiproductsapplication
 *
 * @author jun
 * @date 2019/2/26
 * Copyright (c) 2019 ${ORGANIZATION_NAME}. All rights reserved.
 */
public class MessageEvent{
    private String message;
    public  MessageEvent(String message){
        this.message=message;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
