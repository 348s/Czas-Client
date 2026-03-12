package com.czasclient.config;

import java.util.HashMap;
import java.util.Map;

public class LanguageManager {
    private static String currentLanguage = "english";
    private static final Map<String, Map<String, String>> translations = new HashMap<>();

    static {
        initializeTranslations();
    }

    private static void initializeTranslations() {
        Map<String, String> english = new HashMap<>();
        english.put("client_name", "Czas Client");
        english.put("client_author", "by 348 s");
        english.put("toggle_gui", "Right Shift to toggle");
        english.put("movement", "Movement");
        english.put("combat", "Combat");
        english.put("utilities", "Utilities");
        english.put("world", "World");
        english.put("render", "Render");
        english.put("while_speed", "While Speed");
        english.put("fake_creative", "Fake Creative");
        english.put("nuker", "Nuker");
        english.put("aim_assist", "Aim Assist");
        english.put("spider", "Spider");
        english.put("long_jump", "Long Jump");
        english.put("no_fall", "No Fall");
        english.put("auto_regen", "Auto Regen");
        english.put("inventory_walk", "Inventory Walk");
        english.put("fast_break", "Fast Break");
        english.put("timer", "Timer");
        english.put("scaffold", "Scaffold");
        english.put("auto_armor", "Auto Armor");
        english.put("no_slow", "No Slow");
        english.put("full_bright", "Full Bright");
        english.put("auto_clicker", "Auto Clicker");

        Map<String, String> polish = new HashMap<>();
        polish.put("client_name", "Klient Czas");
        polish.put("client_author", "autorstwa 348 s");
        polish.put("toggle_gui", "Prawy Shift aby przełączyć");
        polish.put("movement", "Ruch");
        polish.put("combat", "Walka");
        polish.put("utilities", "Narzędzia");
        polish.put("world", "Świat");
        polish.put("render", "Render");
        polish.put("while_speed", "Szybkość");
        polish.put("fake_creative", "Fałszywy Kreatywny");
        polish.put("nuker", "Nuker");
        polish.put("aim_assist", "Asystent Celowania");
        polish.put("spider", "Pająk");
        polish.put("long_jump", "Długi Skok");
        polish.put("no_fall", "Bez Upadku");
        polish.put("auto_regen", "Auto Regeneracja");
        polish.put("inventory_walk", "Chodzenie z Ekwipunkiem");
        polish.put("fast_break", "Szybkie Niszczenie");
        polish.put("timer", "Timer");
        polish.put("scaffold", "Scaffold");
        polish.put("auto_armor", "Auto Zbroja");
        polish.put("no_slow", "Bez Spowolnienia");
        polish.put("full_bright", "Pełna Jasność");
        polish.put("auto_clicker", "Auto Klikacz");

        translations.put("english", english);
        translations.put("polish", polish);
    }

    public static void setLanguage(String language) {
        if (translations.containsKey(language.toLowerCase())) {
            currentLanguage = language.toLowerCase();
        }
    }

    public static String getCurrentLanguage() {
        return currentLanguage;
    }

    public static String translate(String key) {
        Map<String, String> lang = translations.get(currentLanguage);
        if (lang != null && lang.containsKey(key)) {
            return lang.get(key);
        }
        
        Map<String, String> english = translations.get("english");
        if (english != null && english.containsKey(key)) {
            return english.get(key);
        }
        
        return key;
    }

    public static String[] getAvailableLanguages() {
        return translations.keySet().toArray(new String[0]);
    }

    public static String getLanguageDisplayName(String language) {
        switch (language.toLowerCase()) {
            case "english": return "English";
            case "polish": return "Polski";
            default: return language;
        }
    }
}
