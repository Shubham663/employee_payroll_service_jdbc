var XMLHttpRequest = require("xmlhttprequest").XMLHttpRequest;
function makePromiseCall(methodType,url,async=true,data=null){
    return new Promise((resolve,reject) => {
        let xhr = new XMLHttpRequest();

        xhr.onreadystatechange = function(){
            if(xhr.readyState == 4){
                if(xhr.status.toString().match('^[2][0-9]{2}$'))
                    resolve(xhr.responseText);
                else if(xhr.status.toString().match('^[4,5][0-9]{2}$')){
                    reject({
                        status: xhr.status,
                        statusText: xhr.statusText
                    });
                    console.log("XHR Failed");
                }
            }
        }
        xhr.open(methodType,url,async);
        if(data){
            xhr.setRequestHeader("Content-Type","application/json");
            xhr.send(JSON.stringify(data));
        }
        else
            xhr.send();
    })
}

const getURL = "http://localhost:3000/employees/2";
// const getUserDetails = (data) => {
//     console.log("The user details as got from get call: " + data);
// };
makePromiseCall("GET",getURL).then(responseText => {
    console.log("The user details as got from get call: " + responseText);
}).catch(error => {
    console.log("GET Error Staus: " + JSON.stringify(error));
});

const postURL = "http://localhost:3000/employees/";
const empData = {"name": "Harry","salary":"5000","id":4};
// const userAdded = (data) => {
//     console.log("The user details added through post call: " + data);
// };
makePromiseCall("POST",postURL,false,empData).then(responseText => {
    console.log("The user details posted through post call: " + responseText);
}).catch(error => {
    console.log("POST Error Staus: " + JSON.stringify(error));
});

const deleteURL = "http://localhost:3000/employees/4"
// const userDeleted = (data) => {
//     console.log("The user details deleted through delete call: " + data);
// };
makePromiseCall("DELETE",deleteURL,true).then(responseText => {
    console.log("The user details deleted through delete call: " + responseText);
}).catch(error => {
    console.log("DELETE Error Staus: " + JSON.stringify(error));
});