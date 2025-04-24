package com.linkedin;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class TextItemProcessor implements ItemProcessor<String,String> {

    @Override
    public String process(String message) throws Exception {
        String masked_message = message.replaceAll("\\d","*");
        return masked_message;
    }
}
