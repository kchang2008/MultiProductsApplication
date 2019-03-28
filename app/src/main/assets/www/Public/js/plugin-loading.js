/**
 * 显示loading图标
 * @type {{hide: Function, show: Function}}
 */
var loading = {
    // 隐藏loading图
    hide: function() {
        var overlay = document.getElementsByClassName('modal-overlay')[0]
        if (overlay) {
            overlay.className = 'modal-overlay'
        }
        var loadingBody = document.getElementsByClassName('loading-body')[0]
        if (loadingBody) {
            loadingBody.style.display = 'none'
        }
    },
    // 显示loading图
    show: function() {
        // 判断是否存在loading图，存在即显示，不存在即添加
        var loadingIcon = '<div class="loading-wrap"><span class="preloader preloader-white"></span></div>'
        var newNode = document.createElement('div')
        newNode.innerHTML = loadingIcon
        newNode.className = 'loading-body'
        var loadingBody = document.getElementsByClassName('loading-body')[0]
        if (typeof loadingBody === 'undefined') {
            document.getElementsByTagName('body')[0].appendChild(newNode)
        } else {
            loadingBody.style.display = 'block'
        }
        // 判断是否存在不透明层，存在即显示，不存在即添加
        var overlay = document.getElementsByClassName('modal-overlay')[0]
        if (typeof overlay === 'undefined') {
            var layoutHtml = document.createElement('div')
            layoutHtml.style.background = 'rgba(0, 0, 0, 0.4)'
            layoutHtml.className = 'modal-overlay modal-overlay-visible'
            document.getElementsByTagName('body')[0].appendChild(layoutHtml)
        } else {
            overlay.style.background = 'rgba(0, 0, 0, 0.4)'
            overlay.className = overlay.className + ' modal-overlay-visible'
        }
    }
}