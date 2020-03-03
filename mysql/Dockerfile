#------------------------------------------
# Starting from Alpine base image
# ------------------------------------------
FROM alpine

# ------------------------------------------
# set container environment on local time
# ------------------------------------------
ENV TZ=Africa/Nairobi
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# ------------------------------------------
# Defining proxy variables
# ------------------------------------------
ENV http_proxy "http://172.29.213.98:8080"
ENV https_proxy "https://172.29.213.98:8080"

# ------------------------------------------
# Defining environment variables
# ------------------------------------------
ARG JMETER_VERSION="5.2.1"
ARG MYSQL_CONNECTOR_VERSION="8.0.19"
ENV JMETER_HOME /opt/apache-jmeter-${JMETER_VERSION}
ENV JMETER_BIN  ${JMETER_HOME}/bin
ENV JMETER_LIB  ${JMETER_HOME}/lib
ENV MIRROR_HOST http://mirrors.ocf.berkeley.edu/apache/jmeter
ENV MYSQL_CONNECTOR_DOWNLOAD_URL https://repo1.maven.org/maven2/mysql/mysql-connector-java/
ENV JMETER_DOWNLOAD_URL ${MIRROR_HOST}/binaries/apache-jmeter-${JMETER_VERSION}.tgz
ENV JMETER_PLUGINS_DOWNLOAD_URL http://repo1.maven.org/maven2/kg/apc
ENV JMETER_PLUGINS_FOLDER ${JMETER_HOME}/lib/ext/

# ------------------------------------------
# Install Java
# ------------------------------------------
#ENV JAVA_HOME="/usr/lib/jvm/java-1.8-openjdk"
RUN apk update \
&& apk upgrade \
        && apk add ca-certificates \
        && update-ca-certificates
