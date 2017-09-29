package com.kyungjoon.springbootjsp.bootjsp.rest;

import ch.qos.logback.classic.Logger;
import org.json.simple.JSONValue;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


@Controller
public class RestController {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(RestController.class);

    @Autowired
    private RestDao testDao;

    @Autowired
    private RestDao restDao;


    @RequestMapping("/")
    public ModelAndView index() {
        ModelAndView mav = new ModelAndView();
        // mav.addObject("message", "업로드성공!");
        mav.setViewName("redirect:/rest/list");
        mav.addObject("message", "고경준은 천재님이십니다 과식하지 말자 운동을 많이 하자!!!");
        return mav;
    }


    @RequestMapping("/rest/writeForm")
    public String writeForm() {

        return "rest/writeForm";
    }


    @RequestMapping("/rest/list")
    public ModelAndView list(Model model,
                             @RequestParam(value = "name", required = false, defaultValue = "World") String name) {
        ModelAndView mav = new ModelAndView();


        List<?> arrList = testDao.getList();

        mav.addObject("arrList", arrList);

        System.out.println(arrList.toString());
        mav.setViewName("/rest/list");
        return mav;

    }


    @CrossOrigin
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping(value = "/rest/getList")
    public @ResponseBody
    String getList() throws IOException {
        HashMap resultMap = new HashMap();
        List<?> arrList = testDao.getList();
        resultMap.put("arrList", arrList);
        return JSONValue.toJSONString(resultMap);

    }


    /**
     * 한개의 이미지와 한개의 명언만 가지고 온다 ^__________^
     * @return
     * @throws IOException
     */
    @CrossOrigin
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping(value = "/rest/getOneJson")
    public @ResponseBody
    String getOne() throws IOException {
        HashMap resultMap = new HashMap();
        int totalCount = testDao.getListCount();

        Random r = new Random();
        int Low = 1;
        int High = totalCount;
        int randNumberResult = r.nextInt(High-Low) + Low;

        Map proverbOne = testDao.getProverbOne(randNumberResult);

        System.out.println("proverbOne-->"+ proverbOne.get("content"));

        String strProverbOne =(String) proverbOne.get("content");

        List<HashMap> imageList = testDao.getImageList();


        Map imageOne = imageList.get(ThreadLocalRandom.current().nextInt(imageList.size()));

        String imageName  =(String) imageOne.get("image_name");

        System.out.println("imagename===>"+ imageName);

        System.out.println("sdklsdkflksdlfksldkflksdlfk");



        resultMap.put("imageName", imageName);
        resultMap.put("strProverbOne", strProverbOne);
        return JSONValue.toJSONString(resultMap);

    }


    @RequestMapping("/rest/getOne")
    public ModelAndView getOne(Model model,
                               @RequestParam(value = "name", required = false, defaultValue = "World") String name) {
        ModelAndView mav = new ModelAndView();


        HashMap resultMap = new HashMap();
        int totalCount = testDao.getListCount();

        Random r = new Random();
        int Low = 1;
        int High = totalCount;
        int randNumberResult = r.nextInt(High-Low) + Low;

        Map proverbOne = testDao.getProverbOne(randNumberResult);

        System.out.println("proverbOne-->"+ proverbOne.get("content"));

        String strProverbOne =(String) proverbOne.get("content");

        List<HashMap> imageList = testDao.getImageList();


        Map imageOne = imageList.get(ThreadLocalRandom.current().nextInt(imageList.size()));

        String imageName  =(String) imageOne.get("image_name");

        System.out.println("imagename===>"+ imageName);


        mav.addObject("imageName", imageName);
        mav.addObject("strProverbOne", strProverbOne);
        mav.setViewName("/rest/proverbOne");
        return mav;

    }



    @PostMapping("/rest/upload") // //new annotation since 4.3
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:/rest/imageList";
        }

        try {

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(Constants.UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);

            String fileName = file.getOriginalFilename();

            String[] fileExt = fileName.split("\\.");


            restDao.insertImage(file.getOriginalFilename(), fileExt[1]);

            redirectAttributes.addFlashAttribute("message", "You successfully uploaded '" + file.getOriginalFilename() + "'");


        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/rest/imageList";
    }


    /**
     * 이미지 리스팅
     * @return
     */
    @GetMapping("/rest/imageList")
    public ModelAndView uploadStatus() {
        ModelAndView mav = new ModelAndView();
        List arrList = restDao.getImageList();


        mav.addObject("arrList", arrList);
        mav.setViewName("/rest/imageList");
        return mav;

    }



    @RequestMapping(value = "/image/{imageName:.+}")
    @ResponseBody
    public byte[] getImage(@PathVariable(value = "imageName") String imageName) throws IOException {

        File serverFile = new File(Constants.UPLOADED_FOLDER + imageName);

        return Files.readAllBytes(serverFile.toPath());
    }


}
