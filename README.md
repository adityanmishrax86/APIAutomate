# REST Assured Test Automation Framework

[![Netlify Status](https://api.netlify.com/api/v1/badges/99ce9f50-0aa7-4581-b0c1-f7ac63cc6b34/deploy-status)](https://app.netlify.com/sites/roaring-heliotrope-5d0d1d/deploys)

## Overview
This framework provides a robust solution for API testing using REST Assured and Allure Reports.

## Features
- Maven-based project structure
- Allure reporting integration
- Multiple environment support
- Flexible authentication methods
- SOLID principles implementation
- Detailed logging and reporting

## Setup Instructions
1. Make Sure Latest Java 23 version is installed
2. Clone the repository
2. Install dependencies: `mvn clean install -DskipTests`
3. Run tests: `mvn clean test -Dtest=OrderedTestSuite -Denv=prod`
4. Generate Allure report: `mvn allure:serve`

## Configuration
- Update `src/test/resources/config.properties` with your environment settings
- Set environment using `-Denv=dev` parameter to run tests on dev APIs
- Set environment using `-Denv=prod` parameter to run tests on prod APIs
 

## Writing Tests
1. Create a new test class extending TestBase
2. Use ApiClient for making API calls
3. Add Allure annotations for better reporting

## Bugs Found

| #  | API Endpoint                                | Bug Description                                                       |
|----|---------------------------------------------|-----------------------------------------------------------------------|
| 1  | List All Users (api-6)                      | Limit/Offset is fetching all results                                  |
| 2  | List All Users (api-21)                     | Meta data Total Count is showing 0, when users are there              |
| 3  | Create a new User (api-3)                   | Nickname property is returned empty, after creation                   |
| 4  | Create a new User (api-22)                  | Getting 500 Internal Server Error                                     |
| 5  | Delete a user (api-1)                       | Getting 500 Internal Server Error                                     |
| 6  | Update a user (api-4)                       | With Existing Email/Nickname able to update other User Email/NickName |
| 7  | Get a user (api-23)                         | Non Existing UUID fetching result                                     |
| 8  | Get a user by email and password (api-7)    | Unable to find the User                                               |
| 9  | Add an item to user's wishlist (api-5)      | Getting 422 Status and unable to add                                  |
| 10 | Remove an item from user's wishlist (api-8) | Item was not removed and still showing in Wishlist                    |
| 11 | Add an item to user's wishlist (api-25)     | Able to add existing item to Wishlist                                 |
| 12 | Search Games (api-2)                        | Fetching all the results with the current search                      |
| 13 | Get a game (api-9)                          | Couldn't fetch game from the Game UUID                                |
| 14 | Get games by category (api-10)              | Fetched games are not matching as per the category                    |
| 15 | Update user's avatar (api-11)               | Wrong inaccessible URL is updated instead of gravatar URL             |
| 16 | Get a cart (api-12)                         | Total Price is not matching with the cart items price sum             |
| 17 | Change an item in user's cart (api-13)      | Cart is getting empty                                                 |
| 18 | Remove an item from user's cart (api-14)    | Getting success response 200 when invalid cart item is provided       |
| 19 | Clear user's cart (api-15)                  | Cart is not getting cleared                                           |
| 20 | Create a new order (api-16)                 | Able to add the same item multiple times to the Order                 |
| 21 | List all orders for a user (api-17)         | Limit doesn't limit the result                                        |
| 22 | Update an order status (api-18)             | Allowing to update the order status again, once it is updated         |
| 23 | Get a payment (api-19)                      | Create_at and Updated_at fields are missing                           |

