package org.example.constant;

public class TextField {
    public static final String CHAT_STATES = "chatStates";
    public static final String CAR_SET_DATA = "carSet";

    public static final String START_DESCRIPTION = "Starts the bot";

    public static final String START_TEXT = """
                                            Welcome to theSIS Bot.
                                            now you have to choose
                                            "Cars" - work with AutoRia
                                            "Weather" - weather forecast
                                            """;
    public static final String STOP_TEXT = """
                                           I hope I helped you find the car you were looking for
                                           Press /start to work with me again
                                           """;
    public static final String UNEXPECTED_MESSAGE = "I did not expect that.";

    public static final String YES_NO = "Does everything look good? Yes/No";
    public static final String CHANGE_PARAMETERS = "Click on the one you want to change";
    public static final String PARAMETERS_TEXT = """
                                                 Write the parameters of the machine
                                                 Brand, Model, StartYear, EndYear, StartPrice, EndPrice,
                                                 """;
    public static final String SET_CITY_TEXT = """
                                               To receive information about the weather
                                               write city name
                                               """;
}