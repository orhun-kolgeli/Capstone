# DevMatch


## Overview
### Description
- **Category:** Matching app
- **Mobile:** Native Android app
- **Story:** Matches nonprofit organizations and businesses with students and professionals willing to build a website or app for a nonprofit for their portfolio.
- **Market:** Any nonprofit and any developer may benefit from the app. The organizations can find developers to build their app and developers can get real-world experience for their portfolio. 
- **Habit:** Developers can view the app requirements of the organizations, and if interested, can share their GitHub, resume, and profile with the organization. The more frequently they share their profile, the higher their chances of being matched to a project. 
- **Scope:** Can be used by any web or app developer willing to improve their portfolio.

## Product Spec

### 1. Features

* Both nonprofits and developers can sign up and log in


https://user-images.githubusercontent.com/74283397/182308665-2132193a-6b50-4996-bf33-311f07cf3503.mp4


* Developers can scroll through the list of projects posted by nonprofits
* Developers can apply to build a posted project by opening up a detail view of the project


https://user-images.githubusercontent.com/74283397/182309072-571e8e77-aeb1-4f70-a5df-4f70e67d2876.mp4


* Developers can edit their profile 


https://user-images.githubusercontent.com/74283397/182308751-ba81577a-6b21-48d2-8ec4-ea4a58719c74.mp4


* Nonprofits can scroll through the list of all developers
* Nonprofits can invite a developer to build their project by opening up a detail view of a developer's profile


https://user-images.githubusercontent.com/74283397/182308824-6b15c36d-a958-4b03-b888-cfef014df223.mp4


* Nonprofits can post their web, iOS, Android, or other projects


https://user-images.githubusercontent.com/74283397/182309446-68331b81-bddb-4ba8-b8fc-96fa1779a3b8.mp4


https://user-images.githubusercontent.com/74283397/182309468-0a7512f1-78a8-48ed-84c1-61158f3395d8.mp4


* Developers can filter projects


https://user-images.githubusercontent.com/74283397/182309188-829e9c44-ae88-4795-9a46-b7a309e7814b.mp4

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


## Schema 
### Models
#### User

   | Property      | Type           | Description |
   | ------------- | --------       | ------------|
   | objectId      | String         | unique id for the user (default field) |
   | createdAt     | DateTime       | date when user is created (default field) |
   | updatedAt     | DateTime       | date when user is last updated (default field) |
   | email         | String         | email address of the user |
   | username      | String         | username of the user |
   | name          | String         | full name of the user |
   | password      | String         | user password |
   | organization  | bool           | false if developer; true if organization |
   
   
#### Project

   | Property      | Type           | Description |
   | ------------- | --------       | ------------|
   | objectId      | String         | unique id for the project (default field) |
   | createdAt     | DateTime       | date when project is created (default field) |
   | updatedAt     | DateTime       | date when project is last updated (default field) |
   | type          | String         | project type, i.e., iOS, Android, Web, or Other |
   | description   | String         | description of the project |
   | image         | File           | image describing the project, e.g., mockup |
   | user         | Pointer to User           | organization that owns the project |

#### Developer

   | Property      | Type           | Description |
   | ------------- | --------       | ------------|
   | objectId      | String         | unique id for the developer (default field) |
   | createdAt     | DateTime       | date when developer profile is created (default field) |
   | updatedAt     | DateTime       | date when developer profile is last updated (default field) |
   | bio           | String         | bio of the developer |
   | github        | String         | GitHub username |
   | skills        | String         | web or mobile development skills |
   | user          | Pointer to User| associated user |
   
   
   
   
### Networking
#### Example network request
   - Search Project Screen
      - (Read/GET) Query all projects
      ```java
        ParseQuery<Project> query = ParseQuery.getQuery(Project.class);
        query.setLimit(LIMIT);
        // Order projects by creation date
        query.addDescendingOrder(CREATED_AT);
        // Start an asynchronous call for projects
        query.findInBackground(new FindCallback<Project>() {
            @Override
            public void done(List<Project> projects, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting projects", e);
                    return;
                } else {
                    Log.i(TAG, "Successfully retrieved projects");
                }
            }
        });
     ```
