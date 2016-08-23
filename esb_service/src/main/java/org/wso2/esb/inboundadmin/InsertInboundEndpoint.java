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

import org.wso2.carbon.authenticator.stub.LoginAuthenticationExceptionException;
import org.wso2.carbon.authenticator.stub.LogoutAuthenticationExceptionException;

import java.rmi.RemoteException;

public class InsertInboundEndpoint {
    public static void main(String[] args)
            throws RemoteException, LoginAuthenticationExceptionException,
            LogoutAuthenticationExceptionException {

        XMLReader reader = new XMLReader();

        String pathToTrustStore = System.getProperty("user.dir") + "/repository/resources/security/wso2carbon.jks";
        System.setProperty("javax.net.ssl.trustStore", pathToTrustStore);
        System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");
//        String backEndUrl = "https://192.168.57.26:9443";
        String backEndUrl = "";

        LoginAdminServiceClient login = new LoginAdminServiceClient(backEndUrl);

        String username = reader.getESBUsername();
        String password = reader.getESBPassword();

        String session = login.authenticate(username, password);
        InboundAdminClient inboundAdminClient = new InboundAdminClient(backEndUrl, session);

        String hTag = reader.getHTags();
        String accessSecret = reader.getAccessSecret();
        String accessToken = reader.getAccessToken();
        String consumerSecret = reader.getConsumerSecret();
        String consumerKey = reader.getConsumerKey();

        inboundAdminClient.addInboundEndpoint(hTag,consumerSecret,consumerKey,accessToken,accessSecret);
        login.logOut();
    }
}