$(document).ready(function () {

    var strategoSelector = new Vue({
        el: '#stratego-selector-wrapper'
    });


    var strategoGame = new Vue({
        el:'#stratego-game-wrapper'
    });

});


Vue.component('stratego-selector', {
    template:`<div class="dropdown">
                <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    Select figure
                </button>
                <div class="dropdown-menu selectbuttons" aria-labelledby="dropdownMenuButton">
                    <a class="dropdown-item" select="0">Flag (&#127988;)</a>
                    <a class="dropdown-item" select="1">Spy (1)</a>
                    <a class="dropdown-item" select="2">Scout (2)</a>
                    <a class="dropdown-item" select="3">Miner (3)</a>
                    <a class="dropdown-item" select="4">Sergeant (4)</a>
                    <a class="dropdown-item" select="5">Lieutenant (5)</a>
                    <a class="dropdown-item" select="6">Captain (6)</a>
                    <a class="dropdown-item" select="7">Major (7)</a>
                    <a class="dropdown-item" select="8">Colonel (8)</a>
                    <a class="dropdown-item" select="9">General (9)</a>
                    <a class="dropdown-item" select="10">Marshal (10)</a>
                    <a class="dropdown-item" select="11">Bomb (&#128163;)</a>
                </div>
                <button class="btn btn-primary" id="newGameButton">New Game</button>
            </div>`
});


