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

package org.wso2.esb.inboundadmin;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

public class XMLReader {

    private Document doc;

    public XMLReader(){
        InputStream file = getClass().getResourceAsStream("/htag.xml");

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;

        try {
            dBuilder = dbFactory.newDocumentBuilder();

            doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    public String getHTags(){
        NodeList groupNodes = doc.getElementsByTagName("group");
        StringBuilder hTagList = new StringBuilder();
        for (int i = 0; i < 2; i++) {
            Element subGroupNodes = (Element) groupNodes.item(i);
            NodeList subGroupList = subGroupNodes.getElementsByTagName("sub-group");

            for (int j = 0; j < subGroupList.getLength(); j++) {
                Element subgroups = (Element) subGroupList.item(j);
                NodeList nl = subgroups.getElementsByTagName("hash-tag");
                String[] hTagArray = nl.item(0).getTextContent().toLowerCase().split(" ");
                if (!("").equals(hTagArray[0])){
                    for (String hashTag: hTagArray) {
                        hTagList.append("#").append(hashTag).append(",");
                    }
                }
            }
        }

        Element commonGroup = (Element) groupNodes.item(2);
        NodeList commonHTag = commonGroup.getElementsByTagName("hash-tag");
        String[] hTagArray = commonHTag.item(0).getTextContent().toLowerCase().split(" ");
        if (!("").equals(hTagArray[0])){
            for (String hashTag: hTagArray) {
                hTagList.append("#").append(hashTag).append(",");
            }
        }
        hTagList.deleteCharAt(hTagList.length()-1);
        return hTagList.toString().replace("\"","");
    }

    public String getAccessSecret(){
        NodeList accessSecret = doc.getElementsByTagName("access-secret");
        return  accessSecret.item(0).getTextContent();
    }
    public String getAccessToken(){
        NodeList accessToken = doc.getElementsByTagName("access-token");
        return  accessToken.item(0).getTextContent();
    }
    public String getConsumerSecret(){
        NodeList consumerSecret = doc.getElementsByTagName("consumer-secret");
        return  consumerSecret.item(0).getTextContent();
    }
    public String getConsumerKey(){
        NodeList consumerKey = doc.getElementsByTagName("consumer-key");
        return  consumerKey.item(0).getTextContent();
    }

    public String getESBUsername(){
        NodeList managmentConsoleNode = doc.getElementsByTagName("management-console");
        Element managmentConsoleElement = (Element)managmentConsoleNode.item(0);
        return managmentConsoleElement.getElementsByTagName("username").item(0).getTextContent();
    }

    public String getESBPassword(){
        NodeList managmentConsoleNode = doc.getElementsByTagName("management-console");
        Element managmentConsoleElement = (Element)managmentConsoleNode.item(0);
        return managmentConsoleElement.getElementsByTagName("password").item(0).getTextContent();
    }

}
