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
            swal("error!");
            // alert($("#searchName").val());
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
            swal("error!");
            // alert($("#searchName").val());
        }
    });
}

function getBlogByAttr() {
    // var _pageSize; // 存储用于搜索
    $.ajax({
        // url: "/blog/search?page=0&size=10",
        url: "/blog/search",
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
            swal("error!");
            alert($("#searchName").val());
        }
    });
}

// function getBannerIndex() {
//     $.ajax({
//         url: "/bannerIndex",
//         type: 'GET',
//         success: function (data) {
//             // window.location.href="#portfolio";
//             // $("#portfolioPage").load(#portfolio);
//             $("#portfolio-content").html(data);
//         },
//         error: function () {
//             alert("error!");
//         }
//     });
// }


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
        processData: false,// 注意这里应设为false,防止自动转换数据格式。
        // data: "username=" + username + "&password=" + password + "",
        data: formData,
        success: function (loginResult) {
            //alert(request.responseText);
//                var data = request.responseText;
            console.info(loginResult);
            var msg = loginResult.msg;
            if (loginResult.success === true) {
                window.location.href = '/background/index';
            }
            if (loginResult.success === false) {
                setTimeout(function(){swal("提示",msg,"warning"); },100);
                //2秒后刷新页面，足够显示swal()的信息
                setTimeout(function(){window.location.href = '/userlogin'; },2000);
                // window.location.href = '/userlogin';
            }
        }

    });
}

/**
 * 若果存在该名字的用户则查询出id并修改
 */
