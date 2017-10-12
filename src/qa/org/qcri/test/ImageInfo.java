/**
 *
 */
package qa.org.qcri.test;

/**
 * @author Firoj Alam
 *
 */
public class ImageInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long collectionFeedId;
    private String collectionCode;
    private String imageURL;
    private String tweetID;
    private String imageType; //photo/vedio
    private String imageFormat; 
    private String physicalLocation; 
    //private BufferedImage image;
    private byte[] imageInByte;

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Long getCollectionFeedId() {
        return collectionFeedId;
    }

    public void setCollectionFeedId(Long collectionFeedId) {
        this.collectionFeedId = collectionFeedId;
    }

    public String getCollectionCode() {
        return collectionCode;
    }

    public void setCollectionCode(String collectionCode) {
        this.collectionCode = collectionCode;
    }

    public String getTweetID() {
        return tweetID;
    }

    public void setTweetID(String tweetID) {
        this.tweetID = tweetID;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }
    
    public void setImageFormat(String imageFormat) {
        this.imageFormat = imageFormat;
    }

    public String getImageFormat() {
        return imageFormat;
    }
    
    public byte[] getImage() {
          return imageInByte;
    }

    public void setImage(byte[] image) {
        this.imageInByte = image;
    }

    public String getPhysicalLocation() {
        return physicalLocation;
    }

    public void setPhysicalLocation(String physicalLocation) {
        this.physicalLocation = physicalLocation;
    }

    @Override
    public String toString() {
        return "ImageInfo{" + "collectionFeedId=" + collectionFeedId + ", collectionCode=" + collectionCode + ", imageURL=" + imageURL + ", tweetID=" + tweetID + ", imageType=" + imageType + ", imageFormat=" + imageFormat + ", physicalLocation=" + physicalLocation + ", image=" + imageInByte + '}';
    }

}
