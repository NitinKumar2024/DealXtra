package com.dealxtra.dealxtra.customer.models;

public class RecentSearch {
    private String query;
    private long timestamp;

    public RecentSearch(String query, long timestamp) {
        this.query = query;
        this.timestamp = timestamp;
    }

    public String getQuery() {
        return query;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getFormattedTime() {
        long now = System.currentTimeMillis();
        long diff = now - timestamp;

        if (diff < 60000) { // less than 1 minute
            return "Just now";
        } else if (diff < 3600000) { // less than 1 hour
            return (diff / 60000) + "m ago";
        } else if (diff < 86400000) { // less than 1 day
            return (diff / 3600000) + "h ago";
        } else {
            return (diff / 86400000) + "d ago";
        }
    }
}
