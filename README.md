# Phantom Mask
You are building a backend service and a database for a pharmacy platform, with the following 2 raw datasets:

## Pharmacy Data
Link: [data/pharmacies.json](data/pharmacies.json)

This dataset contains a list of pharmacies with their names, opening hours, cash balances, and mask products. Cash balance represents the amount of money that a pharmacy hold in the merchant account on this platform. It increases by the mask price whenever a user purchases masks from the pharmacy. 

## User data
Link: [data/users.json](data/users.json)

This dataset contains a list of users with their names, cash balances, and purchasing histories. Cash balance represents the amount of money that a user hold in his wallets on this platform. It decreases by the mask price whenever the user purchases masks.

These are all raw data, which means that you are allowed to process and transform the data, before you load it into your database.

# The Task
The task is to build an API server, with documentation and a backing relational database that will allow a frontend client to navigate through that sea of data easily and intuitively. The frontend team will build the frontend client based on the documentation.

The operations the frontend team would need you to support are:

* List all pharmacies that are open at a certain datetime
* List all pharmacies that are open on a day of the week, at a certain time
* List all masks that are sold by a given pharmacy, sorted by mask name or mask price 
* List all pharmacies that have more or less than x mask products within a price range
* Search for pharmacies or masks by name, ranked by relevance to search term
* The top x users by total transaction amount of masks within a date range
* The total amount of masks and dollar value of transactions that happened within a date range
* Edit pharmacy name, mask name, and mask price
* Remove a mask product from a pharmacy given by mask name
* Process a user purchases a mask from a pharmacy, and handle all relevant data changes in an atomic transaction

In your repository, you would need to document the API interface, the commands to run the ETL (extract, transform and load) script that takes in the raw data sets as input, and outputs to your database, and the command to set up your server and database. You may use docker to ensure a uniform setup across environments.

## Bonus
This is optional, and serves as additional proof points. We will consider it complete even without this functionality

Write appropriate tests with an appropriate coverage report.

## Deployment
It'd be great if you can deploy this on the free tier of any cloud hosting platform (eg. free dyno on Heroku), so that we can easily access the application via an url.

# About Response
* Fork this repository to your github account.
* Add redtear1115 as collaborator to your private repository.
* Write a introduction to all your works on following link.
  * Link: [response.md](response.md) (Current contest as an example. Feel free to edit/remove it.)
