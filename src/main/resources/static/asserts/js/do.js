function changeImg() {
    document.getElementById("check").src = "/code/image";
}


// 根据用户名、页面索引、页面大小获取用户列表
// function getUersByName(pageIndex, pageSize) {
function getUersByName() {
    // var _pageSize; // 存储用于搜索
    $.ajax({
        url: "/user/usersSearch",
        contentType: 'application/json',
        data: {
            // "async":true,
            // "pageIndex":pageIndex,
            // "pageSize":pageSize,
            "searchText": $("#searchName").val()
        },
        success: function (data) {
            // loadPageForRegister('/user/usersSearch');
            $("#content").html(data);
        },
        error: function () {
            alert("error!");
            alert($("#searchName").val());
        }
    });
}

// 根据图片名获取图片信息
// function getBannerByName(pageIndex, pageSize) {
function getBannerByName() {
    // var _pageSize; // 存储用于搜索
    $.ajax({
        url: "/banner/bannerSearch",
        contentType: 'application/json',
        data: {
            // "async":true,
            // "pageIndex":pageIndex,
            // "pageSize":pageSize,
            "searchText": $("#searchName").val()
        },
        success: function (data) {
            $("#content").html(data);
        },
        error: function () {
            alert("error!");
            alert($("#searchName").val());
        }
    });
}


// // 分页
// $.tbpage("#mainContainer", function (pageIndex, pageSize) {
//     getUersByName(pageIndex, pageSize);
//     _pageSize = pageSize;
// });

// // 搜索
// $("#searchNameBtn").click(function() {
//     getUersByName(0, _pageSize);
// });


function login() {
    var form = document.getElementById("myForm");
    var formData = new FormData(form);
    // var username=formData.get("username");
    // var password=formData.get("password");
    // var imageCode=formData.get("imageCode");
    // var rememberme=formData.get("remember-me");
    // formData.append("token","kshdfiwi3rh");
    console.info(formData.get("username"));
    console.info(formData.get("password"));
    $.ajax({
        url: '/login',
        type: 'POST',
        contentType: false,
        processData: false,// 注意这里应设为false
        // data: "username=" + username + "&password=" + password + "",
        data: formData,
        success: function (loginResult) {
            //alert(request.responseText);
//                var data = request.responseText;
            console.info(loginResult);
            var msg = loginResult.msg;
            if (loginResult.success === true) {
                // alert(msg);
                window.location.href = '/background/index';
            }
            if (loginResult.success === false) {
                alert(msg);
                window.location.href = '/userlogin';
            }
        }

    });
}

/**
 * 若果存在该名字的用户则查询出id并修改
 */
window.id;
function forgotPassword() {
    if (checkUserNameAndEmailExist()) {
        // var data = $('#forgotForm').serializeObject();
        var username=$('#userName').val();
        // var email= $('#email').val();
        var pwd1 = $('#passWord').val();
        var pwd2 = $('#rePassword').val();
        if (pwd1 != pwd2) {
            console.info(pwd1);
            console.info(pwd2);
            alert("输入的两次密码不一致，请重新输入！");
        } else {
            $.ajax({
                url: '/user/update',
                type: 'POST',
                data: "userId="+window.id+"&passWord=" + pwd1,
                dataType: 'json',
                // data: data,
                success: function (Result) {
                    alert(Result.msg);
                    window.location.href = 'background/login.html';
                }
            });
        }
    }
}

(function ($) {
    $.fn.extend({
        serializeObject: function () {
            var o = {};
            var a = this.serializeArray();
            $.each(a, function () {
                if (o[this.name]) {
                    if (!o[this.name].push) {
                        o[this.name] = [o[this.name]];
                    }
                    o[this.name].push(this.value || '');
                } else {
                    o[this.name] = this.value || '';
                }
            });
            return o;
        }
    });

})(jQuery);

/**
 * 退出后台
 */
function logoutBackground() {
    $.get("/admin/logout", function (msg) {
        if (msg === "true") {
            window.location.href = "/userlogin";
        } else {
            alert("退出异常!");
        }
    });

}

/**
 * 注册用户
 */
function saveUserForUser() {
    if (!checkUserNameIsExist()) {
        var form = document.getElementById("registerFormForUser");
        var data = new FormData(form);
        $.ajax({
            type: "POST",
            url: '/user/users',
            data: data,
            // 用了content-type=false，可以再加上一个processData = flase,发送 DOM 树信息或其它不希望转换的信息，请设置为 false。
            contentType: false, //告诉服务器，我要发什么类型的数据
            dataType: 'json',  //告诉服务器，我要想什么类型的数据
            processData: false,// 注意这里应设为false
            success: function (Result) {
                // var msg = Result.msg;
                // console.info("进入注册用户");
                // console.info(Result.msg);
                // console.info(Result);
                if (Result.success === true) {
                    alert(Result.msg);
                    window.location.href = '/userlogin';
                }
                if (Result.success === false) {
                    alert(Result.msg);
                    window.location.href = '/registerUserIndex';
                }
                // loadPageForRegister('/user/users');
            }
        });
    }
}

/**
 * 注册管理员
 */
function saveUser() {
    if (!checkAccountIsExist()) {
        var form = document.getElementById("registerFormForAdmin");
        var data = new FormData(form);
        $.ajax({
            type: "POST",
            url: "/user/users",
            data: data,
            contentType: false,
            processData: false,// 注意这里应设为false
            success: function () {
                console.info("进入注册用户");
                loadPageForRegister('/user/users');
            }
        });
    }
}

