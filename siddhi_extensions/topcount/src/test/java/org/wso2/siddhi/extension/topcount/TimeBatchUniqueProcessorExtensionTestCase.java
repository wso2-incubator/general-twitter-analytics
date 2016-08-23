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

import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.wso2.siddhi.core.ExecutionPlanRuntime;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.query.output.callback.QueryCallback;
import org.wso2.siddhi.core.stream.input.InputHandler;
import org.wso2.siddhi.extension.topcount.test.util.SiddhiTestHelper;
import org.wso2.siddhi.core.util.EventPrinter;

public class TimeBatchUniqueProcessorExtensionTestCase {
    static final Logger log = Logger.getLogger(TimeBatchUniqueProcessorExtensionTestCase.class);
    private AtomicInteger count = new AtomicInteger(0);
    private volatile boolean eventArrived;

    @Before
    public void init() {
        count.set(0);
        eventArrived = false;
    }

    @Test
    public void testContainsFunctionExtension() throws InterruptedException {
        log.info("TimeBatchUniqueProcessorExtensionTestCase TestCase ");
        SiddhiManager siddhiManager = new SiddhiManager();
        String inStreamDefinition = "@config(async = 'true')define stream inputStream (user string,userparty String );";
        String query = ("@info(name = 'query1') " + "from inputStream#Count:getCountCandidate(user, userparty) "
                + "select *"
                + "insert into outputStream;");
        ExecutionPlanRuntime executionPlanRuntime = siddhiManager
                .createExecutionPlanRuntime(inStreamDefinition + query);

        executionPlanRuntime.addCallback("query1", new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                for (Event inEvent : inEvents) {
                    count.incrementAndGet();
                    if (count.get() == 1) {
                        Assert.assertEquals(1, inEvent.getData(2));
                        Assert.assertEquals(0, inEvent.getData(3));
                        Assert.assertEquals(0, inEvent.getData(4));
                        Assert.assertEquals(0, inEvent.getData(5));
                    }
                    if (count.get() == 2) {
                        Assert.assertEquals(1, inEvent.getData(2));
                        Assert.assertEquals(1, inEvent.getData(3));
                        Assert.assertEquals(0, inEvent.getData(4));
                        Assert.assertEquals(0, inEvent.getData(5));
                    }

                    eventArrived = true;
                }
            }
        });

        InputHandler inputHandler = executionPlanRuntime.getInputHandler("inputStream");
        executionPlanRuntime.start();
        inputHandler.send(new Object[] { "USER_A","G1_P1" });
        inputHandler.send(new Object[] { "USER_B","G1_P2" });
        inputHandler.send(new Object[] { "USER_A","G1_P2" });
        inputHandler.send(new Object[] { "USER_A","G1_P1" });
        SiddhiTestHelper.waitForEvents(100, 4, count, 60000);
        Assert.assertEquals(4, count.get());
        Assert.assertTrue(eventArrived);
        executionPlanRuntime.shutdown();
    }
}