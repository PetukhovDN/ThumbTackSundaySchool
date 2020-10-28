package net.thumbtack.school.notes.enums;

public class RequestParam {

    private final RequestParamEnum requestParamEnum;
    private final String info;

    public RequestParam(RequestParamEnum requestParamEnum, String info) {
        this.requestParamEnum = requestParamEnum;
        this.info = info;
    }
}
