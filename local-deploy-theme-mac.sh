#!/bin/bash          
echo "======>Building war file..."
mvn -Plocaltheme clean package
echo "======>War file build successfully. Now removing old war file..."
cd /Users/rajashree/Vishal/mac_opt/apache-tomcat-9.0.33/webapps
rm -rf cfmsstheme*
echo "======>Old war file removed successfully. Now copying war file into tomcat..."
cp /Users/rajashree/Dropbox/Vishal_Important/01.Freelancing/Project2_docs/code-repository/theme-integration/fleetmanagement/target/cfmss.war /Users/rajashree/Vishal/mac_opt/apache-tomcat-9.0.33/webapps/cfmsstheme.war
echo "======>New war file copied successfully to tomcat. Now rebooting tomcat..."
cd /Users/rajashree/Vishal/mac_opt/apache-tomcat-9.0.33/bin/
./shutdown.sh
./startup.sh
echo "======>Tomcat reboot completed. Application deployed successfully...!!!"