function forgotPassword() {
    if (checkUserNameAndEmailExist()) {
        // var data = $('#forgotForm').serializeObject();
        var username = $('#userName').val();
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
                data: "userId=" + window.id + "&passWord=" + pwd1,
                dataType: 'json',
                // data: data,
                success: function (Result) {
                    // alert(Result.msg);
                    // window.location.href = 'background/login.html';
                    setTimeout(function(){swal("提示",Result.msg,"success"); },100);
                    //2秒后刷新页面，足够显示swal()的信息
                    setTimeout(function(){window.location.href = 'background/login.html'; },2000);
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
            swal("退出异常!");
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
                    // swal(Result.msg);
                    setTimeout(function(){swal("提示",Result.msg,"success"); },100);
                    //2秒后刷新页面，足够显示swal()的信息
                    setTimeout(function(){ window.location.href = '/userlogin'; },2000);
                    // window.location.href = '/userlogin';
                }
                if (Result.success === false) {
                    setTimeout(function(){swal("提示",Result.msg,"warning"); },100);
                    //2秒后刷新页面，足够显示swal()的信息
                    setTimeout(function(){ window.location.href = '/registerUserIndex'; },2000);
                    // alert(Result.msg);
                    // window.location.href = '/registerUserIndex';
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
            success: function (Result) {
                if (Result.success === true) {
                    // alert(Result.msg);
                    // loadPageForRegister('/user/users');
                    setTimeout(function(){swal("提示",Result.msg,"success"); },100);
                    //2秒后刷新页面，足够显示swal()的信息
                    setTimeout(function(){ loadPageForRegister('/user/users');},2000);
                } else {
                    swal(Result.msg);
                }

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
        success: function (result) {
            // console.info("进入更新用户");
            setTimeout(function(){swal("提示",result.msg,"success"); },100);
            //2秒后刷新页面，足够显示swal()的信息
            setTimeout(function(){ loadPageForRegister('/user/users'); },2000);
            // alert(result.msg);
            // loadPageForRegister('/user/users');
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
    // var id = $("#userId").val();
    var url = "/user/existByUsername?userName=" + username;
    // var url = "/user/existByUsername?userName=" + username + "&userId=" + id;
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
    var url = "/user/existByUsernameAndEmail?userName=" + username + "&email=" + email;
    var isExist = false;
    $.ajax({
        url: url,
        async: false,
        dataType: 'json',
        success: function (Result) {
            var display = Result.success ? "none" : "inline";
            $(".span-exist-tip").css("display", display);
            isExist = Result.success;
            window.id = Result.msg;
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
    swal({
            title: "确认删除该行数据？",
            text: "你将无法恢复该数据！",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "确定删除！",
            cancelButtonText: "取消删除！",
            closeOnConfirm: false,
            closeOnCancel: false
        },
        function (isConfirm) {
            if (isConfirm) {
                $.ajax({
                    url: '/user/userDelete?userId=' + id,
                    type: 'GET',
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == 0) {
                            setTimeout(function () {
                                swal("提示", data.msg, "success");
                            }, 100);
                            //2秒后刷新页面，足够显示swal()的信息
                            setTimeout(function () {
                                loadPageForRegister('/user/users');
                            }, 2000);
                        }
                    }
                })
            } else {
                swal("取消！", "你的虚拟数据是安全的:)",
                    "error");
            }
        })
});

/**
 * 正常博文删除
 */
$('.del-blog').bind('click', function () {
    var id = $(this).attr('id');
    // if (window.confirm('(请注意，删除博文后评论将消失)确认删除该博文?')) {
    //     $.ajax({
    //         url: '/blog/' + id,
    //         type: 'DELETE',
    //         dataType: 'json',
    //         success: function (data) {
    //             if (data.success) {
    //                 setTimeout(function(){swal("提示",data.msg,"success"); },100);
    //                 //2秒后刷新页面，足够显示swal()的信息
    //                 setTimeout(function(){ loadPageForRegister('/blog/list'); },2000);
    //
    //                 // loadPageForRegister('/blog/list');
    //                 // alert(data.msg);
    //             }
    //         }
    //     })
    //
    swal({
            title: "确认删除该博文数据？",
            text: "请注意，你将无法恢复该数据，删除博文后评论也将消失！",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "确定删除！",
            cancelButtonText: "取消删除！",
            closeOnConfirm: false,
            closeOnCancel: false
        },
        function (isConfirm) {
            if (isConfirm) {
                $.ajax({
                    url: '/blog/' + id,
                    type: 'DELETE',
                    dataType: 'json',
                    success: function (data) {
                        if (data.success) {
                            setTimeout(function(){swal("提示",data.msg,"success"); },100);
                            //2秒后刷新页面，足够显示swal()的信息
                            setTimeout(function(){ loadPageForRegister('/blog/list'); },2000);
                        }
                    }
                })
            } else {
                swal("取消！", "你的虚拟数据是安全的:)",
                    "error");
            }
        })
});

/**
 * 审核博文删除
 */
$('.del-blog-check').bind('click', function () {
    var id = $(this).attr('id');
    swal({
            title: "确认删除该博文数据？",
            text: "请注意，你将无法恢复该数据，删除博文后评论也将消失！",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "确定删除！",
            cancelButtonText: "取消删除！",
            closeOnConfirm: false,
            closeOnCancel: false
        },
        function (isConfirm) {
            if (isConfirm) {
                $.ajax({
                    url: '/blog/' + id,
                    type: 'DELETE',
                    dataType: 'json',
                    success: function (data) {
                        if (data.success) {
                            setTimeout(function(){swal("提示",data.msg,"success"); },100);
                            //2秒后刷新页面，足够显示swal()的信息
                            setTimeout(function(){ loadPageForRegister('/blog/examine'); },2000);
                        }
                    }
                })
            } else {
                swal("取消！", "你的虚拟数据是安全的:)",
                    "error");
            }
        })
});


/**
 * 保存博文
 */
function saveBlog() {
    var form = document.getElementById("blogForm");
    var data = new FormData(form);
    var releaseTime = $("#releaseTime").val();
    var img = document.getElementsByClassName("img")[0].src;
    if (releaseTime == '' || releaseTime == undefined || releaseTime == null) {
        swal("发布时间必填");
    }  if ("http://localhost:8090/background/assets/img/blog_default.png" == img) {
        swal('图片不能为空');
    }  else{
        $.ajax({
            type: "POST",
            url: "/blog",
            data: data,
            contentType: false,
            processData: false,// 注意这里应设为false
            success: function (Result) {
                if (Result.success === true) {
                    if (Result.msg=="提交更新成功"){
                        setTimeout(function(){swal("提示",Result.msg,"success"); },100);
                        //2秒后刷新页面，足够显示swal()的信息
                        setTimeout(function(){  loadPageForRegister('/blog/list');},2000);
                    // loadPageForRegister('/blog/list');
                    // alert(Result.msg);
                    }else {
                        setTimeout(function(){swal("提示",Result.msg,"warning"); },100);
                        //2秒后刷新页面，足够显示swal()的信息
                        // setTimeout(function(){ window.location.reload();},2000);
                        // alert(Result.msg);
                        // window.location.reload();
                    }
                } else {
                    swal(Result.msg);
                }

            }
        });
    }
}

/**
 * 根据博文名称、关键词、分类、摘要、作者进行查询
 */
function getContentBySearchText() {
    var val = $("#searchText").val();
    $.ajax({
        url: "/blog/esSearch?page=0&size=10",
        contentType: 'application/json',
        data: {
            // "async":true,
            // "pageIndex":pageIndex,
            // "pageSize":pageSize,
            "searchText": val
        },
        success: function (data) {
            // loadPageForRegister('/user/usersSearch');
            $("#content").html(data);
        },
        error: function () {
            swal("error!");
            // alert($("#searchText").val());
        }
    });
}

/*------------------------------------Banner---------------------------------*/
/**
 * 删除banner
 */
$('.del-banner').click(function () {
    if (window.confirm('确认要删除该行数据?')) {
        $.ajax({
            url: "/banner/delete?id=" + $(this).attr('id'),
            type: "DELETE",
            dataType: "json",
            success: function (data) {
                setTimeout(function(){swal("提示",data.msg,"warning"); },100);
                //2秒后刷新页面，足够显示swal()的信息
                setTimeout(function(){ loadPageForRegister('/banner/list'); },2000);
                // alert(data.msg);
                // loadPageForRegister('/banner/list');
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
    var name =$("#name").val();
    // alert(name);
    // console.log(img);
    // http://localhost:8090/background/assets/img/blog_default.png
    // alert(img);
    if ("http://localhost:8090/background/assets/img/blog_default.png" == img||""==name) {
        swal('图片/图片名称不能为空');
    } else {
        $.ajax({
            type: "POST",
            url: "/banner/add",
            data: data,
            dataType: "json",
            contentType: false,
            processData: false,// 注意这里应设为false
            success: function (Result) {
                if (Result.success === true){
                    setTimeout(function(){swal("提示",Result.msg,"success"); },100);
                    //2秒后刷新页面，足够显示swal()的信息
                    setTimeout(function(){ loadPageForRegister('/banner/list'); },2000);
                    // alert(Result.msg);
                    // loadPageForRegister('/banner/list');
                }else {
                    swal(Result.msg);
                }
            },
            error: function () {
                swal("erro");
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
            setTimeout(function(){swal("提示",data.msg,"success"); },100);
            //2秒后刷新页面，足够显示swal()的信息
            setTimeout(function(){ loadPageForRegister('/banner/list'); },2000);
            // alert(data.msg);
            // console.info("进入更新Banner");
            // loadPageForRegister('/banner/list');
        }
    });
    // }
}

/**
 * 上传图片及时刷新显示
 */
function reads(obj) {
    var file = obj.files[0];
    var reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = function (ev) {
        $("#img").attr("src", ev.target.result);
        $("#img").css({"width":"60%" ,"height":"auto"});
        // style="width: 50%;height: auto"
    }
}


/*------------------------------------评论---------------------------------*/
// 根据评论内容查询
function getCommentByName() {
    $.ajax({
        url: "/comments/commentSearch",
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
            swal("error!");
            // alert($("#searchName").val());
        }
    });
}

/**
 * 删除评论
 */
$('.del-comment').click(function () {
    var id = $(this).attr('id');
    var blogId = $('.blogId').attr('id');
    // alert("id="+id+",blogId="+blogId);
    swal({
            title: "确认要删除该行评论？",
            text: "你将无法恢复该评论！",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "确定删除！",
            cancelButtonText: "取消删除！",
            closeOnConfirm: false,
            closeOnCancel: false
        },
        function (isConfirm) {
            if (isConfirm) {
                $.ajax({
                    url: '/comments/' + id + '?blogId=' + blogId,
                    type: 'DELETE',
                    dataType: "json",
                    success: function (data) {
                        if (data.code == 0) {
                            setTimeout(function () {
                                swal("提示", data.msg, "success");
                            }, 100);
                            //2秒后刷新页面，足够显示swal()的信息
                            setTimeout(function () {
                                loadPageForRegister('/comments/list');
                            }, 2000);
                            // alert(data.msg);
                            // loadPageForRegister('/comments/list');
                        } else {
                            swal("错误信息为：" + data.msg);
                        }
                    },
                    error: function () {
                        swal(data.code + "错误信息为：" + data.msg);
                    }
                });
            } else {
                swal("取消！", "你的虚拟文件是安全的:)",
                    "error");
            }
        });
});

//     if (window.confirm('确认要删除该行评论?')) {
//         $.ajax({
//             url: '/comments/' + id + '?blogId=' + blogId,
//             type: 'DELETE',
//             dataType: "json",
//             success: function (data) {
//                 if (data.code == 0) {
//                     setTimeout(function(){swal("提示",data.msg,"success"); },100);
//                     //2秒后刷新页面，足够显示swal()的信息
//                     setTimeout(function(){loadPageForRegister('/comments/list');},2000);
//                     // alert(data.msg);
//                     // loadPageForRegister('/comments/list');
//                 } else {
//                     swal(data.code + "错误信息为：" + data.msg);
//                 }
//             },
//             error: function () {
//                 swal(data.code + "错误信息为：" + data.msg);
//             }
//         });
//     }
// });

/* -------- 提交message --------*/
function sendMessage() {
    // var contactUsername = $("#form_name").val();
    // var email = $("#form_email").val();
    // var message = $("#form_message").val();
    // alert(contactUsername+"e"+ email+"m"+ message);

    var form = document.getElementById("contact-form");
    var data = new FormData(form);
        $.ajax({
            url: 'message/sendMessage',
            type: 'POST',
            dataType: "json",
            contentType: false,
            processData: false,// 注意这里应设为false
            data: data,
            // data: {"contactUsername": contactUsername, "email": email,"message": message},
            success: function (data) {
                if (data.success) {
                    setTimeout(function(){swal("提示",data.msg,"success"); },100);
                    //2秒后刷新页面，足够显示swal()的信息
                } else {
                    swal(data.msg);
                }
            },
            error: function () {
                swal("error!");
            }
        });
}

/**
 * 删除message
 */
$('.del-message').click(function () {
    if (window.confirm('确认要删除该行数据?')) {
        $.ajax({
            url: "/message/delete?id=" + $(this).attr('id'),
            type: "DELETE",
            dataType: "json",
            success: function (data) {
                // swal(data.msg);
                setTimeout(function(){swal("提示",data.msg,"success"); },100);
                setTimeout(function(){ loadPageForRegister('/message/list');},2000);
                // loadPageForRegister('/message/list');
            }
        });
    }
});

