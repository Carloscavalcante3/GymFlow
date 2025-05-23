package src.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private static final String FILE = "data/users.json";
    private static List<User> users = new ArrayList<>();

    static {
        loadUsersFromFile();
    }

    public static void loadUsersFromFile() {
        users.clear();
        try (FileReader reader = new FileReader(FILE)) {
            List<User> loaded = new Gson().fromJson(reader, new TypeToken<List<User>>(){}.getType());
            if (loaded != null) {
                users.addAll(loaded);
            }
        } catch (IOException e) {
        }
    }

    public static void save(User user) {
        boolean existingUser = false;
        for(int i=0; i < users.size(); i++){
            if(users.get(i).getUsername().equals(user.getUsername())){
                users.set(i, user);
                existingUser = true;
                break;
            }
        }
        if(!existingUser){
            users.add(user);
        }

        try (FileWriter writer = new FileWriter(FILE)) {
            new Gson().toJson(users, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<User> getUsers() {
        return new ArrayList<>(users);
    }

    public static User find(String username, String password) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    public static User findByUsername(String username) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public static void resetRepositoryForTesting() {
        users.clear();
        try (FileWriter writer = new FileWriter(FILE)) {
            writer.write("[]");
        } catch (IOException e) {
            e.printStackTrace();
        }
        loadUsersFromFile();
    }
}