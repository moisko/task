package com.implementit.task

import com.implementit.task.TaskApplication
import com.implementit.task.controller.ProductController
import com.implementit.task.controller.SubscriberController
import com.implementit.task.service.ProductService
import com.implementit.task.service.SubscriberService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = TaskApplication)
class TaskApplicationSpec extends Specification {
    @Autowired
    private ApplicationContext context

    def "should load subscriber service from the application context"() {
        expect:
        context
        context.getBean(SubscriberService.class)
    }

    def "should load subscriber controller from the application context"() {
        expect:
        context
        context.getBean(SubscriberController.class)
    }

    def "should load product service from the application context"() {
        expect:
        context
        context.getBean(ProductService.class)
    }

    def "should load product controller from the application context"() {
        expect:
        context
        context.getBean(ProductController.class)
    }
}
