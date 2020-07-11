package com.csoftz.reactive.playing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//import reactor.blockhound.BlockHound;

@SpringBootApplication
public class ReactivePlayingApplication {

    public static void main(String[] args) {
        /*BlockHound
            .builder()
            .allowBlockingCallsInside(TemplateEngine.class.getCanonicalName(), "process")
            .install();

         */
        SpringApplication.run(ReactivePlayingApplication.class, args);
    }
}
