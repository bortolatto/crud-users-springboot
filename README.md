# Softplan's Public API

This POC aims to provide a public API to manage people's records. It's a RESTFul API which uses good patterns in order to
follow [Richardson's Maturity Model](https://martinfowler.com/articles/richardsonMaturityModel.html).  

# Features implemented
* CRUD operations with input validations and error handling
* API versioning
* Security ~~(basic authentication)~~ using JWT
* ~~Google's OAuth2 integration~~ (integration with [Auth0 machine-to-machine](https://auth0.com/machine-to-machine/))
* Endpoint with a link to this code repository
* Different ways to execute the application (Docker image, AWS or even locally)
* Documentation - Swagger
* Level 3 of the Richardson Maturity Model
* Integration tests using 3rd party tools (Postman) along with the integration
  tests in the project itself

# Features missing
* Chat among people logged in
* Frontend

# Misc
I've provided Postman's collections and environments as well to help the interaction
with API. Detailed explanations about the endpoints can be found on the 
documentation itself (more on this in the next sections).

# Using the API
### Documentation
You can access the documentation in /swagger-ui.html.
On the top-right you should see three specs:
* Commons
* V1
* V2

In **Commons**, you'll find the endpoint which provides the link to this repository,
whereas either **V1** or **V2** will explain the difference between these two versions.

### Interact with API
The most straightforward way to interact with this API is accessing the
application through AWS. The address can be found in the **AWS** Postman environment.
But you have two more options as well:
* Pulling the Docker image on Docker hub: ```docker pull bortolattol/code-challenge-softplan:latest```
* ... or running it locally: ```./mvnw spring-boot:run```

# Postman
As I said before, I've provided Postman's collections and their environments as well.
### Environments
* **aws**: refers to AWS URL
* **docker**: refers to URL running on localhost

These can be found on the root of this project:
* aws.postman_environment.json
* docker.postman_environment.json.

You have to import these to be able to make requests.
More information about Postman's environments can be found [here](https://learning.postman.com/docs/sending-requests/managing-environments/).

### Obtaining an access token
You must obtain an access token, which will allow you to make requests.
You can achieve this in two different ways:
* Using cURL:
```
curl --request POST \
  --url 'https://dev-qgqmfum4.us.auth0.com/oauth/token' \
  --header 'content-type: application/x-www-form-urlencoded' \
  --data grant_type=client_credentials \
  --data 'client_id=JLRmO4dZPB7DM0phHqYTI0ycUfbNrteu' \
  --data client_secret=JyfncS8tZ8cSxGLI_V3m3JaUOs2NQdlGgAx1ARletKAFfmmyDpwoIF6a0_bzu66V \
  --data audience=http://localhost:8080
```

* Using **OAuth2 Token** request inside of **Softplan Challenge** collection.

Both will return an access token, which must be used in **Authorization** header with
**Bearer** prefix.

### Collections
You'll find two collections here:
* Softplan Challenge Integration Tests.postman_collection.json
* Softplan Challenge.postman_collection.json

As the name suggests, the first one contains the integration tests which should be run to
validate JSON structure, input formats, among other things.
After you've imported both collections, run the tests by clicking on the right arrow
(next to the collection's name) and choose the blue-button **Run**.
You should see a screen containing information about the tests that Postman will execute.
To execute, just hit on **"Run Softplan Chall..."** button.

# Doubts & Suggestions
If everything I'd said makes no sense, send me an email! I'll glad to help! 
bortolatto.eduardo@outlook.com