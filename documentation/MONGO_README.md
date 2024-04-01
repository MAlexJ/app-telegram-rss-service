## Query

### 1. MongoDB: how to check if a field contains a string

link: https://www.codecamp.ru/blog/mongodb-contains/ </br>

You can use the following syntax in MongoDB to check if a certain field contains a certain string:

#### Example 1: Check if a field contains a string

```
query: { team : { $regex : /avs/ }}

entity: { 
            _id: ObjectId("618050098ffcfe76d07b1da5"),
            team: 'mavs',
            position: 'Guard',
            points: 31 
        }
```

#### Example 2: Check if a field contains a string (case insensitive)

```
query: db.teams.findOne({ team : { $regex : /AVS/i }})

entity: { 
            _id: ObjectId("618050098ffcfe76d07b1da5"),
            team: 'mavs',
            position: 'Guard',
            points: 31 
        }
```

#### Example 3: Checking for a string in a field (no result)

```
query: { team : { $regex : /marsl/ }}

result: null
```

### 2. Full text search in MongoDB

link: https://habr.com/ru/articles/174457/ </br>

## Configuration

##### How to set up database:

1. Login to Mongo cluster
    * https://account.mongodb.com/account/login


2. Get or set new admin credentials:<br>
    * login
    * password
    * and set permission - Atlas admin


3. Create a Shared Cluster
    * FREE >> Sandbox (Shared RAM, 512 MB Storage)


4. Go to Quick Start Security

    * Set up a username and a new password


5. Set up network access for database:

    * Network Access tab >> Edit IP Access List Entry >>  Allow Access from Anywhere


6. Database Deployments Options <br>
   Go to: Database >> Database Deployments >> Connect button <br>
    * Select your driver and version: Java and latest version of driver <br>
    * Add your connection string into your application code <br>

   example: <br>
   <code>
   mongodb+srv://<username>:<password>@cluster0.8seexos.mongodb.net/?retryWrites=true&w=majority
   </code>


7. Create new DATABASES
    * Go to: DATABASE >> Cluster >> Collections >> Add My Own Data
    * Set up Database name (example: sampleDB ) and collection name (example: collectionDB)


8. Set up required ENV Variables for application:
    * MONGODB_DATABASE - Database name from DATABASE >> Cluster >> Collections  (example: sampleDB )
    * MONGODB_URI - mongodb+srv://<username>:<password>@cluster0.8seexos.mongodb.net/?retryWrites=true&w=majority
