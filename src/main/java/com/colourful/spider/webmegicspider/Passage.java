package com.colourful.spider.webmegicspider;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Passage {
    private int passageId;
    private String name;
    private String content;
    private int flow;
    private int participate;
    private String url;
}
