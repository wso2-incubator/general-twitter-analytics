package org.wso2.siddhi.extension.sentiments;

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

import org.wso2.siddhi.core.config.ExecutionPlanContext;
import org.wso2.siddhi.core.exception.ExecutionPlanRuntimeException;
import org.wso2.siddhi.core.executor.ExpressionExecutor;
import org.wso2.siddhi.core.executor.function.FunctionExecutor;
import org.wso2.siddhi.query.api.definition.Attribute.Type;

public class CandidateTextLines extends FunctionExecutor {

    @Override
    public Type getReturnType() {
        return Type.STRING;
    }

    @Override
    public void start() {
        // Nothing to do here.
    }

    @Override
    public void stop() {
        // Nothing to do here.
    }

    @Override
    public Object[] currentState() {
        // No need to maintain a state.
        return null;
    }

    @Override
    public void restoreState(Object[] state) {
        // No need to maintain a state.

    }

    @Override
    protected void init(ExpressionExecutor[] attributeExpressionExecutors, ExecutionPlanContext executionPlanContext) {
        if (attributeExpressionExecutors.length != 2) {
            throw new IllegalArgumentException(
                    "Invalid no of arguments passed to CandidateTextLines.java class, "
                            + "required 2, but found " + attributeExpressionExecutors.length);
        }
        Type attributeType1 = attributeExpressionExecutors[0].getReturnType();
        if (!(attributeType1 == Type.STRING)) {
            throw new IllegalArgumentException(
                    "Invalid parameter type found for the argument 1 of CandidateTextLines.java class function");
        }
        Type attributeType2 = attributeExpressionExecutors[1].getReturnType();
        if (!(attributeType2 == Type.STRING)) {
            throw new IllegalArgumentException(
                    "Invalid parameter type found for the argument 2 of CandidateTextLines.java class function");
        }
    }

    @Override
    protected Object execute(Object[] data) {
        StringBuilder text = new StringBuilder();
        if (data != null) {
            String[] arr = ((String) data[0]).split("\\.");
            for (String line : arr) {
                if ((line.toLowerCase().contains(((String) data[1]).toLowerCase()))) {
                    text.append(" ".concat(line));
                }
            }
        } else {
            throw new ExecutionPlanRuntimeException("Input to the ClearSentiment:TrackWord() function cannot be null");
        }
        return text.toString().trim();
    }

    @Override
    protected Object execute(Object data) {
        // This method is not call
        return null;
    }

}
