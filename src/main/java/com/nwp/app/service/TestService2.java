package com.nwp.app.service;

import com.nwp.annotations.Autowired;
import com.nwp.annotations.Qualifier;
import com.nwp.annotations.Service;
import com.nwp.app.component.TestComponent2;

@Service
@Qualifier("TestService2")
public class TestService2 implements TestService {

    @Autowired(verbose = true)
    private TestComponent2 testComponent2;

    public TestService2() { }
}
