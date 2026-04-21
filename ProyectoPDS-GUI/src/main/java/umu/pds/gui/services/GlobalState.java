package umu.pds.gui.services;

public class GlobalState {
    private static GlobalState instance;
    private String userEmail;
    private String currentBoardId;
    private String currentListId;
    private String currentCardId;

    private GlobalState() {}

    public static GlobalState getInstance() {
        if (instance == null) {
            instance = new GlobalState();
        }
        return instance;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getCurrentBoardId() {
        return currentBoardId;
    }

    public void setCurrentBoardId(String currentBoardId) {
        this.currentBoardId = currentBoardId;
    }

    public String getCurrentListId() {
        return currentListId;
    }

    public void setCurrentListId(String currentListId) {
        this.currentListId = currentListId;
    }

    public String getCurrentCardId() {
        return currentCardId;
    }

    public void setCurrentCardId(String currentCardId) {
        this.currentCardId = currentCardId;
    }
}
