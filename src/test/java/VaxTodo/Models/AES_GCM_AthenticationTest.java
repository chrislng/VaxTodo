package VaxTodo.Models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AES_GCM_AthenticationTest {


    @Test
    public void checkEncryption() throws Exception {
        // check if AES 256 Encryption works properly
        String strPassword = "Password1!";

        // Encrypted Password and Plain Text Password should not be the same string
        Assertions.assertNotEquals(strPassword, AES_GCM_Athentication.encrypt(strPassword));
    }

    @Test
    public void checkDecryption() throws Exception {
        // check if AES 256 Decryption works properly
        String strPassword = "Password1!",
                strPasswordDecrypted = "x4KlW5I0vFgMFp6RhBLH/KtCPGx7lD7AGiHpVkMq73JR6kPvFjdmCIF5iuUPuBLcNceyDAcK";

        // Decrypted Password and Plain Text Password should be the same string
        Assertions.assertEquals(strPassword, AES_GCM_Athentication.decrypt(strPasswordDecrypted));
    }
}