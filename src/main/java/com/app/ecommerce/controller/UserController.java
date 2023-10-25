package com.app.ecommerce.controller;

import com.app.ecommerce.dao.TransactionDAO;
import com.app.ecommerce.dao.TransactionDetailDAO;
import com.app.ecommerce.dao.UserDAO;
import com.app.ecommerce.model.TransactionDetails;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.app.ecommerce.model.User;

import jakarta.servlet.http.HttpSession;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@RestController
@CrossOrigin
public class UserController {
    Logger logger = LogManager.getLogManager().getLogger(Logger.GLOBAL_LOGGER_NAME);

    private final UserDAO userDAO;

    private final TransactionDAO transactionDAO;

    private final TransactionDetailDAO transactionDetailDAO;

    public UserController(UserDAO userDAO, TransactionDAO transactionDAO, TransactionDetailDAO transactionDetailDAO) {
        this.userDAO = userDAO;
        this.transactionDAO = transactionDAO;
        this.transactionDetailDAO = transactionDetailDAO;
    }

    @GetMapping("/")
    public String login(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        logger.log(Level.INFO, "user", user);
        return "index";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest session, HttpServletRequest request, HttpServletResponse response) {
        String idString;
        String transactionValue = Arrays.stream(request.getCookies()).filter(c -> "transaction".equals(c.getName()))
                .map(Cookie::getValue).reduce((first, second) -> second).orElse(null);
        idString = Objects.requireNonNullElse(transactionValue, "no cookie");
        if (!idString.equals("no cookie")) {
            Long id = Long.parseLong(idString);
            List<TransactionDetails> transactions = transactionDetailDAO
                    .findByTransaction(transactionDAO.findById(id).get());
            transactionDetailDAO.deleteAll(transactions);
            transactionDAO.deleteById(id);

            Cookie transactionCookie = new Cookie("transaction", "");
            transactionCookie.setPath("/");
            transactionCookie.setMaxAge(0);
            response.addCookie(transactionCookie);
        }

        session.getSession().invalidate();

        return "forward:/";
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
    public String save(@Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "newemp";
        }

        userDAO.save(user);
        return "forward:/user/list/1";
    }

    @RequestMapping(value = "/user/list/{pageNum}", method = { RequestMethod.GET, RequestMethod.POST })
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

        User existingUser = userDAO.findByUserName(user.getUserName());
        existingUser.setPassword(user.getPassword());

        userDAO.save(existingUser);
        return "redirect:/welcome";
    }

    @GetMapping("/user/delete/{userID}")
    public String delete(@PathVariable("userID") long id) {
        userDAO.deleteById(id);
        return "forward:/user/list/1";
    }
}
