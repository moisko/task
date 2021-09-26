Task:

Create a Spring Boot application that handles the CRUD operations for a simple shop.
The shop has only 2 required entities: product and subscriber. Every product must have at least the following information: name, creation date (timestamp later to be transformed to human readable format in REST responses) and a field that marks if the product is currently under sale or it is not available. Every subscriber must have at least the following information: first name, last name, unique identifier and a date that marks when the subscriber has joined (same requirements as product date). Every product can be sold to multiple subscribers and of course one subscriber can have multiple purchases.
The shop needs to support the following CRUD operations via REST APIs:
- Add subscriber
- Get subscriber
- Get all subscribers by product
- Update subscriber’s information
- Delete subscriber
- Get product
- Add product
- Get all products by subscriber
- Update product’s information
- Delete product
  The project needs to have at least 1 positive and 1 negative test for all services.

Bonus points: Add an audit service, that returns:
- Number of subscribers
- Number of sold products with filters for date and for the active field.
- Most popular products

Technical requirements:
- java 8+
- Spring
- Relevant way to store the data according to you
- ORM using JPA (Hibernate, etc)
- REST WS
- Junit 5