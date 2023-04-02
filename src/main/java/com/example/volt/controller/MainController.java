package com.example.volt.controller;

import com.example.volt.model.Role;
import com.example.volt.model.User;
import com.example.volt.repository.UserRepository;
import com.example.volt.repository.UserUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@Controller
public class MainController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public MainController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
        //home
    @GetMapping
    public String home() {
        return "index";
    }

    @GetMapping("/user")
    public String userPage(Principal principal, Model model) {
        model.addAttribute("user",
                userRepository.findByUsername(principal.getName()));
        return "user";
    }


        //admin CRUD
    @GetMapping("/admin")
    public String adminPage(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin/admin";
    }

    @GetMapping("/admin/user/{username}")
    public String getUserPage(@PathVariable("username") String username, Model model) {
        model.addAttribute("user", userRepository.findByUsername(username));
        return "admin/get";
    }

    @GetMapping("/admin/new")
    public String newUserPage(@ModelAttribute("user") User user) {
        return "admin/new";
    }

    @PostMapping("/admin/new")
    public String adding(@ModelAttribute("user") User user) {
        List<Role> roleSet = Arrays.asList(new Role("ROLE_USER"));
        user.setRoles(roleSet);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/{username}/edit")
    public String updateUserPage(@PathVariable("username") String username, Model model) {
        model.addAttribute("user", userRepository.findByUsername(username));
        return "admin/update";
    }

    @PatchMapping("/admin/{username}")
    @Transactional
    public String updateUser(@PathVariable("username") String username,
                             @ModelAttribute("user") User user) {
        //TODO:fix update
        user.setUsername(user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setId(user.getId());
        user.setEmail(user.getEmail());
        List<Role> roleSet = Arrays.asList(new Role("ROLE_USER"));
        user.setRoles(roleSet);
        //use dao




        return "redirect:/admin";
    }

    @DeleteMapping("/admin/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin";
    }






}
