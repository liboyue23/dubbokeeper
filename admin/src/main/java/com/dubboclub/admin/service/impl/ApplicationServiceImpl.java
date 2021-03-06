package com.dubboclub.admin.service.impl;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.dubboclub.admin.model.Application;
import com.dubboclub.admin.model.Consumer;
import com.dubboclub.admin.model.Node;
import com.dubboclub.admin.model.Provider;
import com.dubboclub.admin.service.AbstractService;
import com.dubboclub.admin.service.ApplicationService;
import com.dubboclub.admin.service.ConsumerService;
import com.dubboclub.admin.service.ProviderService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by bieber on 2015/6/4.
 */
public class ApplicationServiceImpl extends AbstractService implements ApplicationService {

    private ProviderService providerService;

    private ConsumerService consumerService;

    @Override
    public List<Application> getApplications() {
        ConcurrentMap<String, Map<Long, URL>> providers = getServiceByCategory(Constants.PROVIDERS_CATEGORY);
        ConcurrentMap<String, Map<Long, URL>> consumers = getServiceByCategory(Constants.CONSUMERS_CATEGORY);
        List<Application> applications = new ArrayList<Application>();
        if(providers!=null){
            for(Map.Entry<String, Map<Long, URL>> oneService:providers.entrySet()){
                Map<Long, URL> urls = oneService.getValue();
                for(Map.Entry<Long,URL> url:urls.entrySet()){
                    Application application = new Application();
                    application.setApplication(url.getValue().getParameter(Constants.APPLICATION_KEY));
                    application.setUsername(url.getValue().getParameter("owner"));
                    if(!applications.contains(application)){
                        applications.add(application);
                    }
                    break;
                }
            }
        }
        if(consumers!=null){
            for(Map.Entry<String, Map<Long, URL>> oneService:consumers.entrySet()){
                Map<Long, URL> urls = oneService.getValue();
                for(Map.Entry<Long,URL> url:urls.entrySet()){
                    Application application = new Application();
                    application.setApplication(url.getValue().getParameter(Constants.APPLICATION_KEY));
                    application.setUsername(url.getValue().getParameter("owner"));
                    if(!applications.contains(application)){
                        applications.add(application);
                    }
                    break;
                }
            }
        }
        return applications;
    }

    @Override
    public List<Node> getNodesByApplicationName(String appName) {
        List<Provider> providers = this.providerService.listProviderByApplication(appName);
        List<Consumer> consumers = this.consumerService.listConsumerByApplication(appName);
        List<Node> nodes = new ArrayList<Node>();
        for(Provider provider:providers){
            Node node = new Node();
            node.setType(Node.PROVIDER_TYPE);
            node.setNodeAddress(provider.getAddress());
            node.setId(provider.getId());
            if(!nodes.contains(node)){
                nodes.add(node);
            }
        }
        for(Consumer consumer:consumers){
            Node node = new Node();
            node.setType(Node.CONSUMER_TYPE);
            node.setNodeAddress(consumer.getAddress());
            node.setId(consumer.getId());
            if(!nodes.contains(node)){
                nodes.add(node);
            }
        }

        return nodes;
    }

    public void setProviderService(ProviderService providerService) {
        this.providerService = providerService;
    }

    public void setConsumerService(ConsumerService consumerService) {
        this.consumerService = consumerService;
    }
}
