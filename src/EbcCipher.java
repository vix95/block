class EbcCipher {
    private char[] key;

    EbcCipher() {
    }

    EbcCipher(String key) {
        this.key = key.toCharArray();
    }

    private byte xor(byte b1, byte b2) {
        return (byte) (b1 ^ b2);
    }

    private void encrypt() {
    }

    void encryptImage(final String path) {
        try {

        } catch (Exception e) {
            System.out.print("Error: image file not found, can't do any action\n");
        }
    }
}