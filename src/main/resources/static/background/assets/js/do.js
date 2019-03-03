function loadPage(tag) {
    $("#content").load(tag);
}
// $(function(){
//     // do something
//     alert("do");
// });
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


