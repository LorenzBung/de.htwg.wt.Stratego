window.onload = function() {
    // Callback function to set a figure
    let set = function(r, c) {
        if ($("#row" + r + "col" + c).html().trim() === "") {
            $.get("/set/" + r + "/" + c, function(data) {
                if (data === "F") {
                    data = "\uD83C\uDFF4";
                } else if (data === "B") {
                    data = "\uD83D\uDCA3";
                }
                $("#row" + r + "col" + c).html(data);
            });
        } else {
            $.get("/unset/" + r + "/" + c, function(data) {
                $("#row" + r + "col" + c).html(data);
            });
        }
    };
    // Add listeners to each field for set function
    for (row = 1; row <= 10; row++) {
        for (col = 1; col <= 10; col++) {
            $("#row" + row + "col" + col).click((function(i, j){
                return function(){set(i, j)};
            }(row, col)));
        }
    }
    // Add listener for dropdown menu elements to select figure
    $(".selectbuttons").children().each(function(a, child) {
        let strength = $(child).attr("select");
        $(child).click((function(i){
            return function(){$.get("/select/" + i)};
        }(strength)));
    });
    // Add listener for new game button
    $("#newGameButton").click(function() {
        $.get("/new", function(data){
            $("html").html(data);
        });
    });
};