Vue.component('stratego-game', {
    template: `<table class="table-bordered" id="gameboard-table"><th width="30px" height="30px"></th><th width="30px" height="30px">1</th><th width="30px" height="30px">2</th><th width="30px" height="30px">3</th><th width="30px" height="30px">4</th><th width="30px" height="30px">5</th><th width="30px" height="30px">6</th><th width="30px" height="30px">7</th><th width="30px" height="30px">8</th><th width="30px" height="30px">9</th><th width="30px" height="30px">10</th><tr id="row0"><th width="30px" height="30px">1</th><td id="row1col1" width="30px" height="30px"></td><td id="row1col2" width="30px" height="30px"></td><td id="row1col3" width="30px" height="30px"></td><td id="row1col4" width="30px" height="30px"></td><td id="row1col5" width="30px" height="30px"></td><td id="row1col6" width="30px" height="30px"></td><td id="row1col7" width="30px" height="30px"></td><td id="row1col8" width="30px" height="30px"></td><td id="row1col9" width="30px" height="30px"></td><td id="row1col10" width="30px" height="30px"></td></tr><tr id="row1"><th width="30px" height="30px">2</th><td id="row2col1" width="30px" height="30px"></td><td id="row2col2" width="30px" height="30px"></td><td id="row2col3" width="30px" height="30px"></td><td id="row2col4" width="30px" height="30px"></td><td id="row2col5" width="30px" height="30px"></td><td id="row2col6" width="30px" height="30px"></td><td id="row2col7" width="30px" height="30px"></td><td id="row2col8" width="30px" height="30px"></td><td id="row2col9" width="30px" height="30px"></td><td id="row2col10" width="30px" height="30px"></td></tr><tr id="row2"><th width="30px" height="30px">3</th><td id="row3col1" width="30px" height="30px"></td><td id="row3col2" width="30px" height="30px"></td><td id="row3col3" width="30px" height="30px"></td><td id="row3col4" width="30px" height="30px"></td><td id="row3col5" width="30px" height="30px">X</td><td id="row3col6" width="30px" height="30px">X</td><td id="row3col7" width="30px" height="30px"></td><td id="row3col8" width="30px" height="30px"></td><td id="row3col9" width="30px" height="30px"></td><td id="row3col10" width="30px" height="30px"></td></tr><tr id="row3"><th width="30px" height="30px">4</th><td id="row4col1" width="30px" height="30px"></td><td id="row4col2" width="30px" height="30px"></td><td id="row4col3" width="30px" height="30px"></td><td id="row4col4" width="30px" height="30px"></td><td id="row4col5" width="30px" height="30px">X</td><td id="row4col6" width="30px" height="30px">X</td><td id="row4col7" width="30px" height="30px"></td><td id="row4col8" width="30px" height="30px"></td><td id="row4col9" width="30px" height="30px"></td><td id="row4col10" width="30px" height="30px"></td></tr><tr id="row4"><th width="30px" height="30px">5</th><td id="row5col1" width="30px" height="30px"></td><td id="row5col2" width="30px" height="30px"></td><td id="row5col3" width="30px" height="30px"></td><td id="row5col4" width="30px" height="30px"></td><td id="row5col5" width="30px" height="30px"></td><td id="row5col6" width="30px" height="30px"></td><td id="row5col7" width="30px" height="30px"></td><td id="row5col8" width="30px" height="30px"></td><td id="row5col9" width="30px" height="30px"></td><td id="row5col10" width="30px" height="30px"></td></tr><tr id="row5"><th width="30px" height="30px">6</th><td id="row6col1" width="30px" height="30px"></td><td id="row6col2" width="30px" height="30px"></td><td id="row6col3" width="30px" height="30px"></td><td id="row6col4" width="30px" height="30px"></td><td id="row6col5" width="30px" height="30px"></td><td id="row6col6" width="30px" height="30px"></td><td id="row6col7" width="30px" height="30px"></td><td id="row6col8" width="30px" height="30px"></td><td id="row6col9" width="30px" height="30px"></td><td id="row6col10" width="30px" height="30px"></td></tr><tr id="row6"><th width="30px" height="30px">7</th><td id="row7col1" width="30px" height="30px"></td><td id="row7col2" width="30px" height="30px"></td><td id="row7col3" width="30px" height="30px"></td><td id="row7col4" width="30px" height="30px"></td><td id="row7col5" width="30px" height="30px">X</td><td id="row7col6" width="30px" height="30px">X</td><td id="row7col7" width="30px" height="30px"></td><td id="row7col8" width="30px" height="30px"></td><td id="row7col9" width="30px" height="30px"></td><td id="row7col10" width="30px" height="30px"></td></tr><tr id="row7"><th width="30px" height="30px">8</th><td id="row8col1" width="30px" height="30px"></td><td id="row8col2" width="30px" height="30px"></td><td id="row8col3" width="30px" height="30px"></td><td id="row8col4" width="30px" height="30px"></td><td id="row8col5" width="30px" height="30px">X</td><td id="row8col6" width="30px" height="30px">X</td><td id="row8col7" width="30px" height="30px"></td><td id="row8col8" width="30px" height="30px"></td><td id="row8col9" width="30px" height="30px"></td><td id="row8col10" width="30px" height="30px"></td></tr><tr id="row8"><th width="30px" height="30px">9</th><td id="row9col1" width="30px" height="30px"></td><td id="row9col2" width="30px" height="30px"></td><td id="row9col3" width="30px" height="30px"></td><td id="row9col4" width="30px" height="30px"></td><td id="row9col5" width="30px" height="30px"></td><td id="row9col6" width="30px" height="30px"></td><td id="row9col7" width="30px" height="30px"></td><td id="row9col8" width="30px" height="30px"></td><td id="row9col9" width="30px" height="30px"></td><td id="row9col10" width="30px" height="30px"></td></tr><tr id="row9"><th width="30px" height="30px">10</th><td id="row10col1" width="30px" height="30px"></td><td id="row10col2" width="30px" height="30px"></td><td id="row10col3" width="30px" height="30px"></td><td id="row10col4" width="30px" height="30px"></td><td id="row10col5" width="30px" height="30px"></td><td id="row10col6" width="30px" height="30px"></td><td id="row10col7" width="30px" height="30px"></td><td id="row10col8" width="30px" height="30px"></td><td id="row10col9" width="30px" height="30px"></td><td id="row10col10" width="30px" height="30px"></td></tr></table>`
});