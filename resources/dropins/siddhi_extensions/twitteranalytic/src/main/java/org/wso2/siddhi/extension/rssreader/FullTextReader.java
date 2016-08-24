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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.net.URL;

public class FullTextReader extends FunctionExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(FullTextReader.class);
    private String urlString;
    private URL rssURL;

    @Override
    public Type getReturnType() {
        return Type.STRING;
    }

    @Override
    public void start() {
        // Nothing to do here
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

        urlString = "https://news.google.com/news?cf=all&hl=en&pz=1&ned=us&q=" + data + "&output=rss";

        if ((urlString).startsWith("https:")) {
            try {
                rssURL = new URL((String) data);
                FullTextArticles articles = readFeed();
                String[] links = articles.getLinks();
                String[] titles = articles.getTitles();
                for (int i = 0; i < links.length; i++) {
                    formatedString.append(titles[i].concat(" ").concat(readText(links[i])).concat(" "));
                }
                return formatedString;
            } catch (Exception e) {
                LOGGER.error("error read RSS feeds in FullTextReader class " + e);
            }
        } else {
            throw new ExecutionPlanRuntimeException("Input to the TwitterAnalytic:readRSSText() function should contain URL");
        }
        return formatedString;
    }

    private String readText(String link) {
        String text = "";
        try {
            org.jsoup.nodes.Document doc = (org.jsoup.nodes.Document) Jsoup.connect(link).get();
            Elements paragraphs = doc.select("p");
            for (org.jsoup.nodes.Element p : paragraphs) {
                text = text.concat(p.text().concat(" "));
            }
        } catch (IOException ex) {
            LOGGER.error("error read RSS feeds in ReadText method FullTextReader class " + ex);
        }
        return text;
    }

    private FullTextArticles readFeed() {
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
        return new FullTextArticles(links, titles);
    }

    private String getValue(Element parent, String nodeName) {
        return parent.getElementsByTagName(nodeName).item(0).getFirstChild().getNodeValue();
    }

    private class FullTextArticles {
        private String[] links;
        private String[] titles;

        public FullTextArticles(String[] links, String[] titles) {
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
