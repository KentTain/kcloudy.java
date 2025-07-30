
window.addEventListener("load", function () {
    $("<div class=\"datagrid-mask\"></div>").css({
        display: "block",
        width: "100%",
        opacity: 0.5,
        height: $(window).height()
    }).appendTo("body");
    $("<div class=\"datagrid-mask-msg\"></div>").html("企业未进行认证或认证未通过。<br />请补充基础及认证信息后再操作。。。").appendTo("body").css({
        display: "block",
        left: ($(document.body).outerWidth(true) - 190) / 2,
        top: 160
    });

    setTimeout(function () {
        let authRedirectUrl = $('#hiddenAuthRedirectUrl').val();
        if (authRedirectUrl)
            MainPage_PostMessage("openPage", authRedirectUrl, '', false);

        let closeUrl = $('#hiddenClosePageUrl').val();
        if (closeUrl)
            MainPage_PostMessage("closePage", closeUrl);
    }, 3000);//参数是函数名
});