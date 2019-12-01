import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

class BlockCipher {
    private char[] key;

    BlockCipher(String key) {
        if (key == null) this.key = null;
        else this.key = key.toCharArray();
    }

    private byte xor(byte b1, byte b2) {
        return (byte) (b1 ^ b2);
    }

    void ecbEncrypt(final String path) {
        byte[][] plain = this.loadImage(path);
        byte[][] encrypted = this.ecb(plain); // do ecb encryption
        this.saveImage(path, "ecb_crypto.bmp", encrypted);
    }

    private byte[][] ecb(byte[][] plain) {
        final int image_width = plain.length;
        final int image_height = plain[0].length;
        byte[][] encrypted = new byte[image_width][image_height];
        int k = 0;

        int chunk_width = 3;
        int chunk_height = 4;

        // let's go through chunks
        for (int i = 0; i < image_width; i += chunk_width) {
            for (int j = 0; j < image_height; j += chunk_height) {

                // let's go through pixel by pixel
                for (int x = 0; x < chunk_width; x++) {
                    for (int y = 0; y < chunk_height; y++) {

                        // block when width or height divided by chunks isn't integer
                        if ((i + x >= image_width) || (j + y >= image_height)) break;
                        if (key != null) {
                            encrypted[i + x][j + y] = (xor(plain[i + x][j + y], (byte) this.key[k]));
                            k = ++k % this.key.length;
                        } else encrypted[i + x][j + y] = plain[i + x][j + y];
                    }
                }
            }
        }

        return encrypted;
    }

    void cbcEncrypt(final String path) {
        byte[][] plain = this.loadImage(path);
        byte[][] encrypted = this.cbc(plain); // do ecb encryption
        this.saveImage(path, "cbc_crypto.bmp", encrypted);
    }

    private byte[][] cbc(byte[][] plain) {
        final int image_width = plain.length;
        final int image_height = plain[0].length;
        byte[][] encrypted = new byte[image_width][image_height];
        byte previous_xor = 0;
        int k = 0;

        int chunk_width = 3;
        int chunk_height = 4;

        // let's go through chunks
        for (int i = 0; i < image_width; i += chunk_width) {
            for (int j = 0; j < image_height; j += chunk_height) {

                // let's go through pixel by pixel
                for (int x = 0; x < chunk_width; x++) {
                    for (int y = 0; y < chunk_height; y++) {

                        // block when width or height divided by chunks isn't integer
                        if ((i + x >= image_width) || (j + y >= image_height)) break;

                        // first block of chunk: m ^ randomized vector
                        // each other block of chunk: previous value + XOR of current
                        if ((x == 0 && y == 0))
                            encrypted[i + x][j + y] = xor(plain[i + x][j + y], (byte) new Random().nextInt()); // Random().nextInt() - initialization vector
                        else
                            encrypted[i + x][j + y] = xor(previous_xor, plain[i + x][j + y]);

                        if (key != null) {
                            encrypted[i + x][j + y] = (xor(encrypted[i + x][j + y], (byte) this.key[k]));
                            k = ++k % this.key.length;
                        }

                        previous_xor = encrypted[i + x][j + y];
                    }
                }
            }
        }

        return encrypted;
    }

    private byte[][] loadImage(final String path) {
        BufferedImage bufferedImage = null;
        byte[][] bytes = null;

        // load the image
        try {
            String filepath = path + "/plain.bmp";
            bufferedImage = ImageIO.read(new File(filepath));
            System.out.printf("Image has been loaded: %s\n", filepath);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("Error: image file not found/can't load, can't do any action\n");
        }

        // do operation on image bytes
        if (bufferedImage != null) {
            int image_width = bufferedImage.getWidth();
            int image_height = bufferedImage.getHeight();
            bytes = new byte[image_width][image_height];

            // write bytes from image into bytes array
            for (int x = 0; x < image_width; x++) {
                for (int y = 0; y < image_height; y++) {
                    bytes[x][y] = (byte) bufferedImage.getRGB(x, y);
                }
            }
        }

        return bytes;
    }

    private void saveImage(final String path, final String filename, final byte[][] bytes) {
        final int image_width = bytes.length;
        final int image_height = bytes[0].length;
        BufferedImage bufferedImage = new BufferedImage(image_width, image_height, BufferedImage.TYPE_BYTE_BINARY);
        for (int x = 0; x < image_width; x++) {
            for (int y = 0; y < image_height; y++) {
                // abs to binary values '0, 1' and negative to get color values
                // black: 0; white: -1
                bufferedImage.setRGB(x, y, -Math.abs(bytes[x][y] % 2)); // mod 2 because want binary values
            }
        }

        // save the image
        try {
            String filepath = path + "/" + filename;
            ImageIO.write(bufferedImage, "bmp", new File(filepath));
            System.out.printf("Image encryption succeeded: %s\n", filepath);
        } catch (Exception e) {
            System.out.print("Save image failed\n");
        }
    }
}