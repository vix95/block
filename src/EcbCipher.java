import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Console;
import java.io.File;

class EcbCipher {
    private char[] key;

    EcbCipher(String key) {
        this.key = key.toCharArray();
    }

    private byte xor(byte b1, byte b2) {
        return (byte) (b1 ^ b2);
    }

    private byte[][] encrypt(byte[][] plain, int width, int height) {
        byte[][] encrypted = new byte[width][height];
        int k = 0;

        int chunk_width = 3;
        int chunk_height = 4;

        // let's go through chunks
        for (int i = 0; i < width; i += chunk_width) {
            for (int j = 0; j < height; j += chunk_height) {

                // let's go through pixel by pixel
                for (int x = 0; x < chunk_width; x++) {
                    for (int y = 0; y < chunk_height; y++) {

                        // block when width or height divided by chunks isn't integer
                        if ((i + x == width) || (j + y == height)) break;
                        encrypted[i + x][j + y] = xor(plain[i + x][j + y], (byte) this.key[k]);
                        k = ++k % this.key.length;
                    }
                }
            }
        }

        return encrypted;
    }

    void encryptImage(final String path) {
        BufferedImage bufferedImage = null;

        // load the image
        try {
            bufferedImage = ImageIO.read(new File(path + "/plain.bmp"));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("Error: image file not found/can't load, can't do any action\n");
        }

        // do operation on image bytes
        if (bufferedImage != null) {
            int image_width = bufferedImage.getWidth();
            int image_height = bufferedImage.getHeight();
            byte[][] plain = new byte[image_width][image_height];

            // write bytes from image into bytes array
            for (int x = 0; x < image_width; x++) {
                for (int y = 0; y < image_height; y++) {
                    int a = 0;
                    plain[x][y] = (byte) bufferedImage.getRGB(x, y);
                }
            }

            byte[][] encrypted = this.encrypt(plain, image_width, image_height); // do ecb encryption

            bufferedImage = new BufferedImage(image_width, image_height, BufferedImage.TYPE_BYTE_BINARY);
            for (int x = 0; x < image_width; x++) {
                for (int y = 0; y < image_height; y++) {
                    bufferedImage.setRGB(x, y, encrypted[x][y]);
                }
            }

            // save the image
            try {
                ImageIO.write(bufferedImage, "bmp", new File(path + "/ecb_crypto.bmp"));
            } catch (Exception e) {
                System.out.print("Save image failed\n");
            }
        }
    }
}