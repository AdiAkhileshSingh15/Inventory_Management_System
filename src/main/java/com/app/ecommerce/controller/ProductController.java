package com.app.ecommerce.controller;

import com.app.ecommerce.dao.*;
import com.app.ecommerce.model.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin
@RequestMapping("/Product")
public class ProductController {
    private final ProductDAO productDAO;

    private final TransactionDAO transactionDAO;

    private final TransactionDetailDAO transactionDetailDAO;

    private final BrandDAO brandDAO;

    private final SupplierDAO supplierDAO;

    private final InventoryDAO inventoryDAO;

    public ProductController(ProductDAO productDAO, TransactionDAO transactionDAO,
                             TransactionDetailDAO transactionDetailDAO, BrandDAO brandDAO, SupplierDAO supplierDAO,
                             InventoryDAO inventoryDAO) {
        this.productDAO = productDAO;
        this.transactionDAO = transactionDAO;
        this.brandDAO = brandDAO;
        this.transactionDetailDAO = transactionDetailDAO;
        this.supplierDAO = supplierDAO;
        this.inventoryDAO = inventoryDAO;
    }

    @RequestMapping(value = "/list/{pageNum}", method = {RequestMethod.GET, RequestMethod.POST})
    public String list(Model model, @PathVariable(name = "pageNum") int pageNum, HttpServletRequest request) {
        Integer pageSize = 10;
        Product product = new Product();
        List<String> types = productDAO.getTypes();
        List<String> categories = productDAO.getCategories();
        List<String> subcategories = productDAO.getSubcategories();
        List<String> brandNames = brandDAO.getAllBrandNames();

        Page<Product> page = productDAO.findAll(PageRequest.of(pageNum - 1, 10));
        List<Product> plist = page.getContent();
        List<String> colours = productDAO.getColours();

        String transactionValue = Arrays.stream(request.getCookies()).filter(c -> "transaction".equals(c.getName()))
                .map(Cookie::getValue).reduce((first, second) -> second).orElse(null);
        String cookie = Objects.requireNonNullElse(transactionValue, "no cookie");
        if (cookie.equals("no cookie")) {
            model.addAttribute("fromTransaction", "browse");
        } else {
            model.addAttribute("fromTransaction", "stock");
        }

        model.addAttribute("colours", colours);
        model.addAttribute("brands", brandNames);
        model.addAttribute("types", types);
        model.addAttribute("categories", categories);
        model.addAttribute("subcategories", subcategories);
        model.addAttribute("product", product);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("source", "list");
        model.addAttribute("plist", plist);
        model.addAttribute("pageSize", pageSize);
        return "productlist";
    }

    @RequestMapping(value = "/filterlist/{pageNum}", method = {RequestMethod.GET, RequestMethod.POST})
    public String filterList(@ModelAttribute("product") Product product,
                             Model model, @PathVariable(name = "pageNum") int pageNum, HttpServletRequest request) {
        Integer pageSize = 10;
        if (product.getProductName() == null || product.getProductName().isEmpty()) {
            product.setProductName(null);
        }

        if (product.getType() == null || product.getType().equals("SELECT")) {
            product.setType(null);
        }
        if (product.getCategory() == null || product.getCategory().equals("SELECT")) {
            product.setCategory(null);
        }
        if (product.getSubcategory() == null || product.getSubcategory().equals("SELECT")) {
            product.setSubcategory(null);
        }

        if (product.getColour() == null || product.getColour().equals("SELECT")) {
            product.setColour(null);
        }
        if (product.getBrand() == null || product.getBrand().equals("SELECT")) {
            product.setBrand(null);
        } else {
            product.setBrand(brandDAO.findByBrandName(product.getBrand()).getBrandName());
        }

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<Product> page = productDAO.findAll(pageable);
        List<Product> plist = page.getContent();
        System.out.println("Hello" + plist.size());
        List<String> types = productDAO.getTypes();
        List<String> categories = productDAO.getCategories();
        List<String> subcategories = productDAO.getSubcategories();
        List<String> brandNames = brandDAO.getAllBrandNames();
        List<String> colours = productDAO.getColours();

        String transactionValue = Arrays.stream(request.getCookies()).filter(c -> "transaction".equals(c.getName()))
                .map(Cookie::getValue).reduce((first, second) -> second).orElse(null);
        String cookie = Objects.requireNonNullElse(transactionValue, "no cookie");
        if (cookie.equals("no cookie")) {
            model.addAttribute("fromTransaction", "browse");
        } else {
            model.addAttribute("fromTransaction", "stock");
        }

        model.addAttribute("colours", colours);
        model.addAttribute("brands", brandNames);
        model.addAttribute("types", types);
        model.addAttribute("categories", categories);
        model.addAttribute("subcategories", subcategories);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("plist", plist);
        model.addAttribute("source", "filterlist");
        model.addAttribute("product", product);

        return "productlist";
    }

