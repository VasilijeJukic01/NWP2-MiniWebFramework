package com.nwp.app.service;

import com.nwp.annotations.Autowired;
import com.nwp.annotations.Bean;
import com.nwp.annotations.Qualifier;
import com.nwp.app.component.TestComponent1;

@Bean
@Qualifier("TestService1")
public class TestService1 implements TestService {

    @Autowired(verbose = true)
    private TestComponent1 testComponent1;

    public TestService1() { }
}
