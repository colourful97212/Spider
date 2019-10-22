package com.colourful.spider.webmegicspider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class WangyiDao {
    public int add(Connection connection,Passage passage) throws SQLException {
        String sql = "insert into passage(name,content,flow,participate,url) values(?,?,?,?,?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1,passage.getName());
        ps.setString(2,passage.getContent());
        ps.setInt(3,passage.getFlow());
        ps.setInt(4,passage.getParticipate());
        ps.setString(5,passage.getUrl());
        return ps.executeUpdate();
    }
}
