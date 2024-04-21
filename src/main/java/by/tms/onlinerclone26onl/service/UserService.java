package by.tms.onlinerclone26onl.service;

import by.tms.onlinerclone26onl.dto.UserDao;
import by.tms.onlinerclone26onl.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public void save(User user) {
        if (userDao.findByName(user.getName()).isPresent()) {
            return;
        }

        userDao.save(user);

    }

    public Optional<User> findByName(String name) {
        return userDao.findByName(name);
    }

    public String findPasswordById(Long id) {
        return userDao.findPasswordById(id);
    }

    public Optional<User> findById(Long id) {
        return userDao.findById(id);
    }

    public List<User> findAll() {
        return userDao.findAll();
    }

    public void delete(Long id) {
        userDao.delete(id);
    }

    public void updateName(User user, String name) {
        userDao.updateName(user, name);
    }

    public void updateImg(byte[] file, Long id) {
        userDao.updateImg(file, id);
    }

    public void updatePassword(Long id, String password) {
        userDao.updatePassword(id, password);
    }

}