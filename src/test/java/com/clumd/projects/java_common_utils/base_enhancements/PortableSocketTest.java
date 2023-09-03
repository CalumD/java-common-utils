package com.clumd.projects.java_common_utils.base_enhancements;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.Socket;

class PortableSocketTest {

    @Test
    public void constructor() throws IOException {
//        PortableSocket ps = new PortableSocket(new Socket());
//        PortableSocket ps2 = new PortableSocket(new Socket(), 1);
        PortableSocket ps3 = new PortableSocket(new Socket(), (Object[]) null, 12);

        System.out.println("");
    }

}
