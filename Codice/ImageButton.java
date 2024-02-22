import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.*;

public class ImageButton extends JToggleButton {
    private ImageIcon image;

    public ImageButton(){
    }

    public boolean setImage(String imagePath, String nameImage, int larg, int lung){
        if(imageExist(imagePath)){
            image = new ImageIcon(imagePath);
            Image imageIc = image.getImage().getScaledInstance(larg,lung, Image.SCALE_SMOOTH);
            image = new ImageIcon(imageIc);
            this.setIcon(image);
            this.setText(nameImage);
            this.setHorizontalTextPosition(SwingConstants.CENTER);
            this.setVerticalTextPosition(SwingConstants.BOTTOM);
            this.setName(nameImage);
            return true;
        } else {
            return false;
        }
    }

    public void setImageJar(BufferedImage inputStreamImage, String nameImage, int larg, int lung) throws IOException {
        image = new ImageIcon(inputStreamImage);
        Image imageIc = image.getImage().getScaledInstance(larg,lung, Image.SCALE_SMOOTH);
        image = new ImageIcon(imageIc);
        this.setIcon(image);
        this.setText(nameImage);
        this.setHorizontalTextPosition(SwingConstants.CENTER);
        this.setVerticalTextPosition(SwingConstants.BOTTOM);
        this.setName(nameImage);
    }

    public boolean imageExist(String imagePath){
        File imageCheck = new File(imagePath);
        return imageCheck.exists();
    }

    public void deleteImage(){
        this.setIcon(null);
        this.setName("");
        this.setText("");
    }
}
