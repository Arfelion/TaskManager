package skillclan.taskmanager.testutils.user;

import skillclan.taskmanager.model.User;

import java.util.ArrayList;
import java.util.List;

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

    public static List<User> getUsersList(int startId, int endId){
        List<User> users = new ArrayList<>();
        while(startId + users.size() <= endId){
            User user = getUserWithoutID();
            user.setId(startId + users.size());
            users.add(user);
        }
        return users;
    }

    public static List<User> getUsersList(){
         return new ArrayList<>();
    }
}
