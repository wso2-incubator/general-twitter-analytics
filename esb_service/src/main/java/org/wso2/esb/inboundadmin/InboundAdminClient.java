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

import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.wso2.carbon.inbound.stub.InboundAdminInboundManagementException;
import org.wso2.carbon.inbound.stub.InboundAdminStub;
import org.wso2.carbon.inbound.stub.types.carbon.ParameterDTO;

import java.rmi.RemoteException;

public class InboundAdminClient {
    private InboundAdminStub inboundAdminStub;

    public InboundAdminClient(String backEndUrl, String sessionCookie) throws AxisFault {
        String serviceName = "InboundAdmin";
        String endPoint = backEndUrl + "/services/" + serviceName;
        inboundAdminStub = new InboundAdminStub(endPoint);
        //Authenticate Your stub from sessionCooke
        ServiceClient serviceClient;
        Options option;

        serviceClient = inboundAdminStub._getServiceClient();
        option = serviceClient.getOptions();
        option.setManageSession(true);
        option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, sessionCookie);
    }

    public void addInboundEndpoint(String hTag, String consumerSecret, String consumerKey, String accessToken,
                                   String accessSecret) throws RemoteException {

        ParameterDTO parameterInterval = new ParameterDTO();
        parameterInterval.setName("interval");
        parameterInterval.setValue("40");

        ParameterDTO parameterTwitterOperation = new ParameterDTO();
        parameterTwitterOperation.setName("twitter.operation");
        parameterTwitterOperation.setValue("filter");

        ParameterDTO parameterAccessToken = new ParameterDTO();
        parameterAccessToken.setName("connection.twitter.accessToken");
        parameterAccessToken.setValue(accessToken);

        ParameterDTO parameterAccessSecret = new ParameterDTO();
        parameterAccessSecret.setName("connection.twitter.accessSecret");
        parameterAccessSecret.setValue(accessSecret);

        ParameterDTO parameterCoordination = new ParameterDTO();
        parameterCoordination.setName("coordination");
        parameterCoordination.setValue("true");

        ParameterDTO parameterConsumerKey = new ParameterDTO();
        parameterConsumerKey.setName("connection.twitter.consumerKey");
        parameterConsumerKey.setValue(consumerKey);

        ParameterDTO parameterConsumerSecret = new ParameterDTO();
        parameterConsumerSecret.setName("connection.twitter.consumerSecret");
        parameterConsumerSecret.setValue(consumerSecret);

        ParameterDTO parameterTwitterTrack = new ParameterDTO();
        parameterTwitterTrack.setName("twitter.track");
        parameterTwitterTrack.setValue(hTag);

        ParameterDTO parameterSequential = new ParameterDTO();
        parameterSequential.setName("sequential");
        parameterSequential.setValue("true");

        ParameterDTO[] parameterDTOList = {parameterInterval, parameterTwitterOperation, parameterAccessToken,
                parameterAccessSecret, parameterCoordination, parameterConsumerKey, parameterConsumerSecret,
                parameterTwitterTrack, parameterSequential};

        try {
            inboundAdminStub.addInboundEndpoint("GeneralCase", "Sequence", "fault", null,
                    "org.wso2.carbon.inbound.twitter.poll.TwitterStreamData", "false", parameterDTOList);
        } catch (InboundAdminInboundManagementException e) {
            System.out.println("Error Adding Inbound Endpoint: ");
            e.printStackTrace();
        }
    }
}