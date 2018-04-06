## EDGAR Analytics Coding Challenge

The way I use is pretty simple. It can be divided into two main parts.

### 1、Data structures to store data:

*  **One integer (`period`)**: 

   Record the value denoting the period of inactivity (in seconds) which can be used to identify a user session.
*  **Two lists (`users` and `times`)**: 

   Users for recording the currently active(in-session) users; times for all the timestamps we have till now.
*  **Three maps (`user_start`, `user_end`, and `user_docs`)**: 

   Represent the start access time, the end access time(till now), and the total number of documents during this period a user have visited. (I know it is better to develop these three parts into one module/class, which would make the code more elegant. But I am not sure if I am allowed to write two java files in this challenges ... )



### 2、Then read the log line by line, process the data and write the information if a session is over.

   When reading a line, what we need to do is:

*  **If it is a new timestamp?**

   When a new timestamp coming, we need to traverse all the current users in the **`users`** list to find whether the session of this user is over. (If the time, from the last access time of this user to now, is greater than period. And, this user is not the user read now. Then we can say the session of this user is over)

   If the session of a user is over, then we can:
   
   *  Write the information of this session to the **`output file`**
   *  Remove all information related to this user in the data structure(**`users`**, **`user_start`**, **`user_end`** and **`user_docs`**).
    
*  **Update the information of the read user.**

   If this user is in a session (can be found in **`users`** list), we just need to update its' last access time(**`user_end`**) and increase the number of visited documents by 1.(**`user_docs`** +1)

   If not, this means it is a new user and we need to initialize a new session for this user.  So we do:
       
   *  Add this user to the current users list. (**`users`**)
   *  Initialize both its' start and end access time to now.(**`user_start`**, **`user_end`**)
   *  Initialize its' visited document to be 1.(**`user_docs`**)
