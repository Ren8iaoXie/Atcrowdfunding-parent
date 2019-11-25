function showMenu() {
    var href=window.location.href;
    var host=window.location.host;
    var index=href.indexOf(host);
    var path=href.substring(host.length+index);

    var alink=$(".list-group a[href*='"+path+"']");
    alink.css("color","red");
    alink.parent().parent().removeClass("tree-closed");
    alink.parent().parent().show();
}