package com.example.myretrofit;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface Get_interface {


    @GET("ajax.php?a=fy&f=auto&t=auto&w=hi%20world")
    Observable<Traslation> getcall();



}
