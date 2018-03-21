package io.github.mellamopablo.youtuberssbrowser.support;

import android.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class HTTPClient {
    public static String request(
            String url,
            List<Pair<String, String>> queryString
    ) throws IOException {
        StringBuilder qs = new StringBuilder();

        // Clonamos para no modificar la lista que nos pasen.
        List<Pair<String, String>> ourQs = new ArrayList<>();
        ourQs.addAll(queryString);

        for (Pair<String, String> pair: ourQs) {
            String key = pair.first;
            String value = pair.second;

            qs
                    .append(qs.toString().equals("") ? "?" : "&")
                    .append(key)
                    .append("=")
                    .append(value);
        }

        HttpURLConnection conn = (HttpURLConnection) new URL(url + qs).openConnection();

        conn.setRequestMethod("GET");
        conn.connect();

        InputStream stream = conn.getInputStream();

        if (stream == null) {
            throw new IOException("El stream es nulo!");
        }

        StringBuilder response = new StringBuilder();
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        while((line = reader.readLine()) != null) {
            response.append(line).append("\n");
        }

        return response.toString();
    }
}
