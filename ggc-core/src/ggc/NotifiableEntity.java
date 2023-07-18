package ggc;

import java.util.LinkedList;
import java.util.TreeMap;

public class NotifiableEntity {

    private LinkedList<Notification> _notifications = new LinkedList<>();
    private TreeMap<String, Boolean> _toggleState = new TreeMap<>(); // <productID : true/false>
    private NotificationMethods notificationMethod;



    // NOTIFICATION METHODS

    /**
     * Empties the notification list of the partner
     */
    public void emptyNotificationsList() {
        _notifications.clear();
    }


    /**
     * @param productID product's ID
     * @return true if product is in the list of products susceptible to be notified, false otherwise
     */
    public boolean hasProductToggle(String productID) {
        return _toggleState.containsKey(productID);
    }

    /**
     * Reverts the notifable mode (toggle state) of the product with the given ID
     *
     * @param productID product's ID
     */
    public void setToggle(String productID) {
        _toggleState.put(productID, !_toggleState.get(productID));
    }


    /**
     * Adds notification to the notification list of the partner
     *
     * @param notification Notification object
     */
    public void addNotification(Notification notification) {
        _notifications.add(notification);
    }


    /**
     * @param productID product's ID
     * @return toggle state for the product with the given ID
     */
    public boolean getToggleState(String productID) {
        return _toggleState.get(productID);
    }


    /**
     * Adds productID to the list of products susceptible to be notifiable and sets its mode to ON
     *
     * @param productID
     */
    public void becomeNotifiable(String productID) {
        _toggleState.put(productID, true);
    }


    public LinkedList<Notification> getNotifications() {
        return _notifications;
    }

    public TreeMap<String, Boolean> getToggleState() { return _toggleState; }
}