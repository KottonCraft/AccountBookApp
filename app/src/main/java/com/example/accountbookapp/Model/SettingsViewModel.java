package com.example.accountbookapp.Model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SettingsViewModel extends ViewModel {
    private final MutableLiveData<Boolean> buttonClicked = new MutableLiveData<>();

    public void setButtonClicked() {
        buttonClicked.setValue(true);
    }

    public LiveData<Boolean> getButtonClicked() {
        return buttonClicked;
    }
}
