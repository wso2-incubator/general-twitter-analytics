# general-twitter-analytics

1. Configure DB
    1.1 Download database folder which consists .sql file and go to your terminal  
	1.2. If you already installed mysql server in your local machine please ignore following step 
		Install mysql to your local machine using the command :
		_sudo apt-get install mysql-server mysql-client_  
 	1.3 change the mysql connection limit in your database using this command after login as mysql -u root -p 
   		    _set global max_connections = 10000;_   
 	1.4 Log into mysql server using  mysql -u [username] -p[password] and execute following commands 
		    _CREATE DATABASE twitter_analytics_db;_
		    _CREATE DATABASE twitter_analytic_DAS_db;_
		    _CREATE DATABASE twitter_analytic_ProcessDAS_db;_  
 	1.5 Logout from mysql using quit;   
 	1.6 Import Databases using following commands   
		  _mysql -u [username] -p[password]  twitter_analytics_db < twitterAnalytics_db.sql_  

2. Download required wso2 products   
	 2.1 Download WSO2 Enterprise Service Bus 4.9.0 from http://wso2.com/products/enterprise-service-bus/ and extract.  
	 2.2 Download WSO2 Data Analytic Server 3.1.0 from http://wso2.com/products/data-analytics-server/ and extract.Open DAS_HOME/repository/conf/carbon.xml and change the offset of the server to 7 as <offset>7<offset>  
	 2.3 Download the WSO2 twitter connector from https://storepreview.wso2.com/store/assets/esbconnector/313cbd79-c183-43d2-8a6f-fb2721973ed9 and copy the jar to the ESB_HOME/repository/components/dropins directory in ESB.    
 
3. Deploy cApps  
	Download all .car files in cApps folder and resources folder  
	 3.1 DAS cApp  
        3.1.1 put built resources/dropins/DAS_dropins into DAS_HOME/repository/components/dropins  
     	3.1.2 put downloaded resources/lib/DAS_lib into DAS_HOME/repository/components/lib  
     	3.1.3 put unzipped resources/patches/patches.zip into DAS_HOME/repository/components/patches  
     	3.1.4 replace downloaded resources/datasources/analytics-datasources.xml and masteratasources.xml into DAS_HOME/repository/conf/datasources  
     	3.1.5 copy jaggeray_app/jaggeryapp-templates folder into DAS_HOME/repository/conf/template-manager  
     	3.1.6 Start DAS server using following command:_ sh DAS_HOME/bin/wso2server.sh _  
     	3.1.7 Use this url to access DAS Management Console : https://[host_ip]:9450/carbon/  
     	3.1.8 Using left navigation pane, Create Datasource in configure > Datasources > add Datasources as name=GENERAL_TWITTERANALYTICS_DB database-name=twitter_analytics_db.  
     	3.1.9 Using left navigation pane, Main > Manage > Carbon Applications > Add and browse cApps/TwitterAnalyticsDAScApp_1.0.0.car > Upload > Refresh the page  
     	3.1.10 Using left navigation pane, Main > Dashboard > Template Manager > TwitterAnalytic > Create New Scenario then provide required details.  
     	3.1.11 Cut DAS_HOME/repository/deployment/server/jaggeryapps/[Jaggery Application Name]/htag.xml and paste inside esb_service/src/main/resources  

	 3.2 ESB cApp
	    3.2.1 Start ESB server using following command: _sh ESB_HOME/bin/wso2server.sh_  
	    3.2.2 Use this url to access ESB Management Console : https://[host_ip]:9443/carbon/  
	    3.2.3 Using left navigation pane go to Main > Manage > Carbon Applications > Add and browse   TwitterAnalyticsESBcApp_1.0.0.car > Upload > Refresh the page  
	    3.2.4 Open _InsertInboundEndpoint.java_ file found in _esb_service/src/main/java/org/wso2/esb/inboundadmin_ folder. Insert the ip address that the ESB is hosted on into line 38 (Sample shown in line 37)  
	    3.2.4 Navigate back to _esb_service_ folder and build the project using _mvn clean install._ This will create _ESBService-1.0-SNAPSHOT.jar_ in the _target_ folder  
	    3.2.5 Copy the _ESBService-1.0-SNAPSHOT.jar_ into the _ESB_HOME_ directory   
	    
5. Customize Extensions(if needed any additional functionalities)  
	 5.1 Download siddhi_extensions folder  
	 5.2 Make relevent changes using your desired IDE  
	 5.3 Build pom.xml file in siddhi-extensions to build a new jar  
	 5.4 Replace the jar file in DAS_HOME/repository/components/dropins with new jar  
	 5.5 For more information use wso2 product documentation  