package io.benwilcock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by benwilcock on 03/08/2016.
 */
@RestController
public class StatusController {

    @Autowired
    Status status;

    @RequestMapping("/")
    public Status index(){
        return status;
    }
}
