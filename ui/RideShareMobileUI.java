package ui;

import javax.swing.*;
import java.awt.*;
import java.util.Stack;

public class RideShareMobileUI extends JFrame {
	private boolean loggedIn = false; // ✅ to track login state

	// Getter
	public boolean isLoggedIn() {
	    return loggedIn;
	}

	// Setter
	public void setLoggedIn(boolean status) {
	    this.loggedIn = status;
	    // only call navBar.setLoggedIn if navBar is already constructed
	    if (navBar != null) navBar.setLoggedIn(status);
	}


	private CardLayout cardLayout;
    private JPanel mainPanel;

    private final Stack<String> screenHistory = new Stack<>();
    private String currentScreen = "login";

    private final NavigationBarPanel navBar;
    private final MyTripsPanel myTripsPanel;

    // 🔹 Track login state globally    
    private String currentUserId;
    private String currentUserRole;

    public void setCurrentUser(String id, String role) {
        this.currentUserId = id;
        this.currentUserRole = role;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public String getCurrentUserRole() {
        return currentUserRole;
    }


    public RideShareMobileUI() {
        setTitle("Campus RideShare — Mobile");
        setSize(360, 640);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // ==== Screens ====
        AdminDashboardPanel admin = new AdminDashboardPanel(this);
        ProviderHome providerHome = new ProviderHome(this);
        UserHomePanel userhomePanel = new UserHomePanel(this);
        SearchPanel searchPanel = new SearchPanel(this);
        RideListPanel rideListPanel = new RideListPanel(this);
        myTripsPanel = new MyTripsPanel(this);
        LoginPage loginPage = new LoginPage(this);
        OfferRidePanel offerRide = new OfferRidePanel(this);
        //AdminHomePanel admin = new AdminHomePanel(this);
        ApplicationVerificationPanel verify = new ApplicationVerificationPanel(this);

        SignUpChoicePanel signUpChoicePanel = new SignUpChoicePanel(this);
        DriverSignUpPanel driverSignUpPanel = new DriverSignUpPanel(this);
        RiderSignUpPanel riderSignUpPanel = new RiderSignUpPanel(this);

        // ==== Add Panels ====
        mainPanel.add(admin, "admin");
        mainPanel.add(signUpChoicePanel, "signup_choice");
        mainPanel.add(driverSignUpPanel, "driver_signup");
        mainPanel.add(riderSignUpPanel, "rider_signup");
        mainPanel.add(userhomePanel, "userhome");
        mainPanel.add(providerHome, "providerhome");
        mainPanel.add(searchPanel, "search");
        mainPanel.add(rideListPanel, "rides");
        mainPanel.add(loginPage, "login");
        mainPanel.add(offerRide, "offer");
        mainPanel.add(myTripsPanel, "trips");
        //mainPanel.add(admin, "admin");
        mainPanel.add(verify, "verify");

        // ==== Navigation Bar ====
        navBar = new NavigationBarPanel(this);
        add(mainPanel, BorderLayout.CENTER);
        add(navBar, BorderLayout.SOUTH);

        showScreen("login");
    }

    // 🔹 Called to switch between screens safely
    public void showScreen(String name) {
        if (!name.equals(currentScreen)) {
            screenHistory.push(currentScreen);
        }
        currentScreen = name;
        cardLayout.show(mainPanel, name);
        navBar.update(name);

        // Special handling for MyTripsPanel
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

    /*public void addRideToMyTrips(Ride ride) {
        myTripsPanel.addRide(ride);
    }*/

    // 🔹 Manage login state (for Home access control)
   

   

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RideShareMobileUI().setVisible(true));
    }
}
