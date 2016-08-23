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
	 2.4 Download the WSO2 twitter connector from https://storepreview.wso2.com/store/assets/esbconnector/313cbd79-c183-43d2-8a6f-fb2721973ed9 and copy the jar to the ESB_HOME/repository/components/dropins directory in ESB.  
 
3. Deploy cApps  
	Download all .car files in cApps folder and resources folder
	 2.1 DAS cApp
        2.1.1 put built resources/dropins/DAS_dropins into DAS_HOME/repository/components/dropins
     	2.1.2 put downloaded resources/lib/DAS_lib into DAS_HOME/repository/components/lib
     	2.1.3 put unzipped resources/patches/patches.zip into DAS_HOME/repository/components/patches
     	2.1.4 replace downloaded resources/datasources/analytics-datasources.xml and masteratasources.xml into DAS_HOME/repository/conf/datasources
     	2.1.5 copy jaggeray_app/jaggeryapp-templates folder into DAS_HOME/repository/conf/template-manager
     	2.1.6 Start DAS server using following command:_ sh DAS_HOME/bin/wso2server.sh _
     	2.1.7 Use this url to access DAS Management Console : https://[host_ip]:9450/carbon/
     	2.1.8 Using left navigation pane, Create Datasource in configure > Datasources > add Datasources as name=GENERAL_TWITTERANALYTICS_DB database-name=twitter_analytics_db.
     	2.1.9 Using left navigation pane, Main > Manage > Carbon Applications > Add and browse cApps/TwitterAnalyticsDAScApp_1.0.0.car > Upload > Refresh the page
     	2.1.10 Using left navigation pane, Main > Dashboard > Template Manager > TwitterAnalytic > Create New Scenario then provide required details.
     	2.1.11 Cut DAS_HOME/repository/deployment/server/jaggeryapps/[Jaggery Application Name]/htag.xml and paste inside esb_service/src/main/resources

	 2.1 ESB cApp
	   2.1.1 Start ESB server using following command: _sh ESB_HOME/bin/wso2server.sh_  
	   2.1.2 Use this url to access ESB Management Console : https://[host_ip]:9450/carbon/  
	   2.1.3 Using left navigation pane go to Main > Manage > Carbon Applications > Add and browse   TwitterAnalyticsESBcApp_1.0.0.car > Upload > Refresh the page
	 2.2 CEP cApp  
	   2.2.1 put DOWNLOADED_RESOURCE/dropins/CEP_dropins into CEP_HOME/repository/components/dropins  
	   2.2.2 put DOWNLOADED_RESOURCE/lib/CEP_lib into CEP_HOME/repository/components/lib  
	   2.2.3 Start CEP server using following command: _sh CEP_HOME/bin/wso2server.sh _ 
	   2.2.4 Use this url to access CEP Management Console : https://[host_ip]:9451/carbon/  
	   2.2.5 Create Datasource in configure > Datasources > add Datasources for CREATE DATABASE use16_cep_bck_db and CREATE DATABASE use16_cep_data_db using the names respectively ELECTIONSYSTEMCEP_BACKUP_DB  and ELECTIONSYSTEMCEP_DB. 
	   2.2.6 Using left navigation pane go to Main > Manage > Carbon Applications > Add and browse   USE2016_CEP_cApp_1.0.0.car > Upload > Refresh the page

	
4. Deploy web App  
	 4.1 Download us-election-analytics folder in WebContent folder and copy it into DAS_HOME/repository/deployment/server/jaggeryapps  
	 4.2 Chnge your relevent mysql username, password and url in config_use16_cep_data_db.json  and config_use16_das_data_db.json files which is in us-election-analytics folder  
	
5. Customize Extensions(if needed any additional functionalities)  
	 5.1 Download election-siddhi-extensions in src folder  
	 5.2 Make relevent changes using your desired IDE  
	 5.3 Build pom.xml file in election-siddhi-extensions to build a new jar  
	 5.4 Replace the jar file in DASorCEP_HOME/repository/components/dropins with new jar  
	 5.5 For more information use wso2 product documentation  