package me.bitfrom.whattowatch.utils;


public class ConstantsManager {

    //Constants for api request
    public static final String BASE_URL = "http://www.myapifilms.com/imdb/";
    public static final String API_LIST_START = "1";
    public static final String API_LIST_END = "250";
    public static final String API_TOKEN = "19c94797-333b-45b7-aded-4bdca4e857fa";
    public static final String API_FORMAT = "json";
    public static final String API_DATA = "1";

    //Constants for data storage
    public static final String DATABASE_NAME = "whattowatch.db";
    public static final int DATABASE_VERSION = 1;
    public static final String DB_LOG_TAG = "WhatToWatch";

    //For the PreferencesHelper
    public static final String PREF_FILE_NAME = "what_to_watch_pref_file";
    public static final String APPS_FIRST_LAUNCH = "apps_first_launch";
    //Three days in seconds
    public static final int THREE_DAYS = 259200;
    //Five days in seconds
    public static final int FIVE_DAYS = 432000;
    //Seven days in seconds
    public static final int SEVEN_DAYS = 604800;

    public static final int NOTIFICATION_ID = 50001;

    //For the MvpViewNotAttachedException
    public static final String EXCEPTION_MESSAGE = "Please call Presenter.attachView(MvpView)" +
            " before requesting data to the Presenter";
}
