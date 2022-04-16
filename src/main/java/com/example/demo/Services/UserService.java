package com.example.demo.Services;

import com.example.demo.Dto.TaskDto;
import com.example.demo.Dto.ToDoListDto;
import com.example.demo.Dto.UserDto;
import com.example.demo.Entities.RoleEntity;
import com.example.demo.Entities.TaskEntity;
import com.example.demo.Entities.ToDoListEntity;
import com.example.demo.Entities.UserEntity;
import com.example.demo.Repo.RoleRepo;
import com.example.demo.Repo.ToDoListRepo;
import com.example.demo.Repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @PersistenceContext
    private EntityManager em;
    @Autowired
    UserRepo userRepository;
    @Autowired
    ToDoListRepo toDoListRepo;
    @Autowired
    RoleRepo roleRepository;
    @Autowired
    BCryptPasswordEncoder bCPE;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    public UserEntity findUserById(Long userId) {
        Optional<UserEntity> userFromDb = userRepository.findById(userId);
        return userFromDb.orElse(new UserEntity());
    }

    public List<UserEntity> allUsers() {
        return userRepository.findAll();
    }

    public boolean saveUser(UserEntity user) {
        UserEntity userFromDB = userRepository.findByUsername(user.getUsername());

        if (userFromDB != null) {
            return false;
        }

        user.setRoles(Collections.singleton(new RoleEntity(1L, "ROLE_USER")));
        user.setPassword(bCPE.encode(user.getPassword()));

        userRepository.save(user);
        return true;
    }

    public UserEntity deleteUser(Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            return null;
        }
        return null;
    }

    public List<UserEntity> usergtList(Long idMin) {
        return em.createQuery("SELECT u FROM UserEntity u WHERE u.id > :paramId", UserEntity.class)
                .setParameter("paramId", idMin).getResultList();
    }

    public Long createUser(UserDto userDto){
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userDto.getName());
        this.userRepository.save(userEntity);
        return userEntity.getId();
    }

    public Long createListForUser(long id, ToDoListDto toDoListDto){
        UserEntity userEntity = userRepository.findById(id).get();
        ToDoListEntity listEntity = new ToDoListEntity();
        listEntity.setName(toDoListDto.getName());
        toDoListRepo.save(listEntity);
        userEntity.setToDoListEntities(listEntity);
        this.userRepository.save(userEntity);
        return null;
    }

    public UserEntity getUser(long id){
        return userRepository.findById(id).get();
    }
}
