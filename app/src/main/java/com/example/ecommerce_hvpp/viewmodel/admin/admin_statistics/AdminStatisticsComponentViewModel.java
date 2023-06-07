package com.example.ecommerce_hvpp.viewmodel.admin.admin_statistics;

import androidx.lifecycle.ViewModel;

import com.example.ecommerce_hvpp.model.DataStatisticDouble;
import com.example.ecommerce_hvpp.model.DataStatisticInt;

public class AdminStatisticsComponentViewModel extends ViewModel {
    public static DataStatisticInt dataStatisticOrders;
    public static DataStatisticInt dataStatisticProductSold;
    public static DataStatisticInt dataStatisticVisitors;
    public static DataStatisticDouble dataStatisticRevenue;

    public static String strMinDateOrders;
    public static String strMinDateProductSold;
    public static String strMinDateVisitors;
    public static String strMinDateRevenue;

    public AdminStatisticsComponentViewModel() {}
}
