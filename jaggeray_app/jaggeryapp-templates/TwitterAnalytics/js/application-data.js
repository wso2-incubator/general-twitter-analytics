var readApplicationData = function (e) {

$.ajax({
        url: "js/application-data.jag"
        , dataType: "json"
        , type: "POST"
        , success: function (data) {
           // alert(data.config.G1_P1)
		var isTwo = false;
            var mainTitle = data.config.Main_Title;
            
            document.getElementById("main-title").innerHTML = mainTitle;   

            var g1p1 = data.config.G1_P1;
            var g1p2 = data.config.G1_P2;
            var g2p1 = data.config.G2_P1;
            var g2p2 = data.config.G2_P2;     

            if (g2p1 == null || g2p2 == null) {
                document.getElementById("fourHeadToHead").remove();
                document.getElementById("T2").remove();
                isTwo = true;
            } else {
                document.getElementById("twoHeadToHead").remove();
            }  
            
            var g1Title = data.config.G1;
            var g2Title = data.config.G2;

            document.getElementsByClassName("g1_title")[0].innerHTML = g1Title;
            document.getElementById("g1_titleWC").innerHTML = g1Title;
            document.getElementsByClassName("g2_title")[0].innerHTML = g2Title;
            document.getElementById("g2_titleWC").innerHTML = g2Title;
                        
            document.getElementById("li-name1").innerHTML = "&nbsp;" + g1p1;
            document.getElementById("li-name3").innerHTML = "&nbsp;" + g2p1;
            document.getElementById("li-name2").innerHTML = "&nbsp;" + g1p2;
            document.getElementById("li-name4").innerHTML = "&nbsp;" + g2p2;
  
            
            if (!isTwo){
                document.getElementById("g1_p1_main").innerHTML = g1p1;
                document.getElementById("g2_p1_main").innerHTML = g2p1;
                document.getElementById("g1_p2_main").innerHTML = g1p2;            
                document.getElementById("g2_p2_main").innerHTML = g2p2;
            }

            if (isTwo){
                ajaxGarphSentiment("js/SentimetGraphServer.jag", g1p1, "g1p2", g2p1, "g2p2");
            } else {
                ajaxGarphSentiment("js/SentimetGraphServer.jag", g1p1, g1p2, g2p1, g2p2);
            }
            
            
            var g1p1HashTags = [];
            var g1p2HashTags = [];
            var g2p1HashTags = [];
            var g2p2HashTags = [];

	    var g1p1HashTagsFormatted = [];
            var g1p2HashTagsFormatted = [];
            var g2p1HashTagsFormatted = [];
            var g2p2HashTagsFormatted = [];
            
            g1p1HashTags=data.config.G1_P1_htag.split(' ');
            g1p2HashTags=data.config.G1_P2_htag.split(' ');
            g2p1HashTags=data.config.G2_P1_htag.split(' ');
            g2p2HashTags=data.config.G2_P2_htag.split(' ');

            for (var i = 0; i < g1p1HashTags.length; i++) {
                g1p1HashTagsFormatted.push("<span>#");
                g1p1HashTagsFormatted.push(g1p1HashTags[i]);
                g1p1HashTagsFormatted.push("</span>");
                if (i == 5) {
                    break;
                }
            }
            for (var i = 0; i < g1p2HashTags.length; i++) {
                g1p2HashTagsFormatted.push("<span>#");
                g1p2HashTagsFormatted.push(g1p2HashTags[i]);
                g1p2HashTagsFormatted.push("</span>");
                if (i == 5) {
                    break;
                }
            }
            for (var i = 0; i <  g2p1HashTags.length; i++) {
                g2p1HashTagsFormatted.push("<span>#");
                g2p1HashTagsFormatted.push(g2p1HashTags[i]);
                g2p1HashTagsFormatted.push("</span>");
                if (i == 5) {
                    break;
                }
            }
            for (var i = 0; i <  g2p2HashTags.length; i++) {
                g2p2HashTagsFormatted.push("<span>#");
                g2p2HashTagsFormatted.push(g2p2HashTags[i]);
                g2p2HashTagsFormatted.push("</span>");
                if (i == 5) {
                    break;
                }
            }

            document.getElementById("cHashTags1").innerHTML = g1p1HashTagsFormatted.toString().split(",").join("");
            document.getElementById("cHashTags3").innerHTML = g2p1HashTagsFormatted.toString().split(",").join("");
            
            if(!isTwo){
                document.getElementById("cHashTags2").innerHTML = g1p2HashTagsFormatted.toString().split(",").join("");
                document.getElementById("cHashTags4").innerHTML = g2p2HashTagsFormatted.toString().split(",").join("");
            } else {
                document.getElementById("li-name2").remove();                
                document.getElementById("li-name4").remove();                
            }
            
            
            
	},
        error: function (e) {
            console.log("XML READER ERROR " + e);
        }
    });

   /* $.ajax({
        url: "js/XMLReader.jag"
        , dataType: "xml"
        , type: "POST"
        , success: function (data) {
            
            var isTwo = false;
            var groupNodesTemp = data.getElementsByTagName("sub-group");
            if (groupNodesTemp[1].getElementsByTagName("hash-tag").length == 0) {
                document.getElementById("fourHeadToHead").remove();
                document.getElementById("T2").remove();
                isTwo = true;
            } else {
                document.getElementById("twoHeadToHead").remove();
            }
            
            var mainTitleElement = data.getElementsByTagName("main-title");
            var mainDescriptionElement = data.getElementsByTagName("main-description");
            var sentimentDescriptionElement = data.getElementsByTagName("sentiment-description");
            var mainTitle = mainTitleElement[0].childNodes[0].nodeValue;
            var mainDescription = mainDescriptionElement[0].childNodes[0].nodeValue;
            var sentimentDescription = sentimentDescriptionElement[0].childNodes[0].nodeValue;
            document.getElementById("main-title").innerHTML = mainTitle;
            if (mainDescription.trim() != "") {
                document.getElementById("main-description").innerHTML = mainDescription;
            }
            if (sentimentDescription.trim() != "") {
                document.getElementById("sentiment-description").innerHTML = sentimentDescription;
            }

            var groupNodes = data.getElementsByTagName("group");
            var g1Title = groupNodes[0].getAttribute("name");
            var g2Title = groupNodes[1].getAttribute("name");

            var defaultHashTag = [];

            document.getElementsByClassName("g1_title")[0].innerHTML = g1Title;
            document.getElementById("g1_titleWC").innerHTML = g1Title;
            document.getElementsByClassName("g2_title")[0].innerHTML = g2Title;
            document.getElementById("g2_titleWC").innerHTML = g2Title;

            var groupNodes = data.getElementsByTagName("sub-group");
            var g1p1 = groupNodes[0].getAttribute("name");
            var g1p2 = groupNodes[1].getAttribute("name");
            var g2p1 = groupNodes[2].getAttribute("name");
            var g2p2 = groupNodes[3].getAttribute("name");
            
            
            document.getElementById("li-name1").innerHTML = "&nbsp;" + g1p1;
            document.getElementById("li-name3").innerHTML = "&nbsp;" + g2p1;
            document.getElementById("li-name2").innerHTML = "&nbsp;" + g1p2;
            document.getElementById("li-name4").innerHTML = "&nbsp;" + g2p2;
            
            if (!isTwo){
                document.getElementById("g1_p1_main").innerHTML = g1p1;
                document.getElementById("g2_p1_main").innerHTML = g2p1;
                document.getElementById("g1_p2_main").innerHTML = g1p2;            
                document.getElementById("g2_p2_main").innerHTML = g2p2;
            }

            if (isTwo){
                ajaxGarphSentiment("js/SentimetGraphServer.jag", g1p1, "g1p2", g2p1, "g2p2");
            } else {
                ajaxGarphSentiment("js/SentimetGraphServer.jag", g1p1, g1p2, g2p1, g2p2);
            }

            var g1p1HashTags = [];
            var g1p2HashTags = [];
            var g2p1HashTags = [];
            var g2p2HashTags = [];

            for (var i = 0; i < groupNodes[0].getElementsByTagName("hash-tag").length; i++) {
                g1p1HashTags.push("<span>#");
                g1p1HashTags.push(groupNodes[0].getElementsByTagName("hash-tag")[i].childNodes[0].nodeValue);
                g1p1HashTags.push("</span>");
                if (i == 5) {
                    break;
                }
            }
            for (var i = 0; i < groupNodes[1].getElementsByTagName("hash-tag").length; i++) {
                g1p2HashTags.push("<span>#");
                g1p2HashTags.push(groupNodes[1].getElementsByTagName("hash-tag")[i].childNodes[0].nodeValue);
                g1p2HashTags.push("</span>");
                if (i == 5) {
                    break;
                }
            }
            for (var i = 0; i < groupNodes[2].getElementsByTagName("hash-tag").length; i++) {
                g2p1HashTags.push("<span>#");
                g2p1HashTags.push(groupNodes[2].getElementsByTagName("hash-tag")[i].childNodes[0].nodeValue);
                g2p1HashTags.push("</span>");
                if (i == 5) {
                    break;
                }
            }
            for (var i = 0; i < groupNodes[3].getElementsByTagName("hash-tag").length; i++) {
                g2p2HashTags.push("<span>#");
                g2p2HashTags.push(groupNodes[3].getElementsByTagName("hash-tag")[i].childNodes[0].nodeValue);
                g2p2HashTags.push("</span>");
                if (i == 5) {
                    break;
                }
            }

            document.getElementById("cHashTags1").innerHTML = g1p1HashTags.toString().split(",").join("");
            document.getElementById("cHashTags3").innerHTML = g2p1HashTags.toString().split(",").join("");
            
            if(!isTwo){
                document.getElementById("cHashTags2").innerHTML = g1p2HashTags.toString().split(",").join("");
                document.getElementById("cHashTags4").innerHTML = g2p2HashTags.toString().split(",").join("");
            } else {
                document.getElementById("li-name2").remove();                
                document.getElementById("li-name4").remove();                
            }

        },


        error: function (e) {
            console.log("XML READER ERROR " + e);
        }
    }); */
};
