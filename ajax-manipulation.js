var XMLHttpRequest = require("xmlhttprequest").XMLHttpRequest;
function makeAjaxCall(methodType,url,callback,async=true,data=null){
    let xhr = new XMLHttpRequest();

    xhr.onreadystatechange = function(){
        if(xhr.readyState == 4){
            if(xhr.status == 200 || xhr.status == 201)
                callback("The response obtained is " + xhr.responseText);
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
const getUserDetails = (data) => {
    console.log("The user details as got from get call: " + data);
};
makeAjaxCall("GET",getURL,getUserDetails);

const postURL = "http://localhost:3000/employees/";
const empData = {"name": "Harry","salary":"5000","id":4};
const userAdded = (data) => {
    console.log("The user details added through post call: " + data);
};
makeAjaxCall("POST",postURL,userAdded,false,empData);

const deleteURL = "http://localhost:3000/employees/4"
const userDeleted = (data) => {
    console.log("The user details deleted through delete call: " + data);
};
makeAjaxCall("DELETE",deleteURL,userDeleted,true);