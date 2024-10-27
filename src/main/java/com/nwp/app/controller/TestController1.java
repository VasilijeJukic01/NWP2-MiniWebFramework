package com.nwp.app.controller;

import com.nwp.annotations.*;
import com.nwp.app.service.TestService;
import com.nwp.app.service.TestService1;
import com.nwp.app.service.TestService2;

@Controller
public class TestController1 {

    @Autowired(verbose = true)
    private TestService1 testService1;

    @Autowired(verbose = true)
    private TestService2 testService2;

    @Autowired(verbose = true)
    @Qualifier("TestService3")
    private TestService testService3;

    @GET
    @Path(path = "/test11")
    public void testMethod11() { }

    @POST
    @Path(path = "/test12")
    public void testMethod12() { }

}
