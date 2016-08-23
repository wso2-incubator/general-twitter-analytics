package org.wso2.siddhi.extension.groupdividers;

import junit.framework.Assert;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.wso2.siddhi.core.ExecutionPlanRuntime;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.query.output.callback.QueryCallback;
import org.wso2.siddhi.core.stream.input.InputHandler;
import org.wso2.siddhi.core.stream.output.StreamCallback;
import org.wso2.siddhi.core.util.EventPrinter;
import org.wso2.siddhi.extension.groupdividers.util.SiddhiTestHelper;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by anoukh on 6/2/16.
 */
public class GroupDividerTestCase {

    static final Logger log = Logger.getLogger(GroupDividerTestCase.class);
    private AtomicInteger count = new AtomicInteger(0);
    private volatile boolean eventArrived;

    @Before
    public void init() {
        count.set(0);
        eventArrived = false;
    }

    @Test
    public void testGroupDivider() throws InterruptedException {
        log.info("GroupDivider TestCase ");
        SiddhiManager siddhiManager = new SiddhiManager();
        String inStreamDefinition = "@config(async = 'true')define stream inputStream (htaglist string);";
        String query = ("@info(name = 'query1') "
                + "from inputStream#TwitterAnalytic:getGroup(htaglist) "
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
                    eventArrived = true;
                }
            }
        });

        InputHandler inputHandler = executionPlanRuntime.getInputHandler("inputStream");
        executionPlanRuntime.start();
        inputHandler.send(new Object[] {"ONE , Hillary, India, clinton, trump, cruz, clinton, USVotes2016, bernie"});
        inputHandler.send(new Object[] {"TWO , Anoukh"});
        inputHandler.send(new Object[] {"THREE , trump2016 , trump, cruz"});
        inputHandler.send(new Object[] {"FOUR , India , BERNIE"});
        inputHandler.send(new Object[] {"FIVE, election2016"});
        SiddhiTestHelper.waitForEvents(100, 9, count, 60000);
        Assert.assertEquals(9, count.get());
        Assert.assertTrue(eventArrived);
//        Thread.sleep(1000000);
        executionPlanRuntime.shutdown();
    }
}
