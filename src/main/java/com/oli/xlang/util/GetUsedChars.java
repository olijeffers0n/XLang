package com.oli.xlang.util;

import com.google.gson.Gson;
import com.oli.xlang.XLang;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class GetUsedChars {

    private final XLang plugin;

    public GetUsedChars(XLang plugin) {
        this.plugin = plugin;
    }

    public CharInfo getChars() {
        try {
            URL url = new URL("https://api-free.deepl.com/v2/usage");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            Map<String, String> parameters = new HashMap<>();
            parameters.put("auth_key", this.plugin.getConfig().getString("deepl.apiKey"));

            con.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes(this.plugin.parameterStringBuilder.getParamsString(parameters));
            out.flush();
            out.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));

            String content = in.readLine();
            in.close();
            con.disconnect();

            Gson gsonInstance = new Gson();
            return gsonInstance.fromJson(content, CharInfo.class);

        } catch (IOException e) {
            e.printStackTrace();
        }
        this.plugin.getLogger().warning("The HTTP Request has failed");
        return new CharInfo();
    }

    public class CharInfo {
        public int character_count;
        public int character_limit;
    }
}
