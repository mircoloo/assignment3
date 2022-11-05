<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <style>
        .cell{
            cursor: pointer;
            width: 30px;
            height: 30px;
        }
    </style>

    <script type="text/javascript" src="index.js"></script>
    <title>Assignment 3</title>
</head>
<body>
<h1>Mini spreadsheet</h1>
<input type="text" id="input-cell" onchange="changeInput(event)" onkeyup="reflectInput(event)"></input>
<jsp:include page="spreadsheet.jsp"></jsp:include>

<script>



    var cells = [];
    var firstReq = true;
    var currentSelected;
    var input = document.querySelector("#input-cell");


    function highlightBorder(e){
        currentSelected = e.target
        input.value = cells.filter( (c) => c.id == currentSelected.id )[0].formula
        resetCells();
        currentSelected.style.color = "green"
    }

    function resetCells(){
        let c = document.getElementsByClassName("cell");
        for(let i = 0; i < c.length; i++ ){
            c[i].style.color = "black"
        }
    }

    function requestCellsUpdate(){
        console.log(Date.now());
        let xhttp = new XMLHttpRequest();

        xhttp.onreadystatechange = function (){
            var jsonResponse = this.response;
            updateCells(jsonResponse);
        }
        xhttp.open("GET", "ChangedCellServlet", true);
        xhttp.send();
    }



    function changeInput(e){
        currentSelected.innerText = e.target.id
        let xhttp = new XMLHttpRequest();

        xhttp.onreadystatechange = function (){
            var jsonResponse = this.response;
            updateCells(jsonResponse)
        }
        xhttp.open("POST", "ChangedCellServlet");
        xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        let req_string = "id=" + currentSelected.id.toString() + "&formula=" +  input.value + "&timestamp=" + Date.now();
        xhttp.send(req_string);
    }

    function reflectInput(e) {
        //currentSelected.innerText = input.value;
    }



    function updateCells(data){
            try{
                if(data != ""){
                    const jsonParsed = JSON.parse(data);
                    if (firstReq){
                        jsonParsed.map( (cell) => cells.push(cell))
                        firstReq = false
                    }

                    jsonParsed.map( (cellToUpdate) => {
                        let c = cells.filter( (cell) => cell.id == cellToUpdate.id)[0]
                        c.value, document.querySelector("#" + c.id).innerText = cellToUpdate.value;
                        c.formula = cellToUpdate.formula;
                        c.timestamp = cellToUpdate.timestamp;
                    }
                    )
                    //console.log(cells)
                }
            }catch (e){
                console.log((e))
            }

    }



    function checkTimestamps(){

        let xhttp = new XMLHttpRequest();
        var arrayOfObjects = [];


        xhttp.onreadystatechange = function (){
            var jsonResponse = this.response;
            updateCells(jsonResponse)
            //  console.log(jsonResponse);

        }
        xhttp.open("POST", "CheckTimestampServlet");
        xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        cells.map(  ( cell ) => {
            arrayOfObjects.push( { "id" : cell.id, "timestamp": cell.timestamp } )   }  )
        let req_string  = 'objarray=' + JSON.stringify(arrayOfObjects);
        xhttp.send(req_string);

    }

    requestCellsUpdate();
    setInterval(checkTimestamps, 10000);


    </script>
</body>
</html>