var userNo;

function getDetail()
{
    $.ajax({
        url : "/user/",
        data : {"userNo" : getNo()},
        method : "GET",
        success : (data) =>
        {
            // 데이터가 없을 때
            if(JSON.stringify(data) == "{}")
            {
                location.href = "/";
                return ; // 아예 날려버린다.
            }

            userNo = data.userNo;
            $("#name").val(data.userName);
            $("#id").val(data.userId);

            $("#detail").text(data.userDetail);

            if(data.owner == "true")
            {
                $("#owner").text("본인이시군요 :)");
                $("#dangerBox").show();
            }
            else
            {
                $("#owner").text("방문자이신가요? :)");
                $("#dangerBox").hide();
            }

        },
        error : () => alert(e.responseText)
    })
}

let mode = false;
function update()
{
    if(!mode)
    {
        $("#update").text("완료");
        $("#cancel").show();
        $("#delete").hide();

        $("#name").attr("disabled", false);
        $("#id").attr("disabled", false);
        $("#detail").attr("disabled", false);
        mode = !mode;
    }
    else
    {
        var id = $("#id").val();
        var pw = $("#pw").val();
        var name = $("#name").val();
        var detail = $("#detail").val();

        $.ajax({
            url: "/user/",
            data : {"no" : userNo, "id" : id, "pw" : pw, "name" : name, "detail" : detail},
            method : "PUT",
            success : (message) =>
            {
                if(message == "success") cancel();
                else alert(message);
            },
            error : (e) => alert(e.responseText)

        });

    }

}

function remove()
{
    // "예"를 눌렀다면 정말 삭제시킨다.
    if(confirm("정말로 탈퇴하실건가요?"))
    {
        $.ajax({
           url : "/user/",
           data : {"no" : userNo, "pw" : $("#pw").val()},
           method : "DELETE",
           success : (message) =>
            {
                if(message == "success")
                {
                    location.href = "/user/logout";
                }
                else alert(message);

            },
            error : e => alert(e.responseText)
        });
    }
}

function cancel()
{
         $("#update").text("수정");
         $("#cancel").hide();
         $("#delete").show();

         $("#name").attr("disabled", true);
         $("#id").attr("disabled", true);
         $("#detail").attr("disabled", true);
         mode = !mode;
}

function getNo()
 {
     return  document.location.href.split("/")[4];
 }
