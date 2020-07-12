package com.csoftz.reactive.playing.domain.httptrace;

import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.data.annotation.Id;

public class HttpTraceWrapper {

    @Id
    private String id;

    private final HttpTrace httpTrace;

    public HttpTraceWrapper(HttpTrace httpTrace) {
        this.httpTrace = httpTrace;
    }

    public HttpTrace getHttpTrace() {
        return httpTrace;
    }
}