RUN apk add --update openjdk8-jre tzdata curl unzip bash \
    && apk add --no-cache nss \
    && cp /usr/share/zoneinfo/Europe/Rome /etc/localtime \
    && echo "Europe/Amsterdam" >  /etc/timezone \
    && rm -rf /var/cache/apk/*

RUN apk update && apk upgrade \
&& apk add --no-cache bash \
&& apk add --no-cache --virtual=build-dependencies unzip \
&& apk add --no-cache curl \
&& apk add --no-cache openjdk8-jre

ENV JAVA_HOME="/usr/lib/jvm/java-1.8-openjdk"

#ENV JAVA_HOME /usr/lib/jvm/java-13-openjdk-amd64/
RUN export JAVA_HOME


# ------------------------------------------
# Create temporary download folder
# ------------------------------------------
RUN mkdir -p /tmp/dependencies

# ------------------------------------------
# Download jmeter with CURL
# ------------------------------------------
#RUN apk add --no-cache curl
#RUN curl -L --silent ${JMETER_DOWNLOAD_URL} >  /tmp/dependencies/apache-jmeter-${JMETER_VERSION}.tgz

# ------------------------------------------
# OR download jmeter with wget
# ------------------------------------------
RUN wget ${JMETER_DOWNLOAD_URL} -O /tmp/dependencies/apache-jmeter-${JMETER_VERSION}.tgz
#RUN wget ${JMETER_DOWNLOAD_URL} -P /tmp/dependencies
#RUN wget -c http://mirrors.ocf.berkeley.edu/apache/jmeter/binaries/apache-jmeter-5.2.tgz

# ------------------------------------------
# Unpack jmeter to container $ROOT
# ------------------------------------------
RUN mkdir -p /opt
RUN tar xvzf /tmp/dependencies/apache-jmeter-${JMETER_VERSION}.tgz -C /opt
#RUN tar xvzf apache-jmeter-5.2.tgz -C /opt
RUN rm -rf /tmp/dependencies

# ------------------------------------------
# Download and unpack jmeter plugins
# ------------------------------------------
RUN curl -L --silent ${JMETER_PLUGINS_DOWNLOAD_URL}/jmeter-plugins-dummy/0.4/jmeter-plugins-dummy-0.4.jar -o ${JMETER_PLUGINS_FOLDER}/jmeter-plugins-dummy-0.4.jar
RUN curl -L --silent ${JMETER_PLUGINS_DOWNLOAD_URL}/jmeter-plugins-cmn-jmeter/0.6/jmeter-plugins-cmn-jmeter-0.6.jar -o ${JMETER_PLUGINS_FOLDER}/jmeter-plugins-cmn-jmeter-0.6.jar
RUN curl -L --silent ${JMETER_PLUGINS_DOWNLOAD_URL}/jmeter-plugins-standard/1.4.0/jmeter-plugins-standard-1.4.0.jar -o ${JMETER_PLUGINS_FOLDER}/jmeter-plugins-standard-1.4.0.jar
RUN curl -L --silent ${JMETER_PLUGINS_DOWNLOAD_URL}/jmeter-plugins-extras-libs/1.4.0/jmeter-plugins-extras-libs-1.4.0.jar -o ${JMETER_PLUGINS_FOLDER}/jmeter-plugins-extras-libs-1.4.0.jar
RUN curl -L --silent ${JMETER_PLUGINS_DOWNLOAD_URL}/jmeter-plugins-extras/1.4.0/jmeter-plugins-extras-1.4.0.jar -o ${JMETER_PLUGINS_FOLDER}/jmeter-plugins-extras-1.4.0.jar
#RUN wget -q ${MYSQL_CONNECTOR_DOWNLOAD_URL}/${MYSQL_CONNECTOR_VERSION}/mysql-connector-java-${MYSQL_CONNECTOR_VERSION}.jar -O ${JMETER_LIB}/mysql-connector-java-${MYSQL_CONNECTOR_VERSION}.jar
#RUN wget -q https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.19/mysql-connector-java-8.0.19.jar
#RUN wget -q https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.19/mysql-connector-java-8.0.19.jar
#RUN wget https://github.com/NovatecConsulting/JMeter-InfluxDB-Writer/releases/download/v-1.2/JMeter-InfluxDB-Writer-plugin-1.2.jar  -O ${JMETER_HOME}/lib/ext/JMeter-InfluxDB-Writer-plugin-1.2.jar

#COPY JMeter-InfluxDB-Writer-plugin-1.2.jar ${JMETER_HOME}/lib/ext/
#RUN echo "$PWD"
#RUN ls -lha
# ------------------------------------------
# Set Jmeter PATH
# ------------------------------------------
#ENV JAVA_HOME="/usr/lib/jvm/java-1.8-openjdk"
ENV PATH $PATH:${JMETER_BIN}
#ENV JAVA_OPTS="-XX:PermSize=1024m -XX:MaxPermSize=512m"

# ------------------------------------------
# Set working directory
# ------------------------------------------
WORKDIR ${JMETER_HOME}

# ------------------------------------------
# Copy files to the working directory
# ------------------------------------------
COPY . .
COPY mysql-connector-java-8.0.19.jar ./lib
#RUN chmod +x launch.sh
#RUN chmod +x jmeter.sh
#RUN ls -lha
#RUN echo "$PWD"

#WORKDIR ${JMETER_BIN}
#COPY jmeter.sh .
#COPY ApigeeServicesSandbox.jmx .
#RUN ls -lha | grep jmeter.sh
#RUN jmeter -n -t ApigeeServicesSandbox.jmx -l /tmp/result.jtl -j /tmp/jmeterPerformanceAPITestResults.log
#COPY JMeter-InfluxDB-Writer-plugin-1.2.jar ${JMETER_HOME}/lib/ext/
#WORKDIR ${JMETER_HOME}/lib/ext/
#RUN ls -lha
WORKDIR ${JMETER_HOME}
RUN ls -lha

# ------------------------------------------
# Print docker working directory
# ------------------------------------------
RUN echo "$PWD"
# ------------------------------------------
# Set docker entry or command
# ------------------------------------------
ENTRYPOINT ["bash", "mysql.sh"]