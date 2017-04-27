//package com.gavin.message.producer;
//
//import com.gavin.base.MessageProducer;
//import com.gavin.event.UserCreatedEvent;
//import com.gavin.messaging.UserCreatedProcessor;
//import com.gavin.payload.UserCreatedPayload;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.stream.annotation.EnableBinding;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.support.MessageBuilder;
//import org.springframework.stereotype.Component;
//
//@EnableBinding(UserCreatedProcessor.class)
//@Component
//public class UserCreatedMessageProducer implements MessageProducer<UserCreatedEvent> {
//
//    private final ModelMapper modelMapper;
//
//    private final UserCreatedProcessor source;
//
//    @Autowired
//    public UserCreatedMessageProducer(
//            ModelMapper modelMapper,
//            UserCreatedProcessor source) {
//        this.modelMapper = modelMapper;
//        this.source = source;
//    }
//
//    @Override
//    public boolean sendMessage(UserCreatedEvent _event) {
//        UserCreatedPayload payload = modelMapper.map(_event, UserCreatedPayload.class);
//        payload.setEventId(_event.getOriginId());
//
//        Message<UserCreatedPayload> message = MessageBuilder.withPayload(payload).build();
//        return source.output().send(message);
//    }
//
//}