/**
 * 更新用户
 */
function updateUser() {
    // if (!checkUserNameIsExistForEdit()) {
    var form = document.getElementById("userEditForm");
    var data = new FormData(form);
    $.ajax({
        type: "POST",
        url: "/user/update",
        data: data,
        contentType: false,
        processData: false,// 注意这里应设为false
        success: function () {
            console.info("进入更新用户");
            loadPageForRegister('/user/users');
        }
    });
    // }
}

/**
 * 注册成功跳转
 * @param tag
 */
function loadPageForRegister(tag) {
    $("#content").load(tag);
}

/**
 * 判断帐号是否存在
 */
function checkAccountIsExist() {
    var username = $("#userName").val();
    var id = $("#userId").val();
    var url = "/user/exist?userName=" + username + "&userId=" + id;
    var isExist = false;
    $.ajax({
        url: url,
        async: false,
        success: function (msg) {
            var display = msg ? "inline" : "none";
            $(".span-exist-tip").css("display", display);
            isExist = msg;
        }
    });
    return isExist;
}

/**
 * 根据用户名判断用户是否存在
 * @returns {true-存在，false-不存在}
 * 如果存在则提示显示用户存在
 */
function checkUserNameIsExist() {
    var username = $("#userName").val();
    var url = "/user/existByUsername?userName=" + username;
    var isExist = false;
    $.ajax({
        url: url,
        async: false,
        success: function (msg) {
            var display = msg ? "inline" : "none";
            $(".span-exist-tip").css("display", display);
            isExist = msg;
        }
    });
    return isExist;
}

/**
 * 根据用户名同步请求判断用户是否存在
 * @returns {true-存在，false-不存在}
 * 如果不存在则提示显示用户不存在
 */
function checkUserNameIsNotExist() {
    var username = $("#userName").val();
    var url = "/user/existByUsername?userName=" + username;
    var isExist = false;
    $.ajax({
        url: url,
        async: false,
        success: function (msg) {
            var display = msg ? "none" : "inline";
            $(".span-exist-tip").css("display", display);
            isExist = msg;
        }
    });
    return isExist;
}

/**
 * 根据邮箱和用户名验证用户
 * @returns {true-验证通过，false-验证不通过}
 */
function checkUserNameAndEmailExist() {
    var username = $("#userName").val();
    var email = $("#email").val();
    var url = "/user/existByUsernameAndEmail?userName=" + username+"&email="+email;
    var isExist = false;
    $.ajax({
        url: url,
        async: false,
        dataType: 'json',
        success: function (Result) {
            var display = Result.success ? "none" : "inline";
            $(".span-exist-tip").css("display", display);
            isExist = Result.success;
            window.id=Result.msg;
            // alert(window.id);
        }
    });
    return isExist;
}



/**
 * 用户删除
 */
$('.del-user').bind('click', function () {
    var id = $(this).attr('id');
    if (window.confirm('确认删除该行数据?')) {
        //alert('删除')
        $.ajax({
            url: '/user/userDelete?userId=' + id,
            type: 'GET',
            dataType: 'json',
            success: function (data) {
                if (data.code == 0) {
//                        setTimeout(function () {
//                            //2s后执行如下代码
                    loadPageForRegister('/user/users');
//                        $('#info').html(data.msg);
                    alert(data.msg);
//                            window.location.href = '/user/usersSearch'
//                        }, 2000)
                }

                <!--th:onclick="'loadPageForRegister(\'/user/userDelete?userId='+${user.userId}+'\')'">-->
            }
        })
    }
});


/*------------------------------------Banner---------------------------------*/
/**
 * 删除banner
 */
$('.del-banner').click(function () {
    if (window.confirm('确认要删除该行数据?')) {
        $.ajax({
            url: "/banner/delete?id=" + $(this).attr('id'),
            type: "GET",
            dataType: "json",
            success: function (data) {
                alert(data.msg);
                loadPageForRegister('/banner/list');
//                    if (data.code == 0) {
//                        setTimeout(function () {
//                            window.location.href = "/banner/list";
//                        }, 2000);
//                    }
            }
        });
    }
});
/**
 * 提交Banner
 */
function saveBanner() {
    var form = document.getElementById("bannerEditForm");
    var data = new FormData(form);
    // var img = data.get("img");
    var img = document.getElementsByClassName("img")[0].src;
    console.log(img);
    // http://localhost:8090/background/assets/img/blog_default.png
    // alert(img);
    if ("http://localhost:8090/background/assets/img/blog_default.png"==img) {
        alert('图片不能为空');
    }else {
    $.ajax({
        type: "POST",
        url: "/banner/add",
        data: data,
        contentType: false,
        processData: false,// 注意这里应设为false
        success: function () {
            alert('上传成功')
            loadPageForRegister('/banner/list');
        }
    });
    }

}

/**
 * 更新Banner
 */
function updateBanner() {
    var form = document.getElementById("bannerEditForm");
    var data = new FormData(form);
    $.ajax({
        type: "POST",
        url: "/banner/update",
        data: data,
        dataType: "json",
        contentType: false,
        processData: false,// 注意这里应设为false
        success: function (data) {
            alert(data.msg);
            console.info("进入更新Banner");
            loadPageForRegister('/banner/list');
        }
    });
    // }
}

/**
 * 上传图片及时刷新显示
 */
function reads(obj) {
    var file = obj.files[0];
    var reader = new  FileReader();
    reader.readAsDataURL(file);
    reader.onload = function (ev) {
        $("#img").attr("src", ev.target.result);
    }
}