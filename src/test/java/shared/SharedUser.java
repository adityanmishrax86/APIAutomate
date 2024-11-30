package shared;

import framework.models.users.UserModel;

import java.util.ArrayList;
import java.util.List;

public class SharedUser {
    public static UserModel sharedUser = null;
    public static UserModel existingUser = null;
    public static List<UserModel> sharedUsers = new ArrayList<>();
    public static List<UserModel> sharedCreatedUsers = new ArrayList<>();
}
