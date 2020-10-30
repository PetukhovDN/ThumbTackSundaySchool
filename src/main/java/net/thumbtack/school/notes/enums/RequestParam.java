package net.thumbtack.school.notes.enums;

public class RequestParam {

    private RequestParamType paramType;
    private RequestParamValue paramValue;

    public RequestParam(RequestParamType paramType, RequestParamValue paramValue) {
        this.paramType = paramType;
        this.paramValue = paramValue;
    }
}
