// function hidePhoto() {
//     document.getElementById()
// }

function hidePhoto() {
    let elements = document.getElementsByName("postimg");
    for(let i=0; i<elements.length; i++) {
        if(elements[i].getAttribute("src") === "none"){
            elements[i].style.display = "none";
        }
    }
}
