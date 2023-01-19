import com.sulzhenko.model.hashingPasswords.Sha;
import com.sulzhenko.model.entity.User;
import org.junit.jupiter.api.*;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
class ShaTests {
    @Test
    void hashToHex() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String expected = "21B6C4849DB395945491002D81CE13B7C1AF0CF7D9490C77D17029D73C9D6BA30A986694495A98511C5D50D9578292E6685275E30F3EBBCEA5B6E3D60C9B175B";
        assertEquals(expected, new Sha().hashToHex("password1", Optional.of("user1")));
    }
    @Test
    void hashToBase64() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String expected = "IBBEHJ2ZLZRUKQATGC4TT8GVDPFZSQX30XAP1ZYDA6MKMGAUSVQYURXDUNLXGPLMAFJ14W8+U86LTUPWDJSXWW==";
        assertEquals(expected, new Sha().hashToBase64("password1", Optional.of("user1")));
    }
    @Test
    void hashToHexFromDB() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        User t = new User.Builder()
                .withLogin("user1")
                .withPassword(new Sha().hashToHex("password1", Optional.of("user1"))).build();
        String expected = "21B6C4849DB395945491002D81CE13B7C1AF0CF7D9490C77D17029D73C9D6BA30A986694495A98511C5D50D9578292E6685275E30F3EBBCEA5B6E3D60C9B175B";
        assertEquals(expected, t.getPassword());
    }
}
