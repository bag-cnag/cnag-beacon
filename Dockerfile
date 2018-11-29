FROM tomcat:8.0.32-jre8

MAINTAINER Joan Protasio <joan.protasio@cnag.crg.eu>

ARG user

# Update the default application repository sources list and install dependencies
RUN apt-get update && apt-get -y upgrade
RUN apt-get install -y git

COPY startup.sh /opt/startup.sh
RUN chmod +x /opt/startup.sh

RUN git clone https://$user@github.com/prota/cnag-beacon.git -b develop
RUN cp /usr/local/tomcat/cnag-beacon/target/cnag-beacon-1.0.war /usr/local/tomcat/webapps/


CMD ["/opt/startup.sh"]
