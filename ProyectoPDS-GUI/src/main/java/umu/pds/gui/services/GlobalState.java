package umu.pds.gui.services;

import umu.pds.dto.TarjetaResponseDTO;

public class GlobalState {
    private static GlobalState instance;
    private String userEmail;
    private String userName;
    private String userPhotoUrl;
    private String currentBoardId;
    private String currentListId;
    private String currentCardId;
    private TarjetaResponseDTO currentCard;
    private String currentListName;
    private String currentUserRole;

    private GlobalState() {
    }

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhotoUrl() {
        return userPhotoUrl;
    }

    public void setUserPhotoUrl(String userPhotoUrl) {
        this.userPhotoUrl = userPhotoUrl;
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

    public TarjetaResponseDTO getCurrentCard() {
        return currentCard;
    }

    public void setCurrentCard(TarjetaResponseDTO currentCard) {
        this.currentCard = currentCard;
    }

    public String getCurrentListName() {
        return currentListName;
    }

    public void setCurrentListName(String currentListName) {
        this.currentListName = currentListName;
    }

    private boolean showActivityInDashboard = true;
    public boolean isShowActivityInDashboard() { return showActivityInDashboard; }
    public void setShowActivityInDashboard(boolean show) { this.showActivityInDashboard = show; }

    public String getCurrentUserRole() { return currentUserRole; }
    public void setCurrentUserRole(String currentUserRole) { this.currentUserRole = currentUserRole; }
}
