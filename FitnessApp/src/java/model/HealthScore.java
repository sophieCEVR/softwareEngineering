/**
 * HealthScoreCalculator calculates the user's score from a number of values like their BMI, resting Heart rate. It is stored in the Fitness Tracker
 * @author 100021268 100122248 100137721 100090034
 */

package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HealthScore {
    int healthScore;
    private Date dateTime;
   
    /*
     * Constructs a HealthScore object which calculates the health score using
     * the different values etc. The healthscore is specific to our app and 
     * allows people to give a numerical value to thier health so they can better
     * track it
     */
    public HealthScore(User u){
        healthScore += addBMI(u.getBMI());
        healthScore += addRestingHeartRate(u.getRestingHeartRate());
        healthScore += addBodyFatPercentage(u.getBodyFatPercentage(), u.getSex());
        this.dateTime = new Date();
    }
    
    public HealthScore(String healthScore, String dateTimes) throws ParseException{
        this.healthScore = Integer.parseInt(healthScore);
         
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date dateTime = formatter.parse(dateTimes);
        this.dateTime = dateTime;
    }
    
    /*
    *Translates BMI into a healthscore value, adds this onto the healthScore
    *optional
    */
    private int addBMI(double BMI){
        if(BMI < 18.5)
            return -1;
        else if(BMI < 24.9)
            return +1;
        else if(BMI < 29.9)
            return -1;
        else
            return -2;
    }
    
    /*
    *Translates RestingHeartRate into a healthscore value, adds this onto the healthScore
    *optional
    */
    private int addRestingHeartRate(int restingHeartRate){
        if(restingHeartRate==0){
            return 0;
        }
        else if(restingHeartRate>80){
            return -2;
        }
        else if(restingHeartRate>75){
            return -1;
        }
        else if(restingHeartRate>70){
            return 0;
        }
        else if(restingHeartRate>65){
            return 1;
        }
        else if(restingHeartRate>60){
            return 2;
        }
        else if(restingHeartRate>55){
            return 3;
        }
        return 4;
    }
        
    /*
    *Translates body fat percentage into a healthscore value, adds this onto the healthScore
    *optional
    */
    private int addBodyFatPercentage(double bodyFatPercentage, User.Sex sex){
        if(bodyFatPercentage==0){
            return 0;
        }
        if(sex == User.Sex.Male){
            if(bodyFatPercentage<=5){
                return -1;
            }
            else if(bodyFatPercentage<=13){
                return 2;
            }
            else if(bodyFatPercentage<=17){
                return 1;
            }
            else if(bodyFatPercentage<=24){
                return 0;
            }
            return -1;
        }
        //else if female
        if(bodyFatPercentage<=13){
            return -1;
        }
        else if(bodyFatPercentage<=20){
            return 2;
        }
        else if(bodyFatPercentage<=24){
            return 1;
        }
        else if(bodyFatPercentage<=31){
            return 0;
        }
        return -1;
    }
      
    public int getHealthScore(){
        return healthScore;
    }
    
    /**
     * @return the dateTime
     */
    public Date getDateTime() {
        return dateTime;
    }

    /**
     * @param dateTime the dateTime to set
     */
    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }
    
    /*
    *Translates lung capacity percentage into a healthscore value, adds this onto the healthScore
    *optional
//    */
//    private static void addLungCapacity(){
//        //translates the  into a healthscore value, adds this onto the healthScore
//        //is optional
//        //return type void
//    }
//    private static void addOneMileTime(){
//        //translates the one mile time into a healthscore value, adds this onto the healthScore
//        //is optional
//        //return type void
//    }
    

    
}
