# ZQ_Insight
This is EDGAR Analytics Coding Challenge for Insight Data Engineering.

The way I use is pretty simple. It can divide into the following two main parts.
1、Data structure to store data:
     One integer (period):
         record the value denoting the period of inactivity (in seconds) which can be used to identify a user session.
     Two lists (users and times):
         users for recording the current active(in-session) users; times for all the timestamps we have till now.
     Three maps (user_start, user_end and user_docs):
         represent the start acess time, the end acess time(till now), and the total number of documents during this period a user have visited. (I know it is better to develope these three parts into one module/class, which would make code more elegant. But I am not sure if I am allowed to write two java files in this challenges ... )
2、Then read the log line by line, process the data and write the information if a session is over.
     When read a line, what we need to do is:
    1) If it is a new timestamp?
        When a new timestamp coming, we need to traverse all the current users in the users list to find whether the session of this user is over. (If the time, from the last access time of this user to now, is greater than period. And, this user is not the user read now. Then we can say the session of this user is over)
        If the session of a user is over, then we can write the information of this session to output file, and remove all information related to this user in the data structure(users, user_start, user_end and user_docs).
    2) Update the information of the read user.
        If this user is in a session (can be found in users list), we just need to update its' last access time(user_end) and increase the number of visited documents by 1.(user_docs +1)
        If not, this means it is a new user and we need to initialize a new session for this user.  So we do:
            add this user to current users list. (users)
            initialize both its' start and end access time to now.(user_start, user_end)
            initialize its' visited document to be 1.(user_docs)
    
         

