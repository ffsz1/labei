package com.tongdaxing.xchat_framework.util.util.codec;

/**
 * Created by Zhanghuiping on 14/6/9.
 */

public class RC4Utils {

    public static String encrypt(String s, String key) throws Exception {
        if (s == null || s.length() == 0 || key == null || key.length() == 0) {
            return null;
        }
        return encrypt(s.getBytes(), key);
    }

    public static String encrypt(byte[] input, String key) throws Exception {
        if (input == null || input.length == 0 || key == null || key.length() == 0) {
            return null;
        }
        RC4 encryptHelper = new RC4(key.getBytes());
        encryptHelper.doFinal(input);
        return Base64Utils.encodeToString(input, Base64Utils.NO_WRAP);
    }

    public static String decrypt(String s, String key) throws Exception {
        if (s == null || s.length() == 0 || key == null || key.length() == 0) {
            return null;
        }
        return decrypt(s.getBytes(), key);
    }

    public static String decrypt(byte[] input, String key) throws Exception {
        if (input == null || input.length == 0 || key == null || key.length() == 0) {
            return null;
        }
        RC4 decryptHelper = new RC4(key.getBytes());
        input = Base64Utils.decode(input, Base64Utils.NO_WRAP);
        decryptHelper.doFinal(input);
        return new String(input);
    }

    public static class RC4 {
        private final static int STATE_LENGTH = 256;
        private byte[] engineState = null;
        private int x = 0;
        private int y = 0;
        private byte[] workingKey = null;

        public RC4(byte[] key) {
            this.setKey(key);
        }

        public void setKey(byte[] key) {
            if (key == null || key.length == 0) {
                return;
            }
            workingKey = new byte[key.length];
            System.arraycopy(key, 0, workingKey, 0, key.length);
            x = 0;
            y = 0;
            if (engineState == null) {
                engineState = new byte[STATE_LENGTH];
            }
            // reset the state of the engine
            for (int i = 0; i < STATE_LENGTH; i++) {
                engineState[i] = (byte) i;
            }
            int i1 = 0;
            int i2 = 0;
            for (int i = 0; i < STATE_LENGTH; i++) {
                i2 = ((key[i1] & 0xff) + engineState[i] + i2) & 0xff;
                // do the byte-swap inline
                byte tmp = engineState[i];
                engineState[i] = engineState[i2];
                engineState[i2] = tmp;
                i1 = (i1 + 1) % key.length;
            }
        }

        public void doFinal(byte[] input) {
            doFinal(input, 0, input.length);
        }

        private void doFinal(byte[] input, int start, int count) {
            if (input == null || start < 0 || count < 0) {
                return;
            }
            for (int i = start; i < start + count; i++) {
                x = (x + 1) & 0xff;
                y = (engineState[x] + y) & 0xff;
                // swap
                byte tmp = engineState[x];
                engineState[x] = engineState[y];
                engineState[y] = tmp;
                // xor
                input[i] = (byte) (input[i] ^ engineState[(engineState[x] + engineState[y]) & 0xff]);
            }
        }

        public void reset() {
            this.setKey(workingKey);
        }
    }

}
