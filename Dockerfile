FROM quay.io/wildfly/wildfly
ADD target/g2app.war /opt/jboss/wildfly/standalone/deployments/

