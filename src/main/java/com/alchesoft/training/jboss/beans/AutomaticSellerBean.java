package com.alchesoft.training.jboss.beans;

import com.alchesoft.training.jboss.domain.Seat;
import org.jboss.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.TimerService;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class AutomaticSellerBean {

    private static final Logger log = Logger.getLogger(AutomaticSellerBean.class);

    @Inject
    private TheatreBox theatreBox;

    @Inject
    private PollerBean pollerBean;

    @Resource
    private TimerService timerService;

    @Schedule(hour="*", minute = "*", second = "*/30", persistent = false)
    public void backgroundProcessing() {
        int seatId = findSeat();
        if (seatId == -1) {
            pollerBean.setPollingActive(false);
            cancelTimers();
            log.info("Scheduler gone!");
            return;
        }
        theatreBox.buyTicket(seatId);
        log.info(String.format("Somebody just booked seat number %d", seatId));
    }

    private int findSeat() {
        List<Seat> seats = theatreBox.fetchAllSeats();
        return seats.stream()
                .filter(seat ->!seat.isBooked())
                .map(Seat::getId)
                .findFirst()
                .orElse(-1);
    }

    private void cancelTimers() {
        timerService.getTimers().forEach(timer -> timer.cancel());
    }

}
