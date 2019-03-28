cordova.define("org.apache.cordova.cdvmyplugin", function(require, exports, module) {
    var exec = require('cordova/exec');

    module.exports = {
        doNetworkActionWithoutLoadingFunc: function(message, win, fail) { //进行联网方法模块的调用，不显示联网标志
            exec(win, fail, "MyPlugin", "doNetworkActionWithoutLoading", message);
        },
        dismissToPreviousFunc: function(message, win, fail) { //返回上个页面，针对ios的presentModel形式出现的页面加载进行dismiss的页面剔除
            exec(win, fail, "MyPlugin", "dismissToPrevious", message);
        },
        createOrderSceneFunc: function(message, win, fail) { //生成本地订单页面
            exec(win, fail, "MyPlugin", "createOrderScene", message);
        },
        homepageEnterFunc: function(message, win, fail) { //h5首页homepage的功能点击
            exec(win, fail, "MyPlugin", "homepageEnter", message);
        },
        downloadPluginFunc: function(message, win, fail) { //点击下载新的插件包,h5需提供下载地址、插件包文件夹名
            exec(win, fail, "MyPlugin", "downloadPlugin", message);
        },
        showAlertFunc: function(message, win, fail) { //显示提示信息，android用toast,ios可用自定义形式，暂无定义
            exec(win, fail, "MyPlugin", "showAlert", message);
        },
        getPathFunc: function(message, win, fail) { //对文件路径的查找，如用于首页homepage的配置文件homepage_config.xml的路径查找
            exec(win, fail, "MyPlugin", "getPath", message);
        },
        sceneChangeWithWebViewFunc: function(message, win, fail) { //对h5格式页面模块的跳转
            exec(win, fail, "MyPlugin", "sceneChangeWithWebView", message);
        },
        sceneChangeWithLocalFunc: function(message, win, fail) { //对本地代码的界面进行跳转，需要注明android的包名、类名、android的extra传输内容（string格式）以及ios的类名（用于单纯跳转，不带参）
            exec(win, fail, "MyPlugin", "sceneChangeWithLocal", message);
        },
        checkLoginStatusFunc: function(message, win, fail) { //检查用户登录状态
            exec(win, fail, "MyPlugin", "checkLoginStatus", message);
        },
        showLoginSceneFunc: function(message, win, fail) { //本地登录界面功能调用
            exec(win, fail, "MyPlugin", "showLoginScene", message);
        },
        showBalanceEnquirySceneFunc: function(message, win, fail) { //本地余额查询功能调用
            exec(win, fail, "MyPlugin", "showBalanceEnquiryScene", message);
        },
        changeTitleNameFunc: function(message, win, fail) { //修改本地界面顶部条文字
            exec(win, fail, "MyPlugin", "changeTitleName", message);
        },
        webviewStatusHasChangedFunc: function(message, win, fail) { //当某个页面被变更某状态后用来做检测
            exec(win, fail, "MyPlugin", "webviewStatusHasChanged", message);
        },
        getUserInfoFunc: function(message, win, fail) { //获取用户相关信息，前提用户需登录
            exec(win, fail, "MyPlugin", "getUserInfo", message);
        },
        getMobileCommunicationListFunc: function(message, win, fail) { //调用手机通讯录
            exec(win, fail, "MyPlugin", "getMobileCommunicationList", message);
        },
        getBankCardNumberFunc: function(message, win, fail) { //调用银行卡卡号
            exec(win, fail, "MyPlugin", "getBankCardNumber", message);
        },
        getShareSomethingsFunc: function(message, win, fail) { //调用本地分享
            exec(win, fail, "MyPlugin", "getShareSomethings", message);
        },
        getExitCurrentAccountFunc: function(message, win, fail) { //退出当前账号
            exec(win, fail, "MyPlugin", "getExitCurrentAccount", message);
        },
        getFeedbackFunc: function(message, win, fail) { //调用意见反馈
            exec(win, fail, "MyPlugin", "getFeedback", message);
        },
        getCallCustomerServiceFunc: function(message, win, fail) { //调用打电话
            exec(win, fail, "MyPlugin", "getCallCustomerService", message);
        },
        getPhoneAlbumFunc: function(message, win, fail) { //调用相册
            exec(win, fail, "MyPlugin", "getPhoneAlbum", message);
        },
        getAuthenticationStatusFunc: function(message, win, fail) { //实名认证
            exec(win, fail, "MyPlugin", "getAuthenticationStatus", message)
        },
        getLocalModuleFunc: function(message, win, fail) { //获取本地模块名称（获取www/Others/version.text 内容）
            exec(win, fail, "MyPlugin", "getLocalModule", message);
        },
        deleteWebPluginFloderFunc: function(message, win, fail) { //删除外部模块的文件夹
            exec(win, fail, "MyPlugin", "deleteWebPluginFloder", message);
        },
        initWebPluginFloderFunc: function(message, win, fail) { //重新实例化外部模块文件夹（搬运内部www文件夹中的public和version）
            exec(win, fail, "MyPlugin", "initWebPluginFloder", message);
        },
        getUserMustToKnowFunc: function(message, win, fail) { //用户须知info获取网址
            exec(win, fail, "MyPlugin", "getUserMustToKnow", message);
        },
        toThirdPartyWebsiteFunc: function(message, win, fail) { //去第三方网站
            exec(win, fail, "MyPlugin", "toThirdPartyWebsite", message);
        },
        showGuideWebViewFunc: function(message, win, fail) { //引导图的webView
            exec(win, fail, "MyPlugin", "showGuideWebView", message);
        },
        popUserMustToKnowViewFunc: function(message, win, fail) { //弹出HTML页面用户须知视图
            exec(win, fail, "MyPlugin", "popUserMustToKnowView", message);
        },
        showCanJumpGuideWebViewFunc: function(message, win, fail) { //可跳转的蒙版HTML容器
            exec(win, fail, "MyPlugin", "showCanJumpGuideWebView", message);
        },
        callSystemComesWithSomeOfTheFunctionsFunc: function(message, win, fail) { //可跳转的外部浏览器
            exec(win, fail, "MyPlugin", "callSystemComesWithSomeOfTheFunctions", message);
        },
        popToHomepageWebviewFunc: function(message, win, fail) { //返回首页页面
            exec(win, fail, "MyPlugin", "popToHomepageWebview", message);
        },
        //针对安卓返回首页新的插件
        toBackHomeWebview: function(message, win, fail) { //返回首页页面
            exec(win, fail, "MyPlugin", "toBackHomeWebview", message);
        },
        newMessageNoticeFunc: function(message, win, fail) { //有新消息,消息提示显示红点
            exec(win, fail, "MyPlugin", "newMessageNotice", message);
        },
        eventTrackFunc: function(message, win, fail) { //埋点事件
            exec(win, fail, "MyPlugin", "eventTrack", message);
        },
        configHomePageDataFunc: function(message, win, fail) { //首页模块数据,消息数据,banner图数据
            exec(win, fail, "MyPlugin", "configHomePageData", message);
        },
        deleteModulesFunc: function(message, win, fail) { //删除功能模块,webplugin中version.txt
            exec(win, fail, "MyPlugin", "deleteModules", message);
        },
        loadSpecicalPageFunc: function(message, win, fail) { //底部条切换
            exec(win, fail, "MyPlugin", "loadSpecicalPage", message);
        },
        updateModulesFunc: function(message, win, fail) { //删除功能模块,webplugin中version.txt
            exec(win, fail, "MyPlugin", "updateModules", message);
        },
        takeUserInfoFunc: function(message, win, fail) { //用户信息抓取
            exec(win, fail, "MyPlugin", "takeUserInfo", message);
        },
        saveCurrentWebviewToPhotoAlbumFunc: function(message, win, fail) { //保存瑞码二维码图片到本地
            exec(win, fail, "MyPlugin", "saveCurrentWebviewToPhotoAlbum", message);
        },
        checkUpdatedFunc: function(message, win, fail) { //检查是否刚刚升级
            exec(win, fail, "MyPlugin", "checkUpdated", message);
        },
        sceneChangeWithWebViewNewFunc: function(message, win, fail) { //对h5格式页面模块的跳转（押金支付）
            exec(win, fail, "MyPlugin", "sceneChangeWithWebViewNew", message);
        },
        dismissToPreviousNewFunc: function(message, win, fail) { //返回上个页面，针对ios的presentModel形式出现的页面加载进行dismiss的页面剔除（弹出蒙层需要接受原生回调不等及时关闭，在回调中关闭蒙层页面调用）
            exec(win, fail, "MyPlugin", "dismissToPreviousNew", message);
        },
        getShareSomethingsWithoutDialogFunc: function(message, win, fail) { //新增单独分享
            exec(win, fail, "MyPlugin", "getShareSomethingsWithoutDialog", message);
        },
        scanQRCodeFunc: function(message, win, fail) { //扫一扫
            exec(win, fail, "MyPlugin", "scanQRCode", message);
        },
        checkAppSettingFunc: function(message, win, fail) { //检测是否设置系统通知
            exec(win, fail, "MyPlugin", "checkAppSetting", message);
        },
        openAppSettingFunc: function(message, win, fail) { //开启系统通知
            exec(win, fail, "MyPlugin", "openAppSetting", message);
        },
        getWebPluginPathFunc: function(message, win, fail) { //获取WebPlugin文件夹绝对路径
            exec(win, fail, "MyPlugin", "getWebPluginPath", message);
        },
        getWWWPluginPathFunc: function(message, win, fail) { //获取www文件夹绝对路径
            exec(win, fail, "MyPlugin", "getWWWPluginPath", message);
        },
        changeTitleAllDataFunc: function(message, win, fail) { //改变头部颜色
            exec(win, fail, "MyPlugin", "changeTitleAllData", message);
        },
        contactCustomerServiceOfQQFunc: function(message, win, fail) { //打开qq客服对话框
            exec(win, fail, "MyPlugin", "contactCustomerServiceOfQQ", message);
        },
        checkShareClientIsAvailableFunc: function(message, win, fail) { //判断客户端是否安装了微信、QQ等
            exec(win, fail, "MyPlugin", "checkShareClientIsAvailable", message);
        },
        showRedPointFunc: function(message, win, fail) { //底部功能上的小红点
            exec(win, fail, "showRedPoint", "showRedPoint", message);
        },
        getVersionInfoFunc: function(message, win, fail) { // 获取本地h5包的版本等信息
            exec(win, fail, "MyPlugin", "getVersionInfo", message);
        },
        hasNotchInScreeFunc: function(message, win, fail) { // 是否是安卓的刘海屏
            exec(win, fail, "MyPlugin", "hasNotchInScree", message);
        }
    };
});