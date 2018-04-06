import java.util.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class sessionization{
    
    //A given value representing the inactive time(in seconds) when a session over
    private static int period = -1;
    
    //The current timestamps we have
    private static ArrayList<String> times = new ArrayList<>();
    //The current active users we have
    private static ArrayList<String> users = new ArrayList<>();
    //The start and end access time, the number of documents of a user.
    private static HashMap<String, String> user_start = new HashMap<>();
    private static HashMap<String, String> user_end = new HashMap<>();
    private static HashMap<String, Integer> user_docs = new HashMap<>();
    
    //Write the result
    private static BufferedWriter writer = null;
    
    
    public static void main(String[] args) {
        try {
            //String log_file = "./src/log.csv", period_file ="./src/inactivity_period.txt", out_file="./src/sessionization.txt";
            //Get file name
            String log_file = args[0], period_file =args[1], out_file=args[2];
            
            //Get the period value
            get_period(period_file);
            
            //read and process log file, while writing the user session record
            writer = new BufferedWriter(new FileWriter(out_file));
            process_log(log_file);
            writer.flush();
            writer.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    //Read the period value from period file
    public static void get_period(String period_file) throws Exception{
        try {
            BufferedReader br = new BufferedReader(new FileReader(period_file));
            
            String str = null;
            while ((str= br.readLine()) != null) {
                period = Integer.parseInt(str);
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /*
     Calculate the time difference(in seconds). For exp:
     "2017-06-30 00:00:00" and "2017-06-30 00:00:00"  output 0
     "2017-06-30 00:00:00" and "2017-06-30 00:00:01"  output 1
     */
    public static int time_diff(String start, String end) throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        int second = -1;
        try {
            Date s = df.parse(start);
            Date e = df.parse(end);
            second = (int) ((e.getTime() - s.getTime()) / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return second;
    }
    
    //Record the user session information, then delete this user from current user list and all related maps
    public static void release(String user) throws Exception {
        String s = user_start.get(user);
        String e = user_end.get(user);
        int doc = user_docs.get(user);
        int duration = time_diff(s,e) + 1;
        
        String record = user+","+s+","+e+","+(duration+"")+","+(doc+"");
        //System.out.println(record);
        writer.write(record);
        writer.write("\n");
        
        users.remove(user);
        user_start.remove(user,s);
        user_end.remove(user,e);
        user_docs.remove(user,doc);
    }
    
    //Read the log file and process data
    public static void process_log(String log_file) throws Exception{
        try {
            BufferedReader br = new BufferedReader(new FileReader(log_file));
            
            br.readLine();//Don't process the head
            
            String str = null;
            while ((str= br.readLine()) != null) {
                String []arr = str.split(",");
                String user_ip = arr[0];
                String time = arr[1]+" "+arr[2];
                
                //if it's a new time, we need to decide whether the current users should be released
                if (!times.contains(time)){
                    times.add(time);
                    
                    //Read all current users
                    for(int i=0;i<users.size();i++){
                        String user = users.get(i);
                        String end_of_user = user_end.get(user);
                        
                        int diff = time_diff(end_of_user, time);
                        
                        /*
                         If the inactive time of this user greater than the given period,
                         and this user is not the user read now. This means the session of this user
                         is over. We should release this user.
                         */
                        if((diff>period) && (!user.equals(user_ip))){
                            release(user);
                            i--;//This user has been deleted. To read the next one, the index should -1
                        }
                        
                    }
                }
                
                //Update user information
                if(users.contains(user_ip)){
                    //For the current user, just update end access time and the num of docs
                    user_end.put(user_ip,time);
                    
                    int tmp = user_docs.get(user_ip);
                    user_docs.put(user_ip,tmp+1);
                }else{
                    //For new user, update all related information
                    users.add(user_ip);
                    user_start.put(user_ip,time);
                    user_end.put(user_ip,time);
                    user_docs.put(user_ip,1);
                }
                
            }
            
            
            //After reading the log file, release all current users
            for(int i=0;i<users.size();i++){
                release(users.get(i));
                i--;
            }
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


