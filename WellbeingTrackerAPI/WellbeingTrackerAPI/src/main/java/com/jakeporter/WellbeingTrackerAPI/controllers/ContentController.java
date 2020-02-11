package com.jakeporter.WellbeingTrackerAPI.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author jake
 */

@Controller
public class ContentController {

    @GetMapping("/content")
    public String displayContentPage() {
        return "dataView_content";
    }
}
