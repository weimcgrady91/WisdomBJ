package com.qun.test.wisdombj.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/4/6.
 */

public class NewsDetail {
    public int retcode;
    public NewsData data;

    @Override
    public String toString() {
        return "NewsDetail{" +
                "retcode=" + retcode +
                ", data=" + data +
                '}';
    }

    public class NewsData {
        public String countcommenturl;
        public String more;
        public List<News> news;
        public String title;
        public List<Topic> topic;
        public List<TopNews> topnews;

        @Override
        public String toString() {
            return "NewsData{" +
                    "countcommenturl='" + countcommenturl + '\'' +
                    ", more='" + more + '\'' +
                    ", news=" + news +
                    ", title='" + title + '\'' +
                    ", topic=" + topic +
                    ", topnews=" + topnews +
                    '}';
        }
    }

    public class News {
        public boolean comment;
        public String commentlist;
        public String commenturl;
        public int id;
        public String listimage;
        public String pubdate;
        public String title;
        public String type;
        public String url;

        @Override
        public String toString() {
            return "News{" +
                    "comment=" + comment +
                    ", commentlist='" + commentlist + '\'' +
                    ", commenturl='" + commenturl + '\'' +
                    ", id=" + id +
                    ", listimage='" + listimage + '\'' +
                    ", pubdate='" + pubdate + '\'' +
                    ", title='" + title + '\'' +
                    ", type='" + type + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }

    public class Topic {
        public String description;
        public int id;
        public int sort;
        public String title;
        public String url;

        @Override
        public String toString() {
            return "Topic{" +
                    "description='" + description + '\'' +
                    ", id=" + id +
                    ", sort=" + sort +
                    ", title='" + title + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }

    public class TopNews {
        public boolean comment;
        public String commentlist;
        public String commenturl;
        public int id;
        public String pubdate;
        public String title;
        public String topimage;
        public String type;
        public String url;

        @Override
        public String toString() {
            return "TopNews{" +
                    "comment=" + comment +
                    ", commentlist='" + commentlist + '\'' +
                    ", commenturl='" + commenturl + '\'' +
                    ", id=" + id +
                    ", pubdate='" + pubdate + '\'' +
                    ", title='" + title + '\'' +
                    ", topimage='" + topimage + '\'' +
                    ", type='" + type + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }
}
