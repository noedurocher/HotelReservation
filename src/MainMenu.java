import api.HotelResource;
import model.IRoom;
import model.Reservation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;

public class MainMenu {

    private static final String default_date = "MM/dd/yyyy";
    private static final HotelResource hotelResource = HotelResource.getSingleton();
    public static void main(String[] args) {

        boolean done = false;
        while(!done) {
            Scanner menuScanner = new Scanner(System.in);
            int menuNumber = 0;
            try {
                showMainMenu();
                menuNumber = menuScanner.nextInt();

                switch (menuNumber) {
                    case 1:
                        findReserveRoom();
                        break;
                    case 2:
                        seeMyReservation();
                        break;
                    case 3:
                        createAccount();
                        break;
                    case 4:
                        AdminMenu.adminMenu();
                        break;
                    case 5:
                        done = true;
                        break;
                    default:
                        System.out.println("Wrong option. Type 1 - 5 only.");
                        break;
                }
            }catch(Exception e){
                System.out.println("Error. Please try again!");
            }
        }

    }

    private static void showMainMenu(){
        System.out.println("1. Find and reserve a room" +
                "\n2. See my reservations\n3. Create an account\n4. Admin(" +
                "open the admin menu described below)\n5. Exit(exit the application)");
    }

    private static void findReserveRoom(){
        final Scanner scanner = new Scanner(System.in);

        System.out.println("Enter Check-In Date mm/dd/yyyy example 02/01/2020");
        Date checkIn = getInputDate(scanner);

        System.out.println("Enter Check-Out Date mm/dd/yyyy example 02/21/2020");
        Date checkOut = getInputDate(scanner);
        if(checkIn.compareTo(checkOut)>0){
            System.out.println("Check-out date must be same or later date than the Check-In date\n");
            findReserveRoom();
        }

        else if (checkIn != null && checkOut != null) {
            Collection<IRoom> availableRooms = hotelResource.findARoom(checkIn, checkOut);

            if (availableRooms.isEmpty()) {
                Collection<IRoom> alternativeRooms = hotelResource.findAlternativeRooms(checkIn, checkOut);

                if (alternativeRooms.isEmpty()) {
                    System.out.println("No rooms found.");
                } else {
                    final Date alternativeCheckIn = hotelResource.addSevenDays(checkIn);
                    final Date alternativeCheckOut = hotelResource.addSevenDays(checkOut);
                    System.out.println("We've only found rooms on alternative dates:" +
                            "\nCheck-In Date:" + alternativeCheckIn +
                            "\nCheck-Out Date:" + alternativeCheckOut);

                    showRooms(alternativeRooms);
                    reserveRoom(scanner, alternativeCheckIn, alternativeCheckOut, alternativeRooms);
                }
            } else {
                showRooms(availableRooms);
                reserveRoom(scanner, checkIn, checkOut, availableRooms);
            }
        }
    }

    private static Date getInputDate(final Scanner scanner) {
        try {
            Date nowDate = new Date();
            Date date = new SimpleDateFormat(default_date).parse(scanner.nextLine());

            String nowDateStr = nowDate.toString().substring(0, 10) + nowDate.toString().substring(23, 28);
            String dateStr = date.toString().substring(0, 10) + date.toString().substring(23, 28);

            if(dateStr.equals(nowDateStr) || date.after(nowDate)){
                return date;
            }
            else
                throw new RuntimeException();
        }catch (ParseException ex) {
            System.out.println("Error: Invalid date.");
            findReserveRoom();
        }catch (RuntimeException ex){
            System.out.println("Date must be current or future");
            findReserveRoom();
        }


        return null;
    }

    private static void showRooms(final Collection<IRoom> rooms) {
        if (rooms.isEmpty()) {
            System.out.println("No rooms found.");
        } else {
            rooms.forEach(System.out::println);
        }
    }

    private static void reserveRoom(final Scanner scanner, final Date checkInDate,
                                    final Date checkOutDate, final Collection<IRoom> rooms) {
        System.out.println("Would you like to book? y/n");
        final String bookRoom = scanner.nextLine();

        if ("y".equals(bookRoom)) {
            System.out.println("Do you have an account with us? y/n");
            final String haveAccount = scanner.nextLine();

            if ("y".equals(haveAccount)) {
                System.out.println("Enter Email format: name@domain.com");
                final String customerEmail = scanner.nextLine();

                if (hotelResource.getCustomer(customerEmail) == null) {
                    System.out.println("Customer not found.\nYou may need to create a new account.");
                } else {
                    System.out.println("What room number would you like to reserve?");
                    final String roomNumber = scanner.nextLine();

                    if (rooms.stream().anyMatch(room -> room.getRoomNumber().equals(roomNumber))) {
                        final IRoom room = hotelResource.getRoom(roomNumber);

                        final Reservation reservation = hotelResource
                                .bookARoom(customerEmail, room, checkInDate, checkOutDate);
                        System.out.println("Reservation created successfully!");
                        System.out.println(reservation);
                    } else {
                        System.out.println("Error: room number not available.\nStart reservation again.");
                    }
                }

            } else {
                System.out.println("Please, create an account.");
            }
        } else if ("n".equals(bookRoom)){
            System.out.println("");
        } else {
            reserveRoom(scanner, checkInDate, checkOutDate, rooms);
        }
    }

    private static void seeMyReservation() {
        final Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your Email format: name@domain.com");
        final String customerEmail = scanner.nextLine();

        printReservations(hotelResource.getCustomersReservations(customerEmail));
    }

    private static void printReservations(final Collection<Reservation> reservations) {
        if (reservations == null || reservations.isEmpty()) {
            System.out.println("No reservations found.");
        } else {
            reservations.forEach(reservation -> System.out.println("\n" + reservation));
        }
    }

    private static void createAccount() {
        final Scanner scanner = new Scanner(System.in);

        System.out.println("Enter Email format: name@domain.com");
        final String email = scanner.nextLine();

        System.out.println("First Name:");
        final String firstName = scanner.nextLine();

        System.out.println("Last Name:");
        final String lastName = scanner.nextLine();

        try {
            hotelResource.createACustomer(email, firstName, lastName);
            System.out.println("Account created successfully!");
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getLocalizedMessage());
            createAccount();
        }
    }



}
