package bhashaforge.storage;
import java.util.List;

public interface Storage {

    void saveCode(String username, String code);
    // Get all saved codes for a user
    List<String> loadCodes(String username);
}