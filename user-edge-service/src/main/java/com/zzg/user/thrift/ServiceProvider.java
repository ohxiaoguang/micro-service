package com.zzg.user.thrift;

import com.zzg.thrift.message.MessageService;
import com.zzg.thrift.user.UserService;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author ZZG
 * @Date 2019/10/7 22:39
 * @Version v1.0.0
 */
@Component
public class ServiceProvider {

    @Value("${thrift.userservice.ip}")
    private String userServerIp;
    @Value("${thrift.userservice.port}")
    private int userServerPort;
    @Value("${thrift.messageservice.ip}")
    private String messageServerIp;
    @Value("${thrift.messageservice.port}")
    private int messageServerPort;

    private enum ServiceType{
        USER,
        MESSAGE
    }


    public UserService.Client getUserService(){
        return getService(userServerIp,userServerPort,ServiceType.USER);
    }

    public MessageService.Client getMessageService(){
        return getService(messageServerIp,messageServerPort,ServiceType.MESSAGE);
    }

    private <T> T getService(String ip, int port, ServiceType serviceType){
        TSocket socket = null;
        if (socket == null) {
            socket = new TSocket(ip,port,3000);
        }

        TTransport transport = new TFramedTransport(socket);
        try {
            transport.open();
        } catch (TTransportException e) {
            e.printStackTrace();
            return null;
        }
        TProtocol protocol = new TBinaryProtocol(transport);
        TServiceClient result = null;
        switch (serviceType){
            case USER:
                result = new UserService.Client(protocol);
                break;
            case MESSAGE:
                result = new MessageService.Client(protocol);
                break;
        }
        return (T) result;
    }

}