    @GetMapping("/addToStock/{pid}")
    public String addToStock(@PathVariable("pid") Long prodId, Model model, HttpServletRequest request) {
        String transactionValue = Arrays.stream(request.getCookies()).filter(c -> "transaction".equals(c.getName()))
                .map(Cookie::getValue).reduce((first, second) -> second).orElse(null);
        String idString = Objects.requireNonNullElse(transactionValue, "no cookie");
        Long id = Long.parseLong(idString);
        Transaction transaction = transactionDAO.findById(id).get();

        TransactionDetails detail = new TransactionDetails();
        Product product = productDAO.findById(prodId).get();

        detail.setTransaction(transaction.getTransactionId());
        detail.setProduct(product.getProductId());

        model.addAttribute("detail", detail);
        model.addAttribute("products", product);
        model.addAttribute("transactiontype", transaction.getType());

        return "detailsentry";
    }

    @GetMapping("/viewHistory/{pid}/{pageNum}")
    public String viewHistory(@PathVariable("pid") Long prodId, Model model,
                              @PathVariable(name = "pageNum") int pageNum) {
        DateFilter df = new DateFilter();
        Product product = productDAO.findById(prodId).get();

        List<TransactionDetails> history = transactionDetailDAO.filterByProduct(product.getProductId(), 5,
                (pageNum - 1) * 5);

        model.addAttribute("df", df);
        model.addAttribute("product", product);
        model.addAttribute("history", history);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("hSource", "viewHistory");
        return "viewHistory";
    }

    @GetMapping("/filterHistory/{pid}/{pageNum}")
    public String filterHistory(@ModelAttribute("df") DateFilter df, @PathVariable("pid") Long prodId, Model model,
                                @PathVariable(name = "pageNum") int pageNum) {
        Product product = productDAO.findById(prodId).get();

        List<TransactionDetails> history = transactionDetailDAO.filterProductHistory(product.getProductId(),
                df.getStartDate(), df.getEndDate(), 5, (pageNum - 1) * 5);

        model.addAttribute("product", product);
        model.addAttribute("history", history);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("hSource", "filterHistory");
        return "viewHistory";
    }

    @GetMapping("/productform")
    public String addProduct(Model model) {
        Product product = new Product();

        List<Supplier> suppliers = supplierDAO.findAll();

        model.addAttribute("product", product);
        model.addAttribute("suppliers", suppliers);
        return "productform";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("product") Product product, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            List<Supplier> suppliers = supplierDAO.findAll();

            model.addAttribute("suppliers", suppliers);

            return "productform";
        }

        Brand brand = brandDAO.findByBrandName(product.getBrand().toUpperCase());
        if (brand != null) {
            product.setBrand(brand.getBrandName());
        } else {
            Brand create = new Brand(product.getBrand().toUpperCase());
            brandDAO.save(create);
            product.setBrand(create.getBrandName());
        }
        Long idS = product.getSupplier();
        Supplier supplier = supplierDAO.findById(idS).get();
        product.setSupplier(supplier.getSupplierId());

