jmeter-wildfly-ejb-java-request
===============================

JMeter extension to invoke Wildfly EJBs.

Project is still in progress. This is the TODO list:

1) Improve documentation in README.md

2) Add binaries with folder structure to copy/paste to JMeter installation directory

3) Create sample EJB to test jmeter script.


Installation instructions
=========================

1) Download jmeter 2.11 

2) Start jmeter with java 7  (java 6 has problems with ssl certificates)

3) Load your test or create a new one

4) Add a new "Java Request" element

5) In the Java Request select "WildflyEJBInvocation".

6) Fill in your EJB parameters

7) Execute the test!
