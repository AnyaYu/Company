**Employees CRUD Operations**<br>
This is a sample Spring Boot project with CRUD operations for managing employees.

**Technologies**<br>
The following technologies are used in this project:

* Spring Boot
* Spring Data JPA
* Spring Security
* H2 Database
* Swagger UI
* Apache Kafka

**Getting Started**<br>
* Import the project into your favorite IDE (e.g. IntelliJ IDEA, Eclipse, etc.)
* Edit **application.properties** to specify:<br>
 **server.port** - the port where you want to run the App<br>
  **spring.kafka.bootstrap-servers** - your kafka server (you can run the app without a kafka server as well. 
It will try to connect to a server in a separate thread. That should not affect the work of API )
* Build and run the project: open com.yushina.CompanyApplication and run it
* From there, you can view and test the API endpoints for managing employees.
* Authentication: For demo purposes name=user and password=password
* Please, use the following Postman collection **to test the App:** https://api.postman.com/collections/17010112-130b746a-b6d9-4e44-9f22-e0b525c04b04?access_key=PMAT-01GT95VEBQGZ7VEJ0QMR9YVH3R
* **Swagger** documentation is available here: http://localhost:port/swagger-ui/index.html

**API Endpoints**<br>
The following API endpoints are available for managing employees:

* GET	/employees	    Get a list of all employees
* GET	/employees/{id}	Get an employee by ID
* POST	/employees	Create a new employee
* PUT	/employees/{id}	Update an existing employee by ID
* DELETE	/employees/{id}	Delete an employee by ID

**License**<br>
This project is licensed under the MIT License.