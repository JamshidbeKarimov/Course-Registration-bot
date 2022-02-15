package repositories;

import model.User;

import java.util.List;

public abstract class StudentRepository implements BaseRepository<User, String, List<User>> {

    protected abstract User getByChatId(String chatId);
    protected abstract String editByChatId(String chatId, User editedUser);
}
