#!/bin/bash          
echo "======>Building war file..."
mvn -Pcfmss_prod clean package
echo "======>War file build successfully. Now removing old war file..."
cd /Users/rajashree/Desktop/
rm -rf myhub.war
mv /Users/rajashree/Dropbox/Vishal_Important/01.Freelancing/Project2_docs/code-repository/theme-integration/fleetmanagement/target/cfmss.war /Users/rajashree/Desktop/myhub.war
echo "======>War file moved to deploy folder"
sudo scp -i /Users/rajashree/Dropbox/Vishal_Important/01.Freelancing/Project2_docs/docs/New_Docs/Bilwam-Production.pem /Users/rajashree/Desktop/myhub.war ubuntu@13.233.239.64:/home/ubuntu
echo "======>New war file copied successfully to tomcat. Please login to prod server for tomcat restart"