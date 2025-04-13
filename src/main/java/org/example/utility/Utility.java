package org.example.utility;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class Utility {
    private static final Logger log = LoggerFactory.getLogger(Utility.class);
    private String regex = "^[a-zA-Z0-9@#._-]{1,10}$";

    public String generateUUID(String flowName){
        UUID uuid = UUID.randomUUID();
        return flowName.concat("-").concat(uuid.toString());
    }

}
