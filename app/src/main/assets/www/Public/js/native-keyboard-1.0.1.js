/**
 * 自定义jquery键盘插件, 提供$.keyboardShow键盘显示,$.keyboardHide键盘消失两个方法
 * 其中键盘显示方法只是让上一个输入框的键盘再显示。
 * @param  {Object}  配置初始值，或不传
 */
(function ($){
    var option, cb, curInput, st, errFn;

    //尾部插入中转节点 fz 字体大小
    function appendPreNode(fz) {
        $('body').append('<pre class="preText" style=font-size:'+ fz +'px;></pre>');
    }

    //设置光标的宽度和位置 cI指定输入框
    function setCursor(cI){
        var fz = parseFloat(cI.css("font-size"));
        $(".preText").length == 0 && appendPreNode(fz);
        $(".preText").css("font-size", fz + "px").html(cI.val());
        var iH = parseFloat(cI.outerHeight());
        var w = parseFloat($(".preText").width());
        var pPos = cI.position();
        cI.parent().find(".cursor").css({
            "left": (parseFloat(pPos.left) + parseFloat(w)) + 'px',
            "top": (parseFloat(pPos.top) + (iH - fz) / 2) + 'px'
        }).height(fz);
    }

    //显示当前输入框光标
    function showCursor() {
        setCursor(curInput);
        curInput.parent().find(".cursor").css("visibility", "visible");
    }

    /**
     * 键盘输入错误的处理方法
     * @param  {JSON} entry 插件传回来的数据
     * @return {void}
     */
    function defaultErrFn(entry){ }

    /**
     * 处理键盘输入成功方法
     * @param  {Object} entry  插件传回来的数据
     * @return {void}
     */
    function succFn(entry){
        var allmsg = JSON.parse(entry);
        if(allmsg && allmsg.application == "showKeyboard"){
            var tempval = allmsg.keyboardVal;
            var tempScale = allmsg.keyBoardScale;
            if(tempScale == undefined){
                if(tempval == curInput.val()){
                    keyboardDown();
                }else {
                    curInput.val(tempval);
                }
                cb && cb();
                showCursor();
            }
        }
    }

    var isKBShow = false;
    /**
     * 键盘弹起方法
     */
    function keyboardUp(){
        if(!curInput) return false;
        $(".cursor").css("visibility", "hidden");
        $("input, textarea").length > 0 && $("input, textarea").blur();
        showCursor();
        isKBShow = true;
        var config = [{
            "defaultVal": curInput.attr("placeholder"),
            "currentVal":curInput.val(),
            "keyboardType": st.kbdtype,
            "separateType":st.septype,
            "maxLength":st.maxlen
        }];
        navigator.impKeyboard.showKeyboardFunc(config, succFn, errFn || defaultErrFn);
    }

    /**
     * 键盘弹起方法
     * @return {void}
     */
    function keyboardDown() {
        isKBShow = false;
        $(".cursor").css("visibility", "hidden");
        navigator.impKeyboard.hideKeyboardFunc([], null,null);
    }

    $.keyboardHide = keyboardDown;
    $.keyboardShow = keyboardUp;
    
    // 点击页面其他非输入区域，隐藏键盘方法
    $(document).on("touchstart", function(e) {
        if(isKBShow && (e.target != (curInput && curInput[0])) ) keyboardDown();
        $(".cursor").css("visibility", "hidden");
    });

    $("head").append('<link rel="stylesheet" href="../Public/css/cursor.css">');
    
    //键盘默认配置信息
    var defaults = {"kbdtype": "1","septype":"0","maxlen":"11"};

    /**
     * 键盘插件主方法
     * @param  {Object} options options各个键的含义：option=>键盘配置信息，默认是html标签的data-type属性值
     *                                             cb=>输入时要做处理的方法
     *                                             errFn=>键盘输入错误的处理方法
     * @return {void}
     */
    $.fn.keyboard = function (options) {
        this.each(function () {
            $(this).css({
                "position": "relative",
                "padding": "0"
            }).append('<span class="cursor" style="visibility: hidden;"></span>');
            setCursor($(this).find("input"));
            $(this).on("touchend", function(e){
                if(options){
                    option = options.option || {};
                    cb = options.cb;
                    errFn = options.errFn;
                }
                curInput = $(this).find("input");
                st = $.extend({}, defaults, curInput.data("type") || option || {});
                keyboardUp();
                e.preventDefault();
                e.stopPropagation();
            });
        });
    }
}(jQuery));
