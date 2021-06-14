package com.oli.xlang.translator;

import com.google.gson.Gson;
import com.oli.xlang.XLang;

import java.io.*;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Translator {

    private final Set<String> validLocales = new HashSet<>(Arrays.asList("el","sv","de","es","da","ru","sk","it","lt","hu","nl","fi","zh","ja","et","sl","pl","ro","fr","cs","lv"));
    private XLang plugin;
    
    public Translator(XLang plugin) {
        this.plugin = plugin;
    }

    public String getDeeplCode(String locale) {
        String countryCode = locale.split("_")[0];

        if (countryCode.equalsIgnoreCase("en")) return "EN-GB";
        else if (countryCode.equalsIgnoreCase("bg")) return "BG";
        else if (countryCode.equalsIgnoreCase("pt")) return "PT-PT";
        else if (this.validLocales.contains(countryCode)) return countryCode.toUpperCase();
        else return "ERROR";
    }

    public String getTranslation(String input, String language) {

        try {
            HttpURLConnection con = (HttpURLConnection) this.plugin.url.openConnection();
            con.setRequestMethod("POST");
            Map<String, String> parameters = new HashMap<>();
            parameters.put("auth_key", this.plugin.getConfig().getString("deepl.apiKey"));
            parameters.put("text", input);
            parameters.put("target_lang", language);

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
