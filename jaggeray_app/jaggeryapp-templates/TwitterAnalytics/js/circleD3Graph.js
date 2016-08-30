        var diameter = 600;
        var radius = diameter / 2;
        var margin = 50;

        // Generates a tooltip for a SVG circle element based on its ID
        function addTooltip(circle) {
            var x = parseFloat(circle.attr("cx"));
            var y = parseFloat(circle.attr("cy"));
            var r = parseFloat(circle.attr("r"));
            var text = circle.attr("id");

            var tooltip = d3.select("#plot")
                .append("text")
                .text(text)
                .attr("x", x)
                .attr("y", y)
                .attr("dy", -r * 2)
                .attr("id", "tooltip");

            var offset = tooltip.node().getBBox().width / 2;

            if ((x - offset) < -radius) {
                tooltip.attr("text-anchor", "start");
                tooltip.attr("dx", -r);
            } else if ((x + offset) > (radius)) {
                tooltip.attr("text-anchor", "end");
                tooltip.attr("dx", r);
            } else {
                tooltip.attr("text-anchor", "middle");
                tooltip.attr("dx", 0);
            }
        }

        // Draws an arc diagram for the provided undirected graph
        function drawGraph(graph) {
            graph = JSON.parse(graph);
            // create svg image
            var svg = d3.select("body").select("#circle")
                .append("svg")
                .attr("width", diameter)
                .attr("height", diameter);

            // draw border around svg image
            // svg.append("rect")
            //     .attr("class", "outline")
            //     .attr("width", diameter)
            //     .attr("height", diameter);

            // create plot area within svg image
            var plot = svg.append("g")
                .attr("id", "plot")
                .attr("transform", "translate(" + radius + ", " + radius + ")");

            // draw border around plot area
            // plot.append("circle")
            //     .attr("class", "outline")
            //     .attr("r", radius - margin);

            // fix graph links to map to objects instead of indices
            graph.links.forEach(function (d, i) {
                var obj = _.find(graph.nodes, function (obj) {
                    return obj.name == d.source;
                });
                var obj2 = _.find(graph.nodes, function (obj) {
                    return obj.name == d.target;
                })

                d.source = isNaN(d.source) ? obj : graph.nodes[d.source];
                d.target = isNaN(d.target) ? obj2 : graph.nodes[d.target];
            });



            // calculate node positions
            circleLayout(graph.nodes);

            // draw edges first
            //            drawLinks(graph.links);
            drawCurves(graph.links);

            // draw nodes last
            drawNodes(graph.nodes);
        }

        // Calculates node locations
        function circleLayout(nodes) {
            // sort nodes by group
//            nodes.sort(function (a, b) {
//                return a.degree - b.degree;
//            });

            // use to scale node index to theta value
            var scale = d3.scale.linear()
                .domain([0, nodes.length])
                .range([0, 2 * Math.PI]);

            // calculate theta for each node
            nodes.forEach(function (d, i) {
                // calculate polar coordinates
                var theta = scale(i);
                var radial = radius - margin;

                // convert to cartesian coordinates
                d.x = radial * Math.sin(theta);
                d.y = radial * Math.cos(theta);
            });
        }

        // Draws nodes with tooltips
        function drawNodes(nodes) {
            // used to assign nodes color by group
            //var color = d3.scale.category20();

            d3.select("#plot").selectAll(".node")
                .data(nodes)
                .enter()
                .append("circle")
                .attr("class", "node")
                .attr("id", function (d, i) {
                    return d.name;
                })
                .attr("name", function (d, i) {
                    return d.name;
                })
                .attr("cx", function (d, i) {
                    return d.x;
                })
                .attr("cy", function (d, i) {
                    return d.y;
                })
                .attr("r", function (d) {
                    return Math.sqrt(d.degree) / 2;
                })
                .style("fill", function (d, i) {
                    return d.color;
                })
                .on("mouseover", function (d, i) {
                    addTooltip(d3.select(this));
                })
                .on("mouseout", function (d, i) {
                    d3.select("#tooltip").remove();
                });
        }

        // Draws straight edges between nodes
        function drawLinks(links) {
            d3.select("#plot").selectAll(".link")
                .data(links)
                .enter()
                .append("line")
                .attr("class", "link")
                .attr("x1", function (d) {
                    return d.source.x;
                })
                .attr("y1", function (d) {
                    return d.source.y;
                })
                .attr("x2", function (d) {
                    return d.target.x;
                })
                .attr("y2", function (d) {
                    return d.target.y;
                });
        }

        // Draws curved edges between nodes
        function drawCurves(links) {
            // remember this from tree example?
            var curve = d3.svg.diagonal()
                .projection(function (d) {
                    return [d.x, d.y];
                });

            d3.select("#plot").selectAll(".link")
                .data(links)
                .enter()
                .append("path")
                .attr("class", "link")
                .attr("d", curve);
        }
