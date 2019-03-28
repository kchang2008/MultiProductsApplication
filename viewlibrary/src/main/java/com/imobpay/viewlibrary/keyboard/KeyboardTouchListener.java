package com.imobpay.viewlibrary.keyboard;

import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

/**
 * Created by xuanweijian on 2016/3/31.
 */
public class KeyboardTouchListener implements View.OnTouchListener {
    private KeyboardUtil keyboardUtil;
    private int keyboardType = 1;
    private int scrollTo = -1;
    private boolean noTitle = true;

    public KeyboardTouchListener(KeyboardUtil util,int keyboardType,int scrollTo,boolean noTitle){
        this.keyboardUtil = util;
        this.keyboardType = keyboardType;
        this.scrollTo = scrollTo;
        this.noTitle  = noTitle;
    }
    
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (keyboardUtil != null && keyboardUtil.getEd() !=null &&v.getId() != keyboardUtil.getEd().getId())
                keyboardUtil.showKeyBoardLayout((EditText) v,keyboardType,scrollTo,noTitle);
            else if(keyboardUtil != null && keyboardUtil.getEd() ==null){
                keyboardUtil.showKeyBoardLayout((EditText) v,keyboardType,scrollTo,noTitle);
            }else{
//                Log.d("KeyboardTouchListener", "v.getId():" + v.getId());
//                Log.d("KeyboardTouchListener", "keyboardUtil.getEd().getId():" + keyboardUtil.getEd().getId());
                    if (keyboardUtil != null) {
                        keyboardUtil.setKeyBoardCursorNew((EditText) v);
                }
            }
        }
        return false;
    }

    public void setEncryptKeyboardValueListener(KeyboardUtil.EncryptKeyboardValue encryptKeyboardValueListener){
        if(this.keyboardUtil != null){
            this.keyboardUtil.setEncryptKeyboardValueListener(encryptKeyboardValueListener);
        }
    }

    /**
     * 响应EditText后面的删除按钮
     */
    public void responseClear(){
        if(keyboardUtil != null){
            this.keyboardUtil.base64TextFieldShouldClear();
        }
    }

}
