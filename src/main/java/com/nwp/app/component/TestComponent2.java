package com.nwp.app.component;

import com.nwp.annotations.Autowired;
import com.nwp.annotations.Component;
import com.nwp.annotations.Qualifier;

@Component
@Qualifier("TestComponent2")
public class TestComponent2 implements TestComponent {

    @Autowired(verbose = true)
    @Qualifier("TestComponent3")
    private TestComponent testComponent3;

    public TestComponent2() { }
}
