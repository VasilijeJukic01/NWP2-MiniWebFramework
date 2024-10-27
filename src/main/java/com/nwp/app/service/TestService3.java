package com.nwp.app.service;

import com.nwp.annotations.Qualifier;
import com.nwp.annotations.Service;

@Service
@Qualifier("TestService3")
public class TestService3 implements TestService {

    public TestService3() { }
}
