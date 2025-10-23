package view;

import javax.swing.*;
import java.awt.*;
import java.util.Stack; // ✅ You forgot this import


public class RideShareMobileUI extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private final NavigationBarPanel navBar;
    private String currentScreen = "login";
    private String currentUserId;
    private String currentUserRole;
    private boolean loggedIn = false;
    private final Stack<String> screenHistory = new Stack<>();

    public RideShareMobileUI() {
        setTitle("Campus RideShare — MVC");
        setSize(360, 640);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Instantiate panels (views)
        LoginPage login = new LoginPage(this);
        SignUpChoicePanel signupChoice = new SignUpChoicePanel(this);
        RiderSignUpPanel riderSignup = new RiderSignUpPanel(this);
        DriverSignUpPanel driverSignup = new DriverSignUpPanel(this);
        UserHomePanel userHome = new UserHomePanel(this);
        ProviderHome providerHome = new ProviderHome(this);
        OfferRidePanel offer = new OfferRidePanel(this);
        AdminDashboardPanel admin = new AdminDashboardPanel(this);
        SearchPanel search = new SearchPanel(this);
        MyTripsPanel myTrips = new MyTripsPanel(this); // shared by both

        // Add screens
        mainPanel.add(login, "login");
        mainPanel.add(signupChoice, "signup_choice");
        mainPanel.add(riderSignup, "rider_signup");
        mainPanel.add(driverSignup, "driver_signup");
        mainPanel.add(userHome, "userhome");
        mainPanel.add(providerHome, "providerhome");
        mainPanel.add(offer, "offer");
        mainPanel.add(admin, "admin");
        mainPanel.add(search, "search");
        mainPanel.add(myTrips, "trips");

        navBar = new NavigationBarPanel(this);
        add(mainPanel, BorderLayout.CENTER);
        add(navBar, BorderLayout.SOUTH);

        showScreen("login");
    }

    // ✅ Navigate forward (adds to history)
    public void showScreen(String name) {
        if (!currentScreen.equals(name)) {
            screenHistory.push(currentScreen);
        }
        currentScreen = name;
        cardLayout.show(mainPanel, name);
        navBar.update(name);
    }

    // ✅ Navigate backward safely
    public void goBack() {
        if (!screenHistory.isEmpty()) {
            String previous = screenHistory.pop();
            currentScreen = previous;
            cardLayout.show(mainPanel, previous);
            navBar.update(previous);
        } else {
            // Optional: Exit or go to home
            if (loggedIn) {
                if ("driver".equals(currentUserRole)) showScreen("providerhome");
                else if ("rider".equals(currentUserRole)) showScreen("userhome");
                else showScreen("login");
            } else {
                showScreen("login");
            }
        }
    }

    // ✅ User state
    public void setCurrentUser(String id, String role) {
        this.currentUserId = id;
        this.currentUserRole = role;
    }

    public String getCurrentUserId() { return currentUserId; }
    public String getCurrentUserRole() { return currentUserRole; }

    public void setLoggedIn(boolean val) {
        this.loggedIn = val;
        navBar.setLoggedIn(val);
    }

    public boolean isLoggedIn() { return loggedIn; }

    // ✅ Run application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RideShareMobileUI().setVisible(true));
    }
}
