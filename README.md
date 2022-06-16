# Capstone

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
1. [Schema](#Schema)


## Overview
### Description


### App Evaluation
- **Category:** Matching app
- **Mobile:** Native Android app
- **Story:** Matches nonprofit organizations and businesses with students and professionals willing to build a website or app for a nonprofit for their portfolio.
- **Market:** Any nonprofit and any developer may benefit from the app. The organizations can find developers to build their app and developers can get real-world experience for their portfolio. 
- **Habit:** Developers can view the app requirements of the organizations, and if interested, can share their GitHub, resume, and profile with the organization. The more frequently they share their profile, the higher their chances of being matched to a project. 
- **Scope:** Can be used by any web or app developer willing to improve their portfolio.

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**
* Both nonprofits and developers can sign up and log in
* Developers can scroll through the list of projects posted by nonprofits
* Developers can apply via email to build a posted project by opening up a detail view of the project
* Developers can edit their profile 
* Nonprofits can scroll through the list of all developers
* Nonprofits can invite via email a developer to build their project by opening up a detail view of a developer's profile
* Nonprofits can post their web, iOS, Android, or other projects

**Optional Nice-to-have Stories**

* Nonprofits can filter developers
* Developers can filter projects
* Polished UI
* Animations
* Developers can receive a notification when a nonprofit invites them
* Nonprofits can receive a notification when a developer sends an application
* Developers can showcase their GitHub projects on their profile

### 2. Screen Archetypes

* Login Screen
   * User can login
* Registration Screen
   * User can create a new account
 * Nonprofit search screen
   * User (developer) can see the nonprofits' posted projects
 * Developer Details Screen
     * User (nonprofit) can invite a developer to build their project
 * Project Details Screen
     * User (developer) can send an application to a nonprofit
 * Developer search screen
   * User (nonprofit) can see all the developers and reach out to them
 * Edit Profile Screen
   * User (developer) can set up or update their profile
 * Compose Project Screen
   * User (nonprofit) can post a project 




### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Log in
* Register
* For developer
    * Search project
    * Set up profile
    * Project detail view
* For nonprofit
    * Search developer
    * Post project
    * Developer detail view

**Flow Navigation** (Screen to Screen)
List all your screens from above. Under each screen, list the screens you can navigate to from that screen.

* Login <=> Register
* Login <=> Search screen
* Project search screen <=> Set up profile screen
* Developer search screen <=> Post project screen
* Developer search screen <=> Developer details screen
* Project search screen <=> Project details screen



## Wireframes
![alt-text](https://github.com/orhun-kolgeli/Capstone/blob/main/capstone_wireframes.jpg)

## Schema 
### Models
#### User

   | Property      | Type           | Description |
   | ------------- | --------       | ------------|
   | objectId      | String         | unique id for the user post (default field) |
   | createdAt     | DateTime       | date when post is created (default field) |
   | updatedAt     | DateTime       | date when post is last updated (default field) |
   | email         | String         | email address of the user |
   | profile image | File => url / or initials (Picasso) | profile picture of the user |
   | password      | String         | user password |
   | organization  | enum type      | false if developer; true if organization |
   | project       | Pointer to Project | project tied to the account, if any
   | developer     | Pointer to Developer | developer tied to the account, if any
   
   
#### Project

   | Property      | Type           | Description |
   | ------------- | --------       | ------------|
   | objectId      | String         | unique id for the user post (default field) |
   | createdAt     | DateTime       | date when post is created (default field) |
   | updatedAt     | DateTime       | date when post is last updated (default field) |
   | type          | String         | project type, i.e., iOS, Android, Web, or Other |
   | image         | File           | image describing the project, e.g., mockup |

#### Developer

   | Property      | Type           | Description |
   | ------------- | --------       | ------------|
   | objectId      | String         | unique id for the user post (default field) |
   | createdAt     | DateTime       | date when post is created (default field) |
   | updatedAt     | DateTime       | date when post is last updated (default field) |
   | Bio           | String         | bio of the developer |
   | GitHub        | String         | GitHub username |
   | Skills        | String         | web or mobile development skills |
   Having pre-determined skills to choose from: for filtering, matching, etc.
   
   
   
   
### Networking
#### List of network requests by screen
   - Search Project Screen
      - (Read/GET) Query all projects
      ```java
        // Specify what type of data to query, i.e., Project
        // Error handling
        ParseQuery<Project> query = ParseQuery.getQuery(Project.class);
        // Limit query to latest 20 projects
        query.setLimit(20);
        // Order projects by creation date
        query.addDescendingOrder("createdAt");
        // Start an asynchronous call for projects
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Project> projects, ParseException e) {
                if (e != null) { // exception thrown
                    // Snack bar, toast (error handling)
                    Log.e(TAG, "Issue with getting projects", e);
                    return;
                } else {
                    Log.i(TAG, "Successfully retrieved projects");
                }
            }
        });
     ```
   - Set Up Profile Screen
      - (Create/POST) Create a new developer object
   - Search Developer Screen
      - (Read/GET) Query all developers
   - Post Project Screen
      - (Create/POST) Create a new project object
