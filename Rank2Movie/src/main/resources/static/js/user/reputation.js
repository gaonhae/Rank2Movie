// Create, Update
function repute(userNo, reputation)
{

    $.ajax({
        url : "/user/repute",
        data : {"userNo" : userNo, "reputation" : reputation},
        method : "POST",
        success : (data) =>
        {
            if(data == "success") getUserReputation(userNo);
            else alert(data);
        },
        error : e => alert(e.responseText)

    });

}

// Read
function getUserReputation(userNo)
{
    $.ajax({
        url : "/user/getreputation",
        data : {"userNo" : userNo},
        method : "GET",
        success : data => {
            $("#userPositive").text(data.positive);
            $("#userNegative").text(data.negative);
            $("#userPositive").css("background", "white");
            $("#userNegative").css("background", "white");
            if(data.yours == 1) $("#userPositive").css("background", "green");
            else if(data.yours == -1) $("#userNegative").css("background", "red");

        },
        error : e => alert(e.responseText)

    });

}