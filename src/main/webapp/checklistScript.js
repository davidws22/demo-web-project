/* 
Accepts a JSON string and turns that into a checklist that consists of clubs and checkboxes

---notes---
    - Because we are still working on the database I have creates a false JSON string objects that becomes parsed and is used.
    - Later when we connect servers the parameters of this method will change so that it has "(var jsonText)" where it accepts a json string
        - also at that point we need to remove my placeholder JSON string
    - the json string MUST!!!! be written as such
        -"{"name":[list of clubs], "check"[list of booleans that tells me if the user has previously chosen to select this option or not]}
    
    **--NEED TO KNOWS--**
    - when writing the code in HTML, this will look for the ID "checklist" and write everything in there.
        -make sure that i have a <p> or a <div> with the ID of "checklist"
    - this implements the "style.css" so include it in the HTML file
*/
function onLoadMake(){
    var jsonString = getAJAXString("/events/org");
    makeCheckList(jsonString);
}

function makeCheckList(var jsonString){    
    document.getElementById("checklist").innerHTML ="";
    var jsonObj = JSON.parse(jsonString);
    
    var table = document.createElement("table");
    table.align = "center";
    var tbody = document.createElement("tbody");
    var tr;
    
    for(var i =0; i<jsonObj.length ; i++){
        var butt = document.createElement("input");
        var buttTag = document.createElement("label");
        
        butt.type = "checkbox";
        butt.id = jsonObj[i].orgId;
        butt.name = jsonObj[i].orgName;
        butt.checked = jsonObj[i].check;
        butt.className = "css-checkbox";
        
        buttTag.setAttribute("for" , jsonObj[i].orgId);
        buttTag.className = "css-label";
        buttTag.innerHTML = jsonObj[i].orgName;
        
        if( i%3 == 0){
            tr = document.createElement("tr");
            tbody.append(tr);
        }
        var td = document.createElement("td");
        td.append(butt);
        td.append(buttTag);
        tr.append(td);
        
        if(i+1 == jsonObj.length && i%3 != 0)
            tbody.append(tr);        
    }
    table.append(tbody)
    document.getElementById("checklist").append(table);

}
function getAJAXString(var URLExtention){
    var request= new XMLHttpRequest();
    
    var getUrl = window.location;
    var baseUrl = getUrl.protocol + '//' + getUrl + '/' + getUrl.pathname.split('/')[1];
    
    baseUrl += URLExtention;
    request.open('GET', baseUrl);
    return request.responseText;
}


/*
    scans though the HTML document and collects data to send to the database in JSON format
    
    --note--
        - Because theres no way to send data to the database yet this will use the "window.alert" function to show what would have been sent. 
        - when connected to the database make sure that "return JSON.stringify(sendMe);" is written at the end of this
            - also remove the "window.alert" when that time comes
        --- i still dont know how to send files to the server so this function only creates a JSON string from the HTML code
            
    **--NEED TO KNOW--**
        - this will send a JSON string to the server in the same format that was requested
            - "{"name":[list of clubs], "check"[list of booleans that tells me if the user has previously chosen to select this option or not]}
        - this is used when the "submit" button is hit and a JSON string needs to be generated to be sent to the server       
*/
function sendJSONStringToServer(){
    var jsonString = {"orgIds":[]};
    
    var orgList = document.getElementsByTagName("input");
    
    for(var i=0; i< orgList.length ; i++){
        if(orgList[i].checked){
            var orgId = orgList[i].getAttribute("id");            
            var orgName = orgList[i].name;
            
            var setter = {"orgId":orgId, "orgName":orgName};
            
            jsonString.orgIds.push(setter);
        }            
    }
    return JSON.stringify(jsonString);
}



/*
Combines all functions so that when submit button is pressed it does all the things it needs to do

--NOTE--
    -- the website needs to be changed to the landing page
        -- currently google.com is placed as a placeholder
*/
function submitButtonPress(){
    var sendToServer = sendJSONStringToServer();
    var request = new XMLHttpRequest();
    var URLExtention = "/events/org";
    var getUrl = window.location;
    var baseUrl = getUrl.protocol + '//' + getUrl + '/' + getUrl.pathname.split('/')[1];
    
    baseUrl += URLExtention;
    request.open('POST', baseUrl);
    
    //input redirection here if need be
    
}


$(window).ready(function(){
$(".boton").wrapInner('<div class=botontext></div>');
    
    $(".botontext").clone().appendTo( $(".boton") );
    
    $(".boton").append('<span class="twist"></span><span class="twist"></span><span class="twist"></span><span class="twist"></span>');
    
    $(".twist").css("width", "25%").css("width", "+=3px");
});

