package com.nwp.app.component;

import com.nwp.annotations.Autowired;
import com.nwp.annotations.Bean;
import com.nwp.annotations.Qualifier;

@Bean(scope = "prototype")
@Qualifier("TestComponent1")
public class TestComponent1 implements TestComponent {

    @Autowired(verbose = true)
    private TestComponent3 testComponent3;

    public TestComponent1() { }
}
