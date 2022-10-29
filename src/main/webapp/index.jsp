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

<input type="text" id="input-cell" onchange="changeInput(event)"></input>
<table border="1">
    <tbody>
    <tr>
        <td id="A1" class="cell" onclick="highlightBorder(event)"></td>
        <td id="B1" class="cell" onclick="highlightBorder(event)"></td>
        <td id="C1" class="cell" onclick="highlightBorder(event)"></td>
        <td id="D1" class="cell" onclick="highlightBorder(event)"></td>
    </tr>
    <tr>
        <td id="A2" class="cell" onclick="highlightBorder(event)"></td>
        <td id="B2" class="cell" onclick="highlightBorder(event)"></td>
        <td id="C2" class="cell" onclick="highlightBorder(event)"></td>
        <td id="D2" class="cell" onclick="highlightBorder(event)"></td>
    </tr>
    <tr>
        <td id="A3" class="cell" onclick="highlightBorder(event)"></td>
        <td id="B3" class="cell" onclick="highlightBorder(event)"></td>
        <td id="C3" class="cell" onclick="highlightBorder(event)"></td>
        <td id="D3" class="cell" onclick="highlightBorder(event)"></td>
    </tr>
    <tr>
        <td id="A4" class="cell" onclick="highlightBorder(event)"></td>
        <td id="B4" class="cell" onclick="highlightBorder(event)"></td>
        <td id="C4" class="cell" onclick="highlightBorder(event)"></td>
        <td id="D4" class="cell" onclick="highlightBorder(event)"></td>
    </tr>

    </tbody>
</table>

<script>

    var currentSelected;
    var input = document.querySelector("#input-cell");
    function highlightBorder(e){
        currentSelected = e.target
        input.value = currentSelected.innerText;
        //element.style.borderStyle = "solid blue 4px"
        resetCells();
        currentSelected.style.color = "green"
    }

    function resetCells(){
        let c = document.getElementsByClassName("cell");
        for(let i = 0; i < c.length; i++ ){
            c[i].style.color = "black"
        }
    }

    function changeInput(e){
        console.log(e.target.value)
        currentSelected.innerText = e.target.value;
        let xhttp = new XMLHttpRequest();

        xhttp.onreadystatechange = function (){
            var jsonResponse = this.response;
            //let jsonData = JSON.parse(jsonResponse);
            updateCells(jsonResponse)
        }

        xhttp.open("POST", "ChangedCellServlet", true);
        xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        console.log(input.value.toString())
        let req_string = "id=" + currentSelected.id.toString() + "&formula=" +  input.value;
        xhttp.send(req_string);
    }

    function updateCells(data){
        let toModify = JSON.parse('{"name":"John", "age":30, "city":"New York",}')
        console.log(data);
    }



</script>


</body>
</html>