var finalObject;
$(document).ready(function () {


    console.log("ready!");
    var authenticatingString = window.btoa("admin:admin");
    var Nodes = [];
    var Edges = [];
    var dataN1 = [];
    var dataE1 = [];



    var nodeUrl1 = "https://localhost:9443/analytics/tables/NODESWITHOUTDEGREE"
        , edgeUrl1 = "https://localhost:9443/analytics/tables/STROKEWEIGHT";

    $.when(

        $.ajax({

            url: nodeUrl1
            , beforeSend: function (xhr) {
                xhr.setRequestHeader("Authorization", "Basic " + authenticatingString);
            }
            , method: "GET"
            , contentType: "application/json"
            , success: function (data) {

                for (var i = 0; i < data.length; i++) {
                    //                    console.log(data[i]["values"]);
                    var jsonObj = data[i]["values"];
                                        
                    if(jsonObj.outgoungEdges == null){
                        jsonObj.outgoungEdges = 0;
                    }                    
                    if(jsonObj.incomingEdges == null){
                        jsonObj.incomingEdges = 0;
                    }
                    jsonObj.totDegree = jsonObj.outgoungEdges + jsonObj.incomingEdges;
                    Nodes.push(jsonObj);
                }
                    
                    Nodes.sort(compare);
            }
        }),

        $.ajax({

            url: edgeUrl1
            , beforeSend: function (xhr) {
                xhr.setRequestHeader("Authorization", "Basic " + authenticatingString);
            }
            , method: "GET"
            , contentType: "application/json"
            , success: function (data) {
                for (var i = 0; i < data.length; i++) {
                    //                    console.log(data[i]["values"]);
                    var jsonObj = data[i]["values"];
                    Edges.push(jsonObj);

                }
            }
        })

    ).then(function () {

        var finalObject1 = {
            nodes: Nodes
            , links: Edges
        }

        finalObject = JSON.stringify(finalObject1);
    });
});

function compare(a,b) {
  if (a.totDegree < b.totDegree)
    return -1;
  if (a.totDegree > b.totDegree )
    return 1;
  return 0;
}

function getGraph() {
    //    console.log(window.finalObject);
    return window.finalObject;
}
