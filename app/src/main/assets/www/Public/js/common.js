/**
 * Created by lorra on 16/6/22.常用方法
 */
/**
 *
 * @type {{clientType, toJson, dataMerge, fix, getLocaltime}}
 * @private
 */
var isPerfect = false;
var isDrag = false;
var isPerfect = false;
var isDrag = false;
var docEl = window.document.documentElement;
var winWidth = docEl.getBoundingClientRect().width;
if (winWidth > 640) {
    winWidth = 640;
}
var dprrem = winWidth / 24.84;
docEl.style.fontSize = dprrem + 'px';

/**
 * 内容是否填写完整
 * @param fn
 * @returns {boolean}
 */
function textstatus(fn) {
    if (fn) {
        $('.next a').css({
            "background-color": "#ebebf0",
            "color": "#d2d2d2"
        });
        return false;
    } else {
        $('.next a').css({
            "background-color": "#3ca0ff",
            "color": "#ffffff"
        });
        return true;
    }
}

var _COMMON = (function($) {
    /**
     * 获取客户端类型
     */
    var getClient = function() {
        var u = navigator.userAgent,
            app = navigator.appVersion;
        var browser = { //移动终端浏览器版本信息
            trident: u.indexOf('Trident') > -1, //IE内核
            presto: u.indexOf('Presto') > -1, //opera内核
            webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
            gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核
            mobile: !!u.match(/AppleWebKit.*Mobile.*/) || !!u.match(/AppleWebKit/), //是否为移动终端
            ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
            android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或者uc浏览器
            iPhone: u.indexOf('iPhone') > -1 || u.indexOf('Mac') > -1, //是否为iPhone或者QQHD浏览器
            iPad: u.indexOf('iPad') > -1, //是否iPad
            webApp: u.indexOf('Safari') == -1 //是否web应该程序，没有头部与底部
        };
        if (browser.iPhone) {
            return "iphone";
        } else if (browser.android) {
            return "android";
        } else {
            return "web";
        }
    };
    // 解析xml字符串变量为IXMLDOMDocument2
    function parseXML(data) {
        var xml, tmp
        if (window.DOMParser) { // Standard
            tmp = new DOMParser()
            xml = tmp.parseFromString(data, 'text/xml')
        } else { // IE
            xml = new ActiveXObject('Microsoft.XMLDOM')
            xml.async = 'false'
            xml.loadXML(data)
        }
        tmp = xml.documentElement
        if (!tmp || !tmp.nodeName || tmp.nodeName === 'parsererror') {
            return null
        }
        return xml
    }

    // 将 IXMLDOMDocument2 转换为JSON，参数为IXMLDOMDocument2对象
    function xml2json(obj) {
        if (obj == null) return null
        var retObj = new Object()
        buildObjectNode(retObj,
            /* jQuery */
            obj)
        return retObj

        function buildObjectNode(cycleOBJ,
            /* Element */
            elNode) {
            /* NamedNodeMap */
            var nodeAttr = elNode.attributes
            if (nodeAttr != null) {
                if (nodeAttr.length && cycleOBJ == null) cycleOBJ = new Object()
                for (var i = 0; i < nodeAttr.length; i++) {
                    cycleOBJ[nodeAttr[i].name] = nodeAttr[i].value
                }
            }
            var nodeText = 'text'
            if (elNode.text == null) nodeText = 'textContent'
                /* NodeList */
            var nodeChilds = elNode.childNodes
            if (nodeChilds != null) {
                if (nodeChilds.length && cycleOBJ == null) cycleOBJ = new Object()
                for (var i = 0; i < nodeChilds.length; i++) {
                    if (nodeChilds[i].tagName != null) {
                        if (nodeChilds[i].childNodes[0] != null && nodeChilds[i].childNodes.length <= 1 && (nodeChilds[i].childNodes[0].nodeType == 3 || nodeChilds[i].childNodes[0].nodeType == 4)) {
                            if (cycleOBJ[nodeChilds[i].tagName] == null) {
                                cycleOBJ[nodeChilds[i].tagName] = nodeChilds[i][nodeText]
                            } else {
                                if (typeof(cycleOBJ[nodeChilds[i].tagName]) === 'object' && cycleOBJ[nodeChilds[i].tagName].length) {
                                    cycleOBJ[nodeChilds[i].tagName][cycleOBJ[nodeChilds[i].tagName].length] = nodeChilds[i][nodeText]
                                } else {
                                    cycleOBJ[nodeChilds[i].tagName] = [cycleOBJ[nodeChilds[i].tagName]]
                                    cycleOBJ[nodeChilds[i].tagName][1] = nodeChilds[i][nodeText]
                                }
                            }
                        } else {
                            if (nodeChilds[i].childNodes.length) {
                                if (cycleOBJ[nodeChilds[i].tagName] == null) {
                                    cycleOBJ[nodeChilds[i].tagName] = new Object()
                                    buildObjectNode(cycleOBJ[nodeChilds[i].tagName], nodeChilds[i])
                                } else {
                                    if (cycleOBJ[nodeChilds[i].tagName].length) {
                                        cycleOBJ[nodeChilds[i].tagName][cycleOBJ[nodeChilds[i].tagName].length] = new Object()
                                        buildObjectNode(cycleOBJ[nodeChilds[i].tagName][cycleOBJ[nodeChilds[i].tagName].length - 1], nodeChilds[i])
                                    } else {
                                        cycleOBJ[nodeChilds[i].tagName] = [cycleOBJ[nodeChilds[i].tagName]]
                                        cycleOBJ[nodeChilds[i].tagName][1] = new Object()
                                        buildObjectNode(cycleOBJ[nodeChilds[i].tagName][1], nodeChilds[i])
                                    }
                                }
                            } else {
                                cycleOBJ[nodeChilds[i].tagName] = nodeChilds[i][nodeText]
                            }
                        }
                    }
                }
            }
        }
    }
    /**
     * 将返回内容转成json
     * @param str 需要转换的参数
     * @returns {*}
     */
    var toJson = function(str) {
        if (!str) str = ''
        var result
        if (str.indexOf('\<\?xml') > -1) {
            var tempresult = xml2json(parseXML(str))
            if (tempresult.QtPay) {
                result = tempresult.QtPay
            } else {
                result = tempresult
            }
        } else {
            result = jsonParse(str)
        }
        return result
    }

    /**
     * json数组更新
     * @param newjson 需要更新的数据
     * @returns {Object}
     */
    var dataMerge = function(newjson) {
        var defaultjson = {
            title: "",
            titleName: "",
            pathfloder: "",
            startpage: "",
            tipsUrl: "",
            downloadurl: "",
            titlebarColor: "#f7f7f7",
            titleNameColor: "#3d4245",
            backType: "2",
            backButtonName: "返回",
            backButtonNameColor: "#3ca0ff",
            splitLineColor: "#b2b2b2",
            tipsButtonName: "用户须知",
            tipsButtonNameColor: "#3ca0ff",
            thirdPartyUrl: ""
        };
        return eval("[" + JSON.stringify($.extend({}, defaultjson, newjson)) + "]");
    };

    /**
     * 位数不满len位，前面补零
     * @param num 需要格式的数据
     * @param len 输出数据的长度
     * @returns {string}
     */
    var fix = function(num, len) {
        return ('' + num).length < len ? ((new Array(len + 1)).join('0') + num).slice(-len) : '' + num;
    };

    /**
     * 获取当前时间并格式化：20150112102556
     * @returns {string}
     */
    var getLocaltime = function() {
        var myDate = new Date();
        return myDate.getFullYear() + '' + fix(myDate.getMonth() + 1, 2) + '' + fix(myDate.getDate(), 2) + '' + fix(myDate.getHours(), 2) + '' + fix(myDate.getMinutes(), 2) + '' + fix(myDate.getSeconds(), 2);
    };

    /**
     * json转换
     * @param obj
     * @returns {*}
     */
    var jsonParse = function(obj) {
        if (typeof(obj) == "object" && Object.prototype.toString.call(obj).toLowerCase() == "[object object]" && !obj.length) {
            return obj;
        } else {
            try {
                if (obj) {
                    return JSON.parse(obj);
                } else {
                    return {};
                }
            } catch (e) {
                return {};
            }
        }
    };
    /**
     * 获取银行卡图片
     * @param bankIdValue 银行卡id
     * @returns {*}
     */
    var getBankImg = function(bankIdValue) {
        var bankImgSrc = "bank_default.png";
        switch (bankIdValue) {
            case "102":
                bankImgSrc = "102.png";
                break;
            case "103":
                bankImgSrc = "103.png";
                break;
            case "104":
                bankImgSrc = "104.png";
                break;
            case "105":
                bankImgSrc = "105.png";
                break;
            case "301":
                bankImgSrc = "301.png";
                break;
            case "302":
                bankImgSrc = "302.png";
                break;
            case "303":
                bankImgSrc = "303.png";
                break;
            case "304":
                bankImgSrc = "304.png";
                break;
            case "305":
                bankImgSrc = "305.png";
                break;
            case "306":
                bankImgSrc = "306.png";
                break;
            case "307":
                bankImgSrc = "307.png";
                break;
            case "308":
                bankImgSrc = "308.png";
                break;
            case "309":
                bankImgSrc = "309.png";
                break;
            case "310":
                bankImgSrc = "310.png";
                break;
            case "322":
                bankImgSrc = "322.png";
                break;
            case "403":
                bankImgSrc = "403.png";
                break;
            case "501":
                bankImgSrc = "501.png";
                break;
            case "502":
                bankImgSrc = "502.png";
                break;
            case "531":
                bankImgSrc = "531.png";
                break;
            case "671":
                bankImgSrc = "671.png";
                break;
            default:
                break;
        }
        return bankImgSrc;
    };
    /**
     * 银行卡名称缩写
     * @param bankIdValue 银行卡id
     * @param bankName 银行名称
     * @returns {*}
     */
    var getBankName = function(bankIdValue, bankName) {
        var getBankName = bankName;
        switch (bankIdValue) {
            case "102":
                getBankName = "工商银行";
                break;
            case "103":
                getBankName = "农业银行";
                break;
            case "104":
                getBankName = "中国银行";
                break;
            case "105":
                getBankName = "建设银行";
                break;
            case "301":
                getBankName = "交通银行";
                break;
            case "302":
                getBankName = "中信银行";
                break;
            case "303":
                getBankName = "光大银行";
                break;
            case "304":
                getBankName = "华夏银行";
                break;
            case "305":
                getBankName = "民生银行";
                break;
            case "306":
                getBankName = "广发银行";
                break;
            case "307":
                getBankName = "平安银行";
                break;
            case "308":
                getBankName = "招商银行";
                break;
            case "309":
                getBankName = "兴业银行";
                break;
            case "310":
                getBankName = "浦发银行";
                break;
            case "313":
                getBankName = "城市商业银行";
                break;
            case "322":
                getBankName = "农村商业银行";
                break;
            case "402":
                getBankName = "信用社";
                break;
            case "403":
                getBankName = "邮政储蓄";
                break;
            case "501":
                getBankName = "汇丰银行";
                break;
            case "502":
                getBankName = "东亚银行";
                break;
            case "531":
                getBankName = "花旗银行";
                break;
            case "671":
                getBankName = "渣打银行";
                break;
            default:
                break;
        }
        return getBankName;
    };

    return {
        clientType: getClient,
        toJson: toJson,
        dataMerge: dataMerge,
        fix: fix,
        getLocaltime: getLocaltime,
        jsonP: jsonParse,
        bankImg: getBankImg,
        bankNae: getBankName
    }
})(jQuery);