package org.wso2.siddhi.extension.groupdivider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.siddhi.core.config.ExecutionPlanContext;
import org.wso2.siddhi.core.event.ComplexEventChunk;
import org.wso2.siddhi.core.event.stream.StreamEvent;
import org.wso2.siddhi.core.event.stream.StreamEventCloner;
import org.wso2.siddhi.core.event.stream.populater.ComplexEventPopulater;
import org.wso2.siddhi.core.executor.ConstantExpressionExecutor;
import org.wso2.siddhi.core.executor.ExpressionExecutor;
import org.wso2.siddhi.core.executor.VariableExpressionExecutor;
import org.wso2.siddhi.core.query.processor.Processor;
import org.wso2.siddhi.core.query.processor.stream.StreamProcessor;
import org.wso2.siddhi.query.api.definition.AbstractDefinition;
import org.wso2.siddhi.query.api.definition.Attribute;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class GroupDivider extends StreamProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupDivider.class);
    private List<String> groupOneSubGroupOne = new ArrayList<String>();
    private List<String> groupOneSubGroupTwo = new ArrayList<String>();
    private List<String> groupTwoSubGroupOne = new ArrayList<String>();
    private List<String> groupTwoSubGroupTwo = new ArrayList<String>();
    private List<String> groupCommon = new ArrayList<String>();
    private String group,g1p1,g1p2,g2p1,g2p2;


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
                        if (stopTagSet.contains(hashTag.toLowerCase())) {
                            if (group == null){
                                group = "G1_P1";
                                complexEventPopulater.populateComplexEvent(streamEvent, new Object[]{group});
                            }
                        }
                    }
                    for (String hashTag : groupOneSubGroupTwo) {
                        if (stopTagSet.contains(hashTag.toLowerCase())) {
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
                        if (stopTagSet.contains(hashTag.toLowerCase())) {
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
                        if (stopTagSet.contains(hashTag.toLowerCase())) {
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
                        if (stopTagSet.contains(hashTag.toLowerCase())) {
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
        if (!(attributeExpressionExecutors.length == 5)) {
            throw new UnsupportedOperationException("Invalid number of Arguments");
        }
        if (!(attributeExpressionExecutors[0] instanceof VariableExpressionExecutor)) {
            throw new UnsupportedOperationException("Required a variable, but found a otherparameter");
        } else {
//            variableHashTag = (VariableExpressionExecutor) attributeExpressionExecutors[0];
        }

        if (attributeExpressionExecutors[1] instanceof ConstantExpressionExecutor) {

            g1p1 = ((String) attributeExpressionExecutors[1].execute(null));
            groupOneSubGroupOne= Arrays.asList(g1p1.split("\\s+"));
        } else {
            throw new IllegalArgumentException("Parameter should be a String");
        }
        if (attributeExpressionExecutors[2] instanceof ConstantExpressionExecutor) {

            g1p2 = ((String) attributeExpressionExecutors[2].execute(null));
            groupOneSubGroupTwo=Arrays.asList(g1p2.split("\\s+"));
        } else {
            throw new IllegalArgumentException("Parameter should be a String");
        }
        if (attributeExpressionExecutors[3] instanceof ConstantExpressionExecutor) {

            g2p1 = ((String) attributeExpressionExecutors[3].execute(null));
            groupTwoSubGroupOne=Arrays.asList(g2p1.split("\\s+"));
        } else {
            throw new IllegalArgumentException("Parameter should be a String");
        }
        if (attributeExpressionExecutors[4] instanceof ConstantExpressionExecutor) {

            g2p2 = ((String) attributeExpressionExecutors[4].execute(null));
            groupTwoSubGroupTwo=Arrays.asList(g2p2.split("\\s+"));
        } else {
            throw new IllegalArgumentException("Parameter should be a String");
        }

        List<Attribute> attributeList = new ArrayList<Attribute>();
        attributeList.add(new Attribute("group", Attribute.Type.STRING));
        return attributeList;
    }

    @Override
    public void start() {
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
