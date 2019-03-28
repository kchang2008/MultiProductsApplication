var alertComfirm = {
    /**
     * 隐藏dialog，直接调用即可
     */
    closeAlert: function() {
        var modal = document.getElementsByClassName('modal')[0]
        modal.className = 'modal modal-out'
        setTimeout(function() {
            modal.style.zIndex = '-9999'
        }, 400)
        if (typeof modal !== 'undefined' && modal.length === 0) {
            return
        }
        var overlay = document.getElementsByClassName('modal-overlay')[0]
        if (overlay) {
            overlay.className = 'modal-overlay'
        }
    },
    /**
     * 弹出dialog，根据传入参数判定显示情况，一个参数时为提示内容
     * @param obj 提示内容对象
     * @param obj.type 类型0:确认，取消；1：知道了；2：有logo的提示；3：无标题选项卡；4：有标题选项卡；5：有input的输入框  默认是0
     * @param obj.title  标题
     * @param obj.text   提示内容
     * @param obj.placeholder    输入框默认提示
     * @param obj.logoHtml   logo部分html内容
     * @param obj.opcity   蒙层透明度
     * @param obj.buttonList    按钮数组包括：弹窗不关闭keepAlert，按钮文字text，按钮样式classAttr，按钮点击回调func
     */
    customAlert: function(obj) {
        var params = {
            type: 0, //0:确认，取消；1：知道了；2：有logo的提示；3：无标题选项卡；4：有标题选项卡；5：有input的输入框  默认是0
            title: "提示", //标题
            text: "", //提示语
            placeholder: "请输入",
            logoHtml: "",
            opcity: 0.4,
            buttonList: []
        }
        for (var extendKey in obj) {
            extendKey in params ? params[extendKey] = obj[extendKey] : function() {}
        }
        var _modalTemplateTempDiv = document.createElement('div')
        var titleHTML = '3' != params.type ? '<div class="modal-title">' + params.title + '</div>' : ''
        var textHTML = params.text.length > 0 ? '<div class="modal-text">' + params.text + '</div>' : ''
        var logoHTML = ''
        var inputHTML = ''
        var buttonsHTML = ''
        var btnCntStyle = ''
        switch (params.type.toString()) {
            case "1": //1：知道了
                break;
            case "2": //2：有logo的提示
                logoHTML += params.logoHtml.length > 0 ? '<div class="modal-logo">' + params.logoHtml + '</div>' : ''
                break;
            case "3": //3：无标题选项卡
                btnCntStyle = 'modal-buttons-vertical';
                break;
            case "4": //4：有标题选项卡
                btnCntStyle = 'modal-buttons-vertical';
                break;
            case "5": //5：有input的输入框
                inputHTML = '<div class="modal-input"><input name="modal-input" class="modal-text-input" type="text" placeholder="' + params.placeholder + '"></div>'
                break;
            default: //0:确认，取消；
                break;
        }
        for (var i = 0; i < params.buttonList.length; i++) {
            var tempItem = params.buttonList[i]
            buttonsHTML += '<span data-index=' + i + ' class="modal-button ' + (tempItem.classAttr ? tempItem.classAttr : 'sure') + '">' + tempItem.text + '</span>'
        }
        var modalHTML = '<div class="modal modal-in"><div class="modal-inner">' + (logoHTML + titleHTML + textHTML + inputHTML) + '</div><div class="modal-buttons ' + btnCntStyle + '">' + buttonsHTML + '</div></div>'
        _modalTemplateTempDiv.innerHTML = modalHTML

        // 判断是否存在不透明层，存在即显示，不存在即添加
        var overlay = document.getElementsByClassName('modal-overlay')[0]
        if (typeof overlay === 'undefined') {
            var layoutHtml = document.createElement('div')
            layoutHtml.style.background = 'rgba(0, 0, 0, ' + params.opcity + ')'
            layoutHtml.className = 'modal-overlay modal-overlay-visible'
            document.getElementsByTagName('body')[0].appendChild(layoutHtml)
        } else {
            overlay.style.background = 'rgba(0, 0, 0, ' + params.opcity + ')'
            overlay.className = overlay.className + ' modal-overlay-visible'
        }
        // 如果已经存在modal框，删除
        if (document.getElementsByClassName('modal')[0]) {
            document.getElementsByClassName('modal')[0].parentNode.removeChild(document.getElementsByClassName('modal')[0])
        }
        document.getElementsByTagName('body')[0].appendChild(_modalTemplateTempDiv.firstChild)

        document.getElementsByClassName('modal-in')[0].style.display = 'block'
        var modalHeight = document.getElementsByClassName('modal-in')[0].offsetHeight || 0
        document.getElementsByClassName('modal-in')[0].style.marginTop = -modalHeight / 2 + 'px'
            // Add events on buttons
        var btnCount = document.getElementsByClassName('modal-button').length
        for (var j = 0; j < btnCount; j++) {
            document.getElementsByClassName('modal-button')[j].addEventListener('click', function(e) {
                var thisIndex = Number(e.target.dataset.index);
                var thisBtn = params.buttonList[thisIndex];
                if (thisBtn.func && typeof thisBtn.func == 'function') {
                    var tempData;
                    if ('5' == params.type) {
                        var inputItems = document.getElementsByName('modal-input')
                        if (!(inputItems[0].value.length > 0)) {
                            alert('输入内容不能为空')
                            return
                        }
                        tempData = inputItems[0].value;
                    }
                    thisBtn.keepAlert ? '' : closeAlert()
                    thisBtn.func(tempData)
                } else {
                    thisBtn.keepAlert ? '' : closeAlert()
                }
            })
        }

        function closeAlert() {
            var modal = document.getElementsByClassName('modal')[0]
            modal.className = 'modal modal-out'
            setTimeout(function() {
                modal.style.zIndex = '-9999'
            }, 400)
            if (typeof modal !== 'undefined' && modal.length === 0) {
                return
            }
            var overlay = document.getElementsByClassName('modal-overlay')[0]
            if (overlay) {
                overlay.className = 'modal-overlay'
            }
        }
    }
}