FROM java:8
COPY . /
RUN echo "$PWD"
COPY jmeter.csv /var/lib/mysql/
RUN ls -lha
WORKDIR /
RUN javac ConnectMySQL.java
CMD ["java", "-classpath", "mysql-connector-java-8.0.19.jar:.","ConnectMySQL"]