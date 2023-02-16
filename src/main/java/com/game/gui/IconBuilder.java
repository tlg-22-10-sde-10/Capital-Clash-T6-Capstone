package com.game.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;


class IconBuilder {
    // create ImageIcon from Image
    public ImageIcon imageIcon(String file, int width, int height, int scale) throws IOException {
        InputStream image = getClass().getResourceAsStream(file);
        BufferedImage bufferedImage = ImageIO.read(image);
        Image imgScaled = bufferedImage.getScaledInstance(width, height, scale);

        return new ImageIcon(imgScaled);
    }
}
