package com.example.aplikasiuntukuts.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.aplikasiuntukuts.data.Cheese;
import com.example.aplikasiuntukuts.data.CheeseRepository;

import java.util.List;

public class CheeseViewModel extends AndroidViewModel {

    private CheeseRepository mRepository;
    private LiveData<List<Cheese>> mAllCheese;

    public CheeseViewModel (Application application) {
        super(application);
        mRepository = new CheeseRepository (application);
        mAllCheese = mRepository.getAllCheeses ();
    }
    public void deleteCheese(Cheese cheese) {
        mRepository.deleteCheese(cheese);
    }
    public LiveData<List<Cheese>> getAllCheeses() { return mAllCheese; }
    public void insert(Cheese cheese) {
        mRepository.insert (cheese);
    }
}
