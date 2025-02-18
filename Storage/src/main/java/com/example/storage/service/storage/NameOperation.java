package com.example.storage.service.storage;

public class NameOperation {
    public static String characterRegulatory(String name) {
        if (name == null || name.isBlank()) {
            return "";
        }

        return name.replace("\"", "")
                .replace("!", "")
                .replace("'", "")
                .replace("^", "")
                .replace("+", "")
                .replace("%", "")
                .replace("&", "")
                .replace("/", "")
                .replace("(", "")
                .replace(")", "")
                .replace("=", "")
                .replace("?", "")
                .replace("_", "")
                .replace(" ", "-")
                .replace("@", "")
                .replace("€", "")
                .replace("¨", "")
                .replace("~", "")
                .replace(",", "")
                .replace(";", "")
                .replace(":", "")
                .replace(".", "-")
                .replace("Ö", "o")
                .replace("ö", "o")
                .replace("Ü", "u")
                .replace("ü", "u")
                .replace("ı", "i")
                .replace("İ", "i")
                .replace("ğ", "g")
                .replace("Ğ", "g")
                .replace("æ", "")
                .replace("ß", "")
                .replace("â", "a")
                .replace("î", "i")
                .replace("ş", "s")
                .replace("Ş", "s")
                .replace("Ç", "c")
                .replace("ç", "c")
                .replace("<", "")
                .replace(">", "")
                .replace("|", "");
    }
}
