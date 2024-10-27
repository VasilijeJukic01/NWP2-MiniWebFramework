package com.nwp.app.controller;

import com.nwp.annotations.*;
import com.nwp.app.service.TestService;
import com.nwp.app.service.TestService1;
import com.nwp.app.service.TestService2;

@Controller
public class TestController2 {

    @Autowired(verbose = true)
    private TestService1 testService1;

    @Autowired(verbose = true)
    private TestService2 testService2;

    @Autowired(verbose = true)
    @Qualifier("TestService3")
    private TestService testService3;

    @GET
    @Path(path = "/test21")
    public void testMethod21() { }

    @POST
    @Path(path = "/test22")
    public void testMethod22() { }

}
