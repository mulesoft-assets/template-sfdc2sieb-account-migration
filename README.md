
# Anypoint Template: Salesforce to Siebel Account Migration	

<!-- Header (start) -->
This template moves a large set of accounts from Salesforce to Oracle Siebel. You can trigger this manually, or programmatically with an HTTP call. 

Accounts are upserted so that the migration can be run multiple times without worrying about creating duplicates. This template uses batch to efficiently process many records at a time.

![2e37dac8-ba7c-4aca-96a7-6207c6a955b6-image.png](https://exchange2-file-upload-service-kprod.s3.us-east-1.amazonaws.com:443/2e37dac8-ba7c-4aca-96a7-6207c6a955b6-image.png)
<!-- Header (end) -->

# License Agreement
This template is subject to the conditions of the <a href="https://s3.amazonaws.com/templates-examples/AnypointTemplateLicense.pdf">MuleSoft License Agreement</a>. Review the terms of the license before downloading and using this template. You can use this template for free with the Mule Enterprise Edition, CloudHub, or as a trial in Anypoint Studio. 
# Use Case
<!-- Use Case (start) -->
Synchronize accounts from a Salesforce instance to Oracle Siebel Business Objects.

This template migrates accounts from a Salesforce instance to Oracle Siebel Business Objects, and lets you specify filtering criteria and desired behavior when an account already exists in the Siebel instance. 

As implemented, this template leverage the Mule batch module. The batch job is divided to Process and On Complete stages. The template queries SalesForce for all existing accounts that match the filter criteria. During the Process stage the template creates accounts in Siebel and updates those which already exist. Finally during the On Complete stage, the template outputs statistics data into the console and sends a notification email with the results of the batch execution to the group of configured email addresses.
<!-- Use Case (end) -->

# Considerations
<!-- Default Considerations (start) -->
<!-- Default Considerations (end) -->
<!-- Considerations (start) -->
To make this template run, there are certain preconditions that must be considered. All of them deal with the preparations in both, that must be made for the template to run smoothly. Failing to do so can lead to unexpected behavior of the template.
<!-- Considerations (end) -->

## Salesforce Considerations

- Where can I check that the field configuration for my Salesforce instance is the right one? See: <a href="https://help.salesforce.com/HTViewHelpDoc?id=checking_field_accessibility_for_a_particular_field.htm&language=en_US">Salesforce: Checking Field Accessibility for a Particular Field</a>.
- Can I modify the Field Access Settings? How? See: <a href="https://help.salesforce.com/HTViewHelpDoc?id=modifying_field_access_settings.htm&language=en_US">Salesforce: Modifying Field Access Settings</a>.

### As a Data Source

If a user who configures the template for the source system does not have at least *read only* permissions for the fields that are fetched, then an *InvalidFieldFault* API fault displays.

```
java.lang.RuntimeException: [InvalidFieldFault [ApiQueryFault 
[ApiFault  exceptionCode='INVALID_FIELD'
exceptionMessage='Account.Phone, Account.Rating, Account.RecordTypeId, 
Account.ShippingCity
^
ERROR at Row:1:Column:486
No such column 'RecordTypeId' on entity 'Account'. If you are 
attempting to use a custom field, be sure to append the '__c' 
after the custom field name. Reference your WSDL or the 
describe call for the appropriate names.'
]
row='1'
column='486'
]
]
```

## Oracle Siebel Considerations

This template uses date time or timestamp fields from Oracle Siebel to do comparisons and take further actions. While the template handles the time zone by sending all such fields in a neutral time zone, it cannot discover the time zone in which the Siebel instance is in. It's up to you to provide this information. See [Oracle's Setting Time Zone Preferences](http://docs.oracle.com/cd/B40099_02/books/Fundamentals/Fund_settingoptions3.html).

### As a Data Destination

To make the Siebel connector work smoothly you have to provide the correct version of the Siebel jars (Siebel.jar, SiebelJI_enu.jar) that works with your Siebel installation.

# Run it!
Simple steps to get this template running.
<!-- Run it (start) -->
No matter how you run this template, this is an example of the output you see after you browse to the HTTP endpoint:

{
    "Message": "Batch Process initiated",
    "ID": "fb324340-c0bc-11e8-9a16-2ac63fa6f77a",
    "RecordCount": 7,
    "StartExecutionOn": "2018-09-25T14:17:33Z"
}
<!-- Run it (end) -->

## Running On Premises
In this section we help you run this template on your computer.
<!-- Running on premise (start) -->

<!-- Running on premise (end) -->

### Where to Download Anypoint Studio and the Mule Runtime
If you are new to Mule, download this software:

+ [Download Anypoint Studio](https://www.mulesoft.com/platform/studio)
+ [Download Mule runtime](https://www.mulesoft.com/lp/dl/mule-esb-enterprise)

**Note:** Anypoint Studio requires JDK 8.
<!-- Where to download (start) -->

<!-- Where to download (end) -->

### Importing a Template into Studio
In Studio, click the Exchange X icon in the upper left of the taskbar, log in with your Anypoint Platform credentials, search for the template, and click Open.
<!-- Importing into Studio (start) -->

<!-- Importing into Studio (end) -->

### Running on Studio
After you import your template into Anypoint Studio, follow these steps to run it:

1. Locate the properties file `mule.dev.properties`, in src/main/resources.
2. Complete all the properties required per the examples in the "Properties to Configure" section.
3. Right click the template project folder.
4. Hover your mouse over `Run as`.
5. Click `Mule Application (configure)`.
6. Inside the dialog, select Environment and set the variable `mule.env` to the value `dev`.
7. Click `Run`.
<!-- Running on Studio (start) -->

<!-- Running on Studio (end) -->

### Running on Mule Standalone
Update the properties in one of the property files, for example in mule.prod.properties, and run your app with a corresponding environment variable. In this example, use `mule.env=prod`. 


## Running on CloudHub
When creating your application in CloudHub, go to Runtime Manager > Manage Application > Properties to set the environment variables listed in "Properties to Configure" as well as the setting the mule.env value.
<!-- Running on Cloudhub (start) -->
Once your app is all set and started, if you choose `sfdc2siebaccountmigration` as the domain name to trigger the use case, you just need to browse to `http://sfdc2siebaccountmigration.cloudhub.io/migrateaccounts` and report is generated and sent to the email addresses configured.
<!-- Running on Cloudhub (end) -->

### Deploying a Template in CloudHub
In Studio, right click your project name in Package Explorer and select Anypoint Platform > Deploy on CloudHub.
<!-- Deploying on Cloudhub (start) -->

<!-- Deploying on Cloudhub (end) -->

## Properties to Configure
To use this template, configure properties such as credentials, configurations, etc.) in the properties file or in CloudHub from Runtime Manager > Manage Application > Properties. The sections that follow list example values.
### Application Configuration
<!-- Application Configuration (start) -->
- page.size `100`
- http.port `9090` 

#### SalesForce Connector configuration
- sfdc.username `joan.baez@orgb`
- sfdc.password `JoanBaez456`
- sfdc.securityToken `ces56arl7apQs56XTddf34X`

#### Oracle Siebel Connector configuration
- sieb.user `SADMIN`
- sieb.password `SADMIN`
- sieb.server `192.168.10.8`
- sieb.serverName `SBA_82`
- sieb.objectManager `EAIObjMgr_enu`
- sieb.port `2321`

#### SMTP Services configuration
- smtp.host `smtp.gmail.com`
- smtp.port `587`
- smtp.user `email%40example.com`
- smtp.password `password` 

#### Mail Details
- mail.from `batch.migrateaccounts.migration%40mulesoft.com`
- mail.to `cesar.garcia@mulesoft.com`
- mail.subject `Batch Job Finished Report`
<!-- Application Configuration (end) -->

# API Calls
<!-- API Calls (start) -->
SalesForce imposes limits on the number of API calls that can be made. Therefore calculating this amount may be an important factor to consider. This template uses just one query to Salesforce, therefore this is not something to worry about.
<!-- API Calls (end) -->

# Customize It!
This brief guide provides a high level understanding of how this template is built and how you can change it according to your needs. As Mule applications are based on XML files, this page describes the XML files used with this template. More files are available such as test classes and Mule application files, but to keep it simple, we focus on these XML files:

* config.xml
* businessLogic.xml
* endpoints.xml
* errorHandling.xml<!-- Customize it (start) -->

<!-- Customize it (end) -->

## config.xml
<!-- Default Config XML (start) -->
This file provides the configuration for connectors and configuration properties. Only change this file to make core changes to the connector processing logic. Otherwise, all parameters that can be modified should instead be in a properties file, which is the recommended place to make changes.
<!-- Default Config XML (end) -->
<!-- Config XML (start) -->
<!-- Config XML (end) -->

## businessLogic.xml
<!-- Default Business Logic XML (start) -->
This file holds the functional aspect of the Anypoint template, directed by one flow responsible of conducting the business logic. For the purpose of this particular template the *mainFlow* just executes a batch job which handles all its logic. This flow has Exception Strategy that basically consists on invoking the *defaultChoiseExceptionStrategy* defined in *errorHandling.xml* file.
<!-- Default Business Logic XML (end) -->
<!-- Business Logic XML (start) -->
<!-- Business Logic XML (end) -->

## endpoints.xml
<!-- Default Endpoints XML (start) -->
This is the file where you can find the inbound and outbound sides of your integration app.
This template has only a HTTP connector as the way to trigger the use case.

### Inbound Flow
**HTTP Connector** - Start Report Generation
- `${http.port}` is set as a property to be defined either on a property file or in CloudHub environment variables.
- The path configured by default is `migrateaccounts` and you are free to change for the one you prefer.
- The host name for all endpoints in your CloudHub configuration should be defined as `localhost`. CloudHub will then route requests from your application domain URL to the endpoint.
<!-- Default Endpoints XML (end) -->
<!-- Endpoints XML (start) -->
<!-- Endpoints XML (end) -->

## errorHandling.xml
<!-- Default Error Handling XML (start) -->
This file handles how your integration reacts depending on the different exceptions. This file provides error handling that is referenced by the main flow in the business logic.

<!-- Default Error Handling XML (end) -->
<!-- Error Handling XML (start) -->
<!-- Error Handling XML (end) -->
<!-- Extras (start) -->
<!-- Extras (end) -->
