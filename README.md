# Music Hub - Music Discovery and Recommendation System
Project Overview
Music Hub is a desktop application that helps users discover and enjoy music. Users can search for songs, like their favorites, and get personalized recommendations based on their taste. The system uses machine learning to suggest songs that users might enjoy.
What This Project Does
The application allows users to:

Search for songs by name or artist
View song details and lyrics
Like songs to build a personal collection
Get smart recommendations based on what they like
See trending and popular music
Track their listening history

Technologies Used
Backend:

Java 17
Spring Boot (for REST APIs)
MySQL (for database)
Maven (for building the project)

Frontend:

Java Swing (for desktop interface)

Machine Learning:

Content-based filtering (matches songs by genre and mood)
Collaborative filtering (finds similar users)
Hybrid recommendations (combines multiple approaches)

External APIs:

iTunes API (to get song information)
Lyrics.ovh API (to fetch song lyrics)

How It Works
The system has three main parts:

Frontend (Swing UI): This is what users see and interact with. It shows the search box, song list, and buttons to play or like songs.
Backend (Spring Boot): This handles all the business logic. It processes search requests, manages user accounts, stores data in the database, and runs the recommendation algorithms.
Database (MySQL): This stores all the information about users, songs, likes, and search history.

When a user searches for a song, the backend first checks if we already have it in our database. If not, it fetches the information from iTunes API and saves it for future use. This makes the app faster over time.
The recommendation system works by analyzing what songs users like. If you like pop music with a happy mood, it will suggest more songs like that. It also looks at what similar users are listening to and suggests those songs.
Database Structure
The database has these main tables:

users: Stores user accounts with email and password
songs: Contains all song information like title, artist, genre, mood
likes: Records which users liked which songs
search_logs: Keeps track of all searches for trending analysis
listening_history: Records when users play songs

Machine Learning Approach
The recommendation engine uses three strategies:

Content-Based: Looks at the genre and mood of songs you like and finds similar ones. For example, if you like rock music with an energetic mood, it suggests more rock songs.
Collaborative: Finds other users who like the same songs as you and recommends what they are listening to. This helps discover new genres you might enjoy.
Trending: Adds popular songs that many people are searching for right now.

The final recommendations combine all three approaches with different weights to give the best results.
Setup Instructions
Step 1 - Database Setup:

Install MySQL on your computer
Open MySQL and create a database called music_hub
Run the SQL script provided to create all tables

Step 2 - Backend Setup:

Make sure Java 17 is installed
Open the backend folder in your IDE
Update application.properties with your MySQL username and password
Run: mvn spring-boot:run
The backend will start on port 8080

Step 3 - Frontend Setup:

Open the frontend folder
Make sure gson library is in your classpath
Run the MusicHubUI.java file
The desktop application will open

Step 4 - Start Using:

Register a new account
Login with your credentials
Start searching and liking songs
Get personalized recommendations

API Endpoints
Authentication:

POST /api/auth/register - Create new account
POST /api/auth/login - Login to system

Songs:

GET /api/songs/search - Search for songs
GET /api/songs/{id} - Get song details
GET /api/songs/{id}/lyrics - Get song with lyrics
POST /api/songs/{id}/like - Like a song
GET /api/songs/popular - Get popular songs

Recommendations:

GET /api/recommendations/user/{userId} - Get personalized recommendations
GET /api/recommendations/personalized/{userId} - Get custom mix
GET /api/recommendations/similar/{songId} - Get similar songs
GET /api/recommendations/trending - Get trending songs

Users:

GET /api/users/{id}/liked - Get all liked songs
GET /api/users/{id}/history - Get listening history

How to Use the Application
After logging in, you will see the main screen with a search box at the top. Type a song name or artist and click Search. The results will show in the table below.
Click on any song to see its details on the right side. You can then:

Click Play to hear a preview
Click Like to add it to your favorites
Click Lyrics to read the song lyrics
Click Similar to find related songs

To get recommendations, click the "For You" button. The system will analyze your liked songs and suggest new music you might enjoy.
The "Popular" button shows what songs are trending globally. The "Liked" button shows your personal collection.
