package com.app.ecommerce.controller;

import com.app.ecommerce.dao.SupplierDAO;
import com.app.ecommerce.model.Supplier;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/Supplier")
public class SupplierController extends ExceptionHandlingController {
    private final SupplierDAO supplierDAO;

    public SupplierController(SupplierDAO supplierDAO) {
        this.supplierDAO = supplierDAO;
    }

    @GetMapping("/supplierform")
    public String supplierform(Model model) {
        Supplier supplier = new Supplier();
        model.addAttribute("supplier", supplier);
        return "supplierform";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("supplier") @Valid Supplier supplier, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "supplierform";
        }

        supplierDAO.save(supplier);
        return "forward:/Supplier/supplierlist/1";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable("id") Long id) {
        model.addAttribute("supplier", supplierDAO.findById(id));

        return "supplierform";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        supplierDAO.deleteById(id);

        return "forward:/Supplier/supplierlist/1";
    }

    @RequestMapping(value = "/supplierlist/{pageNum}", method = {RequestMethod.GET, RequestMethod.POST})
    public String viewSupplierList(Model model, @PathVariable(name = "pageNum") int pageNum) {
        Page<Supplier> supplierPage = supplierDAO.findAll(PageRequest.of(pageNum - 1, 5));
        List<Supplier> supplierSlice = supplierPage.getContent();

        model.addAttribute("sslice", supplierSlice);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", supplierPage.getTotalPages());
        model.addAttribute("totalItems", supplierPage.getTotalElements());

        return "supplierlist";
    }
}
