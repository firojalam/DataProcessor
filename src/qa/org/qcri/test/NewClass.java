/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.org.qcri.test;

import com.google.gson.Gson;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import twitter4j.TwitterObjectFactory;
import org.json.JSONObject;
/**
 *
 * @author firojalam
 */
public class NewClass {
    /**
     * Converts Java object to byte array 
     * @param image
     * @param format
     * @return
     * @throws IOException 
     */
    public static byte[] imageToByte(BufferedImage image, String format) throws IOException {
        byte[] imageInByte = null;
    try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();            
            ImageIO.write(image, format, baos);
            baos.flush();
        imageInByte = baos.toByteArray();
            //this.image = image;
        } catch (IOException ex) {
            Logger.getLogger(ImageInfo.class.getName()).log(Level.SEVERE, null, ex);
        }        
//        ByteArrayOutputStream b = new ByteArrayOutputStream();
//        ObjectOutputStream o = new ObjectOutputStream(b);
//        o.writeObject(obj);
        return imageInByte;
    }

    /**
     * Converts byte array to Java object
     *
     * @param imageInByte
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static BufferedImage byteToBufferedImage(byte[] imageInByte) throws IOException, ClassNotFoundException {
    BufferedImage image=null;
        try {
            InputStream in = new ByteArrayInputStream(imageInByte);
            image = ImageIO.read(in);
        } catch (IOException ex) {
            Logger.getLogger(ImageInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return image;
    }
    public static void main(String[] args) {
//        String token = "abbbbbc";
//        token = token.replaceAll("(\\w)\\1\\1+", "\\1\\1");
//        System.out.println(token);
//        Pattern p = Pattern.compile("(\\w)\\1\\1+");
//        Matcher m = p.matcher(token);//replace with string to compare
//        //token = m.replaceAll(m.group(1);
//        System.out.println(token);
//        if (m.find()) {
//            System.out.println("Duplicate character " + m.group(0));
//        }

int number =  Integer.parseInt("0");
System.out.println(number);
    //int number =  Integer.parseInt("832113783195721729");
    BigInteger num = new BigInteger("832113783195721729");
        System.out.println(num);
    String tmp[] = "lskjdalfkjasd|alksdhflas".split("\\|");
        System.out.println(tmp[0]);
        System.out.println(tmp[1]);
    System.exit(0);

        ImageInfo imgInfo = new ImageInfo();
        imgInfo.setCollectionCode("collelctioncode");
        imgInfo.setImageType("imageType");
                    if (imgInfo != null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                String imgInfoString = mapper.writeValueAsString(imgInfo);
                System.out.println(imgInfoString);
                //String tweetID = msgJson.getJSONObject("id").toString();
                //String json = TwitterObjectFactory.getRawJSON(imgInfoString);
                //image info to radis queue
                //subscriberJedis.rpush(this.collectionCode, json);
                JSONObject msgJson = new JSONObject(imgInfoString);
                String imgURL = msgJson.get("imageType").toString();
                System.out.println(imgURL);
                msgJson.put("imageType", "my image name");
                imgURL = msgJson.get("imageType").toString();
                System.out.println(imgURL);
            //ObjectMapper mapper = new ObjectMapper();
            //ImageInfo userIdentifier = gson.fromJson(msgJson, ImageInfo.class);
                //Gson gson=new Gson();
             //ImageInfo imageInfo = gson.fromJson(msgJson.toString(),ImageInfo.class);
              ImageInfo imageInfo = mapper.readValue(msgJson.toString(), ImageInfo.class);   
              String format = "png";
              String imageUrl="http://pbs.twimg.com/media/C27SuO8W8AE7QRH.png";
              URL url = new URL(imageUrl);
              BufferedImage image = ImageIO.read(url);
              byte[] imageByte=imageToByte(image,format);
              imageInfo.setImage(imageByte);
              
              
//                System.out.println(imageInfo.toString());
//                System.out.println("Push the image related info to the redis queue ...");
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                ImageIO.write(image, "jpg", baos);
//                baos.flush();
//                byte[] imageInByte = baos.toByteArray();
//                baos.close();

//                byte[] imgObject = imageToByte(imageInfo);
//                ImageInfo imageInfo2 = (ImageInfo) byteToBufferedImage(imgObject);
                String jsonInString = mapper.writeValueAsString(imageInfo);
                ImageInfo imageInfo2 = mapper.readValue(jsonInString, ImageInfo.class); 
                BufferedImage image2 = byteToBufferedImage(imageInfo2.getImage());
                File outputfile = new File("/Users/firojalam/QCRI/image.jpg");
                ImageIO.write(image2, "jpg", outputfile);
                
                System.out.println("IMAGE: "+ jsonInString);
            } catch (IOException ex) {
                Logger.getLogger(NewClass.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JSONException ex) {
                Logger.getLogger(NewClass.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(NewClass.class.getName()).log(Level.SEVERE, null, ex);
            } 
//            catch (ClassNotFoundException ex) {
//                Logger.getLogger(NewClass.class.getName()).log(Level.SEVERE, null, ex);
//            }
                    }
    }
    
    
    


}
