
package qa.org.qcri.dataprocessor;

import java.io.Serializable;

import org.apache.log4j.Logger;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@SuppressWarnings("serial")
public class Tweet implements Serializable {

    private static Logger logger = Logger.getLogger(Tweet.class);

    private String tweetID;
    private String message;
    private String crisisName;
    private String reTweeted; 
    //private String reTweetCount; 
    private String createdAt;

    private String userID;
    private String userName;
    private String userURL;
    private String tweetURL;
    private String labelAuto = null;
    private String labelManual = null;
    private String imagePath = null;
    private String dupImagePath = null;
    private boolean duplicate = false;
    private boolean relevant = false;
    private boolean sensitive = false;
    private String damageLabel = null;
    private double damageLabelConf = 0.0;
    private double classLabelConf = 0.0;
    /**
     * @return the userURL
     */
    public String getUserURL() {
        return userURL;
    }

    /**
     * @param userURL the userURL to set
     */
    public void setUserURL(String userURL) {
        this.userURL = userURL;
    }

    /**
     * @return the tweetID
     */
    public String getTweetID() {
        return tweetID;
    }

    /**
     * @param tweetID the tweetID to set
     */
    public void setTweetID(String tweetID) {
        this.tweetID = tweetID;
    }

    public String getCrisisName() {
        return crisisName;
    }

    public void setCrisisName(String crisisName) {
        this.crisisName = crisisName;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the userID
     */
    public String getUserID() {
        return userID;
    }

    /**
     * @param userID the userID to set
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt the createdAt to set
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @return the tweetURL
     */
    public String getTweetURL() {
        return tweetURL;
    }

    /**
     * @param tweetURL the tweetURL to set
     */
    public void setTweetURL(String tweetURL) {
        this.tweetURL = tweetURL;
    }

    public String getLabelAuto() {
        return labelAuto;
    }

    public void setLabelAuto(String labelAuto) {
        this.labelAuto = labelAuto;
    }

    public String getLabelManual() {
        return labelManual;
    }

    public void setLabelManual(String labelManual) {
        this.labelManual = labelManual;
    }


    public String toString() {
        return tweetID + "," + message + "," + createdAt + "," + userID + "," + userName + "," + userURL + "," + tweetURL;
    }

    public Tweet toTweet(String str) {
        if (str != null) {
            Tweet tweet = new Tweet();
            try {
                JsonParser parser = new JsonParser();
                JsonObject jsonObj = (JsonObject) parser.parse(str);

                if (jsonObj.get("id") != null) {
                    tweet.setTweetID(jsonObj.get("id").getAsString());
                }

                if (jsonObj.get("text") != null) {
                    tweet.setMessage(jsonObj.get("text").getAsString());
                }

                if (jsonObj.get("created_at") != null) {
                    tweet.setCreatedAt(jsonObj.get("created_at").getAsString());
                }

                JsonObject jsonUserObj = null;
                if (jsonObj.get("user") != null) {
                    jsonUserObj = jsonObj.get("user").getAsJsonObject();
                    if (jsonUserObj.get("id") != null) {
                        tweet.setUserID(jsonUserObj.get("id").getAsString());
                    }

                    if (jsonUserObj.get("screen_name") != null) {
                        tweet.setUserName(jsonUserObj.get("screen_name").getAsString());
                        tweet.setTweetURL("https://twitter.com/" + tweet.getUserName() + "/status/" + tweet.getTweetID());
                    }
                    if (jsonUserObj.get("url") != null) {
                        tweet.setUserURL(jsonUserObj.get("url").toString());
                    }
                }
                return tweet;
            } catch (Exception ex) {
                logger.error("Unable to deserialize the json string to tweet" + ex);
                return null;
            }
        }
        return null;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getDupImagePath() {
        return dupImagePath;
    }

    public void setDupImagePath(String dupImagePath) {
        this.dupImagePath = dupImagePath;
    }

    public boolean isDuplicate() {
        return duplicate;
    }

    public void setDuplicate(boolean duplicate) {
        this.duplicate = duplicate;
    }

    public boolean isRelevant() {
        return relevant;
    }

    public void setRelevant(boolean relevant) {
        this.relevant = relevant;
    }
    

    public boolean isSensitive() {
        return sensitive;
    }

    public void setSensitive(boolean sensitive) {
        this.sensitive = sensitive;
    }

    public String getDamageLabel() {
        return damageLabel;
    }

    public void setDamageLabel(String damageLabel) {
        this.damageLabel = damageLabel;
    }



    public double getDamageLabelConf() {
        return damageLabelConf;
    }

    public void setDamageLabelConf(double damageLabelConf) {
        this.damageLabelConf = damageLabelConf;
    }

    public double getClassLabelConf() {
        return classLabelConf;
    }

    public void setClassLabelConf(double classLabelConf) {
        this.classLabelConf = classLabelConf;
    }

    
}
