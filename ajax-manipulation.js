var XMLHttpRequest = require("xmlhttprequest").XMLHttpRequest;
function makeAjaxCall(methodType,url,async=true,data=null){
    let xhr = new XMLHttpRequest();

    xhr.onreadystatechange = function(){
        if(xhr.readyState == 4){
            if(xhr.status == 200 || xhr.status == 201)
                console.log("The response obtained is " + xhr.responseText);
            else
                console.log("Need for handling 400/500 i.e. Client/Server Error");
        }
    }
    xhr.open(methodType,url,async);
    if(data){
        xhr.setRequestHeader("Content-Type","application/json");
        xhr.send(JSON.stringify(data));
    }
    else
        xhr.send();
}

const getURL = "http://localhost:3000/employees/2";
console.log("The User data as obtained through ajax call on url " + getURL);
makeAjaxCall("GET",getURL);

const postURL = "http://localhost:3000/employees/";
const empData = {"name": "Harry","salary":"5000","id":4};
makeAjaxCall("POST",postURL,false,empData);

const deleteURL = "http://localhost:3000/employees/4"
makeAjaxCall("DELETE",deleteURL,true);