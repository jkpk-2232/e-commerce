DEV_JAVA_HOME="/home/ubuntu/opt/jdk"
DEV_BUILD_WAR_NAME="indiacabs.war"
DEV_WAR_NAME="indiacabsdev"
DEV_SERVER="35.163.255.158"
DEV_USER="ubuntu"
DEV_CATALINA_HOME="/home/ubuntu/opt/tomcat"

DEMO_JAVA_HOME="/home/ubuntu/opt/jdk"
DEMO_BUILD_WAR_NAME="indiacabs.war"
DEMO_WAR_NAME="indiacabsdemo"
DEMO_SERVER="35.163.255.158"
DEMO_USER="ubuntu"
DEMO_CATALINA_HOME="/home/ubuntu/opt/tomcat"

PROD_JAVA_HOME="/home/ubuntu/opt/jdk"
PROD_BUILD_WAR_NAME="indiacabs.war"
PROD_WAR_NAME="FleetManagement"
PROD_SERVER="35.165.34.16"
PROD_USER="ubuntu"
PROD_CATALINA_HOME="/home/ubuntu/opt/tomcat"

function stop_remote_tomcat_and_remove_war () {
	CURRENT_SERVER=$1
	CURRENT_USER=$2
	CURRENT_JAVA_HOME=$3
	CURRENT_CATALINA_HOME=$4
	CURRENT_WAR_NAME=$5
	CURRENT_PEM_FILE=$6
	
	
	echo "Stopping tomcat"
	COMMAND="ssh -i $CURRENT_PEM_FILE $CURRENT_USER@$CURRENT_SERVER \"export JAVA_HOME=$CURRENT_JAVA_HOME; export CATALINA_HOME=$CURRENT_CATALINA_HOME; $CURRENT_CATALINA_HOME/bin/shutdown.sh; sleep 10; rm -rf $CURRENT_CATALINA_HOME/webapps/$CURRENT_WAR_NAME; rm /home/$CURRENT_USER/opt/tomcat/webapps/$CURRENT_WAR_NAME.war\""
	echo $COMMAND
	ssh -i $CURRENT_PEM_FILE $CURRENT_USER@$CURRENT_SERVER "export JAVA_HOME=$CURRENT_JAVA_HOME; export CATALINA_HOME=$CURRENT_CATALINA_HOME; $CURRENT_CATALINA_HOME/bin/shutdown.sh; sleep 10; rm -rf $CURRENT_CATALINA_HOME/webapps/$CURRENT_WAR_NAME; rm $CURRENT_CATALINA_HOME/webapps/$CURRENT_WAR_NAME.war"
}

function start_remote_tomcat () {
	CURRENT_SERVER=$1
	CURRENT_USER=$2
	CURRENT_JAVA_HOME=$3
	CURRENT_CATALINA_HOME=$4
	CURRENT_PEM_FILE=$5
	
	echo "Sleeping for 10 secs"
	# Wait for 5 seconds to give enough time for the previous tomcat shutdown to complete.
	sleep 10
	echo "Starting tomcat"
	COMMAND="ssh -i $CURRENT_PEM_FILE $CURRENT_USER@$CURRENT_SERVER \"export JAVA_HOME=$CURRENT_JAVA_HOME; export CATALINA_HOME=$CURRENT_CATALINA_HOME; $CURRENT_CATALINA_HOME/bin/startup.sh\"; sleep 10"
	echo $COMMAND
	ssh -i $CURRENT_PEM_FILE $CURRENT_USER@$CURRENT_SERVER "export JAVA_HOME=$CURRENT_JAVA_HOME; export CATALINA_HOME=$CURRENT_CATALINA_HOME; $CURRENT_CATALINA_HOME/bin/startup.sh; sleep 10"
}

function deploy () {
	CURRENT_WAR_NAME=$1
	CURRENT_SERVER=$2
	CURRENT_USER=$3
	CURRENT_CATALINA_HOME=$4
	CURRENT_PEM_FILE=$5
	echo "Deploying war"
	COMMAND="scp -i $CURRENT_PEM_FILE target/$DEV_BUILD_WAR_NAME $CURRENT_USER@$CURRENT_SERVER:$CURRENT_CATALINA_HOME/webapps/$CURRENT_WAR_NAME.war"
	echo $COMMAND
	scp -i $CURRENT_PEM_FILE target/$DEV_BUILD_WAR_NAME $CURRENT_USER@$CURRENT_SERVER:$CURRENT_CATALINA_HOME/webapps/$CURRENT_WAR_NAME.war
}

if [ $# -lt 2 ]
then
        echo "Usage : $0 dev | demo | demo pemfile"
        exit
fi

case "$1" in

dev)
	stop_remote_tomcat_and_remove_war $DEV_SERVER $DEV_USER $DEV_JAVA_HOME $DEV_CATALINA_HOME $DEV_WAR_NAME $2
	deploy $DEV_WAR_NAME $DEV_SERVER $DEV_USER $DEV_CATALINA_HOME $2
	start_remote_tomcat $DEV_SERVER $DEV_USER $DEV_JAVA_HOME $CURRENT_CATALINA_HOME $2
	;;

demo)
    stop_remote_tomcat_and_remove_war $DEMO_SERVER $DEMO_USER $DEMO_JAVA_HOME $DEMO_CATALINA_HOME $DEMO_WAR_NAME $2
    deploy $DEMO_WAR_NAME $DEMO_SERVER $DEMO_USER $DEMO_CATALINA_HOME $2
    start_remote_tomcat $DEMO_SERVER $DEMO_USER $DEMO_JAVA_HOME $CURRENT_CATALINA_HOME $2
	;;

prod)
	echo stop_remote_tomcat_and_remove_war $PROD_SERVER $PROD_USER $PROD_JAVA_HOME $PROD_CATALINA_HOME $PROD_WAR_NAME $2
    deploy $PROD_WAR_NAME $PROD_SERVER $PROD_USER $PROD_CATALINA_HOME $2
    start_remote_tomcat $PROD_SERVER $PROD_USER $PROD_JAVA_HOME $CURRENT_CATALINA_HOME $2
	;;

*) echo "Invalid option: $1"
	;;

esac
