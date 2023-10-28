package com.app.ecommerce.service;

import com.app.ecommerce.dao.InventoryDAO;
import com.app.ecommerce.model.Inventory;
import com.app.ecommerce.model.Product;
import com.app.ecommerce.model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class MailService {
    private final JavaMailSender javaMailSender;

    private final InventoryDAO inventoryDAO;

    @Autowired
    public MailService(JavaMailSender javaMailSender, InventoryDAO inventoryDAO) {
        this.javaMailSender = javaMailSender;
        this.inventoryDAO = inventoryDAO;
    }

    public void sendNotification(User user, Product product) throws MessagingException {

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            Inventory i = inventoryDAO.findById(product.getInventory()).get();
            String htmlMsg = "Dear user,"
                    + "<br>"
                    + "<br>"
                    + "Please be informed that the following products are currently low in stock:"
                    + "<br>"
                    + "<table border=\"1\">"
                    + "    <tr>"
                    + "        <th>"
                    + "            Product ID"
                    + "        </th>"
                    + "        <th>"
                    + "            Product Name"
                    + "        </th>"
                    + "        <th>"
                    + "            Quantity left"
                    + "        </th>"
                    + "    </tr>"
                    + "    <tr>"
                    + "        <td>"
                    + product.getProductId()
                    + "        </td>"
                    + "        <td>"
                    + product.getProductName()
                    + "        </td>"
                    + "        <td>"
                    + i.getUnits()
                    + "        </td>"
                    + "    </tr>"
                    + "</table>"
                    + "<br>"
                    + "--This is a system generated email, no response is required--";
            mimeMessage.setContent(htmlMsg, "text/html");
            helper.setText(htmlMsg, true);
            helper.setTo(user.getEmail());
            helper.setSubject("[Low Stock Notification] Product " + product.getProductId() + " " + product.getProductName());
            helper.setFrom("ecomm-inventory@hotmail.com");

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            Logger.getLogger(MailService.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
