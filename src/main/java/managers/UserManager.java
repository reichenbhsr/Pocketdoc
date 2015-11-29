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

    private HistoryManager historyManager;
    private UserConnector userMapper;

    /**
     * Dieser Konstruktor soll offiziell gebraucht werden.
     */
    public UserManager() {
        userMapper = new UserConnector();
        historyManager = new HistoryManager();
    }

    @Override
    public User add() {
        User user = new User();
        History history = historyManager.add();
        user.setHistory(history);

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
            return userMapper.updateUser(user);
        }
    }

    @Override
    public User get(int id) {
        return userMapper.getUser(id);
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

    public User addPasswordRestoreToken(String address)
    {
        return userMapper.addPasswordRestoreToken(address);
    }

    public void deleteTemporaryUsers()
    {
        userMapper.deleteTemporaryUsers();
    }
}