        if (product.getProductId() == null) {
            Inventory inventory = inventoryDAO.findById(product.getInventory()).get();
            inventory.setLastEmailSent(LocalDateTime.now());
            inventory.setReorderFlag(true);
            inventoryDAO.save(inventory);
        }

        productDAO.save(product);

        return "forward:/Product/list/1";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable("id") Long id) {

        Product product = productDAO.findById(id).get();
        List<Supplier> suppliers = supplierDAO.findAll();

        model.addAttribute("product", product);
        model.addAttribute("suppliers", suppliers);

        return "productform";
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable("id") Long id) {

        Product product = productDAO.findById(id).get();
        productDAO.delete(product);

        return "forward:/Product/list/1";
    }

    @GetMapping("/generateReport")
    public String generateReport() throws IOException {

        BufferedWriter bw = null;
        List<Supplier> suppliers = supplierDAO.findAll();

        DecimalFormat df = new DecimalFormat("#,###.00");

        try {
            File file = new File("report.dat");
            file.createNewFile();
            FileWriter fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
            for (Supplier s : suppliers) {
                List<Product> reorderList = productDAO.findReorderProducts(s.getSupplierId());
                if (!reorderList.isEmpty()) {
                    Iterator<Product> itr = reorderList.iterator();
                    String title = "Inventory reorder report for Supplier " + s.getName();
                    String end = "End of report for supplier " + s.getName();
                    String headerLine = "+------------+----------------+---------+-----------------+-------------------+-------------+-----------+";
                    bw.write(headerLine + "\n");
                    String titleLine = "-".repeat((105 - title.length()) / 2) + title
                            + "-".repeat((105 - title.length()) / 2);
                    String endLine = "-".repeat((105 - end.length()) / 2) + end + "-".repeat((105 - end.length()) / 2);
                    bw.write(titleLine + "\n");
                    bw.write(
                            "+------------+----------------+---------+-----------------+-------------------+-------------+-----------+\n");
                    bw.write(
                            "| PartNo     | Unit Price     | Qty     | Reorder Qty     | Min Order Qty     | Ord Qty     | Price     |\n");
                    bw.write(
                            "+------------+----------------+---------+-----------------+-------------------+-------------+-----------+\n");
                    double sum = 0.0;
                    while (itr.hasNext()) {
                        Product p = itr.next();
                        Inventory i = inventoryDAO.findById(p.getInventory()).get();
                        int orderQuantity = (int) Math
                                .ceil(1.0 * (i.getReorderLevel() - i.getUnits())
                                        / i.getMoq())
                                * i.getMoq();
                        double price = orderQuantity * i.getUnitPrice();
                        sum += price;
                        bw.write("| " + String.format("%-10s", String.format("%06d", p.getProductId())) + " |");
                        bw.write(String.format("%15s", i.getUnitPrice()) + " |");
                        bw.write(String.format("%8s", i.getUnits()) + " |");
                        bw.write(String.format("%16s", i.getReorderLevel()) + " |");
                        bw.write(String.format("%18s", i.getMoq()) + " |");
                        bw.write(String.format("%12s", orderQuantity) + " |");
                        bw.write(String.format("%10s", df.format(price)) + " |" + "\n");
                        bw.write(
                                "|------------|----------------|---------|-----------------|-------------------|-------------|-----------|\n");
                    }
                    bw.write(
                            "|            |                |         |                 |                   |        Total|");
                    bw.write(String.format("%10s", df.format(sum)) + " |" + "\n");
                    bw.write(
                            "+------------+----------------+---------+-----------------+-------------------+-------------+-----------+\n");
                    bw.write(endLine + "\n");
                    bw.newLine();
                    bw.flush();
                }
            }

        } finally {
            if (bw != null)
                bw.close();
        }

        return "sample";
    }
}
