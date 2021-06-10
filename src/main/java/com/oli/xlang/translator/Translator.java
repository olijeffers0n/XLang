package com.oli.xlang.translator;

import com.google.gson.Gson;
import com.oli.xlang.XLang;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Translator {

    private XLang plugin;

    public Translator(XLang plugin) {
        this.plugin = plugin;
    }

    public String getTranslation(String input, String language) {
        try {
            HttpURLConnection con = (HttpURLConnection) this.plugin.url.openConnection();
            con.setRequestMethod("POST");
            Map<String, String> parameters = new HashMap<>();
            parameters.put("auth_key", this.plugin.getConfig().getString("apiKey"));
            parameters.put("text", input);
            parameters.put("target_lang", language);

            con.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes(this.plugin.parameterStringBuilder.getParamsString(parameters));
            out.flush();
            out.close();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));

            String content = in.readLine();
            in.close();
            con.disconnect();

            Gson gsonInstance = new Gson();

            return gsonInstance.fromJson(content, Translations.class).translations.get(0).text;

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }
    class Translations {
        List<Contents> translations;
    }
    class Contents {
        String detected_source_language;
        String text;
    }
}
