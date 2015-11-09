package managers;

import database.mappers.UserConnector;
import models.History;
import models.User;

import java.util.ArrayList;

/**
 * Diese Klasse dient als Mittelstück der Applikation wenn es um Objekte der Klasse User geht.
 * <p>
 * Wenn ein solches Objekt verändert, gelesen oder etwas damit gemacht werden soll. Dann muss diese Klasse dafür aufgerufen werden.
 * <p>
 * Für mehr Informationen, siehe die Javadoc von {@link managers.BasicManager}
 *
 * @author Oliver Frischknecht
 */
public class UserManager implements BasicManager<User> {

//    private DatabaseMapper<User> userMapper; FIXME
    private HistoryManager historyManager;
    private UserConnector userMapper;   // FIXME

    /**
     * Dieser Konstruktor soll offiziell gebraucht werden.
     */
    public UserManager() {
//        userMapper = new UserMapper(); FIXME
        userMapper = new UserConnector(); // FIXME
        historyManager = new HistoryManager();
    }

    @Override
    public User add() {
        User user = new User();
        History history = historyManager.add();
        user.setHistory(history);

//        userMapper.create(user); FIXME
        userMapper.createUser(user);
        return user;
    }

    @Override
    public User update(User user) {
        User oldUser = get(user.getId());
        if (oldUser == null) {
            throw new IllegalArgumentException("User " + user.getId() + " doesn't exist");
        } else {
            if (user.getPassword() == null) {
                user.setPassword(oldUser.getPassword());
            }
            if (user.getName() == null) {
                user.setName(oldUser.getName());
            }
            if (user.getHistory() == null) {
                user.setHistory(oldUser.getHistory());
            }
//            return userMapper.update(user);
            return userMapper.updateUser(user); // FIXME
        }
    }

    @Override
    public User get(int id) {

//        return userMapper.read(id);
        return userMapper.getUser(id); // FIXME
    }

    @Override
    public ArrayList<User> getAll() {
        return userMapper.readAll();
    }

    @Override
    public void remove(int id) {

        historyManager.remove(get(id).getHistory().getId());
        userMapper.delete(id);
    }

    public boolean  hasMailAddress(String address){
        return userMapper.hasMailAdress(address);
    }

    public boolean checkPassword(String address, String password)
    {
        return userMapper.checkPassword(address, password);
    }
}
