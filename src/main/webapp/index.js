
function highlightBorder(e){
    let element = e.target;
    let label = document.querySelector("#input-cell");
    label.innerText = element.innerText;
    //element.style.borderStyle = "solid blue 4px"
    element.style.color = "green"
}