package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;

public class TextConverter implements TextGraphicsConverter {
    private int maxWidth;
    private int maxHeight;
    private double maxRatio;
    private TextColorSchema textColorSchema;

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));
        double ratio = (img.getHeight() / img.getWidth()) > 0 ? (double) img.getHeight() / img.getWidth() : (double) img.getWidth() / img.getHeight();
        if (ratio > maxRatio) throw new  BadImageSizeException(ratio, maxRatio);
        int quantityTimeSize = (img.getHeight() > img.getWidth()) ? img.getHeight() / maxHeight : img.getWidth() / maxWidth;
        int newWidth = img.getWidth() / quantityTimeSize;
        int newHeight = img.getHeight() / quantityTimeSize;
        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);
//        ImageIO.write(bwImg, "png", new File("out.png"));
        var bwRaster = bwImg.getRaster();
        return printSchemaArray(createSchemaArray(bwRaster));
    }

    private char[][] createSchemaArray(WritableRaster bwRaster) {
        char[][] schemaArray = new char[bwRaster.getWidth()][bwRaster.getHeight()];

        for ( int h = 0; h < bwRaster.getHeight(); h++) {
            for (int w = 0; w < bwRaster.getWidth(); w++) {
                int color = bwRaster.getPixel(w, h, new int[3])[0];
                char c = textColorSchema.convert(color);
                schemaArray[w][h] = c;
            }
        }
        return schemaArray;
    }

    private String printSchemaArray (char [][] schemaArray) {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<schemaArray.length; i++) {
            for (int j=0; j< schemaArray.length;j++){
                sb.append(schemaArray[j][i])
                        .append(schemaArray[j][i]);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public void setMaxWidth(int width) {
        this.maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) {
        this.maxHeight = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.textColorSchema = schema;
    }
}

