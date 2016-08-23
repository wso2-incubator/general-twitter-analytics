var ColEle = function () {
    var xxx = $(".cTwitterCard-body p");
    xxx.each(function () {
        $(this).html($(this).text().replace(/(@|#)\w+/g, '<span class="blue">$&</span>'));
    });
};
var NumFor = function (x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
};


var mainCount = function () {

    $.ajax({
        url: "js/topCounts.jag"
        , dataType: "json"
        , type: "GET"
        , success: function (data) {

            // alert(JSON.stringify(data));

            $("#div11").html(NumFor(data[0]));
            $("#div12").html(NumFor(data[1]));
            $("#div1").html(NumFor(data[2]));
            $("#div21").html(NumFor(data[3]));
            $("#div22").html(NumFor(data[4]));
            $("#div2").html(NumFor(data[5]));
        }
        , error: function (e) {
            //console.log("Error in count" + e.responseText);

        }
    });
};


var popularLink = function () {
    $.ajax({
        url: "js/PopularLinkServer.jag"
        , dataType: "json"
        , type: "POST"
        , success: function (data) {
            var table = $("#nt-popularLink");
            table.html(data);
            //ListTruncate();

        }
        , error: function (e) {
            console.log("Error" + e);
        }
    });
};


var ModNews = function () {
    alert('sss');
    jQuery(function ($) {
        $("#nt-news").newsTicker();
    });
    /*
    var nt_Newss = $("#nt-news").newsTicker({
                row_height: 400,
                max_rows: 1,
                duration: 4000,
                prevButton: $('#nt-news-prev'),
                nextButton: $('#nt-news-next')
            })(jQuery);*/
};


var getNews = function () {
    $.ajax({
        url: "js/NewsServer.jag"
        , dataType: "json"
        , type: "POST"
        , success: function (data) {
            //alert("sss");
            var table = $("#nt-example1");
            table.html(data);
            /*						str = data.toString().replace('<br><div style="padding-top:0.8em;"><img alt="" height="1" width="1"></div>','');
            						str = str.toString().replace('<br><div style="padding-top:0.8em;"><img alt="" height="1" width="1"></div>','');
            						str = str.toString().replace('<br><div style="padding-top:0.8em;"><img alt="" height="1" width="1"></div>','');
            						str = str.toString().replace('<br><div style="padding-top:0.8em;"><img alt="" height="1" width="1"></div>','');
            						str = str.toString().replace('<br><div style="padding-top:0.8em;"><img alt="" height="1" width="1"></div>','');
            						*/
            /*str = str.toString().replace('</li>,<li>','</li><li>');		
            						str = str.toString().replace('</li>,<li>','</li><li>');
            						str = str.toString().replace('</li>,<li>','</li><li>');
            						str = str.toString().replace('</li>,<li>','</li><li>');
            						str = str.toString().replace('</li>,<li>','</li><li>');*/
            //var htmlObject = $(str);
            //alert(str);
            //table.html(htmlObject);
            //table.html(data);
            //ModNews();
            //pp();		
        }
        , error: function (er) {
            console.log("Error From News" + er);
        }
    });
};

var popularOnTopic = function () {
    $.ajax({
        url: "js/PopularOnTopic.jag"
        , dataType: "json"
        , contentType: 'application/json'
        , type: "POST"
        , success: function (data) {
            //alert("sss");
            var table = $("#nt-popularElection");

            table.html(data); //***********
        }
        , error: function (er) {
            alert("Error Popular Tweet" + er);
        }
    });
};
/*
var ajaxGarphSentiment = function(ur,TopName,secondName,ChooseName){
var Candidates = { Choose : "BERNIE", Top : "TRUMP" , Second : "CLINTON"};
var width = $("#sentimentGrP").width();
var hight = $("#sentimentGrP").height();
var dateCount=20;
 $.ajax({   
            url: "js/DateCount.jag",
            dataType: "json",    
            type: "POST",
            success: function(data){
            //alert(data);
		//alert("hi");
	    dateCount=data;
            },
	    error: function(er){
		console.log("Error Graph GetDate" + er);
	   }
        });
   

        $.ajax({
            url: ur,
            dataType: "json",    
	    contentType:'application/json',
    	    data: JSON.stringify(Candidates),
            type: "POST",
            success: function(data){
                if(data){ 
			//alert(JSON.stringify(data));
		     text =  {
			  "width": (width*0.75),
			  "height": (hight*0.75),

			   "data": [
			   {
			     "name": "table",
			     "values":JSON.stringify(data),
			     "format": {"type": "json", "parse": {"Date": "date", "Rate": "number", "Candidate": "string"}}
			   }
			 ],
			"signals" :[{

				    "name": "hover",
				    "init": {},
				    "streams": [
					{"type": "symbol:mouseover", "expr": "datum"},
					{"type": "symbol:mouseout", "expr": "{}"}
				    ]
			    }],
			  "scales": [
		  	 {
			     "name": "x",
			     "type":  "time",
			     "range": "width",
			     "zero": false,
			     "domain": {"data": "table", "field": "Date"}
			   },
			    {
			     "name": "y",
			     "type": "linear",
			     "range": "height",
			     "nice": true,
			     "zero": true,
			     "domain": {"data": "table", "field": "Rate"}
			   }
			   ,
			    {
			      "name": "c",
			      "type": "ordinal",
			      "range": "category10",
			      "domain": {"data": "table", "field": "Candidate"}
			    }
			  ],
			   "axes": [
				   {"type": "x", "scale": "x","grid": true ,"title": "Date ","ticks":Number(dateCount),"format":"%d/%m/%Y"},
				   {"type": "y", "scale": "y","grid": true,  "title": "Sentiment Rate from Google Top News"}
				 ],
			  "legends": [
			    {"fill": "c", "title": "Candidate"}
			  ],

			"marks": [
			    {
			      "type": "group",
			      "from": {
				"data": "table",
				"transform": [{"type": "facet", "groupby": ["Candidate"]}]
			      },
			      "marks": [
					{

						    "type": "symbol",
						   
						    "properties": {
						       
						   "update": {
								  "size": {"value": 50},
								"stroke": {"value": "red"},
								"x": {"scale": "x", "field": "Date"},
							    "y": {"scale": "y", "field": "Rate"},
        							 "strokeWidth": {"value": 5}
						   },
						   "hover": {
								"size": {"value": 50},
								"stroke": {"value": "blue"}
							    }
							}

						},
				{
				  "type": "line",
				  "properties": {
				    "update": {
				      "x": {"scale": "x", "field": "Date"},
				      "y": {"scale": "y", "field": "Rate"},
				      "stroke": {"scale": "c", "field": "Candidate"},
				      "strokeWidth": {"value": 2},
                                     
				    },

				"hover": {
				  "fillOpacity": {"value": 0.5}
				}
                                   
				  }
				}
			      ]
			    }

              ,        {
            "type": "group",
            "from": {"data": "table",
                "transform": [
                    {
                        "type": "filter",
                        "test":  "datum.Date == hover.Date"
                    }
                ]},
            "properties": {
                "update": {
                    "x": {"scale": "x", "signal": "hover.Date", "offset": -5},
                    "y": {"scale": "y", "signal": "hover.Rate" , "offset": 20},
                    "width": {"value": 500},
                    "height": {"value": 50},
                    "fill": {"value": "#99CCFF"},
                    "background-color": {"value": 0.85},
                    "stroke": {"value": "#aaa"},
                    "strokeWidth": {"value": 0.5}
                }
            },

            "marks": [
 		{
                    "type": "text",
                    "properties": {
                        "update": {
                            "x": {"value": 6},
                            "y": {"value": 14},
                            "text": {"template": "{{hover.News1}}"},
                            "fill": {"value": "black"},
                            "fontWeight": {"value": "bold"}
                        }
                    }
                },                
		{
                    "type": "text",
                    "properties": {
                        "update": {
                            "x": {"value": 6},
                            "y": {"value": 24},
                            "text": {"template": "{{hover.News2}}"},
                            "fill": {"value": "black"},
                            "fontWeight": {"value": "bold"}
                        }
                    }
                },
                
                {
                    "type": "text",
                    "properties": {
                        "update": {
                            "x": {"value": 6},
                            "y": {"value": 34},
                            "text": {"template": "{{hover.News3}}"},
                            "fill": {"value": "black"},
                            "fontWeight": {"value": "bold"}
                        }
                    }
                }
            ]
        }
			  ]
			};
                     };	
		var viewUpdateFunction = (function(chart) {
		this.view = chart({el:"#sentimentGrP"}).update();
		}).bind(this);
		vg.parse.spec(text, viewUpdateFunction);
            },
	    error: function(er){
		console.log("Error Graph Sentiment" + er);
	   }
        });
    };
*/
var ajaxGarphSentiment = function (sentimentURL, g1p1, g1p2, g2p1, g2p2) {
    //var Candidates = { Choose : "BERNIE", Top : "TRUMP" , Second : "CLINTON"};
    var wit = $("#graph2").width();
    var hight = $("#graph2").height();
    var dateCount = 20;
    $.ajax({
        url: "js/DateCount.jag"
        , dataType: "json"
        , type: "POST"
        , success: function (data) {
            //alert(data);
            //		alert("hi");
            dateCount = data;
        }
        , error: function (er) {
            console.log("Error Graph GetDate" + er);
        }
    });


    $.ajax({
        url: sentimentURL
        , dataType: "json"
        , contentType: 'application/json'
        , //    	    data: JSON.stringify(Candidates),
        type: "POST"
        , success: function (data, k) {
            //                alert("Done");
            function convertData(data) {
                var Fdata = new Array();
                for (var i = 0; i < data.length; i++) {
                    var tem = new Array();
                    var d = new Date(data[i].Date + " 00:00:00");
                    //  alert(data[i].Date);
                    tem.push(d.getTime());
                    //  alert(d.getTime());
                    tem.push(data[i].Candidate);
                    tem.push(data[i].Rate);
                    tem.push(data[i].News1);
                    tem.push(data[i].News2);
                    tem.push(data[i].News3);
                    Fdata.push(tem);


                }

                return Fdata;
            }
            //alert(convertData(data,3));
            //alert(Number(dateCount));
            var data = [
                {
                    "metadata": {
                        "names": ["Date", "Candidate", "Rate", "News1", "News2", "News3"]
                        , "types": ["time", "ordinal", "linear", "ordinal", "ordinal", "ordinal"]
                    }
                    , "data": convertData(data, Number(dateCount))
                          }
                        ];

            if (g1p2 != "g1p2" && g2p2 != "g2p2") {
                var config = {
                    x: "Date"
                    , charts: [
                        {
                            axesColor: "#FFFFFF"
                            , titleFontColor: "#FFFFFF"
                            , legendTitleColor: "#FFFFFF"
                            , legendTextColor: "#FFFFFF"
                            , type: "line"
                            , padding: {
                                "top": 10
                                , "left": 50
                                , "bottom": 100
                                , "right": 100
                            }
                            , xAxisAngle: true
                            , y: "Rate"
                            , color: "Candidate"
                            , colorDomain: [g1p1, g1p2, g2p1, g2p2]
                            , colorScale: ["#fa574b", "#e51000", "#3ec2ee", "#1c40fb"]
                            , tooltip: {
                                "enabled": true
                                , "color": "#e5f2ff"
                                , "type": "symbol"
                                , "content": ["News1", "News2", "News3"]
                                , "label": true
                            }
                                }
                            ]
                    , width: wit * 0.95
                    , height: hight * 0.95
                    , xFormat: "%m/%d/%Y"
                    , xTicks: (Number(dateCount) / 3) * 2

                }
            } else {
                var config = {
                    x: "Date"
                    , charts: [
                        {
                            axesColor: "#FFFFFF"
                            , titleFontColor: "#FFFFFF"
                            , legendTitleColor: "#FFFFFF"
                            , legendTextColor: "#FFFFFF"
                            , type: "line"
                            , padding: {
                                "top": 10
                                , "left": 50
                                , "bottom": 100
                                , "right": 100
                            }
                            , xAxisAngle: true
                            , y: "Rate"
                            , color: "Candidate"
                            , colorDomain: [g1p1, g2p1]
                            , colorScale: ["#fa574b", "#3ec2ee"]
                            , tooltip: {
                                "enabled": true
                                , "color": "#e5f2ff"
                                , "type": "symbol"
                                , "content": ["News1", "News2", "News3"]
                                , "label": true
                            }
                                }
                            ]
                    , width: wit * 0.95
                    , height: hight * 0.95
                    , xFormat: "%m/%d/%Y"
                    , xTicks: (Number(dateCount) / 3) * 2

                }
            }

            var lineChart = new vizg(data, config);
            //console.log(lineChart.getSpec());
            lineChart.draw("#graph2");
            //console.log(lineChart.getSpec());

        }

    });

};




var ajaxGarphTop = function (TopName, secondName, ChooseName) {
    var Candidates = {
        Choose: "BERNIE"
        , Top: "TRUMP"
        , Second: "CLINTON"
    };
    var wit = $("#Tgraph2").width();
    var hight = $("#Tgraph2").height();
    var dateCount = 10;
    //alert("call"); 

    $.ajax({
        url: "js/TopCount.jag"
        , dataType: "json"
        , contentType: 'application/json'
        , data: JSON.stringify(Candidates)
        , type: "POST"
        , success: function (data) {
            //alert("ppp");
            function convertData(data) {
                //console.log(JSON.stringify(data));
                //alert(JSON.stringify(data));
                dateCount = data.length / 4;
                var Fdata = new Array();
                for (var i = 0; i < data.length; i++) {
                    var tem = new Array();
                    var d = new Date(data[i].Date.split(" ")[0] + "T" + data[i].Date.split(" ")[1]);
                    //  alert(data[i].Date);
                    tem.push(d.getTime());
                    //  alert(d.getTime());
                    tem.push(data[i].Candidate);
                    tem.push(data[i].Rate);

                    Fdata.push(tem);


                }

                return Fdata;
            }
            //alert(convertData(data,3));
            //alert(Number(dateCount));
            var data = [
                {
                    "metadata": {
                        "names": ["Date", "Candidate", "Rate"]
                        , "types": ["time", "ordinal", "linear"]
                    }
                    , "data": convertData(data, Number(dateCount))
                          }
                        ];

            var config = {
                x: "Date"
                , charts: [
                    {
                        axesColor: "#FFFFFF"
                        , titleFontColor: "#FFFFFF"
                        , legendTitleColor: "#FFFFFF"
                        , legendTextColor: "#FFFFFF"
                        , type: "line"
                        , padding: {
                            "top": 10
                            , "left": 50
                            , "bottom": 100
                            , "right": 100
                        }
                        , xAxisAngle: true
                        , y: "Rate"
                        , color: "Candidate"
                        , colorDomain: ["TRUMP", "CLINTON", "BERNIE", "CRUZ"]
                        , colorScale: ["#fa574b", "#3ec2ee", "#1c40fb", "#e51000"]
                    }
                          ]
                , width: wit * 0.95
                , height: hight * 0.95
                , xFormat: "%m/%d/%Y"
                , xTicks: (Number(dateCount))

            }

            var lineChart = new vizg(data, config);
            //console.log(lineChart.getSpec());
            lineChart.draw("#Tgraph2");
            //console.log(lineChart.getSpec());

        }

    });

};
