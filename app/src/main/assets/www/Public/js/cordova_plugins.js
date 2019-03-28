/**
 * Created by lorra on 16/6/21.
 */
cordova.define('cordova/plugin_list', function(require, exports, module) {
    module.exports = [
        {
            "file": "CDVMyPlugin.js",
            "id": "org.apache.cordova.cdvmyplugin",
            "merges": [ "navigator.myPlugin" ]
        },
        {
            "file": "IMPKeybordPlugin.js",
            "id": "org.apache.cordova.impkeyboard",
            "merges": [ "navigator.impKeyboard" ]
        }

    ];
    module.exports.metadata =
// TOP OF METADATA
    {
        "org.apache.cordova.cdvmyplugin" :"0.0.1",
        "org.apache.cordova.impkeyboard" :"0.0.1"

    };
// BOTTOM OF METADATA
});