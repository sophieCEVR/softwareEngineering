/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.HealthData;

import model.User;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author jxy13mmu
 */
public class PersistanceController {
    
    public static void saveUsers(List<User> users){
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileWriter("userdata.json"));
        } catch (Exception ex) {
            System.out.println("ERROR: UNABLE TO OPEN \"userdata.json\" FILE TO SAVE USERS");
        }
        JSONObject jo = new JSONObject();
        JSONArray ja = new JSONArray();
        
        for(User u : users){
            Map m = new LinkedHashMap();
            
            m.put("email", u.getEmail());
            m.put("password", u.getPassword());
            m.put("firstName", u.getFirstName());
            m.put("lastName", u.getLastName());
            
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
            String dateOfBirth = formatter.format(u.getDob());
            m.put("dob", dateOfBirth);

            m.put("sex", u.getSex().toString());
            m.put("height", u.getHeight().toString());
            m.put("weight", u.getWeight().toString());

            m.put("tracking", u.isTrackingActivity());

            ja.add(m);

            System.out.println("User \"" + u.getFullName() + "\" saved");
        }
        
        jo.put("users", ja);
        
        pw.write(jo.toJSONString());
        
        pw.flush();
        pw.close();
    }
    
    public static List<User> loadUsers(){
        Object obj = null;
        try {
            InputStream inputStream = PersistanceController.class.getResourceAsStream("userdata.json");
            Reader reader = new InputStreamReader(inputStream, "UTF-8");
            obj = new JSONParser().parse(reader);
        } catch (Exception ex) {
            System.out.println("ERROR: UNABLE TO OPEN \"userdata.json\" FILE TO LOAD USERS");
        }
        JSONObject jo = (JSONObject) obj;
        JSONArray ja = (JSONArray) jo.get("users");
        List<User> users = new ArrayList();
        
        Iterator itr2 = ja.iterator();
         
        while (itr2.hasNext()) 
        {
            Iterator<Map.Entry> itr1 = ((Map) itr2.next()).entrySet().iterator();
            
            String firstName = null;
            String lastName = null;
            String email = null;
            String password = null;
            String dob = null;
            String sex = null;
            String height = null;
            String weight = null;
            boolean tracking = false;
            
            while (itr1.hasNext()) {
                Map.Entry pair = itr1.next();
                if("firstName".equals(pair.getKey().toString()))
                    firstName = pair.getValue().toString();
                else if("lastName".equals(pair.getKey().toString()))
                    lastName = pair.getValue().toString();
                else if("email".equals(pair.getKey().toString()))
                    email = pair.getValue().toString();
                else if("password".equals(pair.getKey().toString()))
                    password = pair.getValue().toString();
                else if("dob".equals(pair.getKey().toString()))
                    dob = pair.getValue().toString();
                else if("sex".equals(pair.getKey().toString()))
                    sex = pair.getValue().toString();
                else if("height".equals(pair.getKey().toString()))
                    height = pair.getValue().toString();
                else if("weight".equals(pair.getKey().toString()))
                    weight = pair.getValue().toString();
                else if("tracking".equals(pair.getKey().toString()))
                    tracking = Boolean.parseBoolean(pair.getValue().toString());
            }
            
            User user = null;
            try {
                user = new User(email, password, firstName, lastName, dob, sex, height, weight, tracking);
            } catch (java.text.ParseException ex) {
                System.out.println("ERROR: UNABLE TO LOAD AND INSTIANIATE NEW USER");
            }
            users.add(user);
           
            System.out.println("User \"" + user.getFullName() + "\" loaded");
        }
        
        return users;
    }
    
    public static void saveUser(User u){
        List<User> users = loadUsers();
        users.add(u);
        saveUsers(users);
    }
    
    public static boolean matchUser(String email){
        for(User u : loadUsers())
            if(u.getEmail().equals(email))
                return true;
        return false;
    }
    
    public static User getUser(String email, String password){
        for(User u : loadUsers())
            if(u.getEmail().equals(email) && u.getPassword().equals(password))
                return u;
        return null;
    }
    
    /*
    public static void saveHealthData(List<User> users) throws FileNotFoundException, IOException{
        for(User u : users){
            File file = new File("users/" + u.getEmail() + ".json");
            file.createNewFile();
            PrintWriter pw = new PrintWriter(file);
            JSONObject jo = new JSONObject();
            JSONArray ja = new JSONArray();

            for(HealthData hd : u.getDataList()){
                Map m = new LinkedHashMap();

                m.put("weight", hd.getWeight());
                m.put("height", hd.getHeight());
                m.put("activityLevel", hd.getActivityLevel().toString());

                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
                String dateTime = formatter.format(hd.getDateTime());
                m.put("dateTime", dateTime);

                ja.add(m);
            }

            jo.put("data", ja);

            pw.write(jo.toJSONString());

            pw.flush();
            pw.close();
            
            System.out.println("User \"" + u.getFullName() + "\" data saved");
        }
    }

    public static void loadHealthData(List<User> users) throws FileNotFoundException, IOException, ParseException, java.text.ParseException {
        for(User u : users){
            Object obj = new JSONParser().parse(new FileReader("users/" + u.getEmail() + ".json"));
            JSONObject jo = (JSONObject) obj;
            JSONArray ja = (JSONArray) jo.get("data");

            Iterator itr2 = ja.iterator();

            String weight = null;
            String height = null;
            String activityLevel = null;
            String dateTime = null;
            
            while (itr2.hasNext()) 
            {
                Iterator<Map.Entry> itr1 = ((Map) itr2.next()).entrySet().iterator();

                while (itr1.hasNext()) {
                Map.Entry pair = itr1.next();
                if("weight".equals(pair.getKey().toString()))
                    weight = pair.getValue().toString();
                else if("height".equals(pair.getKey().toString()))
                    height = pair.getValue().toString();
                else if("activityLevel".equals(pair.getKey().toString()))
                    activityLevel = pair.getValue().toString();
                else if("dateTime".equals(pair.getKey().toString()))
                    dateTime = pair.getValue().toString();
            }

                u.updateData(Double.parseDouble(weight), Double.parseDouble(height), activityLevel, dateTime);
                
            }
            
            System.out.println("User \"" + u.getFullName() + "\" data loaded");
        }
    }
    
    public static void saveHealthDataEmail(String email, double weight, double height, String activityLevel) throws FileNotFoundException, IOException, ParseException, java.text.ParseException{
        File file = new File("users/" + email + ".json");
        file.createNewFile();
        PrintWriter pw = new PrintWriter(file);
        JSONObject jo = new JSONObject();
        JSONArray ja = new JSONArray();

        List<HealthData> healthData = loadHealthDataEmail(email);
        healthData.add(new HealthData(weight, height, activityLevel));
        
        for(HealthData hd : healthData){
            Map m = new LinkedHashMap();

            m.put("weight", hd.getWeight());
            m.put("height", hd.getHeight());
            m.put("activityLevel", hd.getActivityLevel().toString());

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
            String dateTime = formatter.format(hd.getDateTime());
            m.put("dateTime", dateTime);

            ja.add(m);

        jo.put("data", ja);

        pw.write(jo.toJSONString());

        pw.flush();
        pw.close();
        }
    }
    
    public static List<HealthData> loadHealthDataEmail(String email) throws FileNotFoundException, IOException, ParseException, java.text.ParseException {
        Object obj = new JSONParser().parse(new FileReader("users/" + email + ".json"));
        JSONObject jo = (JSONObject) obj;
        JSONArray ja = (JSONArray) jo.get("data");

        Iterator itr2 = ja.iterator();

        String weight = null;
        String height = null;
        String activityLevel = null;
        String dateTime = null;

        List<HealthData> healthData = new ArrayList();
        
        while (itr2.hasNext()) 
        {
            Iterator<Map.Entry> itr1 = ((Map) itr2.next()).entrySet().iterator();

            while (itr1.hasNext()) {
            Map.Entry pair = itr1.next();
            if("weight".equals(pair.getKey().toString()))
                weight = pair.getValue().toString();
            else if("height".equals(pair.getKey().toString()))
                height = pair.getValue().toString();
            else if("activityLevel".equals(pair.getKey().toString()))
                activityLevel = pair.getValue().toString();
            else if("dateTime".equals(pair.getKey().toString()))
                dateTime = pair.getValue().toString();
            }

            healthData.add(new HealthData(Double.parseDouble(weight), Double.parseDouble(height), activityLevel, dateTime));
        }
        
        return healthData;
    }*/
        
}
