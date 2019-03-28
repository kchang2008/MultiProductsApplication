/**
 * Created by lorra on 16/6/21.
 */
cordova.define("org.apache.cordova.impkeyboard", function(require, exports, module) {
    var exec = require('cordova/exec');
    module.exports = {
        showKeyboardFunc : function (message, win, fail) {  //显示键盘,1:纯数字;2:数字＋小数点;3:数字＋x;4:其它键盘
            exec(win, fail, "impKeyboard", "showKeyboard", message);
        },
        hideKeyboardFunc : function (message, win, fail) {  //隐藏键盘
            exec(win, fail, "impKeyboard", "hideKeyboard", message);
        }
    };
});