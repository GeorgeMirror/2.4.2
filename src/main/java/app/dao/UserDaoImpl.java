package app.dao;

import app.model.Role;
import app.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class UserDaoImpl implements UserDao{

    @PersistenceContext
    private EntityManager manager;

    @Override
    public void createUser(User user) {
        manager.merge(user);
    }

    @Override
    public List<User> getAllUsers() {
        return manager.createQuery("FROM User", User.class).getResultList();
    }

    @Override
    public User getUserById(long id) {
        return manager.find(User.class, id);
    }

    @Override
    public User getUserByUserName(String username) {
        try {
            return manager.createQuery("FROM User user WHERE user.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (Exception e) {
            System.err.println("Ошибка произошла тут");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void updateUser(User user) {
        manager.merge(user);
    }

    @Override
    public void deleteUser(long id) {
        manager.remove(getUserById(id));
    }

    @Override
    public Set<Role> setRoles(String... roles) {
        Set<Role> result = new HashSet<>();
        for (String role : roles) {
            result.add(manager.createQuery("FROM Role role WHERE role.role = :role", Role.class)
                    .setParameter("role", role)
                    .getSingleResult());
        }
        return result;
    }

}
