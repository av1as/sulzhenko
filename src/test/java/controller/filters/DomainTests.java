package controller.filters;

import com.sulzhenko.controller.filters.domain.Domain;
import org.junit.jupiter.api.Test;

import static com.sulzhenko.controller.filters.domain.DomainActionsSets.*;
import static org.junit.jupiter.api.Assertions.*;


class DomainTests {
    @Test
    void checkPagesTest(){
        Domain domain = Domain.getDomain("/jsp/profile.jsp", "show_profile", "ADMIN");
        assertFalse(domain.checkAccess());
    }
    @Test
    void checkPagesNullTest(){
        Domain domain = Domain.getDomain(null, "add_category", "asdf");
        assertTrue(domain.checkAccess());
    }
    @Test
    void checkPagesWrongTest(){
        Domain domain = Domain.getDomain("add_category", "add_category", "asdf");
        assertTrue(domain.checkAccess());
    }
    @Test
    void getGuestActionsTest(){
        assertTrue(getGuestActions().contains("login"));
        assertTrue(getGuestActions().contains("register"));
        assertTrue(getGuestActions().contains("redirect"));
        assertTrue(getGuestActions().contains("controller"));
        assertTrue(getGuestActions().contains("logout"));
    }
    @Test
    void getSystemUserActionsTest(){
        assertTrue(getSystemUserActions().contains("show_profile"));
        assertTrue(getSystemUserActions().contains("update_user"));
        assertTrue(getSystemUserActions().contains("user_activities"));
        assertTrue(getSystemUserActions().contains("set_amount"));
        assertTrue(getSystemUserActions().contains("remove_activity"));
        assertTrue(getSystemUserActions().contains("add_activity"));
    }
    @Test
    void getAdminActionsTest(){
        assertTrue(getAdminActions().contains("admin_update"));
        assertTrue(getAdminActions().contains("users"));
        assertTrue(getAdminActions().contains("show_activities"));
        assertTrue(getAdminActions().contains("update_activity"));
        assertTrue(getAdminActions().contains("delete_activity"));
        assertTrue(getAdminActions().contains("new_activity"));
        assertTrue(getAdminActions().contains("remove_category"));
        assertTrue(getAdminActions().contains("update_category"));
        assertTrue(getAdminActions().contains("add_category"));
        assertTrue(getAdminActions().contains("approve_request"));
        assertTrue(getAdminActions().contains("decline_request"));
        assertTrue(getAdminActions().contains("show_full_report"));
    }
}
