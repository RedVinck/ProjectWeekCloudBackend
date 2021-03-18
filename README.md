# Create a Spring Boot Web Service

1. Setting up development environment

	Check the quickstart guide on : <https://spring.io/quickstart>
	to install the Java SDK
	
2. Install the Java dependencies tool "Maven"
3. Create a Spring Boot Project:

	Check the guide : <https://spring.io/guides/gs/spring-boot/>
	
	- Starting with Spring Initializr - <https://start.spring.io/>

	For all Spring applications, you should start with the Spring Initializr. The Initializr offers a fast way to pull in all the dependencies you need for an application and does a lot of the setup for you. This example needs only the **Spring Web dependency**.
	
	You can get a Maven build file with the necessary dependencies directly from the Spring Initializr.

4. Start the application

```
# cd <application>
# mvn spring-boot:run
```

This will startup the application but it doesn't do much yet.
In the next step we will create a Product Application : a web service that will return a list of products in JSON format.

### Rename DemoApplication.java -> ProductApplication.java

Make sure to refactor the code so you don't have any errors.
The code should look something like this:
	
```
package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
	
@SpringBootApplication
public class ProductApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ProductApplication.class, args);
	}
	
}
```

This is the main application code that will start our springboot application.

### Next create a "Product.java" file that will describe our Product class (including getters and setters)
	
Here's the code for the Product class:
	
```
package com.example.demo;

public class Product {
	
    private String id;
    private String title;
    private String description;
    private String thumbnail_url;
    private int quantity;
    private float price;
	
    public Product() {
	
    }
	
    public Product(String id, String title, String description, String thumbnail_url, int quantity, float price) {
        super();
        this.id = id;
        this.title = title;
        this.description = description;
        this.thumbnail_url = thumbnail_url;
        this.quantity = quantity;
        this.price = price;
    }
	
    public String getId() {
        return id;
    }
	
    public void setId(String id) {
        this.id = id;
    }
	
    public String getTitle() {
        return title;
    }
	
    public void setTitle(String title) {
        this.title = title;
    }
	
    public String getDescription() {
        return description;
    }
	
    public void setDescription(String description) {
        this.description = description;
    }
	
    public String getThumbnail_url() {
        return thumbnail_url;
    }
	
    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }
	
    public int getQuantity() {
        return quantity;
    }
	
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
	
    public float getPrice() {
        return price;
    }
	
    public void setPrice(int price) {
        this.price = price;
    }
	
}
```
	
### Create another file "ProductController.java" which will implement a RestControler

Here's the code:
	
```
package com.example.demo;
	
import java.util.Arrays;
import java.util.List;
	
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
	
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class ProductController {
    
    @RequestMapping("/products")
    public List<Product> getAllProducts() {
        return Arrays.asList(
            new Product(
                "1",
                "macbook Retina 13.3' ME662 (2013)",
                "3.0GHz Dual-core Haswell Intel Core i5 Turbo Boost up to 3.2 GHz, 3MB L3 cache 8GB (two 4GB SO-DIMMs) of 1600MHz DDR3 SDRAM",
                "https://macbookpics.s3.eu-de.cloud-object-storage.appdomain.cloud/img1.jpeg",
                10,
                2399
            ),
            new Product(
                "2",
                "Macbook Pro 13.3' Retina MF841LL/A",
                "Macbook Pro 13.3' Retina MF841LL/A Model 2015 Option Ram Care 12/2016",
                "https://macbookpics.s3.eu-de.cloud-object-storage.appdomain.cloud/img2.jpeg",
                15,
                1199
            ),
            new Product(
                "3",
                "Macbook Pro 15.4' Retina MC975LL/A Model 2012",
                "3.0GHz Dual-core Haswell Intel Core i5 Turbo Boost up to 3.2 GHz, 3MB L3 cache 8GB (two 4GB SO-DIMMs) of 1600MHz DDR3 SDRAM",
                "https://macbookpics.s3.eu-de.cloud-object-storage.appdomain.cloud/img3.jpeg",
                1,
                1800
            )
        );
    }
}
```
### Modify to server listening port to 8000 instead of 8080
```
Add 'server.port=8000' to 'src/main/resources/application.properties' 
```
### Start the application and check it's working correctly

```
# mvn spring-boot:run
```

Open a browser and open the url: <http://localhost:8000/products>
You should get a JSON result of 3 products.

## Dockerize Spring Boot application

First create a build of the application:

```
# mvn clean install
```

This will result in a "demo-0.0.1-snapshot.jar" file being created in the "target" directory.

Create a "Dockerfile" in the root of the project directory:

```
# cat Dockerfile
FROM openjdk:13-oracle
VOLUME /tmp
EXPOSE 8000
RUN mkdir -p /app/
RUN mkdir -p /app/logs/
ADD target/demo-0.0.1-SNAPSHOT.jar /app/app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=container", "-jar", "/app/app.jar"]
```

Create a docker image and test it locally:

```
# docker build -t product:1.0 .
```
Start a container using the newly created image

```
# docker run --name productservice -d -p 8000:8000 product:1.0
```
Check the application : open a browser <http://localhost:3000/products>

## Install IBM Cloud command-line and RedHat OpenShift tools:

