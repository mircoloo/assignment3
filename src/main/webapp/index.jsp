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

    var currentSelected;
    var input = document.querySelector("#input-cell");
    function highlightBorder(e){
        currentSelected = e.target
        input.value = currentSelected.innerText;
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
        currentSelected.innerText = e.target.value;
        let xhttp = new XMLHttpRequest();

        xhttp.onreadystatechange = function (){
            var jsonResponse = this.response;
            updateCells(jsonResponse)
        }
        xhttp.open("POST", "ChangedCellServlet", true);
        xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        let req_string = "id=" + currentSelected.id.toString() + "&formula=" +  input.value;
        xhttp.send(req_string);
    }

    function reflectInput(e) {
        currentSelected.innerText = input.value;
    }

    function updateCells(data){
            try{
                if(data != ""){
                    const obj = JSON.parse(data);
                    for (let [key, value] of Object.entries(obj)) {
                        console.log("->" ,key, value);
                        document.querySelector("#" + key).innerText = value
                    }
                }
            }catch (e){
                console.log((e))
            }

    }



    </script>
</body>
</html>