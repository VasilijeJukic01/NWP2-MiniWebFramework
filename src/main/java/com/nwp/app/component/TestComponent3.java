package com.nwp.app.component;

import com.nwp.annotations.Autowired;
import com.nwp.annotations.Component;
import com.nwp.annotations.Qualifier;
import com.nwp.app.service.TestService;

@Component
@Qualifier("TestComponent3")
public class TestComponent3 implements TestComponent {

    @Autowired(verbose = true)
    @Qualifier("TestService3")
    private TestService testService3;

    public TestComponent3() { }
}
