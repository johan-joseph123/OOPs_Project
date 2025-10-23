package view;

import javax.swing.*;
import java.awt.*;
import java.util.Stack;

public class RideShareMobileUI extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private final NavigationBarPanel navBar;

    private String currentScreen = "login";
    private String currentUserId;
    private String currentUserRole;
    private boolean loggedIn = false;

    private final Stack<String> screenHistory = new Stack<>();

    // ✅ Panels that need refresh
    private RiderTripsPanel riderTrips;
    private ProviderTripsPanel providerTrips;

    public RideShareMobileUI() {
        setTitle("Campus RideShare — MVC");
        setSize(360, 640);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Instantiate panels
        LoginPage login = new LoginPage(this);
        SignUpChoicePanel signupChoice = new SignUpChoicePanel(this);
        RiderSignUpPanel riderSignup = new RiderSignUpPanel(this);
        DriverSignUpPanel driverSignup = new DriverSignUpPanel(this);
        UserHomePanel userHome = new UserHomePanel(this);
        ProviderHome providerHome = new ProviderHome(this);
        OfferRidePanel offer = new OfferRidePanel(this);
        AdminDashboardPanel admin = new AdminDashboardPanel(this);
        SearchPanel search = new SearchPanel(this);

        // ✅ These two are now instance variables
        riderTrips = new RiderTripsPanel(this);
        providerTrips = new ProviderTripsPanel(this);

        // Add panels
        mainPanel.add(login, "login");
        mainPanel.add(signupChoice, "signup_choice");
        mainPanel.add(riderSignup, "rider_signup");
        mainPanel.add(driverSignup, "driver_signup");
        mainPanel.add(userHome, "userhome");
        mainPanel.add(providerHome, "providerhome");
        mainPanel.add(offer, "offer");
        mainPanel.add(admin, "admin");
        mainPanel.add(search, "search");
        mainPanel.add(riderTrips, "riderTrips");
        mainPanel.add(providerTrips, "providerTrips");

        navBar = new NavigationBarPanel(this);
        add(mainPanel, BorderLayout.CENTER);
        add(navBar, BorderLayout.SOUTH);

        showScreen("login");
    }

    // ✅ Navigate and refresh dynamic screens
    public void showScreen(String name) {
        if (!currentScreen.equals(name)) {
            screenHistory.push(currentScreen);
        }
        currentScreen = name;
        cardLayout.show(mainPanel, name);
        navBar.update(name);

        // Refresh data for dynamic panels
        if ("riderTrips".equals(name) && riderTrips != null) {
            riderTrips.refreshData();
        } else if ("providerTrips".equals(name) && providerTrips != null) {
            providerTrips.refreshData();
        }
    }

    public void goBack() {
        if (!screenHistory.isEmpty()) {
            String previous = screenHistory.pop();
            showScreen(previous);
        } else if (loggedIn) {
            if ("driver".equals(currentUserRole)) showScreen("providerhome");
            else if ("rider".equals(currentUserRole)) showScreen("userhome");
            else showScreen("login");
        } else {
            showScreen("login");
        }
    }

    // ✅ Store current user info
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RideShareMobileUI().setVisible(true));
    }
}
