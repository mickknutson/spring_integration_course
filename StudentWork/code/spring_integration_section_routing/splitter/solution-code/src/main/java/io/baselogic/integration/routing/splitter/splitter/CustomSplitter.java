package io.baselogic.integration.routing.splitter.splitter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.Splitter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

//@MessageEndpoint
@Slf4j
@SuppressWarnings({"Duplicates", "SpringJavaInjectionPointsAutowiringInspection"})
public class CustomSplitter {

    /**
     * Analog to:
     *
     * <int:splitter id="splitter"
     *   ref="splitterBean"
     *   method="extractTitles"
     *   input-channel="inputSplitterChannel"
     *   output-channel="outputSplitterChannel" />
     *
     * @param movie (title,release_date,tagline)
     *              Eg. Avatar,12/10/09,Enter the World of Pandora.
     * @return List<String> movie tokens
     * @throws IOException
     */
    @Splitter(
            inputChannel = "inputSplitterChannel",
            outputChannel = "outputSplitterChannel"
    )
    public List<String> split(String movie) throws IOException {

        log.info("\n==> Movie to Split: [{}]", movie);

        List<String> tokens = Collections.list(new StringTokenizer(movie, ",")).stream()
                .map(token -> (String) token)
                .collect(Collectors.toList());

        tokens.forEach(t -> log.info("\n-->tokens: [{}]", t));
        return tokens;
    }

} // The End...
