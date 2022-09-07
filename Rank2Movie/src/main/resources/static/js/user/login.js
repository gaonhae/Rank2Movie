function login()
{
    $.ajax({
        url : "/user/logincheck",
        data : {"id" : $("#id").val(), "pw" : $("#password").val()},
        method : "POST",
        success : (message) =>
        {
            if(message == "success") $("#loginForm").submit();
            else alert(message);
        },
        error : e => alert(e.responseText)

    });
}