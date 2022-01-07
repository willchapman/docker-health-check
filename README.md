# Docker Health Check Server
A simple utility that can be configured to perform basic 
health checks for Docker containers. It runs a very 
simplified web interface on port 80 and responds with a 
HTTP code based on the health of the configured health
checks.

This is intended to be a small, fast health check server. As such, 
the dependency requirements must be kept to a minimum for this core 
software. If additional dependencies are required, especially for 
heath check logic, those should be kept in separate projects.

## Requirements
 - Maven 3.6 or later
 - Java 8

## Supported Health Checks
Any custom health check can be used, as long as it is a 
subclass of ```AbstractCheck```. Below is a list of built-in 
health checks.
- TCP Check
  - Performs a simple TCP connection to a specified
  host and port. If the connection is successful then the health check passes

## Building
This project is built using Maven. Below is an example of how to build  this repository
```shell
$ git clone https://github.com/willchapman/docker-health-check.git
$ cd docker-health-check
$ mvn package
```
The resulting docker-health-check*.jar file can be executed using the ```java -jar <jarfile>``` command. 

## Configuration
The server loads a properties file on start up that will read the configured 
health checks. If all of them pass, then the health check
will respond with a HTTP 200 code with a simple text
string of how many checks passed. The format is simple
the class name of the check as the key, and a configuration
String the check will parse to perform its task. Below
is an example of checking a TCP connection to google.com
```properties
com.raxware.healthCheck.checks.SimpleTCPHealthCheck=google.com:443
```
It will default to the ```config/checks.properties``` file but can be overridden
by setting the HEALTH_CHECK_CONFIG environment variable.

## Roadmap
Future improvements will be added as time allows. 
- Configuration of web service ports
- Customize text output of web service response
  - A summary report of each health check as an option
- Additional generic health checks
- Improve customization of logging
- Add unit testing

## Contributing
If you would like to submit any improvements to this code base, please fork the code and submit a pull request. I will review
and patch as time permits.

## License

The MIT License (MIT)

Copyright (c) 2022 Will Chapman

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation 
files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, 
copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom 
the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE 
WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR 
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR 
OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.