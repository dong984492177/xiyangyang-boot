package com.ywt.console.config;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.ByteArrayInputStream;

/**
 * @Author: huangchaoyang
 * @Description: request数据取出拷贝
 * @Version: 1.0
 * @Create: 2021/1/12
 */
public class ServletInputStreamCopier extends ServletInputStream {
    private ByteArrayInputStream bais;

    public ServletInputStreamCopier(byte[] in) {
        this.bais = new ByteArrayInputStream(in);
    }

    @Override
    public boolean isFinished() {
        return bais.available() == 0;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setReadListener(ReadListener readListener) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public int read() {
        return this.bais.read();
    }

}
