package com.jst.poc.dispatcher.utilities.TimeAgoPack;

public class Messages {

  public static final String AGO = "ago";
  public static final String DAY = "1 day";
  public static final String DAYS = "{0} days";
  public static final String HOUR = "An hour";
  public static final String HOURS = "{0} hours";
  public static final String MINUTE = "1 minute";
  public static final String MINUTES = "{0} minutes";
  public static final String MONTH = "1 month";
  public static final String MONTHS = "{0} months";
  public static final String SECONDS = "Few seconds";
  public static final String SUFFIX_FROM_NOW = "From now";
  public static final String YEAR = "1 year";
  public static final String YEARS = "{0} years";
  private Messages() {
  }

  /**
   * Get string for key
   *
   * @param key
   * @return string
   */
  public static String getString(String key) {
    try {
      return key;
    } catch (Exception e) {
      return '!' + key + '!';
    }
  }

}
