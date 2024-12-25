package org.example.constant;


public enum UserState {
    AWAITING_NAME,
    PARAMETERS,
    AWAITING_CHANGE,
    REGION_SET,
    DETAILS_COMPLETE,
    AVAILABLE,
    BRAND_SET("brand"),
    MODEL_SET("model"),
    START_YEAR_SET("startYear"),
    END_YEAR_SET("endYear"),
    START_PRICE_SET("startPrice"),
    END_PRICE_SET("endPrice");

    private final String parameter;

    UserState(String description) {
        this.parameter = description;
    }

    UserState(){
        this.parameter = "default";
    }

    public static UserState fromCallbackData(String parameter) {
        for (UserState state : values()) {
            if (parameter.equals(state.parameter)) {
                return state;
            }
        }
        throw new IllegalArgumentException("Unknown callback data: " + parameter);
    }
}