package org.wso2.siddhi.extension.groupdivider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.wso2.siddhi.core.config.ExecutionPlanContext;
import org.wso2.siddhi.core.event.ComplexEventChunk;
import org.wso2.siddhi.core.event.stream.StreamEvent;
import org.wso2.siddhi.core.event.stream.StreamEventCloner;
import org.wso2.siddhi.core.event.stream.populater.ComplexEventPopulater;
import org.wso2.siddhi.core.executor.ExpressionExecutor;
import org.wso2.siddhi.core.executor.VariableExpressionExecutor;
import org.wso2.siddhi.core.query.processor.Processor;
import org.wso2.siddhi.core.query.processor.stream.StreamProcessor;
import org.wso2.siddhi.query.api.definition.AbstractDefinition;
import org.wso2.siddhi.query.api.definition.Attribute;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class GroupDivider extends StreamProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupDivider.class);
    private List<String> groupOneSubGroupOne = new ArrayList<String>();
    private List<String> groupOneSubGroupTwo = new ArrayList<String>();
    private List<String> groupTwoSubGroupOne = new ArrayList<String>();
    private List<String> groupTwoSubGroupTwo = new ArrayList<String>();
    private List<String> groupCommon = new ArrayList<String>();
    private String group;


    @Override
    protected void process(ComplexEventChunk<StreamEvent> streamEventChunk, Processor processor, StreamEventCloner streamEventCloner, ComplexEventPopulater complexEventPopulater) {
        StreamEvent streamEvent;
        synchronized (this) {
            while (streamEventChunk.hasNext()) {
                streamEvent = streamEventChunk.next();
                group = null;
                try {
                    StreamEvent clonedEvent = streamEvent;
//                    ComplexEventChunk<StreamEvent> newStreamEventChunk = new ComplexEventChunk<StreamEvent>();
                    String hTagString = attributeExpressionExecutors[0].execute(streamEvent).toString();
                    String[] hTagArray = hTagString.toLowerCase().replaceAll(" ", "").split(",");
//                    for(String hTag : hTagArray){
//                        StreamEvent clonedEvent = streamEventCloner.copyStreamEvent(streamEvent);
//                        complexEventPopulater.populateComplexEvent(clonedEvent, new Object[]{"G1_P2"});
//                        newStreamEventChunk.add(clonedEvent);
//
//                    }
                    HashSet<String> stopTagSet = new HashSet<String>();
                    for (int i = 0; i < hTagArray.length; i++) {
                        stopTagSet.add(hTagArray[i]);
                    }

                    for (String hashTag : groupOneSubGroupOne) {
                        if (stopTagSet.contains(hashTag)) {
                            if (group == null){
                                group = "G1_P1";
                                complexEventPopulater.populateComplexEvent(streamEvent, new Object[]{group});
                            }
                        }
                    }
                    for (String hashTag : groupOneSubGroupTwo) {
                        if (stopTagSet.contains(hashTag)) {
                            if (group == null){
                                group = "G1_P2";
//                                complexEventPopulater.populateComplexEvent(streamEvent, new Object[]{group});
                            } else {
                                clonedEvent = streamEventCloner.copyStreamEvent(streamEvent);
                                complexEventPopulater.populateComplexEvent(clonedEvent, new Object[]{"G1_P2"});
                                streamEventChunk.insertBeforeCurrent(clonedEvent);
                            }
                        }
                    }
                    for (String hashTag : groupTwoSubGroupOne) {
                        if (stopTagSet.contains(hashTag)) {
                            if (group == null){
                                group = "G2_P1";
//                                complexEventPopulater.populateComplexEvent(streamEvent, new Object[]{group});
                            } else {
                                clonedEvent = streamEventCloner.copyStreamEvent(streamEvent);
                                complexEventPopulater.populateComplexEvent(clonedEvent, new Object[]{"G2_P1"});
                                streamEventChunk.insertBeforeCurrent(clonedEvent);
                            }
                        }
                    }
                    for (String hashTag : groupTwoSubGroupTwo) {
                        if (stopTagSet.contains(hashTag)) {
                            if (group == null){
                                group = "G2_P2";
//                                complexEventPopulater.populateComplexEvent(streamEvent, new Object[]{group});
                            } else {
                                clonedEvent = streamEventCloner.copyStreamEvent(streamEvent);
                                complexEventPopulater.populateComplexEvent(clonedEvent, new Object[]{"G2_P2"});
                                streamEventChunk.insertBeforeCurrent(clonedEvent);
                            }
                        }
                    }
                    for (String hashTag : groupCommon) {
                        if (stopTagSet.contains(hashTag)) {
                            if (group == null){
                                group = "COMMON";
//                                complexEventPopulater.populateComplexEvent(streamEvent, new Object[]{group});
                            } else {
                                clonedEvent = streamEventCloner.copyStreamEvent(streamEvent);
                                complexEventPopulater.populateComplexEvent(clonedEvent, new Object[]{"COMMON"});
                                streamEventChunk.insertBeforeCurrent(clonedEvent);
                            }
                        }
                    }
                    complexEventPopulater.populateComplexEvent(streamEvent, new Object[]{group});
                    if (group == null) {
                        streamEventChunk.remove();
                    }
//                    streamEventChunk.add(clonedEvent);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            nextProcessor.process(streamEventChunk);
        }
    }

    @Override
    protected List<Attribute> init(AbstractDefinition abstractDefinition, ExpressionExecutor[] expressionExecutors, ExecutionPlanContext executionPlanContext) {
        if (!(attributeExpressionExecutors.length == 1)) {
            throw new UnsupportedOperationException("Invalid number of Arguments");
        }
        if (!(attributeExpressionExecutors[0] instanceof VariableExpressionExecutor)) {
            throw new UnsupportedOperationException("Required a variable, but found a otherparameter");
        } else {
//            variableHashTag = (VariableExpressionExecutor) attributeExpressionExecutors[0];
        }

        List<Attribute> attributeList = new ArrayList<Attribute>();
        attributeList.add(new Attribute("group", Attribute.Type.STRING));
        return attributeList;
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
                    Element subgroups = (Element) subGroupList.item(j);
//                    System.out.println(subgroups.getAttributes().getNamedItem("name").toString());
                    NodeList nl = subgroups.getElementsByTagName("hash-tag");
                    for (int k = 0; k < nl.getLength(); k++) {
                        switch (count) {
                            case 0:
                                groupOneSubGroupOne.add(nl.item(k).getTextContent().toLowerCase());
                                break;
                            case 1:
                                groupOneSubGroupTwo.add(nl.item(k).getTextContent().toLowerCase());
                                break;
                            case 2:
                                groupTwoSubGroupOne.add(nl.item(k).getTextContent().toLowerCase());
                                break;
                            case 3:
                                groupTwoSubGroupTwo.add(nl.item(k).getTextContent().toLowerCase());
                                break;
                        }
                    }
                    count++;
                }
            }

            Element commonGroup = (Element) groupNodes.item(2);
            NodeList commonHTag = commonGroup.getElementsByTagName("hash-tag");
            for (int i = 0; i < commonHTag.getLength(); i++) {
                groupCommon.add(commonHTag.item(i).getTextContent().toLowerCase());
            }

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

    }

    @Override
    public Object[] currentState() {
        return new Object[0];
    }

    @Override
    public void restoreState(Object[] objects) {

    }
}
