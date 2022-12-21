import api.AdminResource;
import model.Customer;
import model.IRoom;
import model.Room;
import model.RoomType;

import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;

public class AdminMenu {
    private static final AdminResource adminResource = AdminResource.getSingleton();
    public static void adminMenu() {

        boolean done = false;
        while(!done){
            Scanner menuScanner = new Scanner(System.in);
            try {
                showMenu();
                int menuNumber = menuScanner.nextInt();
                switch (menuNumber) {
                    case 1:
                        displayAllCustomers();
                        break;
                    case 2:
                        displayAllRooms();
                        break;
                    case 3:
                        displayAllReservations();
                        break;
                    case 4:
                        addRoom();
                        break;
                    case 5:
                        done = true;
                        MainMenu.main(null);
                        break;
                    default:
                        System.out.println("Wrong option. Type 1 - 5 only.");
                        break;

                }
            }catch (IllegalArgumentException e){
                System.out.println("Error. Please try again!. Only numbers are allowed.");
            }catch (Exception e){
                System.out.println("Only numbers are allowed for admin menu");
            }
        }

    }

    private static void addRoom() {
        final Scanner scanner = new Scanner(System.in);

        System.out.println("Enter room number:");
        final String roomNumber = scanner.nextLine();
        int roomNumberInt = Integer.parseInt(roomNumber);
        if(roomNumberInt/roomNumberInt != 1){
            throw new IllegalArgumentException();
        }
        System.out.println("Enter price per night:");
        final double roomPrice = enterRoomPrice(scanner);

        System.out.println("Enter room type: 1 for single bed, 2 for double bed:");
        final RoomType roomType = enterRoomType(scanner);

        final Room room = new Room(roomNumber, roomPrice, roomType);

        adminResource.addRoom(Collections.singletonList(room));
        System.out.println("Room added successfully!");

        System.out.println("Would like to add another room? Y/N");
        addAnotherRoom();
    }

    private static double enterRoomPrice(final Scanner scanner) {
        try {
            return Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException exp) {
            System.out.println("Invalid room price! Please, enter a valid double number. " +
                    "Decimals should be separated by point (.)");
            return enterRoomPrice(scanner);
        }
    }

    private static RoomType enterRoomType(final Scanner scanner) {
        try {
            return RoomType.valueOfLabel(scanner.nextLine());
        } catch (IllegalArgumentException exp) {
            System.out.println("Invalid room type! Please, choose 1 for single bed or 2 for double bed:");
            return enterRoomType(scanner);
        }
    }

    private static void addAnotherRoom() {
        final Scanner scanner = new Scanner(System.in);

        try {
            String anotherRoom;

            anotherRoom = scanner.nextLine();

            while ((anotherRoom.charAt(0) != 'Y' && anotherRoom.charAt(0) != 'N')
                    || anotherRoom.length() != 1) {
                System.out.println("Please enter Y (Yes) or N (No)");
                anotherRoom = scanner.nextLine();
            }

            if (anotherRoom.charAt(0) == 'Y') {
                addRoom();
            } else if (anotherRoom.charAt(0) == 'N') {
                System.out.println();
            } else {
                addAnotherRoom();
            }
        } catch (StringIndexOutOfBoundsException ex) {
            addAnotherRoom();
        }
    }

    private static void displayAllRooms() {
        Collection<IRoom> rooms = adminResource.getAllRooms();

        if(rooms.isEmpty()) {
            System.out.println("No rooms found.");
        } else {
            adminResource.getAllRooms().forEach(System.out::println);
        }
    }

    private static void displayAllCustomers() {
        Collection<Customer> customers = adminResource.getAllCustomers();

        if (customers.isEmpty()) {
            System.out.println("No customers found.");
        } else {
            adminResource.getAllCustomers().forEach(System.out::println);
        }
    }

    private static void displayAllReservations() {
        adminResource.displayAllReservations();
    }
    private static void showMenu(){
        System.out.println("1. See all customers" +
                "\n2. See all rooms\n3. See all reservations\n4. Add a room\n5. Back to main menu)");
    }
}
