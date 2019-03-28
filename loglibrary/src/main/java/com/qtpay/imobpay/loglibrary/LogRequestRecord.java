package com.qtpay.imobpay.loglibrary;

import java.io.File;

import retrofit2.Callback;

/**
 * com.qtpay.imobpay.loglibrary
 *
 * @author jun
 * @date 2019/3/27
 * Copyright (c) 2019 ${ORGANIZATION_NAME}. All rights reserved.
 */
public class LogRequestRecord {
    static final int CALL_SENDING_STATUS = 10001;
    static final int CALL_FINISH_STATUS = 10002;
    static final int CALL_WAITING_STATUS = 10003;
    static final int CALL_REMOVED_STATUS = 10003;

    int status;
    File file;
    String logData;
    Callback callback;
}
