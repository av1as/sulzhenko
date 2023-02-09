package DTO;

import com.sulzhenko.DTO.ActivityDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ActivityDtoTests {
    @Test
    void testIsNotificationChecked(){
        ActivityDTO activityDTO = new ActivityDTO("activity", "category", 5);
        assertDoesNotThrow(() -> activityDTO.setNumberOfUsers(7));
        assertEquals(7, activityDTO.getNumberOfUsers());
    }
}
