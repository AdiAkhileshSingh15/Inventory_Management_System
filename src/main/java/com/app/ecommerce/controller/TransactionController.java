package com.app.ecommerce.controller;

import com.app.ecommerce.dao.*;
import com.app.ecommerce.model.*;
import com.app.ecommerce.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    private final UserDAO userDAO;

    private final ProductDAO productDAO;

    private final TransactionDAO transactionDAO;

    private final TransactionDetailDAO transactionDetailDAO;

    private final CustomerDAO customerDAO;

    private final InventoryDAO inventoryDAO;

    private final MailService mailService;

    public TransactionController(UserDAO userDAO, ProductDAO productDAO, TransactionDAO transactionDAO,
                                 TransactionDetailDAO transactionDetailDAO,
                                 CustomerDAO customerDAO, InventoryDAO inventoryDAO, MailService mailService) {
        this.userDAO = userDAO;
        this.productDAO = productDAO;
        this.transactionDAO = transactionDAO;
        this.transactionDetailDAO = transactionDetailDAO;
        this.customerDAO = customerDAO;
        this.inventoryDAO = inventoryDAO;
        this.mailService = mailService;
    }

    @GetMapping("/")
    public String transactionLandingPage(Model model) {
        return "transactionlanding";
    }

    @GetMapping("/raise")
    public String raiseTransaction(Model model, HttpServletRequest request, HttpSession session) {
        String transactionValue = Arrays.stream(request.getCookies()).filter(c -> "transaction".equals(c.getName()))
                .map(Cookie::getValue).reduce((first, second) -> second).orElse(null);
        String idString = Objects.requireNonNullElse(transactionValue, "no cookie");
        if (idString.equals("no cookie")) {
            Transaction transaction = new Transaction();

            User user = (User) session.getAttribute("usession");

            transaction.setTdate(LocalDateTime.now());
            transaction.setUser(user.getUserID());
            model.addAttribute("transaction", transaction);
            return "stockEntry";
        }

        return "redirect:/transaction/transactiondetaillist";
    }

    @PostMapping("/save")
    public String saveTransaction(@Valid @ModelAttribute("transaction") Transaction transaction,
                                  BindingResult bindingResult, Model model, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            if (transaction.getType() == Transaction.TransactionType.USAGE) {
                List<Customer> customers = customerDAO.findAll();
                model.addAttribute("customers", customers);

                return "usageEntry";
            } else {
                return "stockEntry";
            }
        }
        if (transaction.getType() == Transaction.TransactionType.USAGE) {
            Long id = transaction.getCustomer();
            transaction.setCustomer(id);
        }
        transactionDAO.save(transaction);
        Long id = transaction.getTransactionId();

        String idString = id.toString();
        Cookie transactionCookie = new Cookie("transaction", idString);
        transactionCookie.setPath("/");
        response.addCookie(transactionCookie);

        return "redirect:/transaction/transactiondetaillist";
    }

    @GetMapping("/transactiondetaillist")
    public String TransactionDetailList(Model model, HttpServletRequest request) {
        String transactionValue = Arrays.stream(request.getCookies()).filter(c -> "transaction".equals(c.getName()))
                .map(Cookie::getValue).reduce((first, second) -> second).orElse(null);
        String idString = Objects.requireNonNullElse(transactionValue, "no cookie");
        Long id = Long.parseLong(idString);
        Transaction transaction = transactionDAO.findById(id).get();

        List<TransactionDetails> td = transactionDetailDAO.findByTransaction(transaction.getTransactionId());

        if (td == null) {
            td = new ArrayList<TransactionDetails>();
        }

        if (transaction.getType() == Transaction.TransactionType.USAGE) {
            model.addAttribute("customer", transaction.getCustomer());
        }

        model.addAttribute("td", td);

        return "addtransactiondetails";
    }

    @GetMapping("/transactiondetailconfirm")
    public String transactionDetailConfirm(Model model, HttpServletRequest request, HttpServletResponse response) {
        String transactionValue = Arrays.stream(request.getCookies()).filter(c -> "transaction".equals(c.getName()))
                .map(Cookie::getValue).reduce((first, second) -> second).orElse(null);
        String idString = Objects.requireNonNullElse(transactionValue, "no cookie");
        Long id = Long.parseLong(idString);
        Transaction transaction = transactionDAO.findById(id).get();
        List<TransactionDetails> td = transactionDetailDAO.findByTransaction(transaction.getTransactionId());

        if (td == null) {
            td = new ArrayList<TransactionDetails>();
        }
        for (TransactionDetails detail : td) {
            Product product = productDAO.findById(detail.getProduct()).get();
            Inventory i = inventoryDAO.findById(product.getInventory()).get();
            i.setUnits(i.getUnits() - detail.getQuantity());
            productDAO.save(product);

            Transaction t = transactionDAO.findById(detail.getTransaction()).get();
            if (t.getType() == Transaction.TransactionType.PO) {
                i.setReorderFlag(true);
            }

            if (i.getUnits() < i.getReorderLevel()
                    && (LocalDateTime.now().minusHours(24).isAfter(i.getLastEmailSent()) || i.getReorderFlag())) {
                i.setLastEmailSent(LocalDateTime.now());
                i.setReorderFlag(false);

                // Send mail to admins
                List<User> admins = userDAO.findByRole(User.Role.ADMIN);

                for (User admin : admins) {
                    try {
                        mailService.sendNotification(admin, product);
                    } catch (MessagingException e) {
                        Logger.getLogger(MailService.class.getName()).log(Level.SEVERE, null, e);
                    }
                }
            }
            inventoryDAO.save(i);
        }

        // delete cookie
        Cookie transactionCookie = new Cookie("transaction", "");
        transactionCookie.setPath("/");
        transactionCookie.setMaxAge(0);
        response.addCookie(transactionCookie);

        return "redirect:/transaction/";
    }

    @GetMapping("/addtransactiondetails")
    public String addTransactionDetails(Model model, HttpServletRequest request) {

        return "redirect:/Product/list/1";
    }

    @PostMapping("/addtransactiondetails/save")
    public String saveTransactionDetails(@ModelAttribute("detail") TransactionDetails detail, Model model) {
        Product p = productDAO.findById(detail.getProduct()).get();
        detail.setProduct(p.getProductId());
        Inventory i = inventoryDAO.findById(p.getInventory()).get();

        if (i.getUnits() - detail.getQuantity() < 0) {
            model.addAttribute("error", "Exceeded quantity in inventory, only " + i.getUnits() + " available in-stock");
            model.addAttribute("detail", detail);
            return "detailsentry";
        }

        if (transactionDAO.findById(detail.getTransaction()).isPresent()) {
            transactionDetailDAO.save(detail);
        }

        return "redirect:/transaction/transactiondetaillist";
    }

    @GetMapping("/edittransactiondetails/{id}")
    public String editTransactionDetails(Model model, @PathVariable("id") Long id) {
        TransactionDetails td = transactionDetailDAO.findById(id).get();

        model.addAttribute("detail", td);
        model.addAttribute("transactiontype", transactionDAO.findById(td.getTransaction()).get().getType());
        return "detailsentry";
    }

    @GetMapping("/deletetransactiondetails/{id}")
    public String deleteTransactionDetails(Model model, @PathVariable("id") Long id) {
        TransactionDetails transactDetail = transactionDetailDAO.findById(id).get();
        transactionDetailDAO.delete(transactDetail);

        return "redirect:/transaction/transactiondetaillist";
    }

    @GetMapping("/usage")
    public String raiseUsageTransaction(Model model, HttpServletRequest request, HttpSession session) {
        String transactionValue = Arrays.stream(request.getCookies()).filter(c -> "transaction".equals(c.getName()))
                .map(Cookie::getValue).reduce((first, second) -> second).orElse(null);
        String idString = Objects.requireNonNullElse(transactionValue, "no cookie");
        if (idString.equals("no cookie")) {
            List<Customer> customers = customerDAO.findAll();

            Transaction transaction = new Transaction();
            User user = (User) session.getAttribute("usession");
            transaction.setTdate(LocalDateTime.now());
            transaction.setUser(user.getUserID());

            model.addAttribute("transaction", transaction);
            model.addAttribute("customers", customers);

            return "usageEntry";
        }

        return "redirect:/transaction/transactiondetaillist";
    }

    @GetMapping("/transactiondetailcancel")
    public String transactionDetailCancel(Model model, HttpServletRequest request, HttpServletResponse response) {

        String transactionValue = Arrays.stream(request.getCookies()).filter(c -> "transaction".equals(c.getName()))
                .map(Cookie::getValue).reduce((first, second) -> second).orElse(null);
        String idString = Objects.requireNonNullElse(transactionValue, "no cookie");
        Long id = Long.parseLong(idString);
        Transaction transaction = transactionDAO.findById(id).get();
        System.out.println(id);

        // delete td and t
        transactionDetailDAO.deleteByTransaction(transaction.getTransactionId());
        transactionDAO.deleteById(transaction.getTransactionId());

        // remove transaction cookie
        Cookie transactionCookie = new Cookie("transaction", "");
        transactionCookie.setPath("/");
        transactionCookie.setMaxAge(0);
        response.addCookie(transactionCookie);

        return "redirect:/transaction/";
    }
}
