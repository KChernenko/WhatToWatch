package me.bitfrom.whattowatch.utils;

public final class ConstantsManager {

    //Constants for api request
    public static final String BASE_URL = "http://www.myapifilms.com/imdb/";
    public static final String API_LIST_START = "1";
    public static final String API_TOP_LIST_END = "250";
    public static final String API_BOTTOM_LIST_END = "100";
    public static final String API_FORMAT = "json";
    public static final String API_DATA = "1";
    public static final String TEST_LAN = "en";
    public static final int CONNECTION_TIMEOUT = 60;
    public static final int READ_TIMEOUT = 60;
    public static final String CACHE_DIR_NAME = "wtw_http_cache";
    public static final int CACHE_SIZE = 10 * 1024 * 1024; //10 Mb
    public static final String CACHE_CONTROL_HEADER = "Cache-Control";

    //Server errors
    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int NOT_IMPLEMENTED = 501;
    public static final int BAD_GATEWAY = 502;
    public static final int SERVICE_UNAVAILABLE = 503;
    public static final int GATEWAY_TIME_OUT = 504;
    public static final int NETWORK_CONNECT_TIMEOUT_ERROR = 599;

    //AndroidJob
    public static final String SYNC_FILMS_JOB_TAG = "sync_films_job_tag";

    //Constants for data storage
    public static final String DATABASE_NAME = "whattowatch.db";
    public static final int DATABASE_VERSION = 3;
    public static final int NOT_FAVORITE = 0;
    public static final int FAVORITE = 1;
    public static final int CATEGORY_TOP = 1;
    public static final int CATEGORY_BOTTOM = -1;
    public static final int CATEGORY_IN_CINEMAS = 0;
    public static final int CATEGORY_COMING_SOON = 2;
    public static final String ARRAY_DIVIDER = ", ";

    //For the PreferencesHelper
    public static final String PREF_FILE_NAME = "what_to_watch_pref_file";
    public static final String APPS_FIRST_LAUNCH = "apps_first_launch";
    //Three days in seconds
    public static final long THREE_DAYS = 259200000L;
    //Five days in seconds
    public static final long FIVE_DAYS = 432000000L;
    //Seven days in seconds
    public static final long SEVEN_DAYS = 604800000L;

    public static final int NOTIFICATION_ID = 50001;

    //For the MvpViewNotAttachedException
    public static final String EXCEPTION_MESSAGE = "Please call Presenter.attachView(MvpView)" +
            " before requesting data to the Presenter";

    public static final String POSITION_ID_KEY = "position_id";

    public static final int SHARE_COMMENT_SIZE = 50;

    public static final String ARG_LAYOUT_RES_ID = "layoutResId";

    public static final String FRAGMENT_TITLE_KEY = "fragment_title_key";
}