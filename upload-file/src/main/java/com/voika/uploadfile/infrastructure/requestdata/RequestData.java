package com.voika.uploadfile.infrastructure.requestdata;

import lombok.Data;

@Data
public class RequestData {

    public static TokenUser TokenUser;

    public static Header header;

    {
        TokenUser = new TokenUser();
    }

    public RequestData() {

    }
}
