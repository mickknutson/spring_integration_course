package io.baselogic.integration.common;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@SuppressWarnings({"Duplicates", "SpringJavaInjectionPointsAutowiringInspection"})
public class TestUtils {


    @Value("classpath:inputs/movies.csv")
    private Resource movies;

    public static void main(String[] args) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(
                    "/Users/pankaj/Downloads/myfile.txt"));
            String line = reader.readLine();
            while (line != null) {
                System.out.println(line);
                // read next line
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public List<String> getMovieTitles(int limit)
            throws IOException {

        InputStream resource = movies.getInputStream();

        List<String> titles = new ArrayList<>();


        try (
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(resource))
        ) {

            //   title,release_date,tagline

            titles = reader.lines().skip(1)
                    .map(m -> m.substring(0, m.indexOf(',')) )
                    .limit(limit)
                    .collect(Collectors.toList());

            titles.forEach(System.out::println);
        }

        return titles;
    }

    /*
    public List<String> getTokensFromFile(String path , String delim ) {

        List<String> tokens = new ArrayList<>();

        String currLine = "";

        StringTokenizer tokenizer;

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(Application.class.getResourceAsStream(
                        "/" + path )))) {
            while (( currLine = br.readLine()) != null ) {

                tokenizer = new StringTokenizer( currLine , delim );
                while (tokenizer.hasMoreElements()) {
                    tokens.add(tokenizer.nextToken());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tokens;
    }*/



} // The End...
