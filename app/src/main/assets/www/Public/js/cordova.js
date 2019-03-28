"use strict";

(function init_cordova() {
    var deviceAgent = navigator.userAgent.toLowerCase();
    if(deviceAgent.indexOf("android") >= 0){
        document.write('<script src="../Public/js/cordova-Android-3.7.1.js"></script>');
    }else{
        document.write('<script src="../Public/js/cordova-iOS-3.8.0.js"></script>');
    }
    /*if(deviceAgent.indexOf("android") >= 0){
        document.write('<script src="http://www.imobpay.com/v3.7/Public/js/cordova-Android-3.7.1.js"></script>');
    }else{
        document.write('<script src="http://www.imobpay.com/v3.7/Public/js/cordova-iOS-3.8.0.js"></script>');
    }*/
})();