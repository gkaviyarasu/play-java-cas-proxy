To run this application, checkout the source and execute 

```script
$./activator run
```
It will start the server in 9000 port

# Overview

play-java-cas-proxy is an application which exposes rest services on top of a sample data store, which can be accessed from other application on behalf of the logged in user context.
This is a sample project for play framework 2.4 integration with CAS proxy authentication mechanism. This project exposes all rest API's which are protected by cas proxy authentication. the steps to generate the proxy is given below.

Note: This project contains only backend API and does not implemented yet to handle sessions as of now.

# CAS Proxy Ticket

play-java-cas-proxy authenticates using CAS proxy ticket As this RESTFul services will be accessed from other applications which are already authenticated and these call can be backend call, To provide the logged in user context, the invoking application needs to generate proxy  ticket behalf of the loggedin user and send it along with its request as a Query Parameter with name ticket.

Note: Proxy tickets and service tickets are having a validity period of 10 seconds. So, the tickets needs to be used as soon as they were generated.

## Steps to generate proxy granting ticket

1. When login is successful, CAS will redirect to a callback url which was provided at the time of login request .

2. The implementation at the callback url will have the logic to validate the ticket and will get the logged in user details. Along with the service and ticket parameter, an additional parameter pgtUrl also need to be passed to that ValidateService url. Note that the pgtUrl should be SSL enabled and should have a valid SSL certificate.
A sample validation request,

```url
https://localhost:8443/cas/p3/serviceValidate?ticket=ST-1445-h2nftSfQohutTLebUWss-cas.mydomain.com&service=https://app-url/callback&pgtUrl=https://app-url/proxycallback
```
Validation response will have the PGTIOU ticket.

3. CAS will call the pgtUrl specified in the above step first time to validate the ssl certificate and generates a valid Proxy Granting Ticket and it will call the same url again to deliver it.
A sample proxy callback reqyest,

```url
https://app-url/proxycallback?pgtId=TGT-2-2erpnwSjgQqBkmDVh4BdX6iN1Jq1s9chTnHBcEapNE0Z7e2Jjp-cas.mydomain.com&pgtIou=PGTIOU-1-fqLckLIczfpYM6r5de0V-cas.mydomain.com
```
_Note:_ the Proxy Granting Ticket (pgtId) has validity of 2 hours ideal time and 8 hours max validity. using this pgt, proxy tickets can be generated any number of times for any other services which accepts proxy.

## Steps to generate proxy ticket

1. To generate a proxy ticket, the target service and proxy granting ticket needs to be passed to cas proxy URL.
Sample proxy ticket request,

```url
https://localhost:8443/cas/proxy?pgt=TGT-2-2erpnwSjgQqBkmDVh4BdX6iN1Jq1s9chTnHBcEapNE0Z7e2Jjp-cas.myservice.com&targetService=http://prinhydsphp0211/callback
```

This request will return an XML containing the Proxy ticket as below,
```xml
<cas:serviceResponse xmlns:cas="http://www.yale.edu/tp/cas">
	<cas:proxySuccess>
		<cas:proxyTicket>ST-6-9e0ynXWwOqjcvnWSI534-cas.myservice.com</cas:proxyTicket>
	</cas:proxySuccess>
</cas:serviceResponse>
```
2. With the generated proxy ticket, the application safely call the play-java-cas-proxy APIs with the user context.

An example service request to get current logged in Employee details,

```url
http://192.168.1.45:8080/api/employee/?ticket=ST-6-9e0ynXWwOqjcvnWSI534-cas.myservice.com
```
On the response of first request, The play-java-cas-proxy application will return a variable named `CasSessionId` in header. This ticket can be used further to access the application without CAS proxy token. To do that the invoking app needs to set that variable and the corresponding value in request header.

References:

https://wiki.jasig.org/display/CAS/Proxy+CAS+Walkthrough