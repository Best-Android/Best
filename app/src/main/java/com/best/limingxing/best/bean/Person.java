package com.best.limingxing.best.bean;

import java.util.Date;
import java.util.List;
/**
 * Created by Administrator on 2017/3/13.
 */

public class Person {
    private String _id;
    private String createdAt;
    private String desc;
    private List<String> images;
    private String publishedAt;
    private String source;
    private String type;
    private String url;
    private boolean used;
    private String who;

    public Person(String _id, String createdAt, String desc, List<String> images, String publishedAt,
                  String source, String type, String url, boolean used, String who) {
        super();
        this._id = _id;
        this.createdAt = createdAt;
        this.desc = desc;
        this.images = images;
        this.publishedAt = publishedAt;
        this.source = source;
        this.type = type;
        this.url = url;
        this.used = used;
        this.who = who;

    }


    public void set_id(String _id) {
        this._id = _id;
    }
    public String get_id() {
        return _id;
    }

    public void setCreatedat(String createdat) {
        this.createdAt = createdat;
    }
    public String getCreatedat() {
        return createdAt;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getDesc() {
        return desc;
    }

    public void setimages(List<String> images) {
        this.images = images;
    }
    public List<String> getimages() {
        return images;
    }

    public void setPublishedat(String publishedat) {
        this.publishedAt = publishedat;
    }
    public String getPublishedat() {
        return publishedAt;
    }

    public void setSource(String source) {
        this.source = source;
    }
    public String getSource() {
        return source;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public String getUrl() {
        return url;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
    public boolean getUsed() {
        return used;
    }

    public void setWho(String who) {
        this.who = who;
    }
    public String getWho() {
        return who;
    }

    @Override
    public String toString() {
        return "Person{" +
                "_id='" + _id + ","+'\n' +
                "createdAt=" + createdAt +","+'\n' +
                "desc='" + desc + ","+'\n' +
                "images='" + images +","+ '\n' +
                "publishedAt=" + publishedAt +","+'\n' +
                "source='" + source + ","+'\n' +
                "type='" + type + ","+'\n' +
                "url='" + url + ","+'\n' +
                "used=" + used +","+'\n' +
                "who='" + who + ","+'\n' +
                '}';
    }
}
