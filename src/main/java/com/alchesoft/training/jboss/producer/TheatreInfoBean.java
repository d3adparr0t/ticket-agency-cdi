package com.alchesoft.training.jboss.producer;

import com.alchesoft.training.jboss.beans.TheatreBox;
import com.alchesoft.training.jboss.domain.Seat;
import org.jboss.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Model
public class TheatreInfoBean {

    private static final Logger log = Logger.getLogger(TheatreInfoBean.class);

    @Inject
    private TheatreBox theatreBox;

    private List<Seat> seats;

    @Produces
    @Named
    public List<Seat> getSeats() {
        return seats;
    }

    public void onMembersChanged(@Observes(notifyObserver = Reception.IF_EXISTS) Seat member) {
        log.info(String.format("Refreshing after a seat %d has been changed.", member.getId()));
        retrieveAllSeatsOrderedByName();
    }

    @PostConstruct
    private void retrieveAllSeatsOrderedByName() {
        seats = theatreBox.fetchAllSeats();
    }


}
