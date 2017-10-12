/*
 * This work by Firoj Alam is licensed under a Creative Commons Attribution-NonCommercial 4.0 International License.
 * Permissions beyond the scope of this license may be available by sending an email to firojalam@gmail.com.
 * http://creativecommons.org/licenses/by-nc/4.0/deed.en_US
 * 
 */


package qa.org.qcri.suffolk;


import org.slf4j.LoggerFactory;

/**
 *
 * @author Firoj Alam
 */
public class Content {
    private static org.slf4j.Logger LOG = LoggerFactory.getLogger(Content.class);
    // Author 
    private String userID = null;        
    // tweet id
    private String tweetID = null;        
    // tweet
    private String tweet = null;
    // Number of comments
    private int noOfComments = 0;
    // Number of likes
    private int noOfLikes = 0;
    // Number of Dislikes
    private int noOfDislikes = 0;
    // Number of Views
    private int noOfViews = 0;
    // Number of Favorites
    private int noOfFavoritess = 0;
    // Number of Retweets
    private int noOfRetweets = 0;
    // Number of Shares
    private int noOfShares = 0;
    // Number of followers
    private int numberOfFollowers = 0;
    // Number of following
    private int numberOfFollowings = 0;
    // Number of Friends
    private int numberOfFriends = 0;
    // Number of times @author mentioned
    private int authorMentioned = 0;
    // Mood of the article
    private String labelAuto = null;
    private String labelManual = null;
    public Content() {        
    
    }
    
    /**
     * Clone object
     * @return cloned object
     * @throws java.lang.CloneNotSupportedException
     */
    @Override
    public Content clone() throws CloneNotSupportedException{        
        try {
            return (Content)super.clone();
        } catch (CloneNotSupportedException ex) {
            LOG.error("Error in cloning object.");
        }
        return null;
    }
    /**
     * 
     * @return tweet of the article or blog
     */
    public String getTweet() {
        return tweet;
    }

    /**
     * Sets the tweet of the article or blog.
     * @param text 
     */
    public void setTweet(String text) {
        this.tweet = text;
    }
    
    /**
     * Number of comments this article or blog contains.
     * @return number of comments
     */
    public int getNoOfComments() {
        return noOfComments;
    }

    /**
     * Sets the number of comments this article or blog contains.
     * @param noOfComments 
     */
    public void setNoOfComments(int noOfComments) {
        this.noOfComments = noOfComments;
    }

    public int getNoOfLikes() {
        return noOfLikes;
    }

    public void setNoOfLikes(int noOfLikes) {
        this.noOfLikes = noOfLikes;
    }

    public int getNoOfDislikes() {
        return noOfDislikes;
    }

    public void setNoOfDislikes(int noOfDislikes) {
        this.noOfDislikes = noOfDislikes;
    }

    public int getNoOfViews() {
        return noOfViews;
    }

    public void setNoOfViews(int noOfViews) {
        this.noOfViews = noOfViews;
    }

    public int getNoOfFavoritess() {
        return noOfFavoritess;
    }

    public void setNoOfFavoritess(int noOfFavoritess) {
        this.noOfFavoritess = noOfFavoritess;
    }

    public int getNoOfRetweets() {
        return noOfRetweets;
    }

    public void setNoOfRetweets(int noOfRetweets) {
        this.noOfRetweets = noOfRetweets;
    }

    public int getNoOfShares() {
        return noOfShares;
    }

    public void setNoOfShares(int noOfShares) {
        this.noOfShares = noOfShares;
    }

    public int getNumberOfFollowers() {
        return numberOfFollowers;
    }

    public void setNumberOfFollowers(int numberOfFollowers) {
        this.numberOfFollowers = numberOfFollowers;
    }

    public int getNumberOfFollowings() {
        return numberOfFollowings;
    }

    public void setNumberOfFollowings(int numberOfFollowings) {
        this.numberOfFollowings = numberOfFollowings;
    }

    public int getNumberOfFriends() {
        return numberOfFriends;
    }

    public void setNumberOfFriends(int numberOfFriends) {
        this.numberOfFriends = numberOfFriends;
    }

    public int getAuthorMentioned() {
        return authorMentioned;
    }

    public void setAuthorMentioned(int authorMentioned) {
        this.authorMentioned = authorMentioned;
    }

    /**
     * 
     * @return the labelAuto of the article
     */
    public String getLabelAuto() {
        return labelAuto;
    }

    /**
     * Sets the labelAuto of the article, blog and news.
     * @param labelAuto 
     */
    public void setLabelAuto(String labelAuto) {
        this.labelAuto = labelAuto;
    }


    public String getTweetID() {
        return tweetID;
    }

    public void setTweetID(String tweetID) {
        this.tweetID = tweetID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getLabelManual() {
        return labelManual;
    }

    public void setLabelManual(String labelManual) {
        this.labelManual = labelManual;
    }
    
    
              
}
