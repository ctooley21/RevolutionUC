package com.google.sample.snapsey;


import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
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
    private static HashMap<String, String> languages = new HashMap<>();
    private static String API_KEY = "AIzaSyDoml5iMKBAFSdnwC5_JGbcbEku6yvTCWk";

    public static void init() throws IOException, GeneralSecurityException
    {
        t = new Translate.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(), null).setApplicationName("CloudVision").build();

        languages.put("english", "en");
        languages.put("spanish", "es");
        languages.put("french", "fr");
        languages.put("russian", "ru");
    }

    private static void translate(String word, String toLanguage) throws IOException
    {
        Translate.Translations.List list = t.new Translations().list(
                Arrays.asList(word), getAbbreviation(toLanguage));
        list.setKey(getApiKey());
        TranslationsListResponse response = list.execute();

        for (TranslationsResource translationsResource : response.getTranslations()) {
            System.out.println(translationsResource.getTranslatedText());
        }
    }

    private static String getAbbreviation(String language)
    {
        return languages.get(language);
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
