package com.app.ecommerce.controller;

import com.app.ecommerce.dao.CustomerDAO;
import com.app.ecommerce.model.Customer;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/Customer")
public class CustomerController {
    private final CustomerDAO customerDAO;

    public CustomerController(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    @GetMapping("/customerform")
    public String customerform(Model model) {
        Customer customer = new Customer();
        model.addAttribute("customer", customer);
        return "customerform";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("customer") @Valid Customer customer, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "customerform";
        }
        customerDAO.save(customer);
        return "forward:/Customer/customerlist/1";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable("id") Long id) {
        model.addAttribute("customer", customerDAO.findById(id));

        return "customerform";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        customerDAO.deleteById(id);

        return "forward:/Customer/customerlist/1";
    }

    @RequestMapping(value = "/customerlist/{pageNum}", method = { RequestMethod.GET, RequestMethod.POST })
    public String viewCustomerList(Model model, @PathVariable(name = "pageNum") int pageNum) {
        Page<Customer> custPage = customerDAO.findAll(PageRequest.of(pageNum - 1, 5));
        List<Customer> custSlice = custPage.getContent();

        model.addAttribute("cslice", custSlice);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", custPage.getTotalPages());
        model.addAttribute("totalItems", custPage.getTotalElements());

        return "customerlist";
    }
}
