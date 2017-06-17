package com.alchesoft.training.jboss.beans;

import static java.util.stream.Collectors.*;

import com.alchesoft.training.jboss.domain.Seat;
import org.jboss.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Singleton
@Startup
public class TheatreBox implements Serializable {

    private static final Logger log = Logger.getLogger(TheatreBox.class);

    private List<Seat> seats;

    @Inject
    private Event<Seat> seatEvent;

    @PostConstruct
    public  void setup() {
        seats = new ArrayList<>(15);
        List<Seat> stalls = IntStream.range(0, 5).mapToObj(index -> new Seat(index, "Stalls", 40)).collect(toList());
        seats.addAll(stalls);
        List<Seat> circles = IntStream.range(5, 10).mapToObj(index -> new Seat(index, "Circle", 20)).collect(toList());
        seats.addAll(circles);
        List<Seat> balconies = IntStream.range(10, 15).mapToObj(index -> new Seat(index, "Balcony", 10)).collect(toList());
        seats.addAll(balconies);

        log.info("Seat Map constructed.");
    }

    @Lock(LockType.READ)
    public Seat findById(int id) {
        return this.seats.stream()
                .filter(seat -> seat.getId() == id)
                .findFirst().orElse(null);
    }

    @Lock(LockType.READ)
    public int getSeatPrice(int id) {
        return this.seats.stream()
                .filter(seat -> seat.getId() == id)
                .map(Seat::getPrice)
                .findFirst().orElse(0);
    }

    @Lock(LockType.READ)
    public List<Seat> fetchAllSeats() {
        return this.seats.stream()
                .sorted(Comparator.comparing(Seat::getName))
                .collect(toList());
    }

    @Lock(LockType.READ)
    public String printSeats() {
        return this.seats.stream()
                .map(Seat::toString)
                .collect(Collectors.joining("\n"));
    }

    @Lock(LockType.WRITE)
    public void buyTicket(int seatId) {
        Seat seat = findById(seatId);
        seat.setBooked(true);
        seatEvent.fire(seat);
    }
}
