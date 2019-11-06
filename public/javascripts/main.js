window.onload = function() {
    let callback = function(r, c) {
        if (document.getElementById("row" + r + "col" + c).innerHTML.trim() === "") {
            //No figure, proceed to set figure.
            window.location.href = "/set/" + r + "/" + c;
        } else {
            //Figure is already set, unset it.
            window.location.href = "/unset/" + r + "/" + c;
        }
    };
    for (row = 1; row <= 10; row++) {
        for (col = 1; col <= 10; col++) {
            document.getElementById("row" + row + "col" + col).onclick = (function(i, j){
                return function(){callback(i, j)};
            }(row, col));
        }
    }
};
