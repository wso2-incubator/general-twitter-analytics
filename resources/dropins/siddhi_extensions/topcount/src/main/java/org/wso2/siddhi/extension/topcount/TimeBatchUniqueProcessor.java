package org.wso2.siddhi.extension.topcount;

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

import static java.util.concurrent.TimeUnit.HOURS;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

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
import org.wso2.siddhi.query.api.exception.ExecutionPlanValidationException;

public class TimeBatchUniqueProcessor extends StreamProcessor {
    private VariableExpressionExecutor userName;
    private VariableExpressionExecutor userGroup;
    private List<String> groupOneList = new ArrayList<String>();
    private List<String> groupTwoList = new ArrayList<String>();
    private List<String> groupThreeList = new ArrayList<String>();
    private List<String> groupFourList = new ArrayList<String>();
    private int G1_P1, G1_P2, G2_P1, G2_P2=0;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Override
    public void start() {
        scheduler.scheduleAtFixedRate(new Runnable() {
            public void run() {
                groupOneList.clear();
                groupTwoList.clear();
                groupThreeList.clear();
                groupFourList.clear();
                G1_P1 = 0;
                G2_P1 = 0;
                G2_P2 = 0;
                G1_P2 = 0;
            }

        }, 0, 24, HOURS);
    }

    @Override
    public void stop() {
        scheduler.shutdownNow();
    }

    @Override
    public Object[] currentState() {
        return new Object[]{G1_P1, G2_P1, G2_P2, G1_P2};
    }

    @Override
    public void restoreState(Object[] state) {
        // No need to maintain a state
    }

    @Override
    protected void process(ComplexEventChunk<StreamEvent> streamEventChunk, Processor nextProcessor,
                           StreamEventCloner streamEventCloner, ComplexEventPopulater complexEventPopulater) {

        StreamEvent streamEvent;
        synchronized (this) {
            while (streamEventChunk.hasNext()) {
                streamEvent = streamEventChunk.next();

                try {
                    String userName = (String) this.userName.execute(streamEvent);
                    String userGroup = (String) this.userGroup.execute(streamEvent);


                    if (!(groupOneList.contains(userName)) && "G1_P1".equals(userGroup)) {
                        groupOneList.add(userName);
                        G1_P1++;
                    } else if (!(groupTwoList.contains(userName)) && "G1_P2".equals(userGroup)) {
                        groupTwoList.add(userName);
                        G1_P2++;
                    } else if (!(groupThreeList.contains(userName)) && "G2_P1".equals(userGroup)) {
                        groupThreeList.add(userName);
                        G2_P1++;
                    } else if (!(groupFourList.contains(userName)) && "G2_P2".equals(userGroup)) {
                        groupFourList.add(userName);
                        G2_P2++;
                    }

                    complexEventPopulater.populateComplexEvent(streamEvent, new Object[]{G1_P1, G1_P2, G2_P1,
                            G2_P2});

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            nextProcessor.process(streamEventChunk);
        }
    }

    @Override
    protected List<Attribute> init(AbstractDefinition inputDefinition,
                                   ExpressionExecutor[] attributeExpressionExecutors, ExecutionPlanContext executionPlanContext) {

        if (!(attributeExpressionExecutors.length == 2)) {
            throw new ExecutionPlanValidationException("Invalid number of Arguments");
        }
        if (!(attributeExpressionExecutors[0] instanceof VariableExpressionExecutor)) {
            throw new ExecutionPlanValidationException("Required a variable, but found a other parameter");
        } else {
            userName = (VariableExpressionExecutor) attributeExpressionExecutors[0];
        }
        if (!(attributeExpressionExecutors[1] instanceof VariableExpressionExecutor)) {
            throw new ExecutionPlanValidationException("Required a variable, but found a other parameter");
        } else {
            userGroup = (VariableExpressionExecutor) attributeExpressionExecutors[1];
        }
        List<Attribute> attributeList = new ArrayList<Attribute>();
        attributeList.add(new Attribute("G1_P1_Count", Attribute.Type.INT));
        attributeList.add(new Attribute("G1_P2_Count", Attribute.Type.INT));
        attributeList.add(new Attribute("G2_P1_Count", Attribute.Type.INT));
        attributeList.add(new Attribute("G2_P2_Count", Attribute.Type.INT));
        return attributeList;
    }

}