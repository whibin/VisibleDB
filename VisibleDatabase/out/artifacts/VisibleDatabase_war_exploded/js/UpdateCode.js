window.onload = function () {
    let element = document.getElementById("check");
    element.onclick = function () {
        element.src = "/VisibleDatabase_war_exploded/code?a=" + new Date().getTime();
    }
}