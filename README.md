<h1 align="center">Ktor REST API with Exposed and MariaDB</h1>

<p align="center">
  <a href="https://opensource.org/licenses/MIT"><img alt="License" src="https://img.shields.io/badge/License-MIT-blue.svg"/></a>
</p>

This codebase serves as a backend api for Digital Zoo. It is built with **Kotlin + Ktor + Exposed + MariaDB** including 
CRUD operations, authentication and routing. 

# 🛠️ Technologies used
- [Kotlin](https://kotlinlang.org/) - programming language
- [Ktor](https://ktor.io/) - asynchronous WEB Framework
- [Exposed](https://github.com/JetBrains/Exposed) - SQL Framework
- [MariaDB](https://mariadb.org/) - Database
- [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization) - serialization/deserialization of JSON
- Repository pattern

# 🏃 Getting started
1. Clone or download the repo, then open the directory as IntelliJ IDEA (2021+) project.
2. Start MariaDB server at port `3306` and create database `zoo`. These are the values in `hikari.properties`. I'm 
   using [XAMPP](https://www.apachefriends.org/download.html) for this step, but you're allowed to use whatever you like.
3. Use the `zoo.sql` script to populate the database.
4. Run the application (Ctrl+Shift+F10)

# 🕹️ Usage
The app defines the following endpoints at `localhost:8080/`: 
- `GET animals/all` - responds with JSON array that contains all animals in the database.
- `GET animals/photo/{photoPath...}` - responds with a photo from the given photoPath.
- `POST zookeeper/new` - send firstname, lastname, username and password as urlencoded parameters.
- `POST zookeeper/login` - send username and password as urlencoded. Response is JWT.
- `POST animal/new` - Multipart data - JSON object with animal info and photo. Also requires authorization. 
- `POST animal/update` - same as above, but photo is optional.
  
There are examples in `post.http`, however they require IntelliJ Ultimate edition. Community Edition users might 
want to use [Postman](https://www.postman.com/).
When uploading photo, the default save location is `E:\Animals\` and it will not be written in the database, 
instead the corresponding GET query for the photo will be. 

Example: 

File location: 
> E:\Animals\Animals\Vertebrates\Birds\Owl.jpeg

Database entry / GET query:
> photo/Animals/Vertebrates/Birds/Owl.jpeg

# 📝 License
```
MIT License

Copyright (c) 2021 Kostadin Arabadzhiev

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.