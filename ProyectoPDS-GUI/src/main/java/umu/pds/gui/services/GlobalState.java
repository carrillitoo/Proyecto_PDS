package umu.pds.gui.services;

public class GlobalState {
    private static GlobalState instance;
    private String userEmail;
    private String currentBoardId;

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
}
