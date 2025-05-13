# Phantom Mask
You are tasked with building a backend service and a database for a pharmacy platform using the following two raw datasets:

## A. Raw Data
### A.1. Pharmacy Data
Link: [data/pharmacies.json](data/pharmacies.json)

This dataset contains a list of pharmacies with their names, opening hours, cash balances, and mask products. The cash balance represents the amount of money a pharmacy holds in its merchant account on this platform.

> Be cautious when processing the `openingHours` field in `pharmacies.json`, as it contains various time formats.

### A.2. User Data
Link: [data/users.json](data/users.json)

This dataset contains a list of users with their names, cash balances, and purchasing histories. The cash balance represents the amount of money a user holds in their wallet on this platform.

These datasets are raw, meaning you can process and transform the data before loading it into your database.

## B. Task Requirements
Your task is to build an API server with proper documentation and a relational database to allow a front-end client to navigate the data quickly and intuitively. The front-end team will rely on your documentation to build the client application.

The front-end team requires the following operations to be supported:

* List all pharmacies open at a specific time and on a day of the week if requested.
* List all masks sold by a given pharmacy, sorted by mask name or price.
* List all pharmacies with more or fewer than `x` mask products within a specific price range.
* Retrieve the top `x` users by total transaction amount of masks within a date range.
* Calculate the total number of masks and the total transaction value within a date range.
* Search for pharmacies or masks by name and rank the results by relevance to the search term.
* Handle the process of a user purchasing masks, possibly from different pharmacies.

In your repository, you **MUST** include:

1. Documentation for the API interface.
2. Commands to run the ETL (Extract, Transform, and Load) script, which processes the raw datasets and loads them into your database.
3. Commands to set up the server and database.

> If you find the requirements insufficiently detailed, design your solution to be as realistic and practical as possible.

## C. Deliverables
1. Fork this repository to your GitHub account.
2. Write an introduction to your work in [response.md](response.md).
3. Notify HR via email once you have completed the task. Include necessary details such as your GitHub account and the repository URL.

### C.1. Common Mistakes to Avoid
1. Missing documentation.
2. A project that does not function as intended.
3. A solution that fails to meet the requirements.
4. Poor version control practices:
    - Avoid including temporary, redundant, or binary files (e.g., `.DS_Store`, `.idea`, `.vscode`) in the repository. Use an appropriate [.gitignore](https://gist.github.com/octocat/9257657).

## D. Review Process
Your project will be reviewed by at least one backend engineer.

### D.1. Evaluation Criteria
1. **(40%) Familiarity with the programming language and framework:**
    - **Code Quality:**
        - Is the code simple, easy to understand, extendable, reusable, and maintainable?
        - Does the code avoid unnecessary comments or redundant blocks?
        - Is the coding style consistent with language guidelines?
        - Are there any obvious security vulnerabilities?
    - **Logic Design:**
        - Does the database design meet the requirements and remain easy to understand and extend?
        - Is the raw data imported and cleaned effectively before being loaded into the database?
        - Does the API logic meet the requirements and reflect real-world scenarios?
    - **MVC Architecture (optional):**
        - Does the design pattern align with the framework's guidelines?
        - If using a framework, does the solution avoid reinventing the wheel?
2. **(30%) Completion of task requirements.**
3. **(20%) API Quality:**
    - Can the API evolve and add functionality independently of client applications? As the API grows, can existing client applications continue functioning without modification?
    - Are all functionalities discoverable, enabling client applications to fully utilize them?
    - Avoid any 5xx errors when the API is called.
4. **(10%) Communication:**
    - Is the API documentation clear and easy for front-end engineers to understand?

### D.2. Bonus Points
> These are optional and serve as additional proof of your skills. The project will still be considered complete without these features. **If you are applying for a Senior Back-End Engineer position, you MUST complete all bonus tasks.**

1. **(5%) Testing:**
    - Write appropriate tests and provide a coverage report.
2. **(5%) Dockerization:**
    - Use Docker to ensure a consistent setup across environments.
3. **(5%) Deployment:**
    - Deploy the application on a free-tier cloud hosting platform (e.g., [fly.io](https://fly.io/docs/speedrun/) or [render](https://render.com/docs/web-services)) and provide a publicly accessible URL.