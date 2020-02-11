package shoppingpager.wingstud.shopinpager.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import java.util.List;

import shoppingpager.wingstud.shopinpager.model.HomeModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<List<HomeModel>> homeModel;

}
