package com.app.ecommerce.controller;

import com.app.ecommerce.dao.UserDAO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.app.ecommerce.model.User;

import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@RestController
@CrossOrigin
public class UserController {
    Logger logger = LogManager.getLogManager().getLogger(Logger.GLOBAL_LOGGER_NAME);

    private final UserDAO userDAO;

    public UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @GetMapping("/")
    public String login(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        logger.log(Level.INFO, "user", user);
        return "index";
    }

    @PostMapping("/authenticate")
    public String authenticate(@ModelAttribute("user") User user, Model model, HttpSession session) {
        logger.log(Level.INFO, "posted user", user);
        User u = userDAO.findByUserName(user.getUserName());
        boolean exists;
        try {
            exists = u.getPassword().equals(user.getPassword());
        } catch (NullPointerException e) {
            exists = false;
        }
        if (exists) {
            logger.log(Level.INFO, "user", u);

            session.setAttribute("usession", u);

            return "redirect:/welcome";
        } else {
            model.addAttribute("wrongCred", "Incorrect user credentials, please try again");
            return "index";
        }
    }

    @GetMapping("/welcome")
    public String welcomePage() {
        return "welcome";
    }

    @GetMapping("/user/form")
    public String showForm(Model model) {
        User u = new User();
        model.addAttribute("user", u);
        return "newemp";
    }

    @PostMapping("/user/save")
    public String save(@Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "newemp";
        }

        userDAO.save(user);
        return "forward:/user/list/1";
    }

    @RequestMapping(value = "/user/list/{pageNum}", method = {RequestMethod.GET, RequestMethod.POST})
    public String list(Model model, @PathVariable(name = "pageNum") int pageNum) {
        Page<User> userPage = userDAO.findAll(PageRequest.of(pageNum - 1, 5));
        List<User> userSlice = userPage.getContent();

        model.addAttribute("uslice", userSlice);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("totalItems", userPage.getTotalElements());
        return "employeeportal";
    }

    @GetMapping("/user/edit/{userID}")
    public String edit(Model model, @PathVariable("userID") long id) {
        model.addAttribute("user", userDAO.findById(id));
        return "newemp";
    }

    @GetMapping("/user/resetpw/{userID}")
    public String resetPassword(Model model, @PathVariable("userID") long id) {
        model.addAttribute("user", userDAO.findById(id));
        return "resetpw";
    }

    @PostMapping("/user/savepw")
    public String savepw(@Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "resetpw";
        }

        userDAO.save(user);
        return "redirect:/welcome";
    }

    @GetMapping("/user/delete/{userID}")
    public String delete(Model model, @PathVariable("userID") long id) {
        userDAO.deleteById(id);
        return "forward:/user/list/1";
    }
}
