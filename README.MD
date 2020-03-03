# Jmeter-MySQL

This project will try to help you move your Jmeter results csv file onto a MySQL db.
The assumption is that you have already created your test cases in jmeter, run the tests and saved the results file as _jmeter.csv_
Your server has the docker engine istalled.

## Running
The following containers will have to be linked together using [docker networks](https://docs.docker.com/network/) so that they can communicate.

Create a docker network that the containers will use. In this tutorial we will use a Network called _mynet_ .

```bash
docker network create mynet
```

Copy the downloaded folder to your server.
Navigate inside the _mysql_ folder and run the docker command to build the mysql database to host our db:

```bash
docker run -d --network mynet --name jmeter-mysql -e MYSQL_ROOT_PASSWORD=admin -p 8700:3306 mysql

OR

docker run -d --network mynet --name jmeter-mysql --restart always -e MYSQL_ROOT_PASSWORD=admin -p 8700:3306 mysql

OR

docker run -d --network mynet --name jmeter-mysql --restart always -e MYSQL_ROOT_PASSWORD=admin --publish 8700:3306 mysql

```

### Usage

```

jmeter-mysql: our mysql container name
mynet: our network
admin: our root password. Usually mysql will give a default user name as root.
8700: our public port to access the db. However, for our case, the container will bypass this and just use the default 3306
mysql: the image pulled from [Docker Hub](https://hub.docker.com/)

```

In your server, run the following command to get the mysql container IP address.
A container running in its own network will have a randon IP address assigned to it.

```bash
docker inspect jmeter-mysql | grep IPAdress
```

In your server, run the following command to install _phpmyadmin_ .
This is a tool that will provide a UI to access the db from the browser.

```bash
docker run -d --network mynet --name phpmyadmin -e PMA_HOST=jmeter-mysql -p 8800:80 phpmyadmin/phpmyadmin

```

You can now open in your browser _http://yourServerIP:8800/_ to access phpmyadmin. This will give you a window to the mysql db.

From _phpmyadmin_ create a database called _jmeter_ and a table called _loadTest_
In the table, access the SQL table and run the following command to create the columns that a jmeter would result in

```bash
create table loadTest (timeStamp varchar(255),elapsed varchar(255),label varchar(255),responseCode varchar(255),responseMessage varchar(255),threadName varchar(255),dataType varchar(255),success varchar(255),failureMessage varchar(255),bytes varchar(255),sentBytes varchar(255),grpThreads varchar(255),allThreads varchar(255),URL varchar(255),Latency varchar(255),IdleTime varchar(255),Connect varchar(255) );

```

Alternatively, you can do this on the command line:

```
<!--To access the db-->
docker exec -it jmeter-mysql mysql -uroot -padmin -h<yourMysqlContainerIPAddress> -P3306

<!--To dispaly databases in the db-->
show databases;

<!--Create a database called jmeter-->
create database jmeter;

<!--Go into jmeter db to create a table-->
use jmeter;

<!--Create a table called loadTest in jmeter db with all columns needed-->
create table loadTest (timeStamp varchar(255),elapsed varchar(255),label varchar(255),responseCode varchar(255),responseMessage varchar(255),threadName varchar(255),dataType varchar(255),success varchar(255),failureMessage varchar(255),bytes varchar(255),sentBytes varchar(255),grpThreads varchar(255),allThreads varchar(255),URL varchar(255),Latency varchar(255),IdleTime varchar(255),Connect varchar(255) );


We have now created a mysql container, phpmyadmin and we have our mysql connector to knit everything together.

To run the jmeter tests, run the following command:

```bash

docker build --no-cache=True -t mysql/test . && docker run --network mynet --rm -v jmeterResults:/opt/apache-jmeter-5.2.1 mysql/test

```

This will run your tests and save csv file to the jmeterResults volume in the host server.

Run the command below to copy this file into the mysql container.
Use the _docker exec_ command combined with cat. The following command will copy the _jmeter.csv_ file to the running container, 
where _/tmp/jmeter.csv_ represents a file in docker container and _jmeter.csv_ is an existing file on the host.

```bash

docker exec -i jmeter-mysql sh -c 'cat > /tmp/jmeter.csv' < ./jmeter.csv

```

### Push csv file to the _jmeter_ database in MySQL

Navigate to the _mysql-connector_ directory and run the following command:

```bash

docker build --no-cache=True -t connect/test . && docker run --network mynet --rm connect/test

```

This will run and push all rows in the csv file to the db.

Alternatively, this could be done from the mysql container. 

```bash
<!--To access the db-->
docker exec -it jmeter-mysql mysql -uroot -padmin -h<yourMysqlContainerIPAddress> -P3306

<!--To dispaly databases in the db-->
show databases;

<!--Create a database called jmeter-->
create database jmeter;

<!--Go into jmeter db to use it-->
use jmeter;

<!--Go into jmeter db to use it-->
SHOW GLOBAL VARIABLES LIKE 'local_infile';
SET GLOBAL local_infile = true;
SHOW VARIABLES LIKE '%connection%';
SET GLOBAL max_connections = 50000;
SET GLOBAL secure-file-priv = "";
LOAD DATA LOCAL INFILE "./tmp/jmeter.csv" INTO TABLE loadTest COLUMNS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"' ESCAPED BY '"' LINES TERMINATED BY '\n' IGNORE 1 LINES;

```























