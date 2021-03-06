package edu.uw.tcss450.group6App.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * Holds user info needed for account.
 */
public class UserInfoViewModel extends ViewModel {

    private final String mEmail;        //Users current email address
    private final String mJwt;          //JWT used for accessing services
    private static int currentChatId;   //the current chat that the user is in, used when navigating multiple chats

    private UserInfoViewModel(String email, String jwt) {
        mEmail = email;
        mJwt = jwt;
        currentChatId = 0;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getJwt() {
        return mJwt;
    }

    public int getCurrentChatId() {
        return currentChatId;
    }

    public void setCurrentChatId(int n) {
        currentChatId = n;
    }

    public static class UserInfoViewModelFactory implements ViewModelProvider.Factory {

        private final String email;
        private final String jwt;

        public UserInfoViewModelFactory(String email, String jwt) {
            this.email = email;
            this.jwt = jwt;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass == UserInfoViewModel.class) {
                return (T) new UserInfoViewModel(email, jwt);
            }
            throw new IllegalArgumentException(
                    "Argument must be: " + UserInfoViewModel.class);
        }
    }


}

