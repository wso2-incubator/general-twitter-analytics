package org.wso2.siddhi.extension.rssreader;

/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.wso2.siddhi.core.config.ExecutionPlanContext;
import org.wso2.siddhi.core.exception.ExecutionPlanRuntimeException;
import org.wso2.siddhi.core.executor.ExpressionExecutor;
import org.wso2.siddhi.core.executor.function.FunctionExecutor;
import org.wso2.siddhi.query.api.definition.Attribute.Type;
import org.wso2.siddhi.query.api.exception.ExecutionPlanValidationException;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class FullTextReader extends FunctionExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(FullTextReader.class);
    private String G1_P1;
    private String G1_P2;
    private String G2_P1;
    private String G2_P2;
    private String MAIN_TITLE;
    private String urlString;
    private URL rssURL;

    @Override
    public Type getReturnType() {
        return Type.STRING;
    }

    @Override
    public void start() {

        InputStream file = getClass().getResourceAsStream("/htag.xml");

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;

        try {
            dBuilder = dbFactory.newDocumentBuilder();

            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
            NodeList groupNodes = doc.getElementsByTagName("group");
            int count = 0;
            for (int i = 0; i < 2; i++) {
                Element subGroupNodes = (Element) groupNodes.item(i);
                NodeList subGroupList = subGroupNodes.getElementsByTagName("sub-group");

                for (int j = 0; j < subGroupList.getLength(); j++) {

                    switch (count) {
                        case 0:
                            G1_P1 = subGroupList.item(j).getAttributes().getNamedItem("name").toString().substring(6).replace("\"", "");
                            break;
                        case 1:
                            G1_P2 = subGroupList.item(j).getAttributes().getNamedItem("name").toString().substring(6).replace("\"", "");
                            break;
                        case 2:
                            G2_P1 = subGroupList.item(j).getAttributes().getNamedItem("name").toString().substring(6).replace("\"", "");
                            break;
                        case 3:
                            G2_P2 = subGroupList.item(j).getAttributes().getNamedItem("name").toString().substring(6).replace("\"", "");
                            break;
                    }
                    count++;
                }
            }

            NodeList mainTitleNode = doc.getElementsByTagName("main-title");
            MAIN_TITLE = mainTitleNode.item(0).getTextContent().toLowerCase();


        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        // Nothing to do here
    }

    @Override
    public Object[] currentState() {
        // No need to maintain a state
        return null;
    }

    @Override
    public void restoreState(Object[] state) {
        // No need to maintain a state.
    }

    @Override
    protected void init(ExpressionExecutor[] attributeExpressionExecutors, ExecutionPlanContext executionPlanContext) {
        if (attributeExpressionExecutors.length != 1) {
            throw new ExecutionPlanValidationException("Invalid no of arguments passed to TwitterAnalytic:readRSSText() function, "
                    + "required 1, but found " + attributeExpressionExecutors.length);
        }

    }

    @Override
    protected Object execute(Object[] data) {
        Object arr = new Object[0];
        return arr;// No need to maintain a state.
    }

    @Override
    protected Object execute(Object data) {
        StringBuilder formatedString = new StringBuilder();
        if (data.equals("G1_P1")) {
            urlString = "https://news.google.com/news?cf=all&hl=en&pz=1&ned=us&q=" + G1_P1 + "&output=rss";
        } else if (data.equals("G1_P2")) {
            urlString = "https://news.google.com/news?cf=all&hl=en&pz=1&ned=us&q=" + G1_P2 + "&output=rss";
        } else if (data.equals("G2_P1")) {
            urlString = "https://news.google.com/news?cf=all&hl=en&pz=1&ned=us&q=" + G2_P1 + "&output=rss";
        } else if (data.equals("G2_P2")) {
            urlString = "https://news.google.com/news?cf=all&hl=en&pz=1&ned=us&q=" + G2_P2 + "&output=rss";
        } else if (data.equals("Topic")) {
            urlString = "https://news.google.com/news?cf=all&hl=en&pz=1&ned=us&q=" + MAIN_TITLE + "&output=rss";
        } else {
            throw new IllegalArgumentException("The first parameter should be either \"G1_P1\", \"G1_P2\", \"G2_P1\", \"G2_P2\" or \"Topic\" ");
        }

        if ((urlString).startsWith("https:")) {
            try {
                rssURL = new URL((String) data);
                FullTextArticals articals = readFeed();
                String[] links = articals.getLinks();
                String[] titles = articals.getTitles();
                for (int i = 0; i < links.length; i++) {
                    formatedString.append(titles[i].concat(" ").concat(readText(links[i])).concat(" "));
                }
                return formatedString;
            } catch (Exception e) {
                LOGGER.error("error read RSS feeds in FullTextReader class " + e);
            }
        } else {
            throw new ExecutionPlanRuntimeException("Input to the RSS:Reader() function should contain URL");
        }
        return formatedString;
    }

    private String readText(String link) {
        String text = "";
        try {
            String url = link;
            org.jsoup.nodes.Document doc = (org.jsoup.nodes.Document) Jsoup.connect(url).get();
            Elements paragraphs = doc.select("p");
            for (org.jsoup.nodes.Element p : paragraphs) {
                text = text.concat(p.text().concat(" "));
            }
        } catch (IOException ex) {
            LOGGER.error("error read RSS feeds in ReadText method FullTextReader class " + ex);
        }
        return text;
    }

    private FullTextArticals readFeed() {
        String[] links = null;
        String[] titles = null;
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(rssURL.openStream());
            NodeList items = doc.getElementsByTagName("item");
            links = new String[items.getLength()];
            titles = new String[items.getLength()];
            for (int i = 0; i < items.getLength(); i++) {
                Element item = (Element) items.item(i);
                links[i] = (String) getValue(item, "link");
                titles[i] = (String) getValue(item, "title");
            }
        } catch (Exception e) {
            LOGGER.error("error read RSS feeds in FullTextReader class " + e);
        }
        return new FullTextArticals(links, titles);
    }

    private String getValue(Element parent, String nodeName) {
        return parent.getElementsByTagName(nodeName).item(0).getFirstChild().getNodeValue();
    }

    private class FullTextArticals {
        private String[] links;
        private String[] titles;

        public FullTextArticals(String[] links, String[] titles) {
            this.links = links;
            this.titles = titles;
        }

        public String[] getLinks() {
            return this.links;

        }

        public String[] getTitles() {
            return this.titles;

        }
    }
}
