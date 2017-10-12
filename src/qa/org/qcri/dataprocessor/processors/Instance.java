
package qa.org.qcri.dataprocessor.processors;

/**
 *
 * @author firojalam
 */
public class Instance {
    private String id = "";
    private String event = "";
    private String text = "";
    private String textOriginal = "";    
    private String label = "";
    private String dateOfCreation = "";
    private String imageURL = "";    
    private int numOfWord = 0;
    private double longitude = 0.0;
    private double latitude = 0.0;
    
    
    public int getNumOfWord() {
        return numOfWord;
    }

    public void setNumOfWord(int numOfWord) {
        this.numOfWord = numOfWord;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(String dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public String getTextOriginal() {
        return textOriginal;
    }

    public void setTextOriginal(String textOriginal) {
        this.textOriginal = textOriginal;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }



        
    
}
