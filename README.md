  ## Objective

In this sample exercise you will learn how to work to achive following in SAP Cloud Platform Cloud Foundry environment using Web IDE of the SAP Cloud Platform.

  - Store data on HANA database service in Cloud Foundry
	- Store data on Object store on Cloud Foundry
	- Prints a PDF form and store in Object store (Adobe component is only in NEO) – (Test)
	- Test Application Runtime
	- Test Application Logging

Following Architecutre is implemented :
<p align="center"><img width="700" src="res/Architecture.PNG" > </p>
  
  
  ## Prequisites

Make sure to have the following:

1. An SAP Cloud Platform (Cloud Foundry) account containing at least the services:
   - SAP HANA Database (Standard or Enterprise)
   - SAP HANA Schemas & HDI Containers (hdi-shared)
   - Application Logging
   - Object Store
   - Destination

2. An SAP Cloud Platform (Neo) account that provides access to at least SAP Web IDE.

## Exercise description

### 1. Log on to SAP Web IDE

- If your are unsure where to find the Web IDE URL, follow this [tutorial](https://developers.sap.com/tutorials/sapui5-webide-open-webide.html).
- Web IDE opens up and shows your workspace. The workspace is empty if you use it for the first time.

   <p align="center"><img width="480" src="res/pic102.png" alt="Web IDE workspace"> </p>

### 2. Setup workspace settings

2.1. Click on `Cloud Foundry` in the `Workspace Preferences`

   - In the field for the `API endpoint` select the the URL that matches your Cloud Foundry account (usually the first URL).  If you are asked to logon, use your user/password.

   - Same for the values for `Organization` and `Space`: chose the values matching to your account.

   - Should the be an error on the page saying that the builder is outed, press the `Reinstall Builder` button.

   - Click on the **Save** button, even if you haven't changed anything.

   <p align="center"><img width="480" src="res/pic203.png" alt="Enter Cloud Foundry API endpoint"> </p>

   You will get a confirmation message:
   <p align="center"><img width="320" src="res/pic204.png" alt="Confirmation about stored preferences"> </p>

2.2. Click on the preferences icon on the left and select `Extensions`. Next, enter `HANA Database Dev` in the search box and switch `ON` the `SAP HANA Database Devlopment Tools`. Finally click on the `Save` button at the bottom to enable the tools in your workspace.

   <p align="center"><img width="640" src="res/pic201.png" alt="Web IDE workspace"> </p>

   In the pop-up click on **Refresh**, so that the Web IDE can be re-started with the new settings.

   <p align="center"><img width="320" src="res/pic202.png" alt="Refresh Web IDE workspace"> </p>


### 3. Clone the code

3.1. Switch to the `Development` view in your workspace.
   <p align="center"><img width="480" src="res/pic301.png" alt="Switch to development view"> </p>


3.2. Now, right click on the Workspace text in the Web IDE and select `Git > Clone Repository `

   <p align="center"><img width="640" src="res/pic302.png" alt="Git Clone"> </p>

3.3. In the URL field enter the URL `https://github.com/SAP/cloud-sample-spaceflight-java` and click on the `Clone` button.
   <p align="center"><img width="320" src="res/pic303.png" alt="Git URL"> </p>

3.4. After a short while, you will see the code that was cloned into your Web IDE workspace.

   <p align="center"><img width="640" src="res/pic304.png" alt="Project cloned"> </p>

   > This workspace has now a local copy of the cloned code in the Web IDE.  In the next exercise we will take a look into this project.
   
   ### 4. Build the data model and deploy it to SAP HANA

4.1. Click on the folder `db` of your project and from the context menu select `Build` > `Build`.

   <p align="center"><img width="480" src="res/pic101.png" alt="Build the database"> </p>

   > During this step, the data model is compiled into the respective `hdbcds` artifacts, which are then deployed to an HDI Container (a database schema).  In Web IDE's console view you can watch the progress of the deployment operation.

4.2. While the deployment is running, take a look into the data model.  Open the `db` folder, double-click on the file `flight-model.cds`.  Read through the comments and make yourself familiar with the `Bookings` entity, as this is the one the exercises deal with.
   <p align="center"><img width="860" src="res/pic102_2.png" alt="The flight model"> </p>

   > Tip: To help you find files easier in the tree, enable the `Link Workspace to Editor` setting.  Everytime you switch to another editor, this will select the file in the file tree.
       <p align="center"><img width="320" src="res/pic114.png" alt="Link with editor"> </p>

4.3. Once the HANA artifacts are created, you should see a success message in the console view at the bottom.

   <p align="center"><img width="640" src="res/pic103.png" alt="Console with deployment output"> </p>

4.4. Let's take a look into what was deployed. Right-click again on the `db` folder and select `Open HDI container`.  This will open up the SAP HANA database explorer.

   <p align="center"><img width="640" src="res/pic104.png" alt="Open HDI container"> </p>

   In case you are asked whether to add any database, simply click `No`, as the database connection will be added automatically.

   <p align="center"><img width="480" src="res/pic105.png" alt="Database explorer message"> </p>

4.5. Click on the `Views` icon on the left, select view `BOOKINGSERVICE_BOOKINGS`, which opens the view editor.

   <!-- <p align="center"><img width="860" src="res/pic110.png" alt="See table definition"> </p> -->
   <p align="center"><img width="860" src="res/pic110a.png" alt="See viewa definition"> </p>

4.6. If you want to see the table data, click on the `Open Data` button on the top right.

   <p align="center"><img width="480" src="res/pic111.png" alt="Click on Open Data button"> </p>
   <p align="center"><img width="860" src="res/pic112.png" alt="Data view"> </p>

   > The data you see here is maintained in the resource folder `db > src > csv` and was deployed along with the table definitions.
       <p align="center"><img width="860" src="res/pic113.png" alt="CSV files in db folder"> </p>


### 5. Run the OData service

5.1. Go back to the `Development` perspective of your workspace and select the `srv` folder of your project.  Click on the green **Run** icon in the main toolbar to run the app.

   <p align="center"><img width="480" src="res/pic201_2.png" alt="Press Run"> </p>

5.2. In the console you can watch how the service is being build any deployed. Once it is ready, a link is provided. Click on that link to the OData endpoints in the browser.

   <p align="center"><img width="860" src="res/pic202_2.png" alt="Application being deployed"> </p>

5.3. The OData endpoints are displayed. Click on the link for the `BookingService`.

   <p align="center"><img width="480" src="res/pic203_2.png" alt="API endpoints"> </p>

5.4. You can now see the entities of the booking service...

   <p align="center"><img width="640" src="res/pic204_2.png" alt="Bookingservice entities"> </p>

   ... and all bookings, by navigating to the `BookingService/Bookings` URL.
   <p align="center"><img width="640" src="res/pic205.png" alt="Bookings entities"> </p>

So far the database and the service layer are running. In the next step we will deploy the UI.

### 6. Run the UI module

6.1. Click on the `app` folder of your project. Click again on the **Run** icon in the main toolbar.

   <p align="center"><img width="480" src="res/pic301_2.png" alt="Press Run"> </p>


6.2. On the first run, a dialog may be shown asking you to log on to the Neo environment.  Again provide the email address and the password for your user.

   <p align="center"><img width="480" src="res/pic303_2.png" alt="Log on to create destination"> </p>

   > This double login will become superfluous in future versions of Web IDE.

6.3. Once the app is created, a browser window will be opened.  There, click on the first tile named `app`.

   <p align="center"><img width="480" src="res/pic304_2.png" alt="Select the tile named 'app'"> </p>

6.4. In the `Bookings` screen, click on the `Go` button to fetch the bookings from the database.

   <p align="center"><img width="640" src="res/pic305.png" alt="Fetch bookings through the 'Go' button"> </p>

6.5. You can get to the details for each booking, by clicking on one of them.
   <p align="center"><img width="640" src="res/pic306.png" alt="Inspect single booking"> </p>

   Compare the fields on the screen with the `Bookings` entity from `db/flight-model.cds`.  Note how `Itineraries.Name` is shown here in the field `Trip`, instead of its `ID`.
   
   ### 7. Activate SAP API Business Hub for your user

Navigate to SAP API Business Hub by opening the URL https://api.sap.com in a separate browser tab. Select the `Log On` button and enter the email address and password of your user.
<p align="center"><img width="480" src="res/pic303d.png" alt="Lon on to SAP API Business Hub"> </p>

If this is the first logon with this user, you need to accept the terms of use for SAP API Business hub.  Select the two checkboxes and confirm.
<p align="center"><img width="320" src="res/pic308e.png" alt="Lon on to SAP API Business Hub"> </p>

If you don't get to see this screen, this was already done for this user.

### 8. Setup Mock Server for SAP S/4HANA
In case you do not have access to a live SAP S/4HANA system, there is a server that mocks the relevant OData APIs.  Follow [these instructions](MockServer/README.md) to set it up.

### 9. Switch to a different start branch

For this exercise you have to **switch to another code branch** of the Git repository.

In the Web IDE Git pane click the `+` icon:
<p align="center"><img width="420" src="res/pic301_3.png" alt="Create local branch"> </p>

As source branch select `origin/s4bp-start`.  The name of the local branch is suggested automatically.  Click `Ok`.
<p align="center"><img width="420" src="res/pic302_3.png" alt="Select source branch"> </p>

The file explorer always shows the currently active branch:
<p align="center"><img width="330" src="res/pic303a.png" alt="Branch name in explorer"> </p>

### 10. Import S/4HANA service

10.1. On the `srv` folder select `New` > `Data Model from External Service` from the context menu:
   <p align="center"><img width="480" src="res/pic306_3.png" alt="Data model from external service"> </p>

10.2. In the wizard, select `SAP API Business Hub` and enter `business partner` into the search field. If you are asked to logon to API Business Hub, enter your credentials. Select the API `OData Service for Business Partner`and then press `Next`.
   <p align="center"><img width="640" src="res/pic308b.png" alt="Select the model file"> </p>

10.3. **Deselect** the checkbox `Generate Virtual Data Model classes` and press `Finish`.
   <p align="center"><img width="480" src="res/pic310.png" alt="Turn off class generation"> </p>

   > Java class generation for the data model is not needed in our case, as we will be using precompiled and optimized classes provided by S/4HANA cloud SDK.

10.4. Verify in file explorer that the import has generated two service definitions, one in `xml` format, the other in `json` format   These can be found in folder `srv` > `external`.
   <p align="center"><img width="350" src="res/pic311.png" alt="Imported files"> </p>

   > While the `xml` file, the so-called `edmx`, is the original model file from API Business Hub, the `json` file is the compiled representation of the model for CDS.  It is this `json` file, so-called `cson`, that we will reference from other `cds` source files.


### 11. Use external model in flight data and service model

11.1. **Update CDS**

   After the import we need to make the CDS build system aware of the new model.  On the project node in the tree, select `Build CDS` from the context menu.
   <p align="center"><img width="480" src="res/pic313a.png" alt="Build CDS"> </p>

   > In future versions of Web IDE this step will no longer be necessary.

11.2. **Remove comments in file `db/index.cds`**

   Remove the comment markers from all lines.  After selecting all lines, you can use the `Toggle Line Comment` command from the editor context menu, or hit `Ctrl+/`

   **Note**: After you save the file, **CDS auto build will yield errors** for the project.  No worries, we will fix them in the next step.  The file should look like this:
   <p align="center"><img width="480" src="res/pic313.png" alt="Remove comments in db/index.cds"> </p>

   > That seems like a lot of code.  Let's break it down:
   > 1. In the `using` clause in line `8` the imported model is made available under the `bp` alias.  The string after the `from` keyword is a relative path to the model file.
   > 2. `CustomersRemote` entity from line `14` is basically a view on the imported business partner type.
   >    - From the wide range of available business partner fields, it only selects three of them, and adds convenient name aliases `ID`, `Name`, and `Email`.
   >    - Note how the `Email` field is selected by following two associations in line `18`.
   >    - In line `20` the `where` clause further filters records by just selecting natural persons.
   >    - We will use this `CustomersRemote` table/entity for value helps in the UI.
   > 3. `Customers` entity in line `26` builds on top of `CustomersRemote`.
   >    - However, it turns it into a real table through the `@cds.persistence.table` annotation from line `25`.
   >    - We will use this `Customers` table/entity to cache business partner records that we have retrieved from S/4HANA.  We not only do this for performance reasons, but also because we can conveniently join and query over both 'local' and 'remote' data sets.
   > 4. Finally, in line `31-33` the `Bookings` entity is extended by an association to the `Customers` table from above.  By storing the customer ID with each booking, we link `Bookings` with `Customers` records.

11.3. **Remove comments in file `srv/booking-service.cds`**

   - In line `9` to enable the `excluding` clause.
      > Here we remove properties `CustomerName` and `EmailAddress` from the service, since they are now available through our new `Customer` entity.
   - In the last two lines.
      > Much like the other `as projection on` lines, the two new lines expose the new `Customers` and `CustomersRemote` entities in the `BookingService`.

   The file should now like this:
   <p align="center"><img width="480" src="res/pic314.png" alt="Remove comments in srv/booking-servicde.cds"> </p>

   After you save the file, CDS auto build should now successfully compile our CDS model.
   Should there still be errors shown in `db/index.cds`, close the editors and refresh the browser page.

11.4. **Build and deploy to the database**

   The model is now ready to be compiled and deployed to SAP HANA.  Use the `Build` command on the `db` folder to do this.
   <p align="center"><img width="480" src="res/pic319.png" alt="Deploy to database"> </p>

   There should be a success message in console view for the deploy operation:
   <p align="center"><img width="480" src="res/pic320.png" alt="Success message"> </p>

11.5. **Inspect the service**

   Re-start the OData service:
   <p align="center"><img width="480" src="res/pic321.png" alt="Run Java app"> </p>

   Navigate to URL `.../BookingService/$metadata`.  You will find two new entities `Customers` and `CustomersRemote`.  Also, `Bookings` got a new `Customer_ID` property.
   <p align="center"><img width="480" src="res/pic326b.png" alt="New entities"> </p>

   Using URL `.../BookingService/Bookings` you can see that our sample data (in the `db/src/csv` folder) already makes use of `Customer_ID` and stores the link to a (artificial) customer number `1` in the booking.
   <p align="center"><img width="480" src="res/pic326c.png" alt="Customer ID filled"> </p>

11.6. **Browse the database**

   On the `db` folder, select `Open HDI Container`, which will lead you to the deployed tables and views.
   Click on the `Views` item in the tree.
   <p align="center"><img width="480" src="res/pic326a.png" alt="Browse the database"> </p>

   > Note that there is a new table `..._CUSTOMERS` for the `Customers` entity.  Also, in table `..._BOOKINGS` you can see a new column `CUSTOMER_ID` holding the foreign key to `CUSTOMERS`.
   In the next section you will see how this new table is filled with data from S/4HANA.


### 12. Call S/4 in the Java code

12.1. **Change `CustomersRemoteHandler.java`**:

   Locate the file `CustomersRemoteHandler.java` in folder `srv/src/main/java/com/sap/cloudsamples/spaceflight/`.
   To enable reading a single customer record, add a comment in line `31` and remove the comment from line `32`:
   <p align="center"><img width="640" src="res/pic317.png" alt="Change readCustomer method"> </p>

   To enable reading multiple customer records, add a comment in line `47` and remove the comment from line `48`:
   <p align="center"><img width="640" src="res/pic318.png" alt="Change queryCustomers method"> </p>

   > If you are curious, you can take a look into the `CustomersReplicator` class to see how we call the business partner OData service using the S/4HANA Cloud SDK.

12.2. **Verify the code works**

   a. Run the Java application:
   <p align="center"><img width="480" src="res/pic321.png" alt="Run Java app"> </p>

   b. Click the URL of the Java application in the run console:
   <p align="center"><img width="480" src="res/pic322.png" alt="Open Java app"> </p>

   c. Select the BookingService endpoint:
   <p align="center"><img width="480" src="res/pic323.png" alt="Open BookingService"> </p>

   d. Check the data retreived as `Bookings` and as `CustomersRemote`:
   <p align="center"><img width="480" src="res/pic324.png" alt="Query Bookings"> </p>
   <p align="center"><img width="480" src="res/pic325.png" alt="Query CustomersRemote"> </p>

   > Whenever we get or query the `CustomersRemote` entity, a new remote call is made. In the next section we will cache the retrieved customer records.

### 13. Prepare storing S/4 customers in the local database

13.1. **Adjust `BookingsHandler.java`**

   Remove the line comments in line `75`:
   <p align="center"><img width="640" src="res/pic315.png" alt="Call fetchAndSaveCustomer"> </p>

   Also remove the comments from method `fetchAndSaveCustomer`.  Use `Ctrl+/` or the from the menu "Edit -> Comment -> Toggle Line Comment":
   <p align="center"><img width="640" src="res/pic316.png" alt="Implement fetchAndSaveCustomer"> </p>

   Save the file.

13.2. **Run the service again**

   <p align="center"><img width="480" src="res/pic321.png" alt="Run Java app"> </p>

   > Now, for each new booking created, the respective customer record is going to be fetched and saved to the local database.

   To create a booking, we need a UI, though.  Let's do this real quick in the next section.

### 14. Create bookings for S/4 customers

14.1. **Adjust the UI**

   The UI can be adapted when adding/changing Fiori annotations to CDS models. Remove the line comments for the section marked in the following figures.  Also, remove the lines marked with `REMOVE`.
   <p align="center"><img width="480" src="res/pic327.png" alt="Change Customers annotations"> </p>
   <p align="center"><img width="480" src="res/pic328.png" alt="Change Bookings annotations"> </p>
   <p align="center"><img width="480" src="res/pic329.png" alt="Change Bookings annotations"> </p>
   <p align="center"><img width="480" src="res/pic330.png" alt="Change Bookings line item annotation"> </p>

14.2. **Run the UI within SAP Web IDE**

   <p align="center"><img width="600" src="res/pic331.png" alt="Open run configuration"> </p>

   <p align="center"><img width="600" src="res/pic332.png" alt="Enable run with local metadata"> </p>

   <p align="center"><img width="700" src="res/pic333.png" alt="Query bookings"> </p>

14.3. **Create a new booking for an S/4 customer:**

   <p align="center"><img width="700" src="res/pic334.png" alt="Create booking"> </p>

14.4. **Check that this S/4 customer has been persisted (cached) in the database**

   <p align="center"><img width="700" src="res/pic335.png" alt="Query Customers entity"> </p>
   
### 15. Connect CF Application to Adobe Service

15.1 Expose Adobe Service from Neo environment as OAuth Authentication Mechanism
  
 <p align="center"><img width="700" src="res/OAuth_Neo.PNG" > </p>

15.2  Create Destination in CF Sub-Account to Abobe service in Neo

 <p align="center"><img width="700" src="res/OAuthDestination.PNG" > </p>
 
15.3 Create [AdobeService](/srv/src/main/java/com/sap/cloudsamples/spaceflight/adobe/AdobeService.java) Class to call Adobe API's
Abode API help can be found here in [SAP Help](https://help.sap.com/viewer/6d3eac5a9e3144a7b43932a1078c7628/Cloud/en-US/3f4f7318d8c941308696512c2125424e.html)

Adobe Connect is made using Cloud SDK API's - [Destination API](https://help.sap.com/doc/a7234bc9e7bf43c08c8652cdf5b7e160/1.0/en-US/com/sap/cloud/sdk/cloudplatform/connectivity/Destination.html) 	
 
 

 
   
