package com.csoftz.reactive.playing.repository.httptrace;

import java.util.stream.Stream;

import org.springframework.data.repository.Repository;

import com.csoftz.reactive.playing.domain.httptrace.HttpTraceWrapper;

public interface HttpTraceWrapperRepository extends Repository<HttpTraceWrapper, String> {
    Stream<HttpTraceWrapper> findAll();
    void save(HttpTraceWrapper trace);
}
