package br.com.fuzus.run;

import br.com.fuzus.model.Movie;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.regex.MatchResult;

public class Main {

    static Map<String, String> map = new HashMap<>();

    public static void main(String[] args) {
        HttpClient client = HttpClient.newBuilder()
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://imdb-api.com/en/API/Top250Movies/k_ladr4o9k"))
                .GET()
                .build();

        String json = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .join();

        json = json.substring(9, json.lastIndexOf("]") + 1);

        listMoviesString(json);

    }


    /**
     * @Description Lista os filmes utilizando a biblioteca Gson da google
     * @param json
     */
    public static void listMoviesGson(String json) {
        Gson gson = new GsonBuilder().create();

        Movie[] arrMovies = gson.fromJson(json, Movie[].class);
        List<Movie> movies = Arrays.asList(arrMovies);

        movies.forEach(System.out::println);
        List<String> titles = new ArrayList<>();
        List<String> urlImages = new ArrayList<>();

        for (Movie movie : movies) {
            titles.add(movie.getTitle());
            urlImages.add(movie.getImage());
        }
    }

    /**
     * @description Lista os filme utilizando as bibliotecas
     * padrões do java
     * @param json
     */
    public static void listMoviesString(String json) {
        List<String> titles = new ArrayList<>();
        List<String> imageUrl = new ArrayList<>();
        String[] attributes = json.substring(1, json.lastIndexOf("]")).split("},");
        for (String attribute : attributes) {
            titles.add(parseJson(attribute, "title"));
            imageUrl.add(parseJson(attribute, "image"));
        }

        System.out.println(titles);
        System.out.println(imageUrl);

    }

    /**
     * @param attribute
     * @param key
     * @return retorna o valor da key passada
     */
    private static String parseJson(String attribute, String key) {
        String[] parsable = attribute.split(",\"");

        for (String str : parsable) {
            String[] a = str.split("\":\"");
            map.put(a[0].replaceAll("\"",""), a[1].replaceAll("\"", ""));
        }
        return map.get(key);
    }


}
