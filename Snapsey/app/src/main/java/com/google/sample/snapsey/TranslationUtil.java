package com.google.sample.snapsey;

import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.translate.Translate;
import com.google.api.services.translate.model.TranslationsListResponse;
import com.google.api.services.translate.model.TranslationsResource;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TranslationUtil
{

    private static Translate t;
    private static HashMap<String, String> languages;
    private static String API_KEY;

    public static void init() throws IOException, GeneralSecurityException
    {
        t = new Translate.Builder(new ApacheHttpTransport(),
                GsonFactory.getDefaultInstance(), null).setApplicationName("Snapsey").build();
        API_KEY = "AIzaSyDoml5iMKBAFSdnwC5_JGbcbEku6yvTCWk";

        languages = new HashMap<>();
        languages.put("english", "en");
        languages.put("spanish", "es");
        languages.put("french", "fr");
        languages.put("russian", "ru");
    }

    public static String translate(String word, String toLanguage)
    {
        StringBuilder translated = new StringBuilder();
        Translate.Translations.List list;
        TranslationsListResponse response;

        try
        {
            list = t.new Translations().list(Arrays.asList(word), getAbbreviation(toLanguage));
            list.setKey(getApiKey());
            response = list.execute();
        }
        catch (Exception e)
        {
            return "Unable to translate.";
        }

        for (TranslationsResource translationsResource : response.getTranslations()) {
            translated.append(translationsResource.getTranslatedText());
            translated.append(" ");
        }

        translated.setLength(translated.length() - 1);

        return translated.toString();
    }

    private static String getAbbreviation(String language)
    {
        for(String s : languages.keySet())
        {
            if(s.equalsIgnoreCase(language))
            {
                return languages.get(s);
            }
        }
        return null;
    }

    private static String getApiKey()
    {
        return API_KEY;
    }

    private static List<String> getLanguages()
    {
        return new ArrayList<>(languages.keySet());
    }
}
