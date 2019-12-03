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
   <p align="center"><img width="860" src="res/pic102.png" alt="The flight model"> </p>

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

   <p align="center"><img width="480" src="res/pic201.png" alt="Press Run"> </p>

5.2. In the console you can watch how the service is being build any deployed. Once it is ready, a link is provided. Click on that link to the OData endpoints in the browser.

   <p align="center"><img width="860" src="res/pic202.png" alt="Application being deployed"> </p>

5.3. The OData endpoints are displayed. Click on the link for the `BookingService`.

   <p align="center"><img width="480" src="res/pic203.png" alt="API endpoints"> </p>

5.4. You can now see the entities of the booking service...

   <p align="center"><img width="640" src="res/pic204.png" alt="Bookingservice entities"> </p>

   ... and all bookings, by navigating to the `BookingService/Bookings` URL.
   <p align="center"><img width="640" src="res/pic205.png" alt="Bookings entities"> </p>

So far the database and the service layer are running. In the next step we will deploy the UI.

### 6. Run the UI module

6.1. Click on the `app` folder of your project. Click again on the **Run** icon in the main toolbar.

   <p align="center"><img width="480" src="res/pic301.png" alt="Press Run"> </p>


6.2. On the first run, a dialog may be shown asking you to log on to the Neo environment.  Again provide the email address and the password for your user.

   <p align="center"><img width="480" src="res/pic303.png" alt="Log on to create destination"> </p>

   > This double login will become superfluous in future versions of Web IDE.

6.3. Once the app is created, a browser window will be opened.  There, click on the first tile named `app`.

   <p align="center"><img width="480" src="res/pic304.png" alt="Select the tile named 'app'"> </p>

6.4. In the `Bookings` screen, click on the `Go` button to fetch the bookings from the database.

   <p align="center"><img width="640" src="res/pic305.png" alt="Fetch bookings through the 'Go' button"> </p>

6.5. You can get to the details for each booking, by clicking on one of them.
   <p align="center"><img width="640" src="res/pic306.png" alt="Inspect single booking"> </p>

   Compare the fields on the screen with the `Bookings` entity from `db/flight-model.cds`.  Note how `Itineraries.Name` is shown here in the field `Trip`, instead of its `ID`.
