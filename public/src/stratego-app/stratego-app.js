window.onload = function() {
    var websocket = undefined;
    // Callback function to set a figure
    let set = function(r, c) {
        if ($("#row" + r + "col" + c).html().trim() === "") {
            $.get("/set/" + r + "/" + c, function(data) {
                /*if (data === "F") {
                    data = "\uD83C\uDFF4";
                } else if (data === "B") {
                    data = "\uD83D\uDCA3";
                }
                $("#row" + r + "col" + c).html(data);*/
            });
        } else {
            $.get("/unset/" + r + "/" + c, function(data) {
                /*$("#row" + r + "col" + c).html(data);*/
            });
        }
    };

    function loadgrid(grid) {
        $("#gameboard").html('<table class="table-bordered" id="gameboard-table">');
        $("#gameboard-table").append('<th width="30px" height="30px"></th>');
        for (let i = 0; i < 10; i++) {
            $("#gameboard-table").append('<th width="30px" height="30px">' + (i + 1) + '</th>');
        }
        for (let i = 0; i < 10; i++) {
            $('#gameboard-table').append('<tr id="row' + i + '"><th width="30px" height="30px">' + (i + 1) + '</th></tr>');
            for (let j = 0; j < 10; j++) {
                if (((j + 1) === 5 || (j + 1) === 6) && ((i + 1) === 3 || (i + 1) === 4 || (i + 1) === 7 || (i + 1) === 8)) {
                    $('#row' + i).append('<td id="row' + (i + 1) + 'col' + (j + 1) + '" width="30px" height="30px">X</td>');
                } else {
                    let entry = grid["cells"][(10 * i + j)];
                    if (entry["cell"] !== undefined) {
                        $('#row' + i).append('<td id="row' + (i + 1) + 'col' + (j + 1) + '" width="30px" height="30px">' + entry["cell"]["strength"] + '</td>');
                    } else {
                        $('#row' + i).append('<td id="row' + (i + 1) + 'col' + (j + 1) + '" width="30px" height="30px"></td>');
                    }
                    $('#row' + (i + 1) + 'col' + (j + 1)).click((function (i, j) {
                        return function () {
                            set(i, j)
                        };
                    }(i + 1, j + 1)));
                }
            }
        }
    }

    function connectWebSocket() {
        websocket = new WebSocket("ws://localhost:9000/websocket");

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
    }

    // Load JSON gameboard
    $.get("/json", function(data) {
        loadgrid(data);
    });

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
};

class ChessApp extends LitElement {
    static get properties() {
        return;
    }
    constructor() {
        super();
    }

    static get styles() {
        return [
            super.styles,
            css`
            * {
              font-family: Helvetica, Arial, sans-serif;
            }

            #gameview-wrapper {
              width: 70%;
              margin: 0 auto;
              text-align: center;
            }
            #gameview-wrapper table {
              margin: 0 auto;
              border: 1px solid black;
              border-collapse: collapse;
            }
            #gameview-wrapper table th, #gameview-wrapper table td {
              width: 20px;
              height: 20px;
              border: 1px solid black;
            }
            #gameview-wrapper table {
              cursor: pointer;
            }

            #rule-wrapper, #history-wrapper {
              width: 70%;
              margin: 0 auto;
            }
            #rule-wrapper h1, #history-wrapper h1 {
              text-align: center;
            }

            blockquote {
              background-color: lightgray;
              padding: .5em;
            }
            `
        ];
    }

    render() {
        return html`
            <table class="table-bordered" id="gameboard-table">
              <th width="30px" height="30px"></th>
        ${for (let i = 0; i < 10; i++) {
            $("#gameboard-table").append('<th width="30px" height="30px">' + (i + 1) + '</th>');
        }
        for (let i = 0; i < 10; i++) {
            $('#gameboard-table').append('<tr id="row' + i + '"><th width="30px" height="30px">' + (i + 1) + '</th></tr>');
            for (let j = 0; j < 10; j++) {
                if (((j + 1) === 5 || (j + 1) === 6) && ((i + 1) === 3 || (i + 1) === 4 || (i + 1) === 7 || (i + 1) === 8)) {
                    $('#row' + i).append('<td id="row' + (i + 1) + 'col' + (j + 1) + '" width="30px" height="30px">X</td>');
                } else {
                    let entry = grid["cells"][(10 * i + j)];
                    if (entry["cell"] !== undefined) {
                        $('#row' + i).append('<td id="row' + (i + 1) + 'col' + (j + 1) + '" width="30px" height="30px">' + entry["cell"]["strength"] + '</td>');
                    } else {
                        $('#row' + i).append('<td id="row' + (i + 1) + 'col' + (j + 1) + '" width="30px" height="30px"></td>');
                    }
                    $('#row' + (i + 1) + 'col' + (j + 1)).click((function (i, j) {
                        return function () {
                            set(i, j)
                        };
                    }(i + 1, j + 1)));
                }
            }
        }}
        `;
    }
}

customElements.define('stratego-app', StrategoApp);
