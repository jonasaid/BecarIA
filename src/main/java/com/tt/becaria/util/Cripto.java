package com.tt.becaria.util;
import org.jasypt.util.text.BasicTextEncryptor;

public class Cripto{
        private static String PASSWORD = "B3C4R14";

        /**
         * Cifra la información utilizando AES/GCM.
         *
         * @param plaintext La información a cifrar.
         * @return La información cifrada.
         */
        public static String cifrar(String plaintext) {
            BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
            textEncryptor.setPassword(PASSWORD);
            return textEncryptor.encrypt(plaintext);
        }

        /**
         * Descifra la información utilizando AES/GCM.
         *
         * @param encryptedText La información cifrada.
         * @return La información descifrada.
         */
        public static String descifrar(String encryptedText) {
            BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
            textEncryptor.setPassword(PASSWORD);
            return textEncryptor.decrypt(encryptedText);
        }
}
