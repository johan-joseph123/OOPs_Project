package ui;

import javax.swing.*;
import java.awt.*;
import java.util.Stack;

public class RideShareMobileUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    private final Stack<String> screenHistory = new Stack<>();
    private String currentScreen = "login";

    private final NavigationBarPanel navBar;
    private final MyTripsPanel myTripsPanel; 

    public RideShareMobileUI() {
        setTitle("Campus RideShare — Mobile");
        setSize(360, 640);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Screens
        ProviderHome providerHome = new ProviderHome(this);
        UserHomePanel userhomePanel = new UserHomePanel(this);
        SearchPanel searchPanel = new SearchPanel(this);
        RideListPanel rideListPanel = new RideListPanel(this);
        myTripsPanel = new MyTripsPanel(this); 
        LoginPage loginPage = new LoginPage(this);
        OfferRidePanel offerRide = new OfferRidePanel(this);
        
        AdminHomePanel admin = new AdminHomePanel(this);
        ApplicationVerificationPanel verify = new ApplicationVerificationPanel(this);
     // In RideShareMobileUI.java constructor:
     // ... after existing panel instantiations

     // New Sign-Up Panels
     SignUpChoicePanel signUpChoicePanel = new SignUpChoicePanel(this);
     DriverSignUpPanel driverSignUpPanel = new DriverSignUpPanel(this);
     RiderSignUpPanel riderSignUpPanel = new RiderSignUpPanel(this);

     // New Admin Panels
     ViewUsersPanel viewUsersPanel = new ViewUsersPanel(this);
     ViewAllRidesPanel viewAllRidesPanel = new ViewAllRidesPanel(this);

     // ... after existing mainPanel.add() calls

     // Add new sign-up panels
     mainPanel.add(signUpChoicePanel, "signup_choice");
     mainPanel.add(driverSignUpPanel, "driver_signup");
     mainPanel.add(riderSignUpPanel, "rider_signup");

     // Add new admin panels
     mainPanel.add(viewUsersPanel, "view_users");
     mainPanel.add(viewAllRidesPanel, "view_rides");
        mainPanel.add(userhomePanel, "userhome");
        mainPanel.add(providerHome,"providerhome");
        mainPanel.add(searchPanel, "search");
        mainPanel.add(rideListPanel, "rides");
        mainPanel.add(loginPage, "login");
        mainPanel.add(offerRide, "offer");
        mainPanel.add(myTripsPanel, "trips"); 
        
        mainPanel.add(admin,"admin");
        mainPanel.add(verify,"verify");
        navBar = new NavigationBarPanel(this);
        
        add(mainPanel, BorderLayout.CENTER);
        add(navBar, BorderLayout.SOUTH);

        showScreen("login");
    }

    public void showScreen(String name) {
        if (!name.equals(currentScreen)) {
            
            screenHistory.push(currentScreen);
        }
        currentScreen = name;
        cardLayout.show(mainPanel, name);
        navBar.update(name);

        // Special handling for MyTripsPanel to refresh content upon showing
        if ("trips".equals(name)) {
            myTripsPanel.updateTripList();
        }
    }

    public void goBack() {
        if (!screenHistory.isEmpty()) {
            String previous = screenHistory.pop();
            currentScreen = previous;
            cardLayout.show(mainPanel, previous);
            navBar.update(previous);
            if ("trips".equals(previous)) {
            	
            	
                myTripsPanel.updateTripList();
            }
        }
    }

    public void addRideToMyTrips(Ride ride) {
        myTripsPanel.addRide(ride); // Correct call to non-static method
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RideShareMobileUI().setVisible(true));
    }
}