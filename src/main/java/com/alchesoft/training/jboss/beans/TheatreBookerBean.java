package com.alchesoft.training.jboss.beans;

import com.alchesoft.training.jboss.domain.Seat;
import org.jboss.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class TheatreBookerBean implements Serializable {

    private static final Logger log = Logger.getLogger(TheatreBookerBean.class);

    private int wallet;

    @Inject
    private TheatreBox theatreBox;

    @PostConstruct
    public void createCustomer() {
        this.wallet = 100;
    }

    public void bookSeat(int seatId) {
        FacesContext fc = FacesContext.getCurrentInstance();
        log.info(String.format("Booking seat %d", seatId));
        Seat seat = theatreBox.findById(seatId);
        if (seat.getPrice() > wallet) {
            FacesMessage errorMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Not enough money!", "Registration successful");
            fc.addMessage(null, errorMsg);
            return;
        }
        theatreBox.buyTicket(seat.getId());
        FacesMessage successMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Booked!", "Booking successful");
        fc.addMessage(null, successMsg);
        log.info("Seat booked.");
        wallet -= seat.getPrice();
    }

    public int getWallet() {
        return wallet;
    }
}
