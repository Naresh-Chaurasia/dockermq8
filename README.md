# dockermq8
Docker image for IBM® MQ 8 + monitor and mdb example and configuration for JBoss EAP 7

## Run (preferably in linux)

docker build --tag mq:8 --file ./server/mq8 ./server/

docker run --env LICENSE=accept --env MQ_QMGR_NAME=QM1 --volume /mnt/mqm:/var/mqm --publish 1414:1414 --detach mq:8

*/mnt/mqm must be a directory with filesystem supported by IBM MQ, in Docker Toolbox or boot2docker mount an external disk.

## Monitor
* Download JMSToolBox from https://sourceforge.net/projects/jmstoolbox/ unzip it
* Configure IBM MQ with extra jar com.ibm.mq.allclient.jar

## Test with JBoss EAP 7

* Download JBoss EAP 7 from https://developers.redhat.com/download-manager/file/jboss-eap-7.0.0.zip unzip it
* Copy wmq.jmsra.rar to standalone/deployments/
* Edit standalone-full.xml (see standalone-full.xml-changes)
* Build mdbtest project and deploy it


Run JBoss EAP 7 with: sh bin/standalone.sh -c standalone-full.xml

