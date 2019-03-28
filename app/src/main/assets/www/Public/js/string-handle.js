/**
 * Created by lorra on 16/7/12.
 */
var strHandle = (function() {
    /**
     * 字符串全部取代
     * @param sourceStr
     * @param searchStr
     * @param replaceStr
     * @returns {*}
     */
    var replaceAllStr = function(sourceStr, searchStr, replaceStr) {
        return sourceStr.replace(new RegExp(searchStr, 'gm'), replaceStr);
    };

    /**
     * 字符串加密
     * @param str 需处理的字符
     * @param leftLen 从左边起保留位数
     * @param rightLen 从右边起保留位数
     * @param starLen *个数
     * @returns {string}
     */
    var strEncode = function(str, leftLen, rightLen, starLen) {
        var starStr = "******************************";
        var encodeResult = "";
        if (str) {
            if (leftLen < 1) {
                encodeResult = starStr.substr(0, starLen) + str.substr(str.length - rightLen, rightLen);
            } else if (rightLen < 1) {
                encodeResult = str.substr(0, leftLen) + starStr.substr(0, starLen);
            } else {
                encodeResult = str.substr(0, leftLen) + starStr.substr(0, starLen) + str.substr(str.length - rightLen, rightLen);
            }
        } else {
            encodeResult = '**';
        }
        return encodeResult;
    };

    /**
     * 年月日格式化
     * @param date 年月日缩写 20150611
     * @returns {*}
     */
    function ymdFormat(date) {
        if (date.length < 8) {
            return date;
        } else {
            return date.substr(0, 4) + '-' + date.substr(4, 2) + '-' + date.substr(6, 2);
        }

    }

    /**
     * 时分秒格式化
     * @param time 时间缩写 225041
     * @returns {*}
     */
    function hmsFormat(time) {
        var thistime;
        if (time) {
            thistime = time.substr(0, 2) + ':' + time.substr(2, 2) + ':' + time.substr(4, 2);
        } else {
            thistime = time;
        }
        return thistime;
    }

    /**
     * 时间格式化
     * @param time1
     * @param time2
     * @returns {*}
     */
    var timeFormat = function(time1, time2) {
        var temptime;
        if (time1 == "") {
            temptime = time2.substr(0, 2) + ':' + time2.substr(2, 2) + ':' + time2.substr(4, 2);
        } else if (time2 == "") {
            if (time1.length > 10) {
                temptime = time1.substr(0, 4) + '-' + time1.substr(4, 2) + '-' + time1.substr(6, 2) + ' ' + time1.substr(8, 2) + ':' + time1.substr(10, 2) + ':' + time1.substr(12, 2);
            } else {
                temptime = time1.substr(0, 4) + '-' + time1.substr(4, 2) + '-' + time1.substr(6, 2);
            }
        } else {
            temptime = time1.substr(0, 4) + '-' + time1.substr(4, 2) + '-' + time1.substr(6, 2) + ' ' + time2.substr(0, 2) + ':' + time2.substr(2, 2) + ':' + time2.substr(4, 2);
        }
        return temptime;
    };

    /**
     * 检测图片是否存在
     * @param imgurl
     * @returns {boolean}
     * @constructor
     */
    function CheckImgExists(imgurl) {
        var ImgObj = new Image();
        ImgObj.src = imgurl;
        if (ImgObj.fileSize > 0 || (ImgObj.width > 0 && ImgObj.height > 0)) {
            return true;
        } else {
            return false;
        }
    }

    return {
        replacestr: replaceAllStr,
        strEncode: strEncode,
        timeFormat: timeFormat,
        ymd: ymdFormat,
        hms: hmsFormat
    }
})();