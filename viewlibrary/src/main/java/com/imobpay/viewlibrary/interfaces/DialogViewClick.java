package com.imobpay.viewlibrary.interfaces;

/**
 * com.imobpay.viewlibrary.interfaces
 *
 * @author jun
 * @date 2018/11/7
 * Copyright (c) 2018 ${ORGANIZATION_NAME}. All rights reserved.
 */
public interface DialogViewClick {
    /**
     *
     * @param isOK: 点击确认还是取消
     * @param clickFlag： 点击标志，标志从何处进入
     * @param activityUserdata：ialog向外部Activity传递的数据
     */
    void doViewClick(boolean isOK,String clickFlag, String activityUserdata);
}
