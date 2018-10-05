package io.baselogic.integration.common.endpoints;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@Slf4j
@SuppressWarnings({"Duplicates", "SpringJavaInjectionPointsAutowiringInspection"})
public class JobEndpoint {


    private AtomicBoolean enabled = new AtomicBoolean(true);

    //---------------------------------------------------------------------------//

    @GetMapping("/launch")
    public String launchJob(@RequestParam(value = "launchJob", required = false, defaultValue="true") boolean launchJob)
            throws Exception {
        enabled.set(launchJob);
        return "foo bar";
    }

    //---------------------------------------------------------------------------//


} // The End...
