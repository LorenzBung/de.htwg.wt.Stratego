$(document).ready(function () {
    var websocket = undefined;
    // Callback function to set a figure
    let set = function(r, c) {
        if ($("#row" + r + "col" + c).html().trim() === "") {
            $.get("/set/" + r + "/" + c, function(data) {
            });
        } else {
            $.get("/unset/" + r + "/" + c, function(data) {
            });
        }
    };


    function loadgrid(grid) {
        for (let i = 0; i < 10; i++) {
            for (let j = 0; j < 10; j++) {
                if (((j + 1) === 5 || (j + 1) === 6) && ((i + 1) === 3 || (i + 1) === 4 || (i + 1) === 7 || (i + 1) === 8)) {
                    $('#row' + (i + 1) + 'col' + (j + 1)).html('X');
                } else {
                    let entry = grid["cells"][(10 * i + j)];
                    if (entry["cell"] !== undefined) {
                        $('#row' + (i + 1) + 'col' + (j + 1)).html(entry["cell"]["strength"]);
                    } else {
                        $('#row' + (i + 1) + 'col' + (j + 1)).html('');
                    }
                }
            }
        }
    };

    function addListener() {
        for (let i = 0; i < 10; i++) {
            for (let j = 0; j < 10; j++) {
                $('#row' + (i + 1) + 'col' + (j + 1)).click((function (i, j) {
                    return function () {
                        set(i, j)
                    };
                }(i + 1, j + 1)));
            }
        }
    };


    function connectWebSocket() {
        let wsurl = location.origin.replace(/^http/, 'ws') + "/websocket";
        websocket = new WebSocket(wsurl);

        websocket.onopen = function(event) {
            console.log("Connected to Websocket");
            return websocket;
        };

        websocket.onclose = function () {
            console.log('Connection with Websocket Closed!');
            connectWebSocket();
        };

        websocket.onerror = function (error) {
            console.log('Error in Websocket Occured: ' + error);
        };

        websocket.onmessage = function (e) {
            if (typeof e.data === "string") {
                let json = JSON.parse(e.data);
                loadgrid(json);
            }

        };
    };

    // Load JSON gameboard
    $.get("/json", function(data) {
        loadgrid(data);
    });
    addListener();

    // Connect websocket
    connectWebSocket();

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
            loadgrid(data);
        });
    });
});
