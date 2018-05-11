package com.example.mholt2587.farmersmarkettest;

/**
 * Created by mholt2587 on 4/19/2018.
 */

public class Market {
    private String marketname;
    private String id;
    private String marketAddress;



    public Market(String marketname, String id, String marketAddress){

        this.marketname = marketname;
        this.id = id;
        this.marketAddress = marketAddress;  //change to ""
    }
    public String getMarketname(){
        return marketname;
    }
    public String getId(){
        return id;
    }
    public String getMarketAddress() {
        return marketAddress;
    }


}
