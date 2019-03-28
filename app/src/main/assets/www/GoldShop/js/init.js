var globalName = getFloder(location.href);
function getFloder(url){
    var urlArr = url.split('/');
    return urlArr[urlArr.length-2];
}
var docEl = window.document.documentElement;
var winWidth = docEl.getBoundingClientRect().width;
if(winWidth > 540){
    winWidth = 540;
}
var dprrem = winWidth/24.84;
docEl.style.fontSize = dprrem + 'px';
function parseobj(obj){
    if(typeof(obj) == "object"){
        return obj;
    }else {
        try {
            if(obj){
                return JSON.parse(obj);
            }else {
                return [];
            }
        } catch (e) {
            return [];
        }
    }
}