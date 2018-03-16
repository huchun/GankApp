package com.example.gankapp.ui.bean;

/**
 * Created by chunchun.hu on 2018/3/16.
 */

public class SearchBean {

    /**
     * desc : listview的折叠效果
     * ganhuo_id : 56cc6d1d421aa95caa7076fa
     * publishedAt : 2017-09-17T03:43:22.395000
     * readability : <div><article class="markdown-body entry-content">

     <p>ListView&#x6298;&#x53E0;&#x6548;&#x679C;</p>

     <h2><a id="user-content-screenshots" class="anchor" href="https://github.com/huchun/ListItemFold#screenshots"><svg class="octicon octicon-link" height="16" width="16"><path></path></svg></a>Screenshots</h2>

     <p>The MIT License (MIT)</p>

     <p>Copyright (c) 2015 dodola</p>

     <p>Permission is hereby granted, free of charge, to any person obtaining a copy
     of this software and associated documentation files (the "Software"), to deal
     in the Software without restriction, including without limitation the rights
     to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
     copies of the Software, and to permit persons to whom the Software is
     furnished to do so, subject to the following conditions:</p>

     <p>The above copyright notice and this permission notice shall be included in all
     copies or substantial portions of the Software.</p>

     <p>THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
     IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
     FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
     AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
     LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
     OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
     SOFTWARE.</p>
     </article>
     </div>
     * type : Android
     * url : https://github.com/huchun/
     * who : Jason
     */

    private String desc;
    private String ganhuo_id;
    private String publishedAt;
    private String readability;
    private String type;
    private String url;
    private String who;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getGanhuo_id() {
        return ganhuo_id;
    }

    public void setGanhuo_id(String ganhuo_id) {
        this.ganhuo_id = ganhuo_id;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getReadability() {
        return readability;
    }

    public void setReadability(String readability) {
        this.readability = readability;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    @Override
    public String toString() {
        return "SearchBean{" +
                "desc='" + desc + '\'' +
                ", ganhuo_id='" + ganhuo_id + '\'' +
                ", publishedAt='" + publishedAt + '\'' +
                ", readability='" + readability + '\'' +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                ", who='" + who + '\'' +
                '}';
    }
}
