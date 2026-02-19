# PROG3360-Assignment2

### Team Members:

**Group 5**

- Jack Graul (8880032)
- Ethan Brockman (8830509)
- Elowynne Xiong (8965293)
- Alby Mathews Bruse (8980406)
- Parth Gajjar (8959999)

### Project Structure:

- `/OrderService/` - Contains the Order Service microservice code.
- `/ProductService/` - Contains the Product Service microservice code.
- `/Screenshots/` - Contains evidence screenshots.
- `/Scripts/` - Contains a script to initialize feature flags for unleash.
- `/Tests/` - Contains a postman collection for testing the APIs, and results from running the collection.
- `/README.md` - This file, providing an overview of the project and instructions.
- `docker-compose.yml` - Docker Compose file to set up the microservices.

### Instructions:

1. Unzip the project folder.
2. Open a terminal and navigate to the project directory.
3. Run `docker compose up --build` to build and start the microservices. (Make sure Docker is running)

### Unleash
4. Once the unleash container is running go to `localhost:4242` and login with `username: admin` and `password: unleash4all`.
5. Go to profile settings and create a personal access token. Use this token for the unleash-init-flags container in `docker-compose.yml`.
6. Go to API Access and create a client API token. Use this token for both microservices containers in `docker-compose.yml`.
7. Once the tokens are added to `docker-compose.yml` run `docker-compose up --build` to start all the containers and the feature flags should be initialized.
8. To stop running, run `docker-compose down` (not `docker-compose down -v`) or the volume is erased and the api tokens are no longer correct.

9. Open Postman and import the `API_Test.postman_collection.json` file.
10. Use the imported collection to test the APIs for both Order Service and Product Service.
11. (optional) You can use `docker compose down` to stop and remove the containers when done.