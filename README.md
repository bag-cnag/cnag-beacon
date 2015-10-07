# cnag-beacon

##Contents

* [What it is](#what-it-is)
* [System requirements](#system-requirements)
* [How to run it](#how-to-run-it)
* [How to use it](#how-to-use-it)
* [License and terms of use](#license-and-terms-of-use)
* [Technologies](#technologies)

##What it is
CNAG-beacon within the FP7 project RD-Connect collaborates with Beacon Network (formerly known as the Beacon of Beacons, or BoB, for short) which provide an unified REST API to publicly available GA4GH Beacons (see <http://ga4gh.org/#/beacon> for more details about the Beacon project itself). Beacon Network standardizes the way beacons are accessed and aggregates their results, thus addressing one of the missing parts of the Beacon project itself. 

##System requirements
Elasticsearch 1.3 or newer, Java 1.7 or newer, Maven 3.1 or newer, Java EE runtime 

##How to run it
Start the server:

    For Unix:       TOMCAT_HOME/bin/startup.sh 
    For Windows:    TOMCAT_HOME\bin\startup.bat 

Build the project:

    mvn clean install

Deploy (from `cnag-beacon.war` module):

    mvn tomcat7:run -am

After deployment, the application will be running on <http://localhost:8080/cnag-beacon-1.0/api/info>.


##How to use it
API
-----------------

1. RD-Connect beacon description: <http://localhost:8080/cnag-beacon-1.0/api/info>
2. RD-Connect beacon service:     <http://localhost:8080/cnag-beacon-1.0/api/query?chrom=CHROMOSOME_ID&pos=LONG_POSITION&allele=ALLELE&ref=REF> s.a. <http://localhost:8080/cnag-beacon-1.0/api/query?chrom=1&pos=65720708&allele=T&ref=hg19>

##License and terms of use
The code in this repository is licensed under the [MIT license](http://opensource.org/licenses/MIT). An instance of the Beacon Network is a subject to [these terms of use](http://beacon-network.org/#/terms).

##Technologies
Elasticsearch, Java EE. CDI, EJB, JAX-RS, Bean Validation.