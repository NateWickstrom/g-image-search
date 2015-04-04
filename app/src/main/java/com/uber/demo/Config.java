package com.uber.demo;

/**
 * Created by nate on 3/26/15.
 */
public class Config {

    public static int PAGE_SIZE = 8;

    /** Network connection time-out **/
    static final int CONNECTION_TIMEOUT_SECONDS = 5;
    /** Google image search url **/
    public static final String SEARCH_URL = "https://ajax.googleapis.com/ajax/services/search/images?&v=1.0&rsz=" + PAGE_SIZE;

}
