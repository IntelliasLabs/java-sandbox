package com.intellias.basicsandbox.persistence.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.security.NoSuchAlgorithmException;
import javax.crypto.NoSuchPaddingException;
import org.junit.jupiter.api.Test;

class EncryptedStringConverterTest {

    @Test
    void encryptionAndDecryption() throws NoSuchPaddingException, NoSuchAlgorithmException {
        EncryptedStringConverter convertor = new EncryptedStringConverter();
        String sensitiveData = "sensitive data";
        String encryptedData = convertor.convertToDatabaseColumn(sensitiveData);
        String decryptedData = convertor.convertToEntityAttribute(encryptedData);
        assertEquals(sensitiveData, decryptedData);
    }

}