https://cloud.ibm.com/docs/cli?topic=cli-getting-started

https://docs.openshift.com/container-platform/4.6/cli_reference/openshift_cli/getting-started-cli.html#installing-openshift-cli

## Deploy Docker image on Kubernetes

In order to be able to deploy the image, we will need to store the image in a registry which can be accessed by our Kubernetes cluster.
This can be a public registry such a hub.docker.com or in this case we will use a private container registry within IBM CLoud.

You will use the "IBM Cloud Container Registry"-service within IBM Cloud (Frankfurt Region) and use an existing namespace which has already been provide for you.

### Push the local Docker image to the IBM Cloud Container Registry

First login to IBM Cloud and to the regsitry (cr stands for 'container registry'):

```
# ibmcloud login --sso
```
This will allow to login using a one-time password via a browser, authentication
 
```
# ibmcloud target -r eu-de
# ibmdloud cr login
# ibmcloud cr namespace-list
...

```
Take a not of the namespace !!!

Next tag the local docker image to point to the remote registry. In the example below we replace the tag "product:1.0" with "de.icr.io/<your_namespace>/product:1.0" (this points to a registry within the "Frankfurt" region)

```
# docker tag product:1.0 de.icr.io/<your_namespace>/product:1.0
# docker images
REPOSITORY                                 TAG              IMAGE ID       CREATED        SIZE
de.icr.io/<your_namespace>/product             1.0              ffc3ba48e36f   2 hours ago    508MB
```

### Push image to remote repository

```
# docker push de.icr.io/<your_namespace>/product:1.0
```
Now the image resides in the Cloud registry. You can also verify this within the Cloud service or via the following command:
 
```
# ibmcloud cr images
Listing images...

Repository                        Tag   Digest         Namespace      Created      Size     Security status
de.icr.io/<namespace>/product    1.0   30804ebe2a73   ydbnamespace   1 hour ago   269 MB   37 Issues
```

### Login to RedHat OpenShift environment

Within the OpenShift Web-Console : click on your name in the top-right corner and select the 'Copy Login Command'.
Click on the 'Display Token' link and copy the 'oc login ...' command.
Execute this command in your local environment in order to login with token.

### Grant access to OpenShift to the IBM Cloud Container Registry

* Create a Cloud API Key outside your project folder to avoid security exposure when checking-in into Github

```
# ibmcloud iam api-key-create MyKey -d "this is my API key" --file mykey.json
```

Create a Secret in OpenShift from the command-line:

```
# oc create secret docker-registry mysecret --docker-server=de.icr.io --docker-username=iamapikey --docker-password=<apikey from mykey.json-file>
# oc secrets link default mysecret --for=pull

```

### Create a kubernetes deployment and deploy to OpenShift

Replace the following command to include <your_namespace> before executing !

```
# kubectl create deployment product --image=de.icr.io/<your_namespace>/product:1.0 --dry-run=client -o=yaml > deployment.yaml
# cat deployment.yaml
# kubectl apply -f deployment.yaml
```

### Create a service on Openshift for our deployment

```
# kubectl create service clusterip product --tcp=8000:8000 --dry-run=client -o=yaml >> service.yaml
# kubectl apply -f service.yaml
```

### Create a route via ingress

Openshift provides an easy way to create a route via the following command:

```
# oc expose svc/product
```

Of course, you don't have to expose the service via a route if it's only for internal use.

## Automate deployment via Git

In the previous steps you have experienced how to create an application and how to deploy it as a container onto an Openshift cluster.

Each time you would do an update to the application source code, you would have to go through all those steps again.

In the next steps we will show how you can automate this whole process.
First create a new repository in github.com and check in your code from the springboot application into this repo.

For example: create a new repo in Github: 'springboot-productservice'

Next Goto your local directory where you have developed the springboot application:

```
# cd <application>
# echo "# springboot-productservice" >> README.md
# git init
# git add .
# git commit -m "first commit"
# git branch -M master
# git remote add origin https://github.com/<gitusername>/springboot-productservice.git
# git push -u origin master
```

If you followed the previous step to Dockerize the application you should have a Dockerfile ready to use.

Open the Openshift Console and open the Developer perspective.
Click "Add" - "From Dockerfile"
Fill in the form:
* Git Repo URL: e.g. https://github.com/<username>/springboot-productservice.git
* Container Port: 8000
* Select Application -> Create Application
* Application Name : e.g. productservice
* Name: e.g. productservice
* Click Create

Once the build completes, you can verify the service by clicking on the 'Route' generated as part of the deployment.

This should result in a full deployment of the application. the "Build Config" will create a "Build" to construct the final image. Once the Deployment has completed, you can verify the application is working correctly by clicking on the generated route URL.

Adding a Github Webhook to automatically update the Openshift deployment as soon as new code is pushed into the repository:

* Goto the "Build Config" and select your specific Build Config
* Click the Github "Copy URL with Secret"
* Goto to your Github project on "github.com" and select "Settings" -> Webhooks
* Add a new webhook and paste the URL copied in previous step in "Payload URL"
* Select "application/json" as Content Type
* Click "Add webhook"
* The result of this configuration will trigger a new build run on Openshift as soon as new code gets committed into the repository.










