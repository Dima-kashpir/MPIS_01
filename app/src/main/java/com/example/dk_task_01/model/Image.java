package com.example.dk_task_01.model;

public class Image {
    private String id;
    private Urls urls;

    public Urls getUrls() {
        return urls;
    }

    public String getId() {
        return id;
    }

    public static class Urls {
        private String regular;
        private String small;

        public String getRegular() {
            return  regular;
        }

        public String getSmall() {
            return  small;
        }
    }
}


