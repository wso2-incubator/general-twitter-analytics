# general-twitter-analytics

**1. Configure DB**  
    1.1 Download this repository and navigate to _database/_ folder and find _twitterAnalytics_db.sql_ file  
	1.2. If you already installed mysql server in your local machine please ignore this step   
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Install mysql to your local machine by entering the following command in the terminal:  
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`sudo apt-get install mysql-server mysql-client`  
 	1.3 Log into mysql server using `mysql -u [username] -p[password]` and execute following commands  
   		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Change mysql connection limit with: `set global max_connections = 10000;`  
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`CREATE DATABASE twitter_analytics_db;`  
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`CREATE DATABASE twitter_analytic_DAS_db;`  
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`CREATE DATABASE twitter_analytic_ProcessDAS_db;`  
 	1.4 Logout from mysql using `quit;`   
 	1.5 Import Databases using following commands   
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`mysql -u [username] -p[password]  twitter_analytics_db < twitterAnalytics_db.sql` 

**2. Download required wso2 products**   
	 2.1 Download WSO2 Enterprise Service Bus 4.9.0 from http://wso2.com/products/enterprise-service-bus/ and extract.  
	 2.2 Download WSO2 Data Analytic Server 3.1.0 from http://wso2.com/products/data-analytics-server/ and extract.
	 2.3 Open _DAS_HOME/repository/conf/carbon.xml_ and change the offset of the server to 7 by replacing line relevant line with `<offset>7<offset>`  
	 2.3 Download the WSO2 twitter connector from https://storepreview.wso2.com/store/assets/esbconnector/313cbd79-c183-43d2-8a6f-fb2721973ed9 and copy the jar to the _ESB_HOME/repository/components/dropins/_ directory in ESB.    
 
**3. Deploy cApps**  
	 Find all .car files in _cApps/_ folder and other resources in _resources/_ folder  
	 3.1 DAS cApp  
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3.1.1 Navigate into _resources/dropins/_ folder and follow the instructions provided in _DAS_dropins.md_  
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3.1.2 navigate into _resources/lib/_ folder and download the files listed in _DAS_lib.md_ file. Copy the downloads into _DAS_HOME/repository/components/lib/_  
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3.1.3 Unzip _resources/patches/patches.zip_ and place the zip contents in _DAS_HOME/repository/components/patches/_  
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3.1.4 Copy and replace the _resources/datasources/analytics-datasources.xml_ and _master-datasources.xml_ files into _DAS_HOME/repository/conf/datasources/_ and follow the next step  
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3.1.5 Edit lines 17,18,40,41 of _analytics-datasources.xml_ and lines 18,19,38,39 of _master-datasources.xml_  
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3.1.6 Copy _jaggeray_app/jaggeryapp-templates/_ folder into _DAS_HOME/repository/conf/template-manager/_  
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3.1.7 Start DAS server using following command: `sh DAS_HOME/bin/wso2server.sh`  
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3.1.8 Use the following url to access DAS Management Console : https://[host_ip]:9450/carbon/  
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3.1.9 Using left navigation pane, Create Datasource in configure > Datasources > add Datasources with following configurations  
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`name = GENERAL_TWITTERANALYTICS_DB`  
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Database Engine = MySQL`  
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`URL = jdbc:mysql://[machine-name/ip]:[port]/twitter_analytics_db`  
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3.1.10 Using left navigation pane, Main > Manage > Carbon Applications > Add and browse _cApps/TwitterAnalyticsDAScApp_1.0.0.car_ > Upload > Refresh the page  
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3.1.11 Using left navigation pane, Main > Dashboard > Template Manager > TwitterAnalytic > Create New Scenario then provide required details.  
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3.1.12 Cut and paste _DAS_HOME/repository/deployment/server/jaggeryapps/[Jaggery Application Name]/htag.xml_ and paste inside _esb_service/src/main/resources_ in the downloaded repository  

   3.2 ESB cApp
	 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3.2.1 Start ESB server using following command: `sh ESB_HOME/bin/wso2server.sh`  
	 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3.2.2 Use this url to access ESB Management Console : https://[host_ip]:9443/carbon/  
	 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3.2.3 Using left navigation pane go to Main > Manage > Carbon Applications > Add and browse _TwitterAnalyticsESBcApp_1.0.0.car_ > Upload > Refresh the page  
	 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3.2.4 Open _InsertInboundEndpoint.java_ file found in _esb_service/src/main/java/org/wso2/esb/inboundadmin/_ folder. Insert the host ip address of the ESB instance into line 38 (Sample shown in line 37)  
	 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3.2.4 Navigate back to _esb_service/_ folder and build the project using _mvn clean install_ This will create _ESBService-1.0-SNAPSHOT.jar_ in the _target_ folder  
	 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3.2.5 Copy the _ESBService-1.0-SNAPSHOT.jar_ into the _ESB_HOME_ directory and run the file using the terminal command `java -jar ESBService-1.0-SNAPSHOT.jar` 
	    
**4. Customize Extensions(if needed any additional functionalities)**  
	 4.1 Download siddhi_extensions folder  
	 4.2 Make relevent changes using your desired IDE  
	 4.3 Build pom.xml file in siddhi-extensions to build a new jar  
	 4.4 Replace the jar file in DAS_HOME/repository/components/dropins with new jar  
	 4.5 For more information use wso2 product documentation  