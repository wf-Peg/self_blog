function changeImg() {
    document.getElementById("check").src = "/code/image";
}


// 根据用户名、页面索引、页面大小获取用户列表
// function getUersByName(pageIndex, pageSize) {
function getUersByName() {
    if ($("#searchName").val()==null||""==$("#searchName").val()) {
        swal("搜索内容信息不能为空！");
        return;
    }
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
    if ($("#searchName").val()==null||""==$("#searchName").val()) {
        swal("搜索内容信息不能为空！");
        return;
    }
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


function getPrintByName() {
    // var _pageSize; // 存储用于搜索
    if ($("#searchName").val()==null||""==$("#searchName").val()) {
        swal("搜索内容信息不能为空！");
        return;
    }
    $.ajax({
        url: "/print/printSearch",
        contentType: 'application/json',
        data: {
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
    if ($("#searchName").val()==null||""==$("#searchName").val()) {
        swal("搜索内容信息不能为空！");
        return;
    }
    $.ajax({
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

function getBlogIndex() {
    $.ajax({
        url: "/blog/orderList",
        type: 'GET',
        success: function (data) {
            // window.location.href="#portfolio";
            // $("#portfolioPage").load(#portfolio);
            $("#content").html(data);
        },
        error: function () {
            alert("error!");
        }
    });
}

// function getBannerIndex() {
//     $.ajax({
//         url: "/bannerIndex",
//         type: 'GET',
//         success: function (data) {
//             window.location.href="#portfolio";
//             // $("#portfolioPage").load(#portfolio);
//             $("#portfolio_grid").html(data);
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
    // console.info(formData.get("username"));
    // console.info(formData.get("password"));
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
                setTimeout(function () {
                    swal("提示", msg, "warning");
                }, 100);
                //2秒后刷新页面，足够显示swal()的信息
                // setTimeout(function () {
                //     window.location.href = '/userlogin';
                // }, 2000);
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
        if (pwd1==null||""==pwd1||pwd2==null||""==pwd2) {
            swal("密码不能为空！");
            return;
        }
        if (pwd1 != pwd2) {
            swal("输入的两次密码不一致，请重新输入！");
            return
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
                    setTimeout(function () {
                        swal("提示", Result.msg, "success");
                    }, 100);
                    if (Result.msg=="修改成功"||Result.msg=="注册成功")
                    //2秒后刷新页面，足够显示swal()的信息
                    setTimeout(function () {
                        window.location.href = 'background/login.html';
                    }, 2000);
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
                if (Result.success === true) {
                    // swal(Result.msg);
                    setTimeout(function () {
                        swal("提示", Result.msg, "success");
                    }, 100);
                    //2秒后刷新页面，足够显示swal()的信息
                    setTimeout(function () {
                        window.location.href = '/userlogin';
                    }, 2000);
                    // window.location.href = '/userlogin';
                }
                if (Result.success === false) {
                    setTimeout(function () {
                        swal("提示", Result.msg, "warning");
                    }, 100);
                    //2秒后刷新页面，足够显示swal()的信息
                    // setTimeout(function () {
                    //     window.location.href = '/registerUserIndex';
                    // }, 2000);
                }
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
                    setTimeout(function () {
                        swal("提示", Result.msg, "success");
                    }, 100);
                    //2秒后刷新页面，足够显示swal()的信息
                    setTimeout(function () {
                        loadPageForRegister('/user/users');
                    }, 2000);
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
            setTimeout(function () {
                swal("提示", result.msg, "success");
            }, 100);
            //2秒后刷新页面，足够显示swal()的信息
            setTimeout(function () {
                loadPageForRegister('/user/users');
            }, 2000);
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
                            setTimeout(function () {
                                swal("提示", data.msg, "success");
                            }, 100);
                            //2秒后刷新页面，足够显示swal()的信息
                            setTimeout(function () {
                                loadPageForRegister('/blog/list');
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
                            setTimeout(function () {
                                swal("提示", data.msg, "success");
                            }, 100);
                            //2秒后刷新页面，足够显示swal()的信息
                            setTimeout(function () {
                                loadPageForRegister('/blog/examine');
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
 * 保存博文
 */
function saveBlog() {
    var form = document.getElementById("blogForm");
    var data = new FormData(form);
    var releaseTime = $("#releaseTime").val();
    var img = document.getElementsByClassName("img")[0].src;
    if (releaseTime == '' || releaseTime == undefined || releaseTime == null) {
        swal("发布时间必填");
        return;
    }
    if ("http://localhost:8090/background/assets/img/blog_default.png" == img) {
        swal('图片不能为空');
        return;
    } else {
        $.ajax({
            type: "POST",
            url: "/blog",
            data: data,
            contentType: false,
            processData: false,// 注意这里应设为false
            success: function (Result) {
                if (Result.success === true) {
                    if (Result.msg == "提交更新成功") {
                        setTimeout(function () {
                            swal("提示", Result.msg, "success");
                        }, 100);
                        //2秒后刷新页面，足够显示swal()的信息
                        setTimeout(function () {
                            loadPageForRegister('/blog/list');
                        }, 2000);
                        // loadPageForRegister('/blog/list');
                        // alert(Result.msg);
                    } else {
                        setTimeout(function () {
                            swal("提示", Result.msg, "warning");
                        }, 100);
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
    if (val == '' || val == undefined || val == null) {
        swal('搜索内容不能为空');
        return
    }
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
    var id = $(this).attr('id');
    swal({
            title: "确认要删除该图片数据？",
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
                    url: "/banner/delete?id=" + id,
                    type: 'DELETE',
                    dataType: "json",
                    success: function (data) {
                        setTimeout(function () {
                            swal("提示", data.msg, "success");
                        }, 100);
                        //2秒后刷新页面，足够显示swal()的信息
                        setTimeout(function () {
                            loadPageForRegister('/banner/list');
                        }, 2000);
                    },
                    error: function () {
                        swal("错误信息为：" + data.msg);
                    }
                });
            } else {
                swal("取消！", "你的文件是安全的:)",
                    "error");
            }
        });

});

/**
 * 提交Banner
 */
function saveBanner() {
    var form = document.getElementById("bannerEditForm");
    var data = new FormData(form);
    // var img = data.get("img");
    var img = document.getElementsByClassName("img")[0].src;
    var name = $("#name").val();
    if ("http://localhost:8090/background/assets/img/blog_default.png" == img || "" == name) {
        swal('图片/图片名称不能为空');
        return;
    }
    {
        swal('图片上传中，请稍等...');
        $.ajax({
            type: "POST",
            url: "/banner/add",
            data: data,
            dataType: "json",
            contentType: false,
            processData: false,// 注意这里应设为false
            success: function (Result) {
                if (Result.success === true) {
                    setTimeout(function () {
                        swal("提示", Result.msg, "success");
                    }, 100);
                    //2秒后刷新页面，足够显示swal()的信息
                    setTimeout(function () {
                        loadPageForRegister('/banner/list');
                    }, 2000);
                    // alert(Result.msg);
                    // loadPageForRegister('/banner/list');
                } else {
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
 * 后台提交Print---------------------------------------------------
 */
function savePrint() {
    var form = document.getElementById("printAddForm");
    var data = new FormData(form);
    // var img = data.get("img");
    var filepath = $("#filepath").val();
    var filename = $("#filename").val();
    if ("" == filename || "" == filepath) {
        swal('文件/文件名称不能为空');
    } else {
        swal("提示", "上传中...请稍等");
        //2秒后刷新页面，足够显示swal()的信息
        $.ajax({
            type: "POST",
            url: "/print/add",
            data: data,
            dataType: "json",
            contentType: false,
            processData: false,// 注意这里应设为false
            success: function (Result) {
                if (Result.success === true) {
                    setTimeout(function () {
                        swal("提示", Result.msg, "success");
                    }, 100);
                    //2秒后刷新页面，足够显示swal()的信息
                    var page = Result.body;
                    // setTimeout(function(){ loadPageForRegister('/print/list'); },2000);
                    setTimeout(function () {
                        loadPageForRegister('/print/pay?page=' + page);
                    }, 2000);
                } else {
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
 * 前台提交Print---------------------------------------------------
 */
function upSavePrint() {
    var form = document.getElementById("printAddForm");
    var data = new FormData(form);
    var filepath = $("#filepath").val();
    var filename = $("#filename").val();
    if ("" == filename || "" == filepath) {
        swal('文件/文件名称不能为空');
    } else {
        swal("提示", "上传中...请稍等");
        $.ajax({
            type: "POST",
            url: "/print/add",
            data: data,
            dataType: "json",
            contentType: false,
            processData: false,// 注意这里应设为false
            success: function (Result) {
                if (Result.success === true) {
                    setTimeout(function () {
                        swal("提示", Result.msg, "success");
                    }, 100);
                    var page = Result.body;
                    $("#printContent").load('/print/upPay?page=' + page);
                } else {
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
 * 提交Print付款---------------------------------------------------
 */
function pay() {
        swal("提示", "正在打印中...请稍等", "success");
    //2秒后刷新页面，足够显示swal()的信息
    setTimeout(function () {
        swal("提示", "打印成功，我们将尽快送至您的宿舍", "success");
    }, 2000);

}


/**
 * 删除print
 */
$('.del-print').click(function () {
    var id = $(this).attr('id');
    swal({
            title: "确认要删除该条打印数据？",
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
                    url: "/print/delete?id=" + id,
                    type: 'DELETE',
                    dataType: "json",
                    success: function (data) {
                        setTimeout(function () {
                            swal("提示", data.msg, "success");
                        }, 100);
                        //2秒后刷新页面，足够显示swal()的信息
                        setTimeout(function () {
                            loadPageForRegister('/print/list');
                        }, 2000);
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


/**
 * 更新Banner
 */
function updateBanner() {
    var form = document.getElementById("bannerEditForm");
    var data = new FormData(form);
    swal('图片上传中，请稍等...');
    $.ajax({
        type: "POST",
        url: "/banner/update",
        data: data,
        dataType: "json",
        contentType: false,
        processData: false,// 注意这里应设为false
        success: function (data) {
            setTimeout(function () {
                swal("提示", data.msg, "success");
            }, 100);
            //2秒后刷新页面，足够显示swal()的信息
            setTimeout(function () {
                loadPageForRegister('/banner/list');
            }, 2000);
        }
    });
}

/**
 * 上传图片及时刷新显示
 */
function reads(obj) {
    //判断文件格式
    var fileName = obj.value;
    var suffixIndex = fileName.lastIndexOf(".");
    var suffix = fileName.substring(suffixIndex + 1).toUpperCase();
    if (suffix != "BMP" && suffix != "JPEG" && suffix != "JPG" && suffix != "PNG" && suffix != "GIF") {
        swal("请上传BMP、JPG、JPEG、PNG、GIF格式的内容，且文件最大不超过10M");
        return;
    } else {
        var file = obj.files[0];
        var reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = function (ev) {
            $("#img").attr("src", ev.target.result);
            $("#img").css({"width": "60%", "height": "auto"});
        }
    }
}

/**
 * 前台上传print文件处理
 */
function handleFileUP(obj) {
    //判断文件格式
    var fileName = obj.value;
    var suffixIndex = fileName.lastIndexOf(".");
    var suffix = fileName.substring(suffixIndex + 1).toUpperCase();
    if (suffix != "PDF" && suffix != "DOC" && suffix != "DOCX") {
        swal("请上传文件（格式PDF、DOC、DOCX等）!");
        return;
    }
    $('#filepath').val($('#uploadFile').val());
}

/**
 * 后台上传print文件处理
 */
function handleFileBackground(obj) {
    var file = document.getElementById("uploadFile");
    var filepath = document.getElementById("filepath");
    //判断文件格式
    var fileName = obj.value;
    var suffixIndex = fileName.lastIndexOf(".");
    var suffix = fileName.substring(suffixIndex + 1).toUpperCase();
    if (suffix != "PDF" && suffix != "DOC" && suffix != "DOCX") {
        swal("请上传文件（格式PDF、DOC、DOCX等）!");
        return;
    }
    filepath.value = file.value;
}


/*------------------------------------评论---------------------------------*/

// 根据评论内容查询
function getCommentByName() {
    if ($("#searchName").val()==null||""==$("#searchName").val()) {
        swal("搜索内容信息不能为空！");
        return;
    }
    $.ajax({
        url: "/comments/commentSearch?page=0&size=10",
        contentType: 'application/json',
        data: {
            // "async": true,
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
    var blogId = $(this).attr('blogId');
    // var blogId = $('.blogId').attr('id');
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
        // dataType: "json",
        contentType: false,
        // contentType: "application/json;charset=UTF-8",
        processData: false,// 注意这里应设为false
        data: data,
        // data: {"contactUsername": contactUsername, "email": email,"message": message},
        success: function (data) {
            if (data.success) {
                setTimeout(function () {
                    swal("提示", data.msg, "success");
                }, 100);
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
    swal({
            title: "确认要删除该行留言？",
            text: "你将无法恢复该留言！",
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
                    url: "/message/delete?id=" + $(this).attr('id'),
                    type: "DELETE",
                    dataType: "json",
                    success: function (data) {
                        // swal(data.msg);
                        setTimeout(function () {
                            swal("提示", data.msg, "success");
                        }, 100);
                        setTimeout(function () {
                            loadPageForRegister('/message/list');
                        }, 2000);
                        // loadPageForRegister('/message/list');
                    }
                });
            } else {
                swal("取消！", "你的虚拟文件是安全的:)",
                    "error");
            }

        });
});



