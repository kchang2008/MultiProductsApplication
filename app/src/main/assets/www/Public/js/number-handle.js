/**
 * Created by lorra on 16/7/12.
 */
var numHandle = (function($){
    /**
     * 四舍五入
     * @param num
     * @param len
     * @returns {*}
     */
    var roundTo = function(num ,len){
        var zeroStr = "0000000000";
        var returnNum;
        num = getNaNValue(num);

        var isFloat = (num.toString().split(".").length > 1);
        if(isFloat){
            var tempArr = num.toString().split(".");
            if(tempArr[1].length > len){
                if(parseInt(tempArr[1].substr(2,1)) > 4){
                    var tempnum = accAdd(num ,Math.pow(10,-len)).toString().split(".");
                    returnNum = tempnum[0] + "." + tempnum[1].substr(0,len);
                }else{
                    returnNum = tempArr[0] + "." + tempArr[1].substr(0,len);
                }
            }else{
                returnNum = num + zeroStr.substr(0,accSub(len,tempArr[1].length))
            }
        }else {
            if(len > 0){
                returnNum = num + '.' +zeroStr.substr(0,len);
            }else {
                returnNum = num;
            }
        }
        return returnNum;
    };

    /**
     * 舍去
     * @param num
     * @param len
     * @returns {*}
     */
    var roundOff = function(num ,len){
        var zeroStr = "0000000000";
        var returnNum;
        num = getNaNValue(num);
        var isFloat = (num.toString().split(".").length > 1);
        if(isFloat){
            var tempArr = num.toString().split(".");
            if(tempArr[1].length > len){
                returnNum = tempArr[0] + "." + tempArr[1].substr(0,len);
            }else{
                returnNum = num + zeroStr.substr(0,accSub(len,tempArr[1].length))
            }
        }else {
            if(len > 0){
                returnNum = num + '.' +zeroStr.substr(0,len);
            }else {
                returnNum = num;
            }
        }
        return returnNum;
    };

    /**
     * 检查是否为isNaN，如果是返回0
     * @param value 判断的数值
     * @returns {*}
     */
    var getNaNValue = function(value)
    {
        if (isNaN(value))
        {
            return (0.00).toFixed(2);
        }else {
            return value;
        }
    };

    /**
     * 加
     * @param arg1
     * @param arg2
     * @returns {number}
     */
    var accAdd = function(arg1,arg2){
        arg1 = getNaNValue(arg1);
        arg2 = getNaNValue(arg2);
        var r1,r2,m;
        try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}
        try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}
        m=Math.pow(10,Math.max(r1,r2));
        return (arg1*m+arg2*m)/m;
    };

    /**
     * 减
     * @param arg1
     * @param arg2
     * @returns {string}
     */
    var accSub = function(arg1,arg2){
        arg1 = getNaNValue(arg1);
        arg2 = getNaNValue(arg2);
        var r1,r2,m,n;
        try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}
        try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}
        m=Math.pow(10,Math.max(r1,r2));
        //动态控制精度长度
        n=(r1>=r2)?r1:r2;
        return ((arg1*m-arg2*m)/m).toFixed(n);
    };

    /**
     * 乘
     * @param arg1
     * @param arg2
     * @returns {number}
     */
    var accMul = function(arg1,arg2)   {
        arg1 = getNaNValue(arg1);
        arg2 = getNaNValue(arg2);
        var m=0,s1=arg1.toString(),s2=arg2.toString();
        try{m+=s1.split(".")[1].length}catch(e){}
        try{m+=s2.split(".")[1].length}catch(e){}
        return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m);
    };

    /**
     * 除
     * @param arg1
     * @param arg2
     * @returns {number}
     */
    var accDiv = function(arg1,arg2){
        arg1 = getNaNValue(arg1);
        arg2 = getNaNValue(arg2);
        var t1=0,t2=0,r1,r2;
        try{t1=arg1.toString().split(".")[1].length}catch(e){}
        try{t2=arg2.toString().split(".")[1].length}catch(e){}
        r1=Number(arg1.toString().replace(".",""));
        r2=Number(arg2.toString().replace(".",""));
        return (r1/r2)*Math.pow(10,t2-t1);
    };

    var getTrueValue = function(val){
        return accMul(getNaNValue(val),0.01);
    };

    return {
        roundTo : roundTo,
        roundOff : roundOff,
        getNaNValue : getNaNValue,
        aAdd : accAdd,
        aSub : accSub,
        aMul : accMul,
        aDiv : accDiv,
        trueVal : getTrueValue
    }
})(jQuery);