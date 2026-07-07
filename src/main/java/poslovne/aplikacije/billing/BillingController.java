package poslovne.aplikacije.billing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/billing")
public class BillingController {

    @Autowired
    private BillingService billingService;

    @PostMapping("/pay/{porudzbinaId}")
    public String payShipment(@PathVariable Long porudzbinaId) {
        try {
            billingService.processPayment(porudzbinaId);
            return "Uplata uspesna za porudzbinu: " + porudzbinaId;
        } catch (Exception e) {
            return "Greska pri uplati: " + e.getMessage();
        }
    }
}