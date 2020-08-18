package com.softplan.codechallenge.web;

import com.softplan.codechallenge.constants.Constants;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/source")
@Api(tags = Constants.GIT_REPOSITORY_TAG)
public class GitRepositoryController {

    @Value("${com.softplan.gitlab.url}")
    private String url;

    @GetMapping
    public String getUrl() {
        return url;
    }
}
