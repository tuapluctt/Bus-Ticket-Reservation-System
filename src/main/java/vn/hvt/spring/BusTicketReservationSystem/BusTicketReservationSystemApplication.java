package vn.hvt.spring.BusTicketReservationSystem;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import vn.hvt.spring.BusTicketReservationSystem.entity.Booking;
import vn.hvt.spring.BusTicketReservationSystem.entity.Trip;
import vn.hvt.spring.BusTicketReservationSystem.repository.BookingReposity;
import vn.hvt.spring.BusTicketReservationSystem.repository.StopRepository;
import vn.hvt.spring.BusTicketReservationSystem.repository.TripRepository;
import vn.hvt.spring.BusTicketReservationSystem.service.BookingSevice;
import vn.hvt.spring.BusTicketReservationSystem.service.TripSevice;
import vn.hvt.spring.BusTicketReservationSystem.util.QRCodeGenerator;

import java.util.List;

@SpringBootApplication
public class BusTicketReservationSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(BusTicketReservationSystemApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(TripRepository tripRepository, StopRepository stopRepository){
		return runner -> {
//			List<Booking> booking = bookingSevice.searchBookingsByKeyword("0971025649");
//
//			booking.forEach(a->{
//				System.out.println( "fuck "+a.getFullName());
//			});

//			Booking booking = bookingSevice.getBookingById(1);
//
//			System.out.println(QRCodeGenerator.generateQRCode(booking));



//			String dateString = "2024-10-27";
//			// Định dạng của chuỗi ngày tháng
//			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//			// Chuyển đổi chuỗi thành LocalDate
//			LocalDate date = LocalDate.parse(dateString, formatter);
//			System.out.println("LocalDate: " + date);
//			System.out.println("LocalDate: " + SeatStatus.EMPTY);
//
//			List<TripCard> tripCards = tripSevice.filterTrips(14, 13,date);
//
//			int fuck  = tripSevice.getNumberOfEmptySeats(7);
//			System.out.println("LocalDate seat : " + fuck);
//
//			tripCards.forEach(tripCard -> {
//				System.out.println(tripCard.toString());
//			});








//			System.out.println("ngáo vl ");
//			List<Seat> seats = seatRepository.getSeatByBookingAndTrip(7);
//
//			seats.forEach(seat ->{
//				System.out.println("moa fuck " +seat.getSeatName());
//
//			});
//
//			List<Trip> trips = tripRepository.findAll();
//
//			trips.forEach(trip -> {
//				String h1 =stopRepository.findFirstStopNameByScheduleIdMax(trip.getSchedule().getId());
//				System.out.println(h1);
//				String h2 =stopRepository.findFirstStopNameByScheduleIdMin(trip.getSchedule().getId());
//				System.out.println(h2);
//				trip.setName(h1+ " - "+h2);
//
//				tripRepository.save(trip);
//
//			});
		};
	}
}
