package com.api.messaging.ftp.sender;

import com.enterprisedt.net.ftp.ssl.SSLFTPClient;

import java.io.ByteArrayInputStream;

public class SSLFTPSingleton {
    private SSLFTPSingleton() {
        return;
    }

    public synchronized static void AppendToFile(SSLFTPClient ftp, String file,
            String content) throws Exception {
        ByteArrayInputStream byt = new ByteArrayInputStream(content.getBytes());
        ftp.put(byt, file, true);

    }
}
