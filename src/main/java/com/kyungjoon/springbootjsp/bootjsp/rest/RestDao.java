package com.kyungjoon.springbootjsp.bootjsp.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class RestDao {

    @Autowired
    private JdbcTemplate template;

    /**
     * Get hello list, using BeanPropertyRowMapper
     *
     * @return
     */
    public List<?> getList() {
        List arrList  = template.queryForList("SELECT * FROM proverbs order by id desc");
        return arrList;
    }

    public Map getProverbOne(int randNumber) {
        return template.queryForMap("SELECT * FROM proverbs where id =?", randNumber);
    }


    public Map getImageOne(int randNumber) {
        return template.queryForMap("SELECT * FROM images where id =?", randNumber);
    }

    public Map getOnePicture(int randNumber) {
        return template.queryForMap("select * from images where id=?", randNumber);
    }

    public List getImageList() {
        List arrList = template.queryForList("select *  from images order by id desc");
        return arrList;
    }


    public int getListCount() {
        Map resultMap = template.queryForMap("SELECT count(*) as count FROM proverbs order by id desc");
        return (int) resultMap.get("count");
    }

    /**
     * Insert hello
     *
     * @param hello
     * @return
     */
    public int insert(Hello hello) {
        String query = "INSERT INTO blogs(content) VALUES( ?)";
        return template.update(query, hello.getName());
    }


    public int insertImage(String imageName, String extension) {
        String query = "INSERT INTO images(image_name, extension) VALUES( ?,?)";
        return template.update(query, imageName, extension);
    }

    /*public List<?> getImageList() {

        List arrList  = template.queryForList("SELECT * FROM images order by id desc");
        return arrList;
    }*/

}