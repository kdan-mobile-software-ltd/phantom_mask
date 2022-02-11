# Phantom Mask
You are building a backend service and a database for a pharmacy platform, with the following 2 raw datasets:

## Pharmacy Data
Link: [data/pharmacies.json](data/pharmacies.json)

This dataset contains a list of pharmacies with their names, opening hours, cash balances, and mask products. Cash balance represents the amount of money that a pharmacy holds in the merchant account on this platform. It increases by the mask price whenever a user purchases masks from the pharmacy.

> Please be careful at processing pharmacies.json's openingHours value. It contains some different kinds of time formats.

## User data
Link: [data/users.json](data/users.json)

This dataset contains a list of users with their names, cash balances, and purchasing histories. Cash balance represents the amount of money that a user holds in his wallets on this platform. It decreases by the mask price whenever the user purchases masks.

These are all raw data, which means that you are allowed to process and transform the data before you load it into your database.

# The Task Requirments
The task is to build an API server, with documentation and a backing relational database that will allow a frontend client to navigate through that sea of data easily and intuitively. The frontend team will build the frontend client based on the documentation.

The operations the frontend team would need you to support are:

* List all pharmacies that are open at a certain time, and on a day of the week if requested
* List all masks that are sold by a given pharmacy, sorted by mask name or mask price
* List all pharmacies that have more or less than x mask products within a price range
* The top x users by total transaction amount of masks within a date range
* The total amount of masks and dollar value of transactions that happened within a date range
* Search for pharmacies or masks by name, ranked by relevance to search term
* Process a user purchases a mask from a pharmacy, and handle all relevant data changes in an atomic transaction

In your repository, you would need to document the API interface, the commands to run the ETL (extract, transform, and load) script that takes in the raw data sets as input, and outputs to your database, and the command to set up your server and database.

> If you thought that the requirement of description is not detailed, please try to make your design as close to the living and make sense. 

# Response Your Job
1. Fork this repository to your GitHub account.
2. Add [redtear1115](https://github.com/redtear1115) as collaborator to your private repository.
3. Write an introduction to all your works on [response.md](response.md).
    * Please describe how to use the API in the API document. You can edit by any format (eg. Markdown or OpenAPI) or free tools (eg. [hackMD](https://hackmd.io/), [postman](https://www.postman.com/), [google doc](https://docs.google.com/document/u/0/) or [swagger](https://swagger.io/specification/)).
4. Write an email to let HR know you are all done. Don't forget necessary information, such as your GitHub account and the repository URL.

### Common Mistakes You Should Avoid

1. Missing documentation.
2. Project does not work.
3. Provided solution does not meet the requirments.
4. Poor knowledge of version control systems.
    - Temporary/redundant/binary files (.DS_Store, .idea, .vscode) are in repository or result archive. Choose a good [.gitignore](https://gist.github.com/octocat/9257657)

# How We Review

Your project will be reviewed by at least one of our backend engineers.
## Main Standards
1. (40%) How familiar with your programming language and framework.
    - Code Quality
        - Is the code simple, easy to understand/extend, reuseable and maintainable?
        - is polluted with bad comments or redundant code blocks inside?
        - Is the coding style consistent with the language guidelines?
        - Are there any obvious vulnerability for security?
    - Logic Design
        - Is the database design match the requirements, and is easy to understand/extend?
        - The way you import and clean the raw data to your database.
        - Is the API logic design match the requirements and close to the living?
    - MVC Architecture (optional)
        - Is the design pattern consistent with the framework guidelines?
        - Don't forget the rule `Stop trying to reinvent the wheel` if you're using the framework.
2. (30%) Finish rate of the task requirements.
3. (20%) API Quality
    - API should be able to evolve and add functionality independently from client applications. As the API evolves, existing client applications should continue to function without modification.
    - All functionality should be discoverable so that client applications can fully use it.
4. (10%) Communication
    - Is the API document easy to understand for frontend engineers?
## Bonus
> This is optional and serves as additional proof points. We will consider it complete even without this functionality. **If you are applying for Senior Back-End Engineer, you have to do all bonus task requirements.**

1. (5%) Testing
    - Write appropriate tests with an appropriate coverage report.
2. (5%) Dockerlize
    - You may use docker to ensure a uniform setup across environments.
3. (5%) Deploy
    - It'd be great if you can deploy this on the free tier of any cloud hosting platform (eg. [free dyno on Heroku](https://devcenter.heroku.com/categories/dynos)) so that we can easily access the application via an URL.
