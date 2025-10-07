package skillclan.taskmanager.testutils.user;

import skillclan.taskmanager.model.User;

public final class TestUser {

    private TestUser(){
    }

    public static User getUser(){
        User user = new User();
        user.setId(10);
        user.setName("TestUserName");
        user.setEmail("test@test.test");
        user.setPhoneNumber("380991234567");
        return user;
    }

    public static User getUserWithoutID(){
        User user = new User();
        user.setName("TestUserName");
        user.setEmail("test@test.test");
        user.setPhoneNumber("380991234567");
        return user;
    }
